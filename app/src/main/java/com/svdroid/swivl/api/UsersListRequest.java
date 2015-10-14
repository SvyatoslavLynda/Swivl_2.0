package com.svdroid.swivl.api;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.svdroid.swivl.data.UsersModel;

import retrofit.http.GET;
import retrofit.http.Query;

public class UsersListRequest extends RetrofitSpiceRequest<UsersModel.List, UsersListRequest.Users>
{
	public static final String REQUEST_USERS = "REQUEST_USERS";
	private int _count;

	public UsersListRequest(int count)
	{
		super(UsersModel.List.class, Users.class);
		_count = count;
	}

	@Override
	public UsersModel.List loadDataFromNetwork()
	{
		return getService().contributors(_count);
	}

	public interface Users
	{
		@GET("/users?per_page=100")
		UsersModel.List contributors(@Query("since") int since);
	}
}
