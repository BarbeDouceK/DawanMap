package fr.dawan.myapplication.ui;

import static fr.dawan.myapplication.ui.DetailFragment.CLE_ADRESSE;
import static fr.dawan.myapplication.ui.DetailFragment.CLE_LATITUDE;
import static fr.dawan.myapplication.ui.DetailFragment.CLE_LONGITUDE;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.dawan.myapplication.R;

@RunWith(AndroidJUnit4.class)
public class DetailFragmentTest {

    @Test
    public void verifieLaCreationDeLaNouvelleInstance() {
        // 1. Préparation des données factices (*mocks*)
        String adresseAttendue = "10 Rue de la Paix, Paris";
        double latitudeAttendue = 48.8698;
        double longitudeAttendue = 2.3322;

        // 2. Appel de la méthode statique (la fabrique)
        DetailFragment fragment = DetailFragment.nouvelleInstance(
                adresseAttendue,
                latitudeAttendue,
                longitudeAttendue
        );

        // 3. Récupération du paquet de données (*Bundle*)
        Bundle arguments = fragment.getArguments();

        // 4. Vérifications (*Assertions*)
        Assert.assertNotNull("Les arguments ne devraient pas être nuls", arguments);
        Assert.assertEquals("L'adresse n'est pas celle attendue", adresseAttendue, arguments.getString(CLE_ADRESSE));
        Assert.assertEquals("La latitude n'est pas celle attendue", latitudeAttendue, arguments.getDouble(CLE_LATITUDE), 0.0001);
        Assert.assertEquals("La longitude n'est pas celle attendue", longitudeAttendue, arguments.getDouble(CLE_LONGITUDE), 0.0001);
    }

    @Test
    public void verifieLeRenduDesDonneesSurLaVue() {
        // 1. Définition des paramètres
        String adresseMock = "Nantes";
        double latitudeMock = 47.2184;
        double longitudeMock = -1.5536;

        // 2. Initialisation du paquet de données (*Bundle*)
        Bundle arguments = new Bundle();
        arguments.putString("adresse_centre", adresseMock);
        arguments.putDouble("latitude_centre", latitudeMock);
        arguments.putDouble("longitude_centre", longitudeMock);

        // 3. Lancement du composant de manière isolée
        FragmentScenario.launchInContainer(DetailFragment.class, arguments);

        // 4. Vérification de l'adresse (on suppose que FormatUtils renvoie "Nantes")
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_address))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText(adresseMock)));

        // 5. Vérification des coordonnées
        String coordonneesAttendues = "48.8698, 2.3322"; // Adapte selon le formatage réel (ex: avec ° N, etc.)
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_coords))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText(coordonneesAttendues)));
    }

    @Test
    public void verifieLaValeurParDefautSiAdresseVide() {
        Bundle arguments = new Bundle();
        // On passe intentionnellement une adresse nulle
        arguments.putString("adresse_centre", null);
        arguments.putDouble("latitude_centre", 0.0);
        arguments.putDouble("longitude_centre", 0.0);

        FragmentScenario.launchInContainer(DetailFragment.class, arguments);

        // Vérification de la valeur de repli (*fallback*) "Adresse non communiquée"
        Espresso.onView(ViewMatchers.withId(R.id.tv_detail_address))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText("Adresse non communiquée")));
    }
}