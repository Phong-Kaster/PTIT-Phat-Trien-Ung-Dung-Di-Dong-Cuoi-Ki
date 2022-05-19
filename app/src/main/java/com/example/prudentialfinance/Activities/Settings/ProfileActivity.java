package com.example.prudentialfinance.Activities.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Settings.ProfileViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    User AuthUser;
    ImageButton backBtn;
    AppCompatImageView ivEdit;
    ImageView ivAvatar;
    TextView tvEmail;
    EditText firstname, lastname;
    AppCompatButton saveBtn;

    ProfileViewModel viewModel;
    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;

    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setComponent();

        setControl();

        setEvent();

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable) getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(ProfileActivity.this);
        alert = new Alert(ProfileActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        AuthUser = global.getAuthUser();
    }

    private void setEvent() {
        ivEdit.setOnClickListener(view -> {
            verifyStoragePermissions(this);

            Intent intent = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickerImageActivity.launch(intent);

        });

        backBtn.setOnClickListener(view -> finish());

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        saveBtn.setOnClickListener(view -> updateData());

        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.startLoadingDialog();
            } else {
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if (object == null) {
                alert.showAlert(getResources().getString(R.string.alertTitle),
                        getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                global.getAuthUser().setAvatar(object.getImage());
                FancyToast.makeText(this, object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                        R.drawable.ic_check, true).show();
                ;
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        viewModel.getObjectProfile().observe(this, object -> {
            if (object == null) {
                alert.showAlert(getResources().getString(R.string.alertTitle),
                        getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                global.setAuthUser(object.getData());
                FancyToast.makeText(this, object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                        R.drawable.ic_check, true).show();
                ;
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });
    }

    private void updateData() {
        String firstName = firstname.getText().toString().trim();
        String lastName = lastname.getText().toString().trim();
        String action = "save";

        viewModel.updateData(headers, action, firstName, lastName);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the
    // activity is displayed
    ActivityResultLauncher<Intent> pickerImageActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        selectedImage = data.getData();

                        loadImgToElement(selectedImage.toString(), true);
                    }
                }
            });

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        tvEmail = findViewById(R.id.signUpEmail);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        saveBtn = findViewById(R.id.saveBtn);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivEdit = findViewById(R.id.ivEdit);

        firstname.setText(AuthUser.getFirstname());
        lastname.setText(AuthUser.getLastname());
        tvEmail.setText(AuthUser.getEmail());

        loadImgToElement(HTTPService.UPLOADS_URL + "/" + AuthUser.getAvatar(), false);

    }

    private void uploadAvatar() {
        // get real path
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        String token = global.getAccessToken();

        viewModel.uploadAvatar(token, picturePath);
    }

    private void loadImgToElement(String img, boolean isUpload) {
        Picasso
                .get()
                .load(img)
                .fit()
                .placeholder(R.drawable.someone)
                .error(R.drawable.someone)
                .transform(Helper.getRoundedTransformationBuilder())
                .into(ivAvatar, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (isUpload && selectedImage != null) {
                            uploadAvatar();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1);
        }
    }
}