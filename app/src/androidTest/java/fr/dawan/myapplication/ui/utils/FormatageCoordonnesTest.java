package fr.dawan.myapplication.ui.utils;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.dawan.myapplication.R;
import fr.dawan.myapplication.ui.DetailFragment;

@RunWith(AndroidJUnit4.class)
public class FormatageCoordonnesTest {

    @Test
    public void verifierAffichageComposantsDetailFragment() {
        // 1. Préparer les données factices (mocks)
        String adresseMock = "10 Rue de la Paix, Paris";
        double latitudeMock = 48.8698;
        double longitudeMock = 2.3322;

        // 2. Créer le paquet de données (Bundle) attendu par ton Fragment
        // Ces clés doivent correspondre à celles utilisées dans ta méthode "nouvelleInstance"
        Bundle arguments = new Bundle();
        arguments.putString("ARG_ADRESSE", adresseMock);
        arguments.putDouble("ARG_LATITUDE", latitudeMock);
        arguments.putDouble("ARG_LONGITUDE", longitudeMock);

        // 3. Lancer le Fragment de manière isolée dans un conteneur
        FragmentScenario.launchInContainer(DetailFragment.class, arguments);

        // 4. Vérifier que les composants sont bien dessinés à l'écran
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_title))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.barre_horizontale))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // 5. Vérifier que les données sont bien injectées et formatées
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_address))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText(adresseMock)));

        // Remplace la chaîne de caractères ci-dessous par le format exact attendu par ton application
        String coordonneesFormateesAttendues = "48.8698, 2.3322";
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_coords))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText(coordonneesFormateesAttendues)));
    }
}