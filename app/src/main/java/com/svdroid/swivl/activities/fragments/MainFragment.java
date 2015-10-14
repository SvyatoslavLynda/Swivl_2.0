package com.svdroid.swivl.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.svdroid.swivl.R;
import com.svdroid.swivl.activities.MainActivity;
import com.svdroid.swivl.activities.fragments.adapter.UsersAdapter;
import com.svdroid.swivl.api.UsersListRequest;
import com.svdroid.swivl.data.UsersModel;

public class MainFragment extends Fragment
	implements SwipeRefreshLayout.OnRefreshListener, RequestListener<UsersModel.List>
{
	private static final int COUNT_LOAD_ITEMS = 300;

	private RecyclerView _usersList;
	private SwipeRefreshLayout _refreshControl;
	private TextView _stubView;

	private int _lastListId = 0;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View root = inflater.inflate(R.layout.fragment_main, container, false);

		_refreshControl = (SwipeRefreshLayout) root.findViewById(R.id.refresh_control);
		_refreshControl.setColorSchemeResources(R.color.colorPrimary);
		_refreshControl.setOnRefreshListener(this);

		_stubView = (TextView) root.findViewById(R.id.stub_view);

		_usersList = (RecyclerView) root.findViewById(R.id.users_list);
		final LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
		_usersList.setLayoutManager(manager);

		return root;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		setHasOptionsMenu(true);
		getActivity().supportInvalidateOptionsMenu();
		((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(false);
		((MainActivity) getActivity()).setShowActionBar(true);

		_lastListId = 0;
		_makeRequest();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public void onRefresh()
	{
		_makeRequest();
	}

	private void _makeRequest()
	{
		_setRefreshing(true);

		final SpiceManager manager = ((MainActivity) getActivity()).getSpiceManager();
		if (manager != null) {
			final UsersListRequest request = new UsersListRequest(_lastListId);
			manager.execute(
				request,
				UsersListRequest.REQUEST_USERS,
				DurationInMillis.ALWAYS_EXPIRED,
				this
			);
		}
	}


	private void _setRefreshing(final boolean refreshing)
	{
		if (_refreshControl != null) {
			_refreshControl.post(
				new Runnable()
				{
					@Override
					public void run()
					{
						_refreshControl.setRefreshing(refreshing);
					}
				}
			);
		}
	}

	private void _setStubViewVisibility(int stubVisibility, int listVisibility)
	{
		if (_stubView != null && _usersList != null) {
			_stubView.setVisibility(stubVisibility);
			_usersList.setVisibility(listVisibility);
		}
	}

	@Override
	public void onRequestFailure(SpiceException spiceException)
	{
		_setRefreshing(false);
		_stubView.setText(spiceException.getMessage());
		_setStubViewVisibility(View.VISIBLE, View.GONE);
	}

	@Override
	public void onRequestSuccess(final UsersModel.List result)
	{
		if (_usersList == null) {
			return;
		}

		_lastListId = result.get(result.size() - 1).id;
		_setStubViewVisibility(View.GONE, View.VISIBLE);

		final UsersAdapter adapter;
		if (_usersList.getAdapter() == null) {
			adapter = new UsersAdapter(getContext(), result);
			_usersList.setAdapter(adapter);
		} else {
			adapter = (UsersAdapter) _usersList.getAdapter();
			for (UsersModel usersModel : result) {
				adapter.add(usersModel);
			}
		}

		int itemsCount = _usersList.getAdapter().getItemCount();

		if (itemsCount >= COUNT_LOAD_ITEMS) {
			_setRefreshing(false);
		} else {
			_makeRequest();
		}
	}
}