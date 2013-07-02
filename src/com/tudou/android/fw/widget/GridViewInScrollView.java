
package com.tudou.android.fw.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * specially used in a scrollview
 */
public class GridViewInScrollView extends GridView {

    public GridViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public GridViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public GridViewInScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        measureHeight();
    }

    public static final int DEFAULT_COLUMNS = 3;

    private void measureHeight() {
        int childHeight = 0;
        int mItemCount = getAdapter() == null ? 0 : getAdapter().getCount();
        //TODO 找出可以得到每行列数的方式，getNumColumns只能用于api11以上的
        int mNumColumns = 3;
        final int count = mItemCount;
        if (count > 0) {
            final View child = getAdapter().getView(0, null, null);
            AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                child.setLayoutParams(p);
            }

            int childHeightSpec = getChildMeasureSpec(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, p.height);
            int childWidthSpec = getChildMeasureSpec(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, p.width);
            child.measure(childWidthSpec, childHeightSpec);
            childHeight = child.getMeasuredHeight();
        }
        int ourSize = 0;
        for (int i = 0; i < count; i += mNumColumns) {
            ourSize += childHeight;
        }
        int heightSize = ourSize + this.getPaddingTop() + this.getPaddingBottom();
        android.view.ViewGroup.LayoutParams params = getLayoutParams();
        params.height = heightSize;
        setLayoutParams(params);
    }
}
