package com.example.myapplication.Home.Animal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AnimalApiService {
    @GET("AbdmAnimalProtect")
    Call<AnimalResponse> getAnimals(
            @Query("KEY") String apiKey,
            @Query("Type") String type,
            @Query("SPECIES_NM") String speciesName

    );
}

