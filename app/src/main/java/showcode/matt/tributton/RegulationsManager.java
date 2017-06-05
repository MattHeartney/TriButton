package showcode.matt.tributton;

import android.content.Context;
import android.util.Log;

import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manager class to access api.data.gov JSON information.
 */

public class RegulationsManager {

    private String API_BASE_URL = "https://api.data.gov/";
    private RegulationsClient client;
    private Context context;

    public RegulationsManager(Context context) {
        this.context = context;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        client =  retrofit.create(RegulationsClient.class);
    }

    public void retrieveJSON(final Consumer<ResponseBody> callback) {
        Call<ResponseBody> call =
                client.data(context.getString(R.string.regulations_gov_key));

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody data = response.body();
                try {
                    callback.accept(data);
                } catch (Exception e) {}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // the network call was a failure
                callback.accept(null);
            }
        });
    }
}
