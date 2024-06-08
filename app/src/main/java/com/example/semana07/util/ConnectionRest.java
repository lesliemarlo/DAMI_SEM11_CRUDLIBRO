package com.example.semana07.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionRest {

    private static Retrofit retrofit ;

    private static final String URL = "https://app-rest-cibertec-210f267f7414.herokuapp.com/";

    public static Retrofit getConnection(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


}