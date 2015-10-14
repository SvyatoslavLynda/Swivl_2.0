package com.svdroid.swivl.api;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.svdroid.swivl.data.UserModel;

import retrofit.http.GET;
import retrofit.http.Path;

public class UserRequest extends RetrofitSpiceRequest<UserModel, UserRequest.User>
{
	public static final String REQUEST_USER = "REQUEST_USER";
	private String _userName;

	public UserRequest(String userName)
	{
		super(UserModel.class, User.class);
		_userName = userName;
	}

	@Override
	public UserModel loadDataFromNetwork()
		throws Exception
	{
		return getService().user(_userName);
	}

	public interface User
	{
		@GET("/users/{user}")
		UserModel user(@Path("user") String user);
	}
}
