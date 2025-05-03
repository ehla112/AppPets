package co.edu.uniminuto.appproject.apiexterna;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenFDAService {
    @GET("animalandveterinary/event.json")
    Call<OpenFDAResponse> buscarProducto(@Query("search") String nombre, @Query("limit") int limit);
}
