package com.svdroid.swivl.api;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class ApiService extends RetrofitGsonSpiceService
{
	private static final String USERS_URL = "https://api.github.com";

	@Override
	public void onCreate()
	{
		super.onCreate();
		addRetrofitInterface(UsersListRequest.Users.class);
		addRetrofitInterface(UserRequest.User.class);
	}

	@Override
	protected String getServerUrl()
	{
		return USERS_URL;
	}
}
