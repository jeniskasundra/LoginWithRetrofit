package com.example.loginwithretrofit.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginwithretrofit.model.User;
import com.example.loginwithretrofit.rest.ApiClient;
import com.example.loginwithretrofit.rest.ApiInterface;
import com.example.loginwithretrofit.utils.Config;
import com.example.loginwithretrofit.fragment.ChangePasswordFragment;
import com.example.loginwithretrofit.fragment.EditProfileFragment;
import com.example.loginwithretrofit.fragment.ViewUserFragment;
import com.example.loginwithretrofit.R;
import com.example.loginwithretrofit.preference.UserPrefs;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelComeActivity extends AppCompatActivity implements
		OnNavigationItemSelectedListener {
	TextView tvName, tvEmail;
	ImageView imgProfile;
	private int id;
	LinearLayout llDrawerHeader;

	private Toolbar toolbar;
	private NavigationView mDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private int mSelectedId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		init();
		bindView();
		setToolbar();
		getUser();

		drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close);
		mDrawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
		// default it set first item as selected
		mSelectedId = savedInstanceState == null ? R.id.navigation_item_view_user
				: savedInstanceState.getInt("SELECTED_ID");
		itemSelection(mSelectedId);

	}

	private void init() {

		Intent intent = getIntent();
		id = intent.getIntExtra(Config.TAG_ID,0);
		UserPrefs userPrefs = new UserPrefs(getApplicationContext());

		if (id == 0) {
			id = userPrefs.getUserId();
		}
		mDrawer = (NavigationView) findViewById(R.id.main_drawer);
		mDrawer.setNavigationItemSelectedListener(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	}

	private void bindView() {
        View header = mDrawer.getHeaderView(0);
		llDrawerHeader = (LinearLayout) header.findViewById(R.id.llDrawerHeader);
		tvName = (TextView) header.findViewById(R.id.tvUserName);
		tvEmail = (TextView) header.findViewById(R.id.tvUserEmail);
		imgProfile = (ImageView) header.findViewById(R.id.imgUser);

	}

	private void setToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
	}

	private void itemSelection(int mSelectedId) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		switch (mSelectedId) {

		case R.id.navigation_item_view_user:
			mDrawerLayout.closeDrawer(GravityCompat.START);
			ViewUserFragment viewUser = new ViewUserFragment();
			fragmentTransaction.replace(R.id.main_content, viewUser);
			fragmentTransaction.commit();
			break;

		case R.id.navigation_item_edit:
			EditProfileFragment editProfile = new EditProfileFragment();
			fragmentTransaction.replace(R.id.main_content, editProfile);
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(GravityCompat.START);
			break;

		case R.id.navigation_item_change_password:
			ChangePasswordFragment changePassword = new ChangePasswordFragment();
			fragmentTransaction.replace(R.id.main_content, changePassword);
			fragmentTransaction.commit();
			mDrawerLayout.closeDrawer(GravityCompat.START);
			break;

		case R.id.navigation_item_logout:
			mDrawerLayout.closeDrawer(GravityCompat.START);
			Intent i = new Intent(WelComeActivity.this, LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			break;

		}

	}

	private void getUser() {

		//Creating an object of our api interface
		ApiInterface api = ApiClient.getApiService();

		/**
		 * Calling JSON
		 */

		Call<User> call = api.getUserById(id);

		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.isSuccessful()) {
					/**
					 * Got Successfully
					 */
					tvName.setText(response.body().getName());
					tvEmail.setText(response.body().getEmail());
					Picasso.with(getApplicationContext())
							.load(Config.URL_IMAGES + response.body().getImage())
							.memoryPolicy(MemoryPolicy.NO_CACHE)
							.networkPolicy(NetworkPolicy.NO_CACHE).into(imgProfile);

					UserPrefs userPrefs = new UserPrefs(getApplicationContext());
					if (id != 0) {
						userPrefs.setUserId(id);
					}

					userPrefs.setUserName(response.body().getName());
					userPrefs.setUserEmail(response.body().getEmail());
                    userPrefs.setUserImage(response.body().getImage());
					userPrefs.setUserPassword(response.body().getPassword());
				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {

			}
		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		menuItem.setChecked(true);
		mSelectedId = menuItem.getItemId();
		itemSelection(mSelectedId);
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState,
			PersistableBundle outPersistentState) {
		outState.putInt("SELECTED_ID", mSelectedId);
	}

}
