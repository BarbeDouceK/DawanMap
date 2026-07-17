package fr.dawan.myapplication.utils;

import java.util.Locale;

public class FormatUtils {

    /**
     * Formate les coordonnées GPS
     */
    public static String formaterCoordonneesGps(double latitude, double longitude) {
        // L'utilisation de Locale.US force l'utilisation du point "." pour les décimales
        // Cela évite que tes tests plantent si l'ordinateur qui compile est configuré en français (qui utilise la virgule)
        return String.format(Locale.US, "Coordonnées GPS : %.5f, %.5f", latitude, longitude);
    }
}