package com.tudou.android.fw.page;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.tudou.android.fw.R;
import com.tudou.android.fw.widget.ViewPagerWithTab;

public class ViewPagerComponent extends BaseComponent {
	private static final String TAG = ViewPagerComponent.class.getSimpleName();

	protected View[] mTabs;

	@Override
	protected String getLogTag() {
		return TAG;
	}
	// TODO naming convertion bysong@tudou.com
	private ViewPagerWithTab viewPagerWithTab;
	
	protected ViewPagerWithTab getViewPagerWithTab(){
		return viewPagerWithTab;
	}
	
	public ViewPagerComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ViewPagerComponent, defStyle, -1);
		final int tabSpecId = a.getResourceId(
				R.styleable.ViewPagerComponent_viewPagerSpec, -1);

		if (-1 == tabSpecId) {
			String detailMessage = "you MUST give a viewPageSpec attr value.";
			throw new IllegalArgumentException(detailMessage);
		}
		int tabSpectArrayItemLen = 4;
		Resources res = getResources();
		TypedArray tabSpec = null;
		tabSpec = res.obtainTypedArray(tabSpecId);

		String tabStr = "";
		int tabDrawableId = -1;
		int tabBackgroundId = -1;
		int tabContentId = -1;

		final int count = tabSpec.length();

		if (count == 0 || count % tabSpectArrayItemLen != 0) {
			String detailMessage = "tab spec array must base on number "
					+ tabSpectArrayItemLen + ". count: " + count;
			throw new IllegalArgumentException(detailMessage);
		}

        int layoutRes = a.getResourceId(R.styleable.ViewPagerComponent_viewPagerLayout, 0);
        int pagerId = R.id.lib_view_pager;
        
        inflate(context, layoutRes, this);
        
        viewPagerWithTab = (ViewPagerWithTab) findViewById(pagerId);
		mTabs = new View[count / tabSpectArrayItemLen];
		for (int i = 0, j = 0; i < count; i += tabSpectArrayItemLen, j++) {
			tabStr = tabSpec.getString(i);
			tabBackgroundId = tabSpec.getResourceId(i + 1, 0);
			tabDrawableId = tabSpec.getResourceId(i + 2, 0);
			tabContentId = tabSpec.getResourceId(i + 3, 0);
			mTabs[j] = inflate(context, tabContentId, null);
			viewPagerWithTab.addPageHost(j, mTabs[j], tabStr, tabBackgroundId,
					tabDrawableId, count / tabSpectArrayItemLen);
		}

		int intTab = a.getInt(
				R.styleable.ViewPagerComponent_viewPagerInitTabIndex, 0);
		viewPagerWithTab.setCurrentViewPager(intTab);

		int offScreen = a.getInt(
				R.styleable.ViewPagerComponent_viewPagerOffScreenViews, -1);
		if (-1 != offScreen) {
			viewPagerWithTab.setOffscreenPageLimit(offScreen);
		}

		tabSpec.recycle();
		a.recycle();
	}

	public ViewPagerComponent(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewPagerComponent(Context context) {
		this(context, null);
	}
}
