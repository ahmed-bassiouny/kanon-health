package com.germanitlab.kanonhealth.async;


import com.germanitlab.kanonhealth.helpers.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eslam on 8/28/16.
 */
public class ApiClient {

   // public static final String BASE_URL = "http://172.16.10.12:8080/RestService/api/";
//    public static final String BASE_URL = "http://41.33.24.57:8080/RestService/api/";
//    public static final String restauranBanner = "https://Orderingfood.upskwt.com:6060/Restaurants/Large/";
//    public static final String restaurantLogo = "https://Orderingfood.upskwt.com:6060/Restaurants/small/";
//    public static final String itemsBanner = "https://Orderingfood.upskwt.com:6060/Items/Large/";
//    public static final String itemLogo = "https://Orderingfood.upskwt.com:6060/Items/small/";
//    public static final String offerBunner = "https://orderingfood.upskwt.com:6060/Offers/large/";
//    public static final String offerLogo = "https://Orderingfood.upskwt.com:6060/Offers/small/";


    private static Retrofit retrofit = null;


    public static Retrofit getClient() {

        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient
                    .Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);
            httpClient.addInterceptor(loggingInterceptor);  // <-- this is the important line!
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.CHAT_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;


    }
}
