package com.svdroid.swivl.activities.fragments.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.svdroid.swivl.R;
import com.svdroid.swivl.activities.MainActivity;
import com.svdroid.swivl.activities.dialogs.AvatarViewerDialog;
import com.svdroid.swivl.activities.fragments.UserFragment;
import com.svdroid.swivl.data.UsersModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>
{
	private List<UsersModel> _data;
	private Context _context;

	public UsersAdapter(Context context, List<UsersModel> data)
	{
		_context = context;
		_data = data;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(_context).inflate(R.layout.list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		holder.set(position);
	}

	@Override
	public int getItemCount()
	{
		return _data == null ? 0 : _data.size();
	}

	public void add(UsersModel usersModel)
	{
		if (_data == null) {
			_data = new ArrayList<>();
		}

		_data.add(usersModel);
		notifyItemInserted(_data.size() - 1);
	}

	protected class ViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView _icon;
		private TextView _iconHint;
		private TextView _name;
		private ImageView _actionOpenInBrowser;

		public ViewHolder(View itemView)
		{
			super(itemView);
			_icon = (ImageView) itemView.findViewById(R.id.user_icon);
			_name = (TextView) itemView.findViewById(R.id.user_name);
			_iconHint = (TextView) itemView.findViewById(R.id.icon_hint);
			_actionOpenInBrowser = (ImageView) itemView.findViewById(R.id.action_open_in_browser);

			View.OnClickListener onClickListener = new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final int position = getLayoutPosition();
					final UsersModel item = _data.get(position);

					if (item == null) {
						return;
					}

					switch (v.getId()) {
						case R.id.user_icon: {
							Bundle args = new Bundle(1);
							args.putString(
								AvatarViewerDialog.KEY_AVATAR_URL,
								item.avatar_url
							);

							AvatarViewerDialog dialog = new AvatarViewerDialog();
							dialog.show(
								((MainActivity) _context).getSupportFragmentManager(),
								AvatarViewerDialog.class.getName()
							);
							dialog.setArguments(args);
						}
						break;
						case R.id.action_open_in_browser:
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(item.html_url));
							_context.startActivity(i);
							break;
						default: {
							Bundle args = new Bundle(1);
							args.putString(UserFragment.KEY_TEXT, item.login);
							((MainActivity) _context).startAction(MainActivity.ACTION_OPEN_USER_DATA, args);
						}
						break;
					}
				}
			};

			itemView.setOnClickListener(onClickListener);
			_icon.setOnClickListener(onClickListener);
			_actionOpenInBrowser.setOnClickListener(onClickListener);
		}

		public void set(int position)
		{
			UsersModel usersModel = _data.get(position);

			if (usersModel != null) {
				_name.setText(usersModel.login);
				_iconHint.setText(usersModel.login.toUpperCase());
				ImageLoader.getInstance().displayImage(usersModel.avatar_url, _icon);
			}
		}
	}
}
