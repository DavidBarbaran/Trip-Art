package art.trip.com.tripart.networking;

import java.util.List;
import java.util.concurrent.TimeUnit;

import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Image;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

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



    @GET("/image.json")
    Call<List<Image>> getData();

}
