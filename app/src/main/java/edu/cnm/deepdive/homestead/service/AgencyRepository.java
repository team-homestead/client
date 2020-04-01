package edu.cnm.deepdive.homestead.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Content;
import edu.cnm.deepdive.homestead.model.Service;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AgencyRepository {

  private static final int NETWORK_POOL_SIZE = 10;
  private static final String OAUTH_HEADER_FORMAT = "Bearer %s";
  private static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
  private static final String FAVORITES_KEY = "favorites";

  private static Context context;

  private final HomesteadService proxy;
  private final Executor networkPool;
  private final DateFormat formatter;
  private final SharedPreferences sharedPreferences;

  @SuppressLint("SimpleDateFormat")
  private AgencyRepository() {
    proxy = HomesteadService.getInstance();
    networkPool = Executors.newFixedThreadPool(NETWORK_POOL_SIZE);
    formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void setContext(Context context) {
    AgencyRepository.context = context;
  }

  public static AgencyRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<List<Agency>> getAllAgencies() {
    return proxy.getAllAgencies()
        .subscribeOn(Schedulers.from(networkPool))
        .map((agencies) -> {
          Set<String> favorites = getFavorites();
          for (Agency agency : agencies) {
            agency.setFavorite(favorites.contains(agency.getId().toString()));
          }
          return agencies;
        });
  }

  public Single<List<Service>> getAllServices() {
    return proxy.getAllServices()
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Completable saveAgency(String token, Agency agency) {
    if (agency.getId() == null) {
      return Completable.fromSingle(
          proxy.postAgency(String.format(OAUTH_HEADER_FORMAT, token), agency)
              .subscribeOn(Schedulers.from(networkPool))
      );
    } else {
      return Completable.fromSingle(
          proxy.putAgency(String.format(OAUTH_HEADER_FORMAT, token), agency, agency.getId())
              .subscribeOn(Schedulers.from(networkPool))
      );
    }
  }

  public Completable removeAgency(String token, Agency agency) {
    if (agency.getId() != null) {
      return proxy.deleteAgency(String.format(OAUTH_HEADER_FORMAT, token), agency.getId())
            .subscribeOn(Schedulers.from(networkPool));
    } else {
      return Completable.complete();
    }
  }

  public Single<Agency> get(UUID id) {
    return proxy.getAgency(id)
        .subscribeOn(Schedulers.from(networkPool));
  }

  private Set<String> getFavorites() {
    return sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
  }

  public void setFavorite(Agency agency, boolean favorite) {
    Set<String> favorites = getFavorites();
    boolean updated = false;
    if (favorite) {
      updated = favorites.add(agency.getId().toString());
    } else {
      updated = favorites.remove(agency.getId().toString());
    }
    if (updated) {
      sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply();
    }
  }

  private static class InstanceHolder {

    private static final AgencyRepository INSTANCE = new AgencyRepository();

  }

}
