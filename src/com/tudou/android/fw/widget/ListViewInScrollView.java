
package com.tudou.android.fw.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * specially used in a scrollview
 *
 */
public class ListViewInScrollView extends ListView {

    public ListViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ListViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListViewInScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
		setCacheColorHint(0x00000000);
	}

	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
}
