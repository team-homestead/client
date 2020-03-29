package edu.cnm.deepdive.homestead.controller;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.homestead.service.GoogleSignInService;
import edu.cnm.deepdive.homestead.R;

public class LoginActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 1000;

  private GoogleSignInService repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    repository = GoogleSignInService.getInstance();
    repository.refresh()
        .addOnSuccessListener((account) -> switchToMain())
        .addOnFailureListener((ex) -> {
          setContentView(R.layout.activity_login);
          findViewById(R.id.sign_in_button).setOnClickListener((v) ->
              repository.startSignIn(this, LOGIN_REQUEST_CODE));
        });
   /* Button button = (Button) findViewById(R.id.skip_button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
      }
    }); */
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == LOGIN_REQUEST_CODE) {
      repository.completeSignIn(data)
          .addOnSuccessListener((account) -> switchToMain())
          .addOnFailureListener((ex) ->
              Toast.makeText(this, R.string.login_failure, Toast.LENGTH_LONG).show());
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void switchToMain() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  public void skipToMain(View view) {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

}





