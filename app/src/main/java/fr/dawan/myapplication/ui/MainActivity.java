package fr.dawan.myapplication.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import fr.dawan.myapplication.R;
import fr.dawan.myapplication.data.AppDatabase;
import fr.dawan.myapplication.data.DawanApi;
import fr.dawan.myapplication.data.LocationDao;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private static final String URL_API_DAWAN = "https://dawan.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuration du moteur cartographique OpenStreetMap
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_main);

        initialiserCarte();
        initialiserBaseDeDonnees();
        synchroniserCentresFormation();
    }

    /**
     * Paramètre la vue cartographique et définit le point de centrage par défaut.
     */
    private void initialiserCarte() {
        carteInteractive = findViewById(R.id.map);
        carteInteractive.setTileSource(TileSourceFactory.MAPNIK);
        carteInteractive.setMultiTouchControls(true);
        carteInteractive.getController().setZoom(6.0);
        // Centrage sur la France continentale
        carteInteractive.getController().setCenter(new GeoPoint(46.603354, 1.888334));
    }

    /**
     * Prépare l'instance de la base de données locale (SQLite).
     */
    private void initialiserBaseDeDonnees() {
        AppDatabase baseDeDonnees = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "dawan_database")
                .allowMainThreadQueries() // Toléré dans le cadre de cette maquette d'expertise
                .build();
        gestionnaireDonneesLocales = baseDeDonnees.locationDao();
    }

    /**
     * Interroge l'API REST pour récupérer les dernières données et met à jour l'affichage.
     */
    private void synchroniserCentresFormation() {
        Retrofit clientReseau = new Retrofit.Builder()
                .baseUrl(URL_API_DAWAN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DawanApi api = clientReseau.create(DawanApi.class);

        api.getLocations().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> appel, Response<List<Location>> reponse) {
                if (reponse.isSuccessful() && reponse.body() != null) {
                    // Sauvegarde persistante des données récupérées
                    gestionnaireDonneesLocales.insertAll(reponse.body());
                    actualiserMarqueursSurCarte();
                }
            }

            @Override
            public void onFailure(Call<List<Location>> appel, Throwable erreur) {
                // Déclenchement du mode hors-connexion
                Toast.makeText(MainActivity.this, "Réseau indisponible : Mode hors-ligne activé", Toast.LENGTH_LONG).show();
                actualiserMarqueursSurCarte();
            }
        });
    }

    /**
     * Extrait les centres de formation de la base de données et les positionne sur la carte.
     */
    private void actualiserMarqueursSurCarte() {
        List<Location> centresDawan = gestionnaireDonneesLocales.getAll();
        carteInteractive.getOverlays().clear();

        for (Location centre : centresDawan) {
            GeoPoint coordonneesGPS = new GeoPoint(centre.getLatitude(), centre.getLongitude());

            Marker epingle = new Marker(carteInteractive);
            epingle.setPosition(coordonneesGPS);
            epingle.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            epingle.setTitle(centre.getName());

            // Écouteur d'évènement : ouverture du panneau de détails au clic
            epingle.setOnMarkerClickListener((marqueurClique, vueCarte) -> {
                afficherDetailsCentre(centre.getAddress(), centre.getLatitude(), centre.getLongitude());
                return true;
            });

            carteInteractive.getOverlays().add(epingle);
        }
        carteInteractive.invalidate(); // Rafraîchissement graphique du composant
    }

    /**
     * Déploie le Fragment contenant les informations détaillées du centre sélectionné.
     */
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