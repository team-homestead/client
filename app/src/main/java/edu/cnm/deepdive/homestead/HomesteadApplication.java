package edu.cnm.deepdive.homestead;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.homestead.service.AgencyRepository;
import edu.cnm.deepdive.homestead.service.GoogleSignInService;
import edu.cnm.deepdive.homestead.service.HomesteadService;

public class HomesteadApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
    AgencyRepository.setContext(this);
    Stetho.initializeWithDefaults(this);
  }
}
