package edu.cnm.deepdive.homestead.viewmodel;

import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Content;
import edu.cnm.deepdive.homestead.model.Service;
import edu.cnm.deepdive.homestead.service.AgencyRepository;
import edu.cnm.deepdive.homestead.service.GoogleSignInService;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import java.util.UUID;

public class AgencyViewModel extends ViewModel implements LifecycleObserver {

    private MutableLiveData<Agency> agency;
    private MutableLiveData<Service> service;
    private MutableLiveData<List<Agency>> agencies;
    private MutableLiveData<List<Service>> services;
    private MutableLiveData<List<Content>> contents;
    private final MutableLiveData<Throwable> throwable;
    private final AgencyRepository repository;
    private CompositeDisposable pending;

    public AgencyViewModel() {
        repository = AgencyRepository.getInstance();
        pending = new CompositeDisposable();
        agencies = new MutableLiveData<>();
        services = new MutableLiveData<>();
        contents = new MutableLiveData<>();
        agency = new MutableLiveData<>();
        service = new MutableLiveData<>();
        throwable = new MutableLiveData<>();
        refreshAgencies();
        refreshServices();
        refreshContents();
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

    public LiveData<List<Service>> getServices() {
        return services;
    }

    public LiveData<List<Content>> getContents() {
      return contents;
    }

    public LiveData<Throwable> getThrowable() {
        return throwable;
    }

    public void refreshAgencies() {
        throwable.postValue(null);
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener((account) -> {
                pending.add(
                    repository.getAllAgencies(account.getIdToken())
                        .subscribe(
                            agencies::postValue,
                            throwable::postValue
                        )
                );
            })
            .addOnFailureListener(throwable::postValue);
    }

    public void refreshServices() {
        throwable.postValue(null);
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener((account) -> {
                pending.add(
                    repository.getAllServices(account.getIdToken())
                        .subscribe(
                            services::postValue,
                            throwable::postValue
                        )
                );
            })
            .addOnFailureListener(throwable::postValue);
    }

  public void refreshContents() {
    throwable.postValue(null);
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
          pending.add(
              repository.getAllContents(account.getIdToken())
                  .subscribe(
                      contents::postValue,
                      throwable::postValue
                  )
          );
        })
        .addOnFailureListener(throwable::postValue);
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
                                refreshContents();
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
                                refreshContents();
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
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener(
                (account) -> pending.add(
                    repository.get(account.getIdToken(), id)
                        .subscribe(
                            agency::postValue,
                            throwable::postValue
                        )
                )
            )
            .addOnFailureListener(throwable::postValue);
    }

  /*  public void save(Service service) {
        throwable.setValue(null);
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener((account) -> {
                pending.add(
                    repository.saveService(account.getIdToken(), service)
                        .subscribe(
                            () -> {
                                this.service.postValue(null);
                                refreshContents();
                                refreshAgencies();
                                refreshServices();
                            },
                            throwable::postValue
                        )
                );
            })
            .addOnFailureListener(throwable::postValue);
    }

    public void remove(Service service) {
        throwable.setValue(null);
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener((account) -> {
                pending.add(
                    repository.removeAgency(account.getIdToken(), service)
                        .subscribe(
                            () -> {
                                this.service.postValue(null);
                                refreshContents();
                                refreshServices();
                            },
                            throwable::postValue
                        )
                );
            })
            .addOnFailureListener(throwable::postValue);
    }

    public void setServiceId(UUID id) {
        throwable.setValue(null);
        GoogleSignInService.getInstance().refresh()
            .addOnSuccessListener(
                (account) -> pending.add(
                    repository.get(account.getIdToken(), id)
                        .subscribe(
                            service::postValue,
                            throwable::postValue
                        )
                )
            )
            .addOnFailureListener(throwable::postValue);
    } */

    @OnLifecycleEvent(Event.ON_STOP)
    private void clearPending() {
        pending.clear();
    }

}