
package com.tudou.android.fw.msgdispatch;

import android.app.DownloadManager.Request;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.annotation.Member2String;


/**
 * msg is an interact request with pages (think intent how to de-couple android's 3 components:
 * Activity, Service, BroadCastReceiver). Actually, it's an variation of 
 * <a href="http://en.wikipedia.org/wiki/Chain-of-responsibility_pattern">Chain of responsibility</a> pattern.
 * <p>
 * when one page want another to
 * cooperate to complete some request, a msg will be dispatched to 
 * parent, parent's parent, and so on, to RootPage, then a view-tree
 * traversing will be done.
 * </p>
 * <p>
 * when adding a code plz plz plz described data.
 * </p>
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Chain-of-responsibility_pattern">Chain of responsibility </a>pattern
 * @author bysong@tudou.com
 */
public interface IMsg {

    /**
     * come or from local (other page). 
     * <p>
     * in this situation, code will be constant defined in this class.
     */
    @Member2String
    public static final int CATEGORY_LOCAL = 1;
    /**
     * come from or go to remote (network). 
     * <p>
     * in this situation, code will be constant defined
     * in {@link IProtocol}; data will the corresponding {@link Response} or 
     * data of {@link Request}.
     * 
     * @see IProtocol
     */
    @Member2String
    public static final int CATEGORY_REMOTE = 2;

    /**
     * new a {@link IRequest} based on this msg.
     * 
     * @return
     */
    public abstract IRequest toRequest();
    
    public abstract int getCode();

    public abstract IMsg setCode(int mCode);

    public abstract IMsg setCategory(int category);

    /**
     * @return
     * @see Msg#CATEGORY_LOCAL
     * @see Msg#CATEGORY_REMOTE
     */
    public abstract int getCategory();

    /**
     * code category specific data.
     * 
     * @return
     */
    public abstract Object getData();

    public abstract IMsg setData(Object data);

}
