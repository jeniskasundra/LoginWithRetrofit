package com.example.loginwithretrofit.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginwithretrofit.model.User;
import com.example.loginwithretrofit.utils.Config;
import com.example.loginwithretrofit.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class UserListViewAdapter extends BaseAdapter {
	private static LayoutInflater inflater = null;
	private Context mContext;
	private List<User> listUser = new ArrayList<User>();

	public UserListViewAdapter(FragmentActivity activity,
			List<User> listUser) {
		// TODO Auto-generated constructor stub
		mContext = activity;
		this.listUser = listUser;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listUser.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		TextView tvUserName;
		ImageView imgUserPic;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_user, null);
			viewHolder = new ViewHolder();
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserNameListName);
			viewHolder.imgUserPic = (ImageView) convertView
					.findViewById(R.id.imgProfileListItem);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvUserName.setText(listUser.get(position).getName());
		Picasso.with(mContext)
				.load(Config.URL_IMAGES + listUser.get(position).getImage())
				.memoryPolicy(MemoryPolicy.NO_CACHE)
				.networkPolicy(NetworkPolicy.NO_CACHE)
				.into(viewHolder.imgUserPic);
		
		

		return convertView;
	}

}
