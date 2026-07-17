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
}