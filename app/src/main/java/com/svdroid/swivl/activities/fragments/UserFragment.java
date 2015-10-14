package com.svdroid.swivl.activities.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.svdroid.swivl.R;
import com.svdroid.swivl.activities.MainActivity;
import com.svdroid.swivl.api.UserRequest;
import com.svdroid.swivl.data.UserModel;

public class UserFragment extends Fragment implements RequestListener<UserModel>
{
	private static final String TAG = "com.svdroid.swivl.activities.fragments.UserFragment";
	public static final String KEY_TEXT = TAG + ".KEY_TEXT";

	private Toolbar _toolbar;
	private ImageView _avatar;
	private ProgressBar _loadingSpinner;
	private TextView _nameTitle;
	private TextView _name;
	private TextView _emailTitle;
	private TextView _email;
	private TextView _locationTitle;
	private TextView _location;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View root = inflater.inflate(R.layout.fragment_user, container, false);
		_toolbar = (Toolbar) root.findViewById(R.id.toolbar);
		_toolbar.inflateMenu(R.menu.uset_menu);
		_toolbar.setNavigationOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					getActivity().onBackPressed();
				}
			}
		);
		_avatar = (ImageView) root.findViewById(R.id.avatar);
		_loadingSpinner = (ProgressBar) root.findViewById(R.id.loading_spinner);
		_loadingSpinner.getIndeterminateDrawable().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
		_nameTitle = (TextView) root.findViewById(R.id.title_name);
		_name = (TextView) root.findViewById(R.id.name);
		_emailTitle = (TextView) root.findViewById(R.id.title_email);
		_email = (TextView) root.findViewById(R.id.email);
		_locationTitle = (TextView) root.findViewById(R.id.title_location);
		_location = (TextView) root.findViewById(R.id.location);

		return root;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		((MainActivity) getActivity()).setShowActionBar(false);

		String userLogin = _setUpArguments();

		if (userLogin != null) {
			final UserRequest request = new UserRequest(userLogin);
			((MainActivity) getActivity()).getSpiceManager().execute(
				request,
				UserRequest.REQUEST_USER,
				DurationInMillis.ALWAYS_EXPIRED,
				this
			);
		}
	}

	@Override
	public void onRequestFailure(SpiceException spiceException)
	{
		_loadingSpinner.setVisibility(View.GONE);
	}

	@Override
	public void onRequestSuccess(final UserModel userModel)
	{
		_loadingSpinner.setVisibility(View.GONE);

		if (userModel != null) {
			_toolbar.setTitle(userModel.login);
			_toolbar.getMenu().getItem(0).setVisible(userModel.blog != null);
			_toolbar.getMenu().getItem(1).setVisible(userModel.html_url != null);
			_toolbar.setOnMenuItemClickListener(
				new Toolbar.OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						Intent i = new Intent(Intent.ACTION_VIEW);

						switch (item.getItemId()) {
							case R.id.open_blog:
								if (userModel.blog != null) {
									i.setData(Uri.parse(userModel.blog));
									startActivity(i);
								}
								break;
							case R.id.open_profile_in_browser:
								if (userModel.html_url != null) {
									i.setData(Uri.parse(userModel.html_url));
									startActivity(i);
								}
								break;
						}

						return false;
					}
				}
			);

			ImageLoader.getInstance().displayImage(userModel.avatar_url, _avatar);

			_name.setText(userModel.name == null ? "" : userModel.name);
			_email.setText(userModel.email == null ? "" : userModel.email);
			_location.setText(userModel.location == null ? "" : userModel.location);

			_name.setVisibility(userModel.name == null ? View.GONE : View.VISIBLE);
			_email.setVisibility(userModel.email == null ? View.GONE : View.VISIBLE);
			_location.setVisibility(userModel.location == null ? View.GONE : View.VISIBLE);

			_nameTitle.setVisibility(userModel.name == null ? View.GONE : View.VISIBLE);
			_emailTitle.setVisibility(userModel.email == null ? View.GONE : View.VISIBLE);
			_locationTitle.setVisibility(userModel.location == null ? View.GONE : View.VISIBLE);
		}
	}

	private String _setUpArguments()
	{
		Bundle args = getArguments();

		if (args != null && args.containsKey(KEY_TEXT)) {
			return args.getString(KEY_TEXT);
		}

		return null;
	}
}
