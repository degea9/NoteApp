package com.exmaple.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstapp.R;
import com.example.model.Note;
import com.example.utils.CommonUtil;

/**
 * a grid note adapter that use the view holder pattern for better performance
 * set note title,detail, background color,etc... here
 * 
 * @author tuanda
 * 
 */
public class GridNoteAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context mContext;
	List<Note> mList = new ArrayList<Note>();
	public static final String UN_TITLE = "Untitle";

	public GridNoteAdapter(Context context, ArrayList<Note> list) {
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
		Log.e("updatelogic","getViewNote position "+position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_note_item, parent, false);

			holder = new ViewHolder();
			holder.imv_alarm = (ImageView) convertView
					.findViewById(R.id.imv_alarm);
			holder.tv_note_id = (TextView) convertView
					.findViewById(R.id.tv_note_id);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_details = (TextView) convertView
					.findViewById(R.id.tv_detail);
			holder.tv_created_at = (TextView) convertView
					.findViewById(R.id.tv_created_at);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		int bottom = convertView.getPaddingBottom();
		int top = convertView.getPaddingTop();
		int right = convertView.getPaddingRight();
		int left = convertView.getPaddingLeft();
		holder.tv_note_id.setText("" + mList.get(position).getId());
		if (!mList.get(position).getTitle().isEmpty())
			holder.tv_title.setText(mList.get(position).getTitle());
		else
			holder.tv_title.setText(UN_TITLE);
		holder.tv_details.setText(mList.get(position).getNoteDetail());
		holder.tv_created_at.setText(CommonUtil.getDateFromMiliseconds(mList
				.get(position).getCreatedAt()));
		if (mList.get(position).getAlarmTime() != 0)
			holder.imv_alarm.setVisibility(View.VISIBLE);
		if (mList.get(position).getAlarmTime() != 0)
			convertView.setVisibility(View.VISIBLE);
		if (mList.get(position).getColor() == mContext.getResources().getColor(
				android.R.color.holo_orange_dark)) {
			convertView
					.setBackgroundResource(R.drawable.background_with_shadow_orange);
		} else if (mList.get(position).getColor() == mContext.getResources()
				.getColor(android.R.color.holo_blue_bright)) {
			convertView
					.setBackgroundResource(R.drawable.background_with_shadow_blue);
		} else if (mList.get(position).getColor() == mContext.getResources()
				.getColor(android.R.color.holo_green_dark)) {
			convertView
					.setBackgroundResource(R.drawable.background_with_shadow_green);
		} else if (mList.get(position).getColor() == mContext.getResources()
				.getColor(android.R.color.holo_purple)) {
			convertView
					.setBackgroundResource(R.drawable.background_with_shadow_purple);
		} else {
			convertView
					.setBackgroundResource(R.drawable.background_with_shadow_normal);
		}
		// fix issue lost padding when set background,read more at
		// http://stackoverflow.com/questions/2886140/does-changing-the-background-also-change-the-padding-of-a-linearlayout?lq=1

		convertView.setPadding(left, top, right, bottom);
		return convertView;

	}

	static class ViewHolder {
		ImageView imv_alarm;
		TextView tv_note_id;
		TextView tv_title;
		TextView tv_details;
		TextView tv_created_at;
	}

}
