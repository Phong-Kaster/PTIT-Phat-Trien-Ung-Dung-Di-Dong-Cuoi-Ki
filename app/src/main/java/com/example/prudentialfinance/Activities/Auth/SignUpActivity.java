package com.example.prudentialfinance.Activities.Auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Auth.SignUpViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpFirstname, signUpLastname, signUpEmail, signUpPass, signUpPassConfirm;
    TextView signUpLoginBtn;
    AppCompatButton signUpBtn;
    SignUpViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    GlobalVariable state;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    ImageButton loginSignInWithGoogle, loginSignInWithFacebook;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setComponent();
        setControl();
        setEvent();
    }

    private void setComponent() {
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        loadingDialog = new LoadingDialog(SignUpActivity.this);
        alert = new Alert(SignUpActivity.this, 1);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

    }

    private void setControl() {
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn);
        signUpFirstname = findViewById(R.id.signUpFirstname);
        signUpLastname = findViewById(R.id.signUpLastname);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPass);
        signUpPassConfirm = findViewById(R.id.signUpPassConfirm);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginSignInWithGoogle = findViewById(R.id.loginSignInWithGoogle);
        loginSignInWithFacebook = findViewById(R.id.loginSignInWithFacebook);
    }

    private void setAuthorizedToken(String accessToken) {
        String token = "JWT " + accessToken.trim();
        state = ((GlobalVariable) this.getApplication());

        state.setAccessToken(token);

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        signUpLoginBtn.setOnClickListener(view -> finish());
        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        signUpBtn.setOnClickListener(view -> {
            String firstName = signUpFirstname.getText().toString();
            String lastName = signUpLastname.getText().toString();
            String email = signUpEmail.getText().toString();
            String pass = signUpPass.getText().toString();
            String passConfirm = signUpPassConfirm.getText().toString();

            viewModel.signUp(firstName, lastName, email, pass, passConfirm);
        });

        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.startLoadingDialog();
            } else {
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if (object == null) {
                setAuthorizedToken("");
                alert.showAlert(getResources().getString(R.string.alertTitle),
                        getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                setAuthorizedToken(object.getAccessToken());
                state.setAuthUser(object.getData());

                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);

                FancyToast.makeText(this, object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                        R.drawable.ic_check, true).show();
                finish();
            } else {
                setAuthorizedToken("");
                state.setAuthUser(null);
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        loginSignInWithGoogle.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            loginWithGoogle.launch(intent);
        });

        loginSignInWithFacebook.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onError(@NonNull FacebookException e) {
                        alert.showAlert(getString(R.string.alertTitle), e.getMessage(), R.drawable.ic_close);
                    }

                    @Override
                    public void onCancel() {
                        alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault),
                                R.drawable.ic_close);
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        viewModel.loginFacebook(accessToken.getToken());
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    ActivityResultLauncher<Intent> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            viewModel.loginGoogle(idToken);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more
            // information.
            alert.showAlert(getString(R.string.alertTitle), "signInResult:failed code=" + e.getStatusCode(),
                    R.drawable.ic_close);
        }
    }
}