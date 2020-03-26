package edu.cnm.deepdive.homestead;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.homestead.service.GoogleSignInService;

public class HomesteadApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
    Stetho.initializeWithDefaults(this);
  }
}
