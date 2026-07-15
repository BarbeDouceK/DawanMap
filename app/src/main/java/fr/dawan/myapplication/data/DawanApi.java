package fr.dawan.myapplication.data;

import fr.dawan.myapplication.model.Location;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DawanApi {
    /**
     * Interface définissant les points d'entrée du service Web REST.
     * @return Une liste d'objets Location encapsulée dans un appel Retrofit.
     */
    @GET("public/location/")
    Call<List<Location>> getLocations();
}