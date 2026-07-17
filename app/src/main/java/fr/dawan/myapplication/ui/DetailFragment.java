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
    private static final String CLE_ADRESSE = "adresse_centre";
    private static final String CLE_LATITUDE = "latitude_centre";
    private static final String CLE_LONGITUDE = "longitude_centre";

    /**
     * Fabrique une nouvelle instance du composant avec ses paramètres requis.
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater constructeurVue, @Nullable ViewGroup conteneur, @Nullable Bundle etatSauvegarde) {
        View gabaritVue = constructeurVue.inflate(R.layout.fragment_detail, conteneur, false);

        TextView champAdresse = gabaritVue.findViewById(R.id.tv_detail_address);
        TextView champCoordonnees = gabaritVue.findViewById(R.id.tv_detail_coords);

        if (getArguments() != null) {
            champAdresse.setText(getArguments().getString(CLE_ADRESSE));

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