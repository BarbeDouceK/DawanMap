package fr.dawan.myapplication.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FormatUtilsTest {

    @Test
    public void verificationFormatageArrondi() {
        // On teste avec plus de 5 décimales pour vérifier l'arrondi
        String resultat = FormatUtils.formaterCoordonneesGps(47.123456, -1.987654);

        // Le 6ème chiffre arrondi au supérieur/inférieur
        assertEquals("Coordonnées GPS : 47.12346, -1.98765", resultat);
    }

    @Test
    public void verificationFormatageZeros() {
        // On teste des entiers purs pour vérifier l'ajout des zéros
        String resultat = FormatUtils.formaterCoordonneesGps(47.0, -1.0);

        assertEquals("Coordonnées GPS : 47.00000, -1.00000", resultat);
    }
    @Test
    public void verificationTexteApi_avecValeurNulle() {
        // Scénario : L'API ne renvoie pas le champ "adresse" (null)
        String resultat = FormatUtils.securiserTexteApi(null, "Adresse inconnue");

        // On s'attend à recevoir la valeur de secours
        assertEquals("Adresse inconnue", resultat);
    }

    @Test
    public void verificationTexteApi_avecValeurVide() {
        // Scénario : L'API renvoie un titre rempli d'espaces blancs ou vide
        String resultat = FormatUtils.securiserTexteApi("   ", "Titre non renseigné");

        // On s'attend à recevoir la valeur de secours
        assertEquals("Titre non renseigné", resultat);
    }

    @Test
    public void verificationTexteApi_avecValeurValide() {
        // Scénario : L'API renvoie une donnée parfaitement valide
        String resultat = FormatUtils.securiserTexteApi("10 rue de la Paix", "Adresse inconnue");

        // On s'attend à recevoir la donnée d'origine, intacte
        assertEquals("10 rue de la Paix", resultat);
    }
}