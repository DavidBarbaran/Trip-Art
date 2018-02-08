package art.trip.com.tripart.networking;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.List;
import java.util.concurrent.TimeUnit;

import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Audio;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.model.Video;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by David on 14/01/2018.
 */

public interface RestApi {

    OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(Setting.TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Setting.TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(Setting.TIMEOUT, TimeUnit.SECONDS)
            .build();

    Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Setting.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OK_HTTP_CLIENT)
            .build();

    @GET("/image.json?")
    Call<JsonObject> getImage(@Query("orderBy") String order, @Query("limitToLast") int lengthLast);

    @GET("/audio.json?")
    Call<JsonObject> getAudio(@Query("orderBy") String order, @Query("limitToLast") int lengthLast);

    @GET("/image.json?")
    Call<JsonObject> getImageById(@Query("orderBy") String order, @Query("equalTo") int lengthLast);

    @GET("/audio.json?")
    Call<List<Audio>> getAudio();

    @GET("/video.json")
    Call<List<Video>> getVideo();
}
