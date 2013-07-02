package com.tudou.android.fw.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.MsgHandler;

public class ViewPagerPage extends ViewPagerComponent implements MsgHandler {

	public ViewPagerPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ViewPagerPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewPagerPage(Context context) {
		super(context);
	}

	@Override
	public boolean handle(IMsg msg, int direction) {
		boolean handled = false;
		for (View v : mTabs) {
			if (null != v) {
				if (v instanceof MsgHandler) {
					handled |= ((MsgHandler) v).handle(msg, direction);
				} else if (v instanceof ViewGroup
						&& v.getClass()
								.isAnnotationPresent(
										com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
					handled |= MsgDispatcher.getInstance().dispatch(
							(ViewGroup) v, msg);
				}
			}
		}

		return handled;
	}

	@Override
	public boolean canHandle(IMsg msg) {
		boolean canHandle = false;

		for (View v : mTabs) {
			if (null != v) {
				if (v instanceof MsgHandler) {

					canHandle |= ((MsgHandler) v).canHandle(msg);
				} else if (v instanceof ViewGroup
						&& v.getClass()
								.isAnnotationPresent(
										com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
					canHandle |= MsgDispatcher.getInstance().canHandle(
							(ViewGroup) v, msg);
				}
			}
		}

		return canHandle;
	}

	@Override
	public void onError(IMsg errorMsg) {
		for (View v : mTabs) {

			if (null != v) {
				if (v instanceof MsgHandler) {
					((MsgHandler) v).onError(errorMsg);
				} else if (v instanceof ViewGroup
						&& v.getClass()
								.isAnnotationPresent(
										com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
					MsgDispatcher.getInstance().dispatch((ViewGroup) v,
							errorMsg);
				}
			}
		}
	}

}
