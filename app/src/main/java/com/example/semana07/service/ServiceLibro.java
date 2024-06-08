package com.example.semana07.service;

import com.example.semana07.entity.Libro;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceLibro {


    @GET("servicio/libro/porTitulo/{titulo}")
    public Call<List<Libro>> listaPorTitulo(@Path("titulo")String titulo);


    @GET("servicio/libro/porTituloIgual/{titulo}")
    public Call<List<Libro>> listaPorTituloIgual(@Path("titulo")String titulo);

    @POST("servicio/libro")
    public Call<Libro> registraLibro(@Body Libro obj);

    @PUT("servicio/libro")
    public Call<Libro> actualizaLibro(@Body Libro obj);


}
