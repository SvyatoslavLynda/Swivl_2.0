package com.svdroid.swivl.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.octo.android.robospice.SpiceManager;
import com.svdroid.swivl.R;
import com.svdroid.swivl.activities.fragments.AboutFragment;
import com.svdroid.swivl.activities.fragments.MainFragment;
import com.svdroid.swivl.activities.fragments.UserFragment;
import com.svdroid.swivl.api.ApiService;

public class MainActivity extends AppCompatActivity
{
	public static final int ACTION_OPEN_MAIN_LIST = 1;
	public static final int ACTION_OPEN_ABOUT = 2;
	public static final int ACTION_OPEN_USER_DATA = 3;

	private FragmentManager _fragmentManager;
	private SpiceManager spiceManager = new SpiceManager(ApiService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		_fragmentManager = getSupportFragmentManager();

		if (_fragmentManager.getBackStackEntryCount() == 0) {
			startAction(ACTION_OPEN_MAIN_LIST, null);
		}

		_initImageLoader();
	}

	@Override
	protected void onStart()
	{
		spiceManager.start(this);
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		spiceManager.shouldStop();
		super.onStop();
	}

	public SpiceManager getSpiceManager()
	{
		return spiceManager;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.action_open_about:
				startAction(ACTION_OPEN_ABOUT, null);
				break;
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setDisplayHomeAsUpEnabled(boolean enabled)
	{
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(enabled);
		}
	}

	public void setShowActionBar(boolean isShow)
	{
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			if (isShow) {
				actionBar.show();
			} else {
				actionBar.hide();
			}
		}
	}

	public boolean startAction(int action, Bundle bundle)
	{
		final Fragment fragment;
		final boolean backStack;

		switch (action) {
			case ACTION_OPEN_MAIN_LIST:
				fragment = new MainFragment();
				backStack = false;
				break;
			case ACTION_OPEN_ABOUT:
				fragment = new AboutFragment();
				backStack = true;
				break;
			case ACTION_OPEN_USER_DATA:
				fragment = new UserFragment();
				backStack = true;
				break;
			default:
				return false;
		}

		if (bundle != null) {
			fragment.setArguments(bundle);
		}

		_replaceFragment(fragment, backStack);

		return true;
	}

	private void _replaceFragment(Fragment fragment, boolean backStack)
	{
		final String tag = fragment.getClass().getName();
		_fragmentManager = getSupportFragmentManager();
		_fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

		final FragmentTransaction transaction = _fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment, tag);

		if (backStack) {
			transaction.addToBackStack(tag);
		}

		transaction.commit();
	}

	private void _initImageLoader()
	{
		final DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(android.R.color.transparent)
			.showImageForEmptyUri(android.R.color.transparent)
			.showImageOnFail(android.R.color.transparent)
//			.showImageOnFail(R.drawable.)
//			.showImageOnLoading(R.drawable.ic_picture)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.considerExifParams(true)
			.build();

		final ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(displayImageOptions);

		ImageLoader.getInstance().init(builder.build());
	}
}
