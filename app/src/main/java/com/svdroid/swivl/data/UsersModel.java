package com.svdroid.swivl.data;

import java.util.ArrayList;

public class UsersModel
{
	public int id;
	public String login;
	public String avatar_url;
	public String html_url;

	@SuppressWarnings("serial")
	public static class List extends ArrayList<UsersModel>
	{
	}
}
