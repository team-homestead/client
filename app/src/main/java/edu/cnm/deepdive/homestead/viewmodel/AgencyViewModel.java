package edu.cnm.deepdive.homestead.viewmodel;

import android.util.Log;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Service;
import edu.cnm.deepdive.homestead.service.AgencyRepository;
import edu.cnm.deepdive.homestead.service.GoogleSignInService;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AgencyViewModel extends ViewModel implements LifecycleObserver {

  private MutableLiveData<Agency> agency;
  private MutableLiveData<Service> service;
  private MutableLiveData<List<Agency>> agencies;
  private MutableLiveData<List<Agency>> favoriteAgencies;
  private MutableLiveData<List<Agency>> searchAgencies;
  private MutableLiveData<List<Service>> services;
  private MutableLiveData<List<String>> serviceTypes;
  private final MutableLiveData<Throwable> throwable;
  private final AgencyRepository repository;
  private CompositeDisposable pending;

  public AgencyViewModel() {
    repository = AgencyRepository.getInstance();
    pending = new CompositeDisposable();
    agencies = new MutableLiveData<>();
    favoriteAgencies = new MutableLiveData<>();
    searchAgencies = new MutableLiveData<>();
    services = new MutableLiveData<>();
    serviceTypes = new MutableLiveData<>();
    agency = new MutableLiveData<>();
    service = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    refreshAgencies();
    refreshServices();
    refreshServiceTypes();
  }

  public LiveData<Agency> getAgency() {
    return agency;
  }

  public LiveData<Service> getService() {
    return service;
  }

  public LiveData<List<Agency>> getAgencies() {
    return agencies;
  }

  public LiveData<List<Agency>> getFavoriteAgencies() {
    return favoriteAgencies;
  }

  public LiveData<List<Agency>> getSearchAgencies() {
    return searchAgencies;
  }

  public LiveData<List<String>> getServiceTypes() {
    return serviceTypes;
  }

  public LiveData<List<Service>> getServices() {
    return services;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void refreshAgencies() {
    throwable.postValue(null);
    pending.add(
        repository.getAllAgencies()
            .subscribe(
                (agencies) -> {
                  this.agencies.postValue(agencies);
                  List<Agency> favorites = new LinkedList<>(agencies);
                  Iterator<Agency> iter = favorites.iterator();
                  while (iter.hasNext()) {
                    Agency agency = iter.next();
                    if (!agency.isFavorite()) {
                      iter.remove();
                    }
                  }
                  favoriteAgencies.postValue(favorites);
                },
                throwable::postValue
            )
    );
  }

  public void refreshServiceTypes() {
    throwable.postValue(null);
    pending.add(
        repository.getServiceTypes()
            .subscribe(
                serviceTypes::postValue,
                throwable::postValue
            )
    );
  }
  public void setSearchFilter(String fragment, String serviceType) {
    throwable.postValue(null);
    if (fragment != null || serviceType != null) {
      pending.add(
          repository.searchAgencies(fragment, serviceType)
              .subscribe(
                  searchAgencies::postValue,
                  throwable::postValue
              )
      );
    } else {
      searchAgencies.setValue(Collections.emptyList());
    }
  }

  public void refreshServices() {
    throwable.postValue(null);
    pending.add(
        repository.getAllServices()
            .subscribe(
                services::postValue,
                throwable::postValue
            )
    );
  }

  public void save(Agency agency) {
    throwable.setValue(null);
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
          pending.add(
              repository.saveAgency(account.getIdToken(), agency)
                  .subscribe(
                      () -> {
                        this.agency.postValue(null);
                        refreshAgencies();
                        refreshServices();
                      },
                      throwable::postValue
                  )
          );
        })
        .addOnFailureListener(throwable::postValue);
  }

  public void remove(Agency agency) {
    throwable.setValue(null);
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
          pending.add(
              repository.removeAgency(account.getIdToken(), agency)
                  .subscribe(
                      () -> {
                        this.agency.postValue(null);
                        refreshAgencies();
                      },
                      throwable::postValue
                  )
          );
        })
        .addOnFailureListener(throwable::postValue);
  }

  public void setAgencyId(UUID id) {
    throwable.setValue(null);
    pending.add(
        repository.get(id)
            .subscribe(
                agency::postValue,
                throwable::postValue
            )
    );
  }

  public void setFavorite(Agency agency, boolean favorite) {
    repository.setFavorite(agency, favorite);
    agency.setFavorite(favorite);
    this.agencies.setValue(this.agencies.getValue());
    List<Agency> favorites = favoriteAgencies.getValue();
    if (favorite) {
      favorites.add(agency);
    } else {
      favorites.remove(agency);
    }
    this.favoriteAgencies.setValue(favorites);
    Log.d(getClass().getName(), agency.getId().toString() + " toggled to " + agency.isFavorite());
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}