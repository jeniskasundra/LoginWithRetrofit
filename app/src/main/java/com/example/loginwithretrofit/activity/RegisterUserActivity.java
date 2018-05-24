package com.example.loginwithretrofit.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends Activity implements OnClickListener {
    private Button btnLinkToLoginScreen, btnRegister;
    EditText edtName, edtEmail, edtPassword, edtCpassword;
    private static final int REQUEST_CAMERA = 101;
    private static final int PICK_IMAGE_REQUEST = 102;
    private static final int REQUEST_CODE_CROP_IMAGE = 103;
    private Bitmap cameraBitmap;
    private ImageView imgPro, imgUploadPro;
    public static final String UPLOAD_KEY = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        bindView();
        addListener();

    }

    private void init() {

    }

    private void bindView() {
        edtName = (EditText) findViewById(R.id.edtNameRegister);
        edtEmail = (EditText) findViewById(R.id.edtEmailRegister);
        edtPassword = (EditText) findViewById(R.id.edtPasswordRegister);
        edtCpassword = (EditText) findViewById(R.id.edtCpasswordRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        imgUploadPro = (ImageView) findViewById(R.id.imgUploadProfile);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);
        imgPro = (ImageView) findViewById(R.id.imageView);

    }

    private void addListener() {

        btnRegister.setOnClickListener(this);
        btnLinkToLoginScreen.setOnClickListener(this);
        imgUploadPro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.btnRegister:

                if (edtName.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Name cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    edtName.setError("Name cannot be Blank");

                    return;

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        edtEmail.getText().toString()).matches()) {
                    // Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email",
                            Toast.LENGTH_LONG).show();
                    edtEmail.setError("Invalid Email");

                    return;

                } else if (edtPassword.getText().length() <= 7) {

                    Toast.makeText(getApplicationContext(),
                            "Password must be 8 characters above",
                            Toast.LENGTH_LONG).show();
                    edtPassword.setError("Password must be 8 characters above");

                    return;

                } else if (!edtPassword.getText().toString()
                        .equals(edtCpassword.getText().toString())) {

                    Toast.makeText(getApplicationContext(),
                            "Passwords do not match.", Toast.LENGTH_LONG).show();
                    edtPassword.setError("Passwords do not match.");

                    return;

                } else {

                    insertData();
                }

                break;

            case R.id.btnLinkToLoginScreen:

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.imgUploadProfile:
                selectImage();
                // showFileChooser();

                break;

            default:
                break;
        }

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File out = Environment.getExternalStorageDirectory();
        out = new File(out, "abc");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_CODE_CROP_IMAGE);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case PICK_IMAGE_REQUEST:

                    Uri u = data.getData();
                    performCrop(u);

                    break;

                case REQUEST_CAMERA:

                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "abc");
                    // Crop the captured image using an other intent
                    try {
                        /* the user's device may not support cropping */
                        performCrop(Uri.fromFile(file));
                    } catch (ActivityNotFoundException aNFE) {
                        // display an error message if user device doesn't support
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;

                case REQUEST_CODE_CROP_IMAGE:

                    if (resultCode == Activity.RESULT_OK) {
                        Bundle extras = data.getExtras();
                        cameraBitmap = extras.getParcelable("data");
                        imgPro.setImageBitmap(cameraBitmap);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void insertData() {

        final String name = edtName.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();

        BitmapDrawable drawable = (BitmapDrawable) imgPro.getDrawable();
        Bitmap profileBitmap = drawable.getBitmap();

        Bitmap bitmap = null;

        if (cameraBitmap == null) {
            bitmap = profileBitmap;
        } else {
            bitmap = cameraBitmap;
        }

        final String image = getStringImage(bitmap);


        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(RegisterUserActivity.this);
        dialog.setTitle("Loding");
        dialog.setMessage("Please Wait...");
        dialog.show();

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();

        Call<ResponseBody> call = api.registerUser(name, email, password, image);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Dismiss Dialog
                    dialog.dismiss();
                    try {
                        Toast.makeText(RegisterUserActivity.this, response.body().string().toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(RegisterUserActivity.this,
                            LoginActivity.class);
                    startActivity(i);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });


    }

}
