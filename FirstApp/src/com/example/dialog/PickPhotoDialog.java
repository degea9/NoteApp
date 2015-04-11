package com.example.dialog;

import java.io.File;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.firstapp.NoteDetailActivity;
import com.example.firstapp.R;

/**
 * a dialog that use to pick photo
 * 
 * @author tuanda
 * 
 */
public class PickPhotoDialog extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_pick_up_photo, container);
		getDialog().setTitle("Insert Picture");
		RelativeLayout rl_picture = (RelativeLayout) view
				.findViewById(R.id.rl_photo);
		RelativeLayout rl_cam = (RelativeLayout) view
				.findViewById(R.id.rl_camera);
		rl_picture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				getActivity().startActivityForResult(photoPickerIntent,
						NoteDetailActivity.SELECT_PHOTO);
				dismiss();
			}
		});

		rl_cam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = System.currentTimeMillis()+".jpg";
				File folder = new File(android.os.Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"tuandang");
				folder.mkdir();
				File file = new File(folder, name);
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				((NoteDetailActivity)getActivity()).setCapturedImageFile(file);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				getActivity().startActivityForResult(cameraIntent,
						NoteDetailActivity.CAMERA_REQUEST);
				dismiss();
			}
		});
		return view;
	}

}
