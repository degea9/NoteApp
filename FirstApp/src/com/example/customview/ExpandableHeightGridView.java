package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * an Expandable Height GridView that allow itself can be laid out inside scroll view.It's a
 * temporary solution because it has a bad performance due to it will not
 * recycle grid items..Consider it's not good solution, read more Neil Traft's
 * answer at.It should be replaced by a grid view with header and footer like listview
 * http://stackoverflow.com/questions/4523609/grid-of-images-inside-scrollview
 * /4536955#4536955
 * 
 * @author tuanda
 * 
 */
public class ExpandableHeightGridView extends GridView {

	boolean expanded = false;

	public ExpandableHeightGridView(Context context) {
		super(context);
	}

	public ExpandableHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandableHeightGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean isExpanded() {
		return expanded;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isExpanded()) {
			// Calculate entire height by providing a very large height hint.
			// View.MEASURED_SIZE_MASK represents the largest height possible.
			int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);

			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
