package com.example.semana07.service;


import com.example.semana07.entity.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategoriaLibro {

    @GET("servicio/util/listaCategoriaDeLibro")
    public Call<List<Categoria>> listaTodos();

}
