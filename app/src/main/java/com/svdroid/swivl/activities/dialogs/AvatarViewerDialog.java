package com.svdroid.swivl.activities.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.svdroid.swivl.R;

public class AvatarViewerDialog extends DialogFragment implements View.OnTouchListener
{
	private static final String TAG = "com.svdroid.swivl.activities.dialogs.AvatarViewerDialog";
	public static final String KEY_AVATAR_URL = TAG + ".KEY_AVATAR_URL";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View root = inflater.inflate(R.layout.dialog_avatar_viewer, container, false);
		root.setOnTouchListener(this);
		final ImageView avatar = (ImageView) root.findViewById(R.id.avatar);
		_setupAvatar(avatar);

		return root;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		dismiss();
		return false;
	}

	private void _setupAvatar(ImageView avatar)
	{
		Bundle args = getArguments();

		if (args != null && args.containsKey(KEY_AVATAR_URL)) {
			ImageLoader.getInstance().displayImage(args.getString(KEY_AVATAR_URL), avatar);
		}
	}
}