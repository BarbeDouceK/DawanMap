package fr.dawan.myapplication.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe utilitaire implémentant le patron de conception Singleton pour la configuration du client HTTP.
 * <p>
 * Fournit une instance unique de {@link Retrofit} configurée pour communiquer avec l'interface
 * de programmation (API) de Dawan. Utilise GSON pour la conversion automatique des objets JSON.
 * </p>
 */
public class RetrofitClient {

    /**
     * Instance unique (Singleton) du client Retrofit.
     */
    private static Retrofit retrofit = null;

    /**
     * Récupère l'instance globale du client HTTP.
     * Si l'instance n'existe pas encore, elle est construite avec l'URL de base et le convertisseur JSON.
     *
     * @return L'instance configurée de {@link Retrofit}.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://dawan.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}