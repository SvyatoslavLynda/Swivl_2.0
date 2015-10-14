package com.svdroid.swivl.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svdroid.swivl.R;
import com.svdroid.swivl.activities.MainActivity;

public class AboutFragment extends Fragment
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		return inflater.inflate(R.layout.fragment_about, container, false);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(true);
	}
}
