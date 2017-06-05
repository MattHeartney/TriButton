package showcode.matt.tributton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit class for api calls to api.data.gov
 */

public interface RegulationsClient {

    @GET("regulations/v3/documents")
    Call<ResponseBody> data(
            @Query("api_key") String apiKey
    );
}
