package edu.cnm.deepdive.homestead.service;

import android.annotation.SuppressLint;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AgencyRepository {

  private static final int NETWORK_POOL_SIZE = 10;
  private static final String OAUTH_HEADER_FORMAT = "Bearer %s";
  private static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

  private final HomesteadService proxy;
  private final Executor networkPool;
  private final DateFormat formatter;

  @SuppressLint("SimpleDateFormat")
  private AgencyRepository() {
    proxy = HomesteadService.getInstance();
    networkPool = Executors.newFixedThreadPool(NETWORK_POOL_SIZE);
    formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
  }

  public static AgencyRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<List<Agency>> getAllAgencies(String token) {
    return proxy.getAllAgencies(String.format(OAUTH_HEADER_FORMAT, token))
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Single<List<Service>> getAllServices(String token) {
    return proxy.getAllServices(String.format(OAUTH_HEADER_FORMAT, token))
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

  public Single<Agency> get(String token, UUID id) {
    return proxy.getAgency(String.format(OAUTH_HEADER_FORMAT, token), id)
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Single<List<Content>> getAllContents(String token) {
    return getAllServices(token)
        .subscribeOn(Schedulers.io())
        .map((services) -> {
          List<Content> combined = new ArrayList<>();
          for (Service service : services) {
            combined.add(service);
            Collections.addAll(combined, service.getAgency());
          }
          return combined;
        });
  }

  private static class InstanceHolder {

    private static final AgencyRepository INSTANCE = new AgencyRepository();

  }

}
