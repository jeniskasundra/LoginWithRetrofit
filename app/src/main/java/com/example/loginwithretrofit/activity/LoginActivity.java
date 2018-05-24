package com.example.loginwithretrofit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginwithretrofit.R;
import com.example.loginwithretrofit.model.User;
import com.example.loginwithretrofit.model.UserList;
import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.utils.Config;
import com.example.loginwithretrofit.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements OnClickListener {

    Button btnLinkToRegisterScreen, btnLogin;
    EditText edtEmail, edtPassword;
    private String JSON_STRING;
    private int id;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isStoragePermissionGranted();
        init();
        bindView();
        addListener();
    }

    private void init() {
        userList = new ArrayList<>();

    }

    private void bindView() {
        btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    private void addListener() {
        btnLogin.setOnClickListener(this);
        btnLinkToRegisterScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.btnLogin:

                if (edtEmail.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Email id cannot be Blank", Toast.LENGTH_LONG).show();
                    edtEmail.setError("Email cannot be Blank");

                    return;

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        edtEmail.getText().toString()).matches()) {

                    Toast.makeText(getApplicationContext(), "Invalid Email",
                            Toast.LENGTH_LONG).show();
                    edtEmail.setError("Invalid Email");

                } else if (edtPassword.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Password cannot be Blank", Toast.LENGTH_LONG).show();
                    edtPassword.setError("Password cannot be Blank");

                    return;
                } else {
                    showUsers();
                }

                break;

            case R.id.btnLinkToRegisterScreen:

                Intent i = new Intent(getApplicationContext(), RegisterUserActivity.class);
                startActivity(i);

                break;

            default:
                break;
        }

    }

    private void showUsers() {


        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Login");
            dialog.setMessage("Please Wait...");
            dialog.show();

            //Creating an object of our api interface
            ApiInterface api = ApiClient.getApiService();

            /**
             * Calling JSON
             */
            Call<UserList> call = api.getAllUser();

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(Call<UserList> call, Response<UserList> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        userList = response.body().getResult();

                        Boolean login = false;
                        for (int i = 0; i < userList.size(); i++) {
                            if (edtEmail.getText().toString()
                                    .equals(userList.get(i).getEmail())
                                    && edtPassword.getText().toString()
                                    .equals(userList.get(i).getPassword())) {

                                id = userList.get(i).getId();

                                login = true;

                            }
                        }

                        if (login) {
                            Intent intent = new Intent(getApplicationContext(), WelComeActivity.class);
                            intent.putExtra(Config.TAG_ID, id);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Sucess.....", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid Email Id of Password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Some Thing Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserList> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(),
                    "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat
                        .requestPermissions(
                                this,
                                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                1);
                return false;
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was "
                    + grantResults[0]);
            // resume tasks needing this permission
        } else {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            1);
        }
    }

}