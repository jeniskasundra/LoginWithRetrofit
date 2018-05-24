package com.example.loginwithretrofit.fragment;

import java.io.IOException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.R;
import com.example.loginwithretrofit.preference.UserPrefs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

	private EditText edtOldPass, edtNewPass, edtConfirmPass;
	private Button btnChangePass;
	private int id;
	private String password;

	public ChangePasswordFragment() {
		super();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_change_password, container,
				false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("Change Password");
		getData();
		bindView();
		addListener();
	}

	private void getData() {
		UserPrefs up = new UserPrefs(getActivity());
		id = up.getUserId();
		password = up.getUserPassword();
	}

	private void bindView() {

		edtOldPass = (EditText) getActivity().findViewById(R.id.edtOldPassWord);
		edtNewPass = (EditText) getActivity().findViewById(R.id.edtNewPassword);
		edtConfirmPass = (EditText) getActivity().findViewById(
				R.id.edtConfirmPassword);
		btnChangePass = (Button) getActivity().findViewById(
				R.id.btnChangePassword);
	}

	private void addListener() {

		btnChangePass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edtOldPass.getText().toString().length() == 0) {

					Toast.makeText(getActivity(),
							"Old Password cannot be Blank", Toast.LENGTH_LONG)
							.show();
					edtOldPass.setError("Old Password cannot be Blank");

					return;

				} else if (edtNewPass.getText().toString().length() == 0) {
					Toast.makeText(getActivity(),
							"New Password cannot be Blank", Toast.LENGTH_LONG)
							.show();
					edtNewPass.setError("New Password cannot be Blank");
					return;
					
				} else if (edtNewPass.getText().length() <= 7) {

					Toast.makeText(getActivity(),
							"Password must be 8 characters above",
							Toast.LENGTH_LONG).show();
					edtNewPass.setError("Password must be 8 characters above");

					return;

				} else if (!password.equals(edtOldPass.getText().toString())) {
					Toast.makeText(getActivity(), "Old Password Is Not Match.",
							Toast.LENGTH_SHORT).show();
					edtOldPass.setError("Old Password Is Not Match");

				} else if (!edtNewPass.getText().toString()
						.equals(edtConfirmPass.getText().toString())) {

					Toast.makeText(getActivity(),
							"Confirm Password Is Not Match.",
							Toast.LENGTH_SHORT).show();

					edtConfirmPass.setError("Confirm Password Is Not Match.");

				} else {
					updatePassword(id,edtNewPass.getText().toString());
				}

			}
		});

	}

	private void updatePassword(int id,String newPassword) {

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

		Call<ResponseBody> call = api.updatePasswordUser(id,newPassword);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					//Dismiss Dialog
					dialog.dismiss();
					FragmentTransaction fragmentTransaction = getActivity()
							.getSupportFragmentManager()
							.beginTransaction();
					ViewUserFragment viewUserFragment = new ViewUserFragment();
					fragmentTransaction.replace(R.id.main_content,
							viewUserFragment);
					fragmentTransaction.commit();
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
