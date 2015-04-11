package com.example.firstapp;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.utils.CommonUtil;
import com.squareup.picasso.Picasso;

public class SwipeActivity extends Activity {
	private ArrayList<String> paths = new ArrayList<>();
	int selectedPos;
	public static final String CURRENT_POS = "CURRENT_POS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_swipe);
		// get intent data
		Intent i = getIntent();
		// Selected image id
		if (i != null) {
			paths = i.getStringArrayListExtra("photo_path");
			selectedPos = i.getIntExtra(CURRENT_POS, 0);
			ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
			ImagePagerAdapter adapter = new ImagePagerAdapter();
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(selectedPos);
		}
	}
	
	@Override
	protected void onDestroy() {
		Log.e("updatelogic","SwipeActivity destroy");
		super.onDestroy();
	}

	private class ImagePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = SwipeActivity.this;
			ImageView imageView = new ImageView(context);
			Picasso.with(context)
					.load(new File(paths.get(position)))
					.resize(CommonUtil.getScreenWidth(context),
							CommonUtil.getScreenWidth(context)).onlyScaleDown().centerInside().into(imageView);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.e("updatelogic","ImagePagerAdapter destroy");
			((ViewPager) container).removeView((ImageView) object);
		}
	}

}
