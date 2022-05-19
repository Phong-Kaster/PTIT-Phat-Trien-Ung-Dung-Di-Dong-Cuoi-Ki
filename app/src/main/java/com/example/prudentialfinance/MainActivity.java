package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.prudentialfinance.Activities.Auth.LoginActivity;
import com.example.prudentialfinance.Activities.Settings.DarkModeActivity;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LanguageManager;
import com.example.prudentialfinance.Helpers.Notification;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.ViewModel.MainViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private Intent intent;
    private LanguageManager languageManager;
    /**
     * Activities Order Thread
     * 1. Main Activity
     * 2. Login Activity or Introduce Activity or Sign Up Activity
     * 3. Home Activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        GlobalVariable globalVariable = ((GlobalVariable) this.getApplication());

        languageManager = new LanguageManager(this, globalVariable.getAppName());

        SharedPreferences preferences = this.getApplication().getSharedPreferences(globalVariable.getAppName(), this.MODE_PRIVATE);
        String accessToken  = preferences.getString("accessToken",null);
        Boolean isFirstOpen  = preferences.getBoolean("isFirstOpen",true);

        languageManager.getCurrent();
        languageManager.updateResource();

        viewModel.getObjectLogin().observe(this, object -> {
            if(object != null && object.getResult() == 1){
                globalVariable.setAuthUser( object.getData() );
                globalVariable.setAccessToken(accessToken);
                intent = new Intent(MainActivity.this, HomeActivity.class);

                String fullName =  object.getData().getFirstname() + " " + object.getData().getLastname();
                Notification notification = new Notification();
                notification.showNotification(MainActivity.this,
                        getString(R.string.introduce5) + fullName + " !", getString(R.string.nice_day) );

                languageManager.setLang(object.getData().getLanguage());
                languageManager.updateResource();
            }else{
                intent = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                preferences.edit().putString("accessToken", "").apply();
            }
            openActivity();
        });


        Alert alert = new Alert(this, 1);

        viewModel.getObjectAppInfo().observe(this, object -> {
            if(object == null){
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }
            if(object.getResult() == 1){
                // set app info into global variable
                globalVariable.setAppInfo(object.getData());

                // check token is saved in local
                if(accessToken != null && !accessToken.isEmpty()){
                    viewModel.getInfoUser("JWT " + accessToken);
                }
                else{
                    intent = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                    openActivity();
                }
            }
        });

        alert.btnOK.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finishAffinity();
        });

        viewModel.getInfoSettings();
    }

    private void openActivity(){
        startActivity(intent);
        finish();
    }
}