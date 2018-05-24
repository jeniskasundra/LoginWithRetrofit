package com.example.loginwithretrofit.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.loginwithretrofit.model.User;
import com.example.loginwithretrofit.model.UserList;
import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.utils.Config;
import com.example.loginwithretrofit.R;
import com.example.loginwithretrofit.adapter.UserListViewAdapter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewUserFragment extends Fragment {

	private SwipeMenuListView mListView;
	private String JSON_STRING;
	private List<User> userList;
	int id;
	String deleteUserId;
	private UserListViewAdapter adapter;

	public ViewUserFragment() {
		super();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_user, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("Available User List");
		bindView();
		init();
		onCreateSwapview();
		addListner();
		showUsers();

	}

	private void bindView() {
		mListView = (SwipeMenuListView) getActivity().findViewById(
				R.id.listViewUser);
	}

	private void init()
	{
		userList=new ArrayList<>();
	}

	private void addListner() {
		mListView
				.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
												   SwipeMenu menu, int index) {
						switch (index) {
						case 0:
							FragmentTransaction fragmentTransaction = getActivity()
									.getSupportFragmentManager()
									.beginTransaction();

							String name = userList.get(position).getName();
							String email = userList.get(position).getEmail();
							String image = userList.get(position).getImage();

							Bundle bundle = new Bundle();
							bundle.putString(Config.KEY_NAME, name);
							bundle.putString(Config.KEY_EMAIL, email);
							bundle.putString(Config.KEY_IMAGE, image);
							UserDetailsFragment userDetails = new UserDetailsFragment();
							userDetails.setArguments(bundle);
							fragmentTransaction.replace(R.id.main_content,
									userDetails);
							fragmentTransaction.commit();
							break;
						case 1:
							// delete
							confirmDeleteEmployee(userList.get(position).getId(),position);
							break;
						}
						return false;
					}
				});

	}

	private void onCreateSwapview() {

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}

		};
		mListView.setMenuCreator(creator);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void showUsers() {


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

				if(response.isSuccessful()) {
					/**
					 * Got Successfully
					 */
					userList = response.body().getResult();
				 adapter = new UserListViewAdapter(
							getActivity(), userList);
					mListView.setAdapter(adapter);

				} else {
					Toast.makeText(getActivity(),
							"Some Thing Wrong", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<UserList> call, Throwable t) {
				dialog.dismiss();
			}
		});

	}

	private void confirmDeleteEmployee(final int deleteUserId, final int position) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder
				.setMessage("Are you sure you want to delete this user?");

		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteEmployee(deleteUserId,position);
					}
				});

		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void deleteEmployee(int deleteUserId, final int position) {
		//Creating an object of our api interface
		ApiInterface api = ApiClient.getApiService();

		/**
		 * Calling JSON
		 */
		Call<ResponseBody> call = api.deleteUser(deleteUserId);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					userList.remove(position);
					adapter.notifyDataSetChanged();
					try {
						Toast.makeText(getActivity(), response.body().string().toString(), Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {

			}
		});
	}
}
