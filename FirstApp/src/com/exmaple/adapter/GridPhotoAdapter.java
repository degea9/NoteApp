package com.exmaple.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstapp.NoteDetailActivity;
import com.example.firstapp.R;
import com.example.model.NoteManager;
import com.example.model.Photo;
import com.example.utils.CommonUtil;
import com.squareup.picasso.Picasso;

/**a grid photo adapter that attach with note
 * @author tuanda
 *
 */
public class GridPhotoAdapter extends BaseAdapter implements OnClickListener {
	LayoutInflater inflater;
	Context mContext;
	List<Photo> mList = new ArrayList<Photo>();
	private int mCurrentPosition;
	ViewHolder holder;

	public GridPhotoAdapter(Context context, ArrayList<Photo> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("updatelogic","getViewPhoto position "+position);
		mCurrentPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent,false);

			holder = new ViewHolder();
			holder.tvPhotoId = (TextView)convertView.findViewById(R.id.tv_photo_id);
			holder.imgView = (ImageView) convertView.findViewById(R.id.test);
			holder.imgDelete = (ImageView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.tvPhotoId.setText(""+mList.get(position).getPhotoId());
		Picasso.with(mContext).load(new File(mList.get(position).getPath())).placeholder(R.drawable.ic_photo_default).resize(CommonUtil.getScreenWidth(mContext)/3, CommonUtil.getScreenWidth(mContext)/3).centerCrop().into(holder.imgView);
		holder.imgDelete.setTag(mList.get(position));
		holder.imgDelete.setOnClickListener(this);
		return convertView;

	}

	static class ViewHolder {
		TextView tvPhotoId;
		ImageView imgView;
		ImageView imgDelete;
	}

	@Override
	public void onClick(View v) {
		showConfirmDeleteDialog(v);
		
	}
	
	private void showConfirmDeleteDialog(final View v) {
		new AlertDialog.Builder(((NoteDetailActivity)mContext))
				.setTitle("Confirm Delete")
				.setMessage("Are you sure you want to delete this?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								Log.e("tuandang","remove "+mList.get(mCurrentPosition).getPhotoId());
								NoteManager noteManager = new NoteManager(mContext);
								Photo photo = (Photo)v.getTag();
								noteManager.deletePhoto(photo.getPhotoId());
								mList.remove(mCurrentPosition);
								notifyDataSetChanged();
							}
						}).setNegativeButton(R.string.action_cancel, null)
				.setIcon(0).show();
	}

}
