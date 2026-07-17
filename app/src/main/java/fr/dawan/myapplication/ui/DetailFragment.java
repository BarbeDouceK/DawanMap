package fr.dawan.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.dawan.myapplication.R;
import fr.dawan.myapplication.utils.FormatUtils;

/**
 * Composant d'interface (Fragment) dédié à la présentation des détails d'un centre.
 * <p>
 * Reçoit et formate les données de géolocalisation pour l'utilisateur final.
 * </p>
 * @author Baptiste LOUERAT
 */
public class DetailFragment extends Fragment {

    /** Clé d'identification pour transmettre l'adresse via le paquet de données (Bundle). */
    public static final String CLE_ADRESSE = "adresse_centre";
    /** Clé d'identification pour transmettre la latitude via le paquet de données (Bundle). */
    public static final String CLE_LATITUDE = "latitude_centre";
    /** Clé d'identification pour transmettre la longitude via le paquet de données (Bundle). */
    public static final String CLE_LONGITUDE = "longitude_centre";

    /**
     * Fabrique une nouvelle instance du composant avec ses paramètres requis.
     * Cette méthode centralise la création du paquet de données (Bundle) pour éviter
     * les erreurs de clés lors de l'instanciation.
     *
     * @param adresse   L'adresse postale du centre de formation.
     * @param latitude  La latitude GPS du centre.
     * @param longitude La longitude GPS du centre.
     * @return Une instance préparée de {@link DetailFragment}.
     */
    public static DetailFragment nouvelleInstance(String adresse, double latitude, double longitude) {
        DetailFragment fragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(CLE_ADRESSE, adresse);
        arguments.putDouble(CLE_LATITUDE, latitude);
        arguments.putDouble(CLE_LONGITUDE, longitude);
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Construit et initialise la vue de ce composant.
     * Récupère les données transmises lors de l'instanciation, les sécurise, les formate
     * et les injecte dans les composants textuels (TextView).
     *
     * @param constructeurVue L'objet permettant de gonfler (inflate) le fichier XML en vue Java.
     * @param conteneur       Le conteneur parent qui accueillera l'interface.
     * @param etatSauvegarde  L'état précédent du composant s'il a été reconstruit.
     * @return La hiérarchie de vues (View) prête à être affichée.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater constructeurVue, @Nullable ViewGroup conteneur, @Nullable Bundle etatSauvegarde) {
        View gabaritVue = constructeurVue.inflate(R.layout.fragment_detail, conteneur, false);

        TextView champAdresse = gabaritVue.findViewById(R.id.tv_detail_address);
        TextView champCoordonnees = gabaritVue.findViewById(R.id.tv_detail_coords);

        if (getArguments() != null) {
            // Protection contre les nulls : on utilise le FormatUtils
            String adresseSecurisee = FormatUtils.securiserTexteApi(getArguments().getString(CLE_ADRESSE), "Adresse non communiquée");
            champAdresse.setText(adresseSecurisee);

            // Appel de la méthode externalisée
            String texteCoordonnees = FormatUtils.formaterCoordonneesGps(
                    getArguments().getDouble(CLE_LATITUDE),
                    getArguments().getDouble(CLE_LONGITUDE)
            );
            champCoordonnees.setText(texteCoordonnees);
        }
        return gabaritVue;
    }
}