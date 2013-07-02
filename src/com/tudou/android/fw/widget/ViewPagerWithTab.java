package com.tudou.android.fw.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudou.android.fw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sun
 */
public abstract class ViewPagerWithTab extends LinearLayout implements
		OnPageChangeListener, OnClickListener {
	private final static String TAG = "ViewPagerWithTab";
	protected Context mContext;
	private ViewPager mPager;
	private MyPagerAdapter mAdapter;
	private LinearLayout mTabLayout;
	private boolean mFirst = true;
	private viewPageListner mviewPageListner = null;
	private List<TabInfo> mTabInfo = new ArrayList<TabInfo>();
	private int mScreenHeight;
	protected int mScreenWidth;

	private class TabInfo {
		View Tab;
		View Page;
		String tag;
	}

	public ViewPagerWithTab(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public ViewPagerWithTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public ViewPagerWithTab(Context context, AttributeSet attrs,
			viewPageListner pageListener) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		mTabLayout = new LinearLayout(mContext);
		// mTabLayout.setBackgroundResource(R.drawable.details_bg_tab);
		LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		addView(mTabLayout, lp);
		mTabLayout.setOrientation(HORIZONTAL);
		mAdapter = new MyPagerAdapter();
		mPager = new ViewPager(mContext) {
			@Override
			public boolean onTouchEvent(MotionEvent arg0) {
				switch (arg0.getAction()) {
				case MotionEvent.ACTION_MOVE:
					requestDisallowInterceptTouchEvent(true);
					break;

				default:
					break;
				}
				return super.onTouchEvent(arg0);
			}

			/**
			 * use this to stop scroll horizontally by gesture in landscape mode
			 * the method is to allow child scroll(which means viewpager's
			 * onInterceptTouchEvent return false),so pager won't be scrolled
			 */
			@Override
			protected boolean canScroll(View arg0, boolean arg1, int arg2,
					int arg3, int arg4) {
				if (arg0.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					return true;
				} else {
					return super.canScroll(arg0, arg1, arg2, arg3, arg4);
				}
			}
		};
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0);
		lp.weight = 1;
		addView(mPager, lp);
		mPager.setAdapter(mAdapter);
		// mPager.setCurrentItem(1);
		mPager.setOnPageChangeListener(this);
		setOrientation(VERTICAL);

		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		display.getMetrics(localDisplayMetrics);
		mScreenHeight = display.getHeight();
		mScreenWidth = display.getWidth();
		mviewPageListner = implementPageListener();
	}

	/**
	 * 使用默认的背景 R.Drawable.Button
	 * 
	 * @param page
	 *            viewpager
	 * @param tag
	 *            tab text
	 */
	public void addPageHost(View page, String tag) {
		TabInfo tabInfo = new TabInfo();
		Button btn;
		btn = new Button(mContext);
		btn.setBackgroundResource(R.color.detail_layout_bg);
		btn.setText(tag);
		btn.setTextSize(getContext().getResources().getDimension(
				R.dimen.text_size_medium_min));
		btn.setOnClickListener(this);
		tabInfo.Tab = btn;
		tabInfo.Page = page;
		tabInfo.tag = tag;
		mTabInfo.add(tabInfo);
		LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		// XXX why 9 bysong@tudou.com
		lp.height = mScreenHeight / 9;
		mTabLayout.addView(btn, lp);

		notifyDataSetChanged();
	}

	/**
	 * 手动提供背景res
	 * 
	 * @param pageIndex
	 *            TODO
	 * @param page
	 *            viewpager
	 * @param tag
	 *            tab text
	 * @param bgRes
	 *            tab background
	 * @param imgRes
	 *            TODO
	 * @param count
	 *            TODO
	 */
	public void addPageHost(int pageIndex, View page, String tag, int bgRes,
			int imgRes, int count) {
		TabInfo tabInfo = new TabInfo();
		LinearLayout tab;
		tab = new LinearLayout(mContext);
		tab.setBackgroundResource(bgRes);
		tab.setOrientation(HORIZONTAL);
		tab.setGravity(Gravity.CENTER);
		ImageView image = new ImageView(mContext);
		TextView txt = new TextView(mContext);
		txt.setText(tag);
		tab.setOnClickListener(this);
		tabInfo.Tab = tab;
		tabInfo.Page = page;
		tabInfo.tag = tag;
		mTabInfo.add(tabInfo);
		mTabLayout.addView(tab);

		FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lp1.gravity = Gravity.CENTER;

		image.setImageResource(imgRes);
		doCustomConfig(tab, txt, image, count);
		tab.addView(image, lp1);
		tab.addView(txt, lp1);
		notifyDataSetChanged();
	}

	protected abstract void doCustomConfig(LinearLayout tab, TextView tabText,
			ImageView tabImg, int tabCount);

	/**
	 * 对外接口处理viewpager的定向选择
	 * 
	 * @param i
	 */
	public void setCurrentViewPager(int position) {
		int length = mTabInfo.size();

		if (mviewPageListner != null) {
			mviewPageListner.PageOnPageSelected(position);
			List<View> pages = new ArrayList<View>();
			for (TabInfo tab : mTabInfo) {
				pages.add(tab.Page);
			}
			mviewPageListner.handlePageOnPageSelected(position, pages);
		}

		if (length > 0) {
			for (int i = 0; i < length; i++) {
				mTabInfo.get(i).Tab.setSelected(false);
			}
			mTabInfo.get(position).Tab.setSelected(true);
		}
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mPager.setCurrentItem(position, false);
		} else {
			mPager.setCurrentItem(position);
		}
	}

	/**
	 * 设置初显示时的状态
	 * 
	 * @param position
	 */
	public void setFirstSelected(int position) {
		if (mviewPageListner != null) {
			mviewPageListner.PageOnPageSelected(position);
			mPager.setCurrentItem(position);
		}
	}

	public ViewPager getPager() {
		return mPager;
	}

	/**
	 * 设置不显示的view的数量
	 * 
	 * @param limit
	 *            默认1， 即为在当前item距离1内的view都是保持着。
	 */
	public void setOffscreenPageLimit(int limit) {
		if (limit < 1) {
			Log.w(TAG, "Requested offscreen page limit " + limit
					+ " too small; defaulting to " + 1);
		} else
			mPager.setOffscreenPageLimit(limit);
	}

	public void removepageHost(String tag) {
		for (int i = 0; i < mTabInfo.size(); i++) {
			if (tag == mTabInfo.get(i).tag) {
				mTabInfo.remove(i);
				mTabLayout.removeAllViews();
				LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.weight = 1;
				for (int j = 0; j < mTabInfo.size(); j++) {
					mTabLayout.addView(mTabInfo.get(j).Tab, lp);
				}
				int now = mPager.getCurrentItem();
				mPager.setAdapter(mAdapter);
				if (now - 1 > 0) {
					mPager.setCurrentItem(now - 1);

				} else {
					mPager.setCurrentItem(0);
				}
				notifyDataSetChanged();
				return;
			}
		}
		Toast.makeText(mContext, "no " + tag, Toast.LENGTH_SHORT).show();
	}

	public boolean isExistHost(String tag) {
		for (int i = 0; i < mTabInfo.size(); i++) {
			if (tag == mTabInfo.get(i).tag)
				return true;
		}
		return false;
	}

	private void notifyDataSetChanged() {

		mAdapter.notifyDataSetChanged();
	}

	private class MyPagerAdapter extends PagerAdapter {

		public int getCount() {
			return mTabInfo.size();
		}

		public Object instantiateItem(View collection, int position) {

			View view = mTabInfo.get(position).Page;
			((ViewPager) collection).addView(view, 0);
			// Log.d(TAG, "aaa:"+ mPager.getChildCount()+"position:"+position);
			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
			// Log.d(TAG, "ddd:"+ mPager.getChildCount()+"position:"+arg1);

		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// Log.d("SUN", "restoreState:\t"+arg0+"\t"+arg1);
		}

		@Override
		public Parcelable saveState() {
			// Log.d("SUN", "saveState:\t");
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// Log.d("SUN", "startUpdate:\t"+arg0);
			// if(mFirst){
			// mTabInfo.get(0).Tab.setSelected(true);
			// mFirst = false;
			// }
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (mviewPageListner != null) {
			mviewPageListner.PageOnPageScrolled();
		}
	}

	@Override
	public void onPageSelected(int position) {
		int length = mTabInfo.size();

		if (mviewPageListner != null) {
			mviewPageListner.PageOnPageSelected(position);
			List<View> pages = new ArrayList<View>();
			for (TabInfo tab : mTabInfo) {
				pages.add(tab.Page);
			}
			mviewPageListner.handlePageOnPageSelected(position, pages);
		}

		// Log.d(TAG, "xxx:" + mPager.getChildCount());

		if (length > 0) {
			for (int i = 0; i < length; i++) {
				mTabInfo.get(i).Tab.setSelected(false);
			}
			mTabInfo.get(position).Tab.setSelected(true);
		}
		addTabFlurry(mTabInfo.get(position).tag);
	}

	/**
	 * add flurry 标签页
	 */
	protected void addTabFlurry(String tag) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public void setOnviewPageListner(viewPageListner listner) {
		mviewPageListner = listner;
	}

	/**
	 * viewPager 操作时 需要的实时处理 接口 初写 不完整
	 * 
	 * @author sun
	 */
	public interface viewPageListner {
		/**
		 * viewPager 滑动时
		 */
		public void PageOnPageScrolled();

		/**
		 * viewPager 滑动完毕
		 * 
		 * @param position
		 *            完成后即可确定position 回调改变tab状态
		 */
		public void PageOnPageSelected(int position);

		/**
		 * viewPager 状态改变
		 */
		public void PageOnPageScrollStateChanged();

		/**
		 * 可以在这里对pageadapter里的item也就是page本身进行设置
		 */
		public void handlePageOnPageSelected(int position, List<View> pages);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mTabInfo.size(); i++) {
			if (v == mTabInfo.get(i).Tab) {
				mPager.setCurrentItem(i);
			}
		}
	}

	public LinearLayout getTabLayout() {
		return mTabLayout;
	}

	protected viewPageListner implementPageListener() {
		return null;
	};
}
