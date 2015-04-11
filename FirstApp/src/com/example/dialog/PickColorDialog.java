package com.example.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firstapp.NoteDetailActivity;
import com.example.firstapp.R;

/**
 * a color dialog that use to pick color
 * 
 * @author tuanda
 * 
 */
public class PickColorDialog extends DialogFragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_pick_color, container);
		TextView tv_color_orange = (TextView) view
				.findViewById(R.id.tv_color_orange);
		TextView tv_color_purple = (TextView) view
				.findViewById(R.id.tv_color_purple);
		TextView tv_color_green= (TextView) view
				.findViewById(R.id.tv_color_green);
		TextView tv_color_blue = (TextView) view
				.findViewById(R.id.tv_color_blue);
		tv_color_orange.setOnClickListener(this);
		tv_color_purple.setOnClickListener(this);
		tv_color_green.setOnClickListener(this);
		tv_color_blue.setOnClickListener(this);

		getDialog().setTitle("Choose Color");
		return view;
	}
	

	@Override
	public void onClick(View v) {
		int color = 0;
		switch (v.getId()) {
		case R.id.tv_color_orange:
			color = getResources().getColor(android.R.color.holo_orange_dark);
			((NoteDetailActivity) getActivity()).changeViewColor(color);
			dismiss();
			break;

		case R.id.tv_color_purple:
			color = getResources().getColor(android.R.color.holo_purple);
			((NoteDetailActivity) getActivity()).changeViewColor(color);
			dismiss();
			break;
		case R.id.tv_color_green:
			color = getResources().getColor(android.R.color.holo_green_dark);
			Log.e("updatelogic", "pickcolor " + color);
			((NoteDetailActivity) getActivity()).changeViewColor(color);
			dismiss();
			break;
		case R.id.tv_color_blue:
			color = getResources().getColor(android.R.color.holo_blue_bright);
			((NoteDetailActivity) getActivity()).changeViewColor(color);
			dismiss();
			break;

		default:
			break;
		}

	}

}
