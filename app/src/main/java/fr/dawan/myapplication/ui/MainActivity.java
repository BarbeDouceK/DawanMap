package fr.dawan.myapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import fr.dawan.myapplication.R;
import fr.dawan.myapplication.data.AppDatabase;
import fr.dawan.myapplication.data.DawanApi;
import fr.dawan.myapplication.data.LocationDao;
import fr.dawan.myapplication.data.RetrofitClient;
import fr.dawan.myapplication.model.Location;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activité principale gérant l'interface cartographique de l'application.
 * <p>
 * Implémente une architecture robuste respectant les principes SOLID.
 * Assure la synchronisation entre le service web (API REST) et la base de données
 * locale (SQLite via Room). Le principe de "Priorité au mode hors-ligne" (Offline-first)
 * est appliqué pour garantir l'accès aux sites sans connexion réseau.
 * </p>
 * @author Baptiste LOUERAT
 */
public class MainActivity extends AppCompatActivity {

    private MapView carteInteractive;
    private LocationDao gestionnaireDonneesLocales;

    // COmme le linter /code analysis me disait de mettre des try with ressources mais que cela faisait planter le multi-thread, je déclare un exécuteur réutilisable
    private final java.util.concurrent.ExecutorService executeurDeTaches = java.util.concurrent.Executors.newSingleThreadExecutor();

    private boolean estEnModeHorsLigne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_main);

        FloatingActionButton btnHorsLigne = findViewById(R.id.btn_mode_hors_ligne);
        btnHorsLigne.setOnClickListener(vue -> {
            estEnModeHorsLigne = !estEnModeHorsLigne;

            // Toast
            String message = estEnModeHorsLigne ? "Mode Hors-ligne ACTIVÉ" : "Mode En ligne RÉTABLI";
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();

            // Déclenchement de la mise à jour de la carte
            synchroniserCentresFormation();
        });

        initialiserCarte();
        initialiserBaseDeDonnees();
        synchroniserCentresFormation();
    }

    private void initialiserCarte() {
        carteInteractive = findViewById(R.id.map);
        carteInteractive.setTileSource(TileSourceFactory.MAPNIK);
        carteInteractive.setMultiTouchControls(true);
        carteInteractive.getController().setZoom(6.0);
        // Centré sur laFrance
        carteInteractive.getController().setCenter(new GeoPoint(46.603354, 1.888334));
    }

    private void initialiserBaseDeDonnees() {
        AppDatabase baseDeDonnees = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "dawan_database")
                            .build();
        gestionnaireDonneesLocales = baseDeDonnees.locationDao();
    }

    private void synchroniserCentresFormation() {

        if (estEnModeHorsLigne) {
            actualiserMarqueursSurCarte();
            return;
        }
        // Par défaut du else vu qu'on a return

        // On utilise le singleton RetrofitCLient !! => On use interface donc on respecte Dependency Inversion Principle + Interface Segregation Principle
        DawanApi api = RetrofitClient.getClient().create(DawanApi.class);

        // Utilisation de l'opérateur diamant <> (Explicit type argument) ??
        api.getLocations().enqueue(new Callback<>() {
            @Override
            // @NON NULL demandé par le COde Analysis => Warning:(97, 57) Not annotated parameter overrides @EverythingIsNonNull parameter
            public void onResponse(@NonNull Call<List<Location>> appel, @NonNull Response<List<Location>> reponse) {
                if (reponse.isSuccessful() && reponse.body() != null) {
                    // Pour respecter SOLID : newSingleThreadExecutor() J'ai import au début le Executor

                    executeurDeTaches.execute(() -> {
                        gestionnaireDonneesLocales.insertAll(reponse.body()); // On ajoute en BDD la réponse de l'API
                        runOnUiThread(MainActivity.this::actualiserMarqueursSurCarte); // On met à jour la mAP
                        });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Location>> appel, @NonNull Throwable erreur) {
                // ISO 27001 (Confidentialité) : L'erreur technique n'est pas exposée à l'utilisateur
                Toast.makeText(MainActivity.this, "Réseau indisponible : Mode hors-ligne activé", Toast.LENGTH_LONG).show();
                actualiserMarqueursSurCarte();
            }
        });
    }

    private void actualiserMarqueursSurCarte() {
            executeurDeTaches.execute(() -> {
                List<Location> centresDawan = gestionnaireDonneesLocales.getAll();

                // Retour sur le fil principal (Main/UI Thread) pour mettre à jour l'affichage
                runOnUiThread(() -> {
                    carteInteractive.getOverlays().clear();

                    for (Location centre : centresDawan) {
                        GeoPoint coordonneesGPS = new GeoPoint(centre.getLatitude(), centre.getLongitude());

                        Marker epingle = new Marker(carteInteractive);
                        epingle.setPosition(coordonneesGPS);
                        epingle.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        epingle.setTitle(centre.getName());

                        epingle.setOnMarkerClickListener((marqueurClique, vueCarte) -> {
                            afficherDetailsCentre(centre.getAddress(), centre.getLatitude(), centre.getLongitude());
                            return true;
                        });

                        carteInteractive.getOverlays().add(epingle);
                    }
                    carteInteractive.invalidate();
            });
        });
    }

    private void afficherDetailsCentre(String adresse, double latitude, double longitude) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, DetailFragment.nouvelleInstance(adresse, latitude, longitude))
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        carteInteractive.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        carteInteractive.onPause();
    }
}