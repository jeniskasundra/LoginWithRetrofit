package com.example.loginwithretrofit.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.utils.Config;
import com.example.loginwithretrofit.R;
import com.example.loginwithretrofit.preference.UserPrefs;
import com.example.loginwithretrofit.activity.WelComeActivity;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

	Button btnUpdateProfile;
	EditText edtUserName;
	ImageView imgProfilePic, imgUploadPic;
	int id;
	private static final int REQUEST_CAMERA = 101;
	private static final int PICK_IMAGE_REQUEST = 102;
	private static final int REQUEST_CODE_CROP_IMAGE = 103;
	private Bitmap cameraBitmap;

	public EditProfileFragment() {
		super();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_edit_profile, container,
				false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("Edit Profile");

		bindView();
		addListner();
		setData();

	}

	private void bindView() {
		edtUserName = (EditText) getActivity().findViewById(
				R.id.edtNameUpdateProfile);
		btnUpdateProfile = (Button) getActivity().findViewById(
				R.id.btnUpdateProfile);
		imgProfilePic = (ImageView) getActivity().findViewById(
				R.id.imgProfilePicUpdateProfile);
		imgUploadPic = (ImageView) getActivity().findViewById(
				R.id.imgUploadPicUpdateProfile);

	}

	private void addListner() {
		imgUploadPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectImage();

			}
		});
		btnUpdateProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (edtUserName.getText().toString().length() == 0) {

					Toast.makeText(getActivity(), "UserList Name Cannot be Blank",
							Toast.LENGTH_LONG).show();
					edtUserName.setError("Name cannot be Blank");

					return;

				} else {
					updateData();
				}

			}
		});

	}

	private void setData() {
		UserPrefs userPrefs = new UserPrefs(getActivity());
		id = userPrefs.getUserId();
		String name = userPrefs.getUserName();
		String image = userPrefs.getUserImage();

		edtUserName.setText(name);
		Picasso.with(getActivity()).load(Config.URL_IMAGES + image)
				.into(imgProfilePic);
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
			Toast toast = Toast.makeText(getActivity(), errorMessage,
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == getActivity().RESULT_OK) {

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
					Toast toast = Toast.makeText(getActivity(), errorMessage,
							Toast.LENGTH_SHORT);
					toast.show();
				}
				break;

			case REQUEST_CODE_CROP_IMAGE:

				if (resultCode == Activity.RESULT_OK) {
					Bundle extras = data.getExtras();
					cameraBitmap = extras.getParcelable("data");
					imgProfilePic.setImageBitmap(cameraBitmap);
				}
				break;
			}
		}
	}

	private String getStringImage(Bitmap bmp) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	private void updateData() {
		final String name = edtUserName.getText().toString().trim();
		BitmapDrawable drawable = (BitmapDrawable) imgProfilePic.getDrawable();
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
		dialog = new ProgressDialog(getActivity());
		dialog.setTitle("Loding");
		dialog.setMessage("Please Wait...");
		dialog.show();

		//Creating an object of our api interface
		ApiInterface api = ApiClient.getApiService();

		Call<ResponseBody> call = api.updateUser(id,name,image);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					//Dismiss Dialog
					dialog.dismiss();
					Intent i = new Intent(getActivity(),WelComeActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getActivity().startActivity(i);

					try {
						Toast.makeText(getActivity(), response.body().string().toString(), Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
