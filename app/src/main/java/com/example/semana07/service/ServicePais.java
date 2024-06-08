package com.example.semana07.service;


import com.example.semana07.entity.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicePais {

    @GET("servicio/util/listaPais")
    public Call<List<Pais>> listaTodos();


}
