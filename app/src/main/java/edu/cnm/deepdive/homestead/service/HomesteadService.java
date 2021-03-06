package edu.cnm.deepdive.homestead.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.homestead.BuildConfig;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Service;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomesteadService {

  @GET("agencies")
  Single<List<Agency>> getAllAgencies();

  @GET("agencies/{id}")
  Single<Agency> getAgency(@Path("id") UUID id);

  @GET("service-types")
  Single<List<String>> getServiceTypes();

  @GET("services")
  Single<List<Service>> getAllServices();

  @GET("agencies/search")
  Single<List<Agency>> search(@Query("q") String fragment, @Query("service-type") String type);


  @POST("agencies")
  Single<Agency> postAgency(@Header("Authorization") String oauthHeader, @Body Agency agency);

  @PUT("agencies/{id}")
  Single<Agency> putAgency(
      @Header("Authorization") String oauthHeader, @Body Agency agency, @Path("id") UUID id);

  @DELETE("agencies/{id}")
  Completable deleteAgency(
      @Header("Authorization") String oauthHeader, @Path("id") UUID id);

  static HomesteadService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final HomesteadService INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .setDateFormat(TIMESTAMP_FORMAT)
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .readTimeout(60, TimeUnit.SECONDS)
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(HomesteadService.class);
    }
  }

}
