package com.av.avmessenger.Class;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login.php")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    @POST("ajouter_utilisateur.php")
    Call<ServerResponse> ajouterUtilisateur(@Body UserData userData);
}