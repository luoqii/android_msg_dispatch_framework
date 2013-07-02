package com.tudou.android.fw.model.ambassador;

/**
 * request sent by client.
 * 
 * <pre>
 * <strong>NOTE</strong>:
 * this request is application-domain specific, i.e it's 
 * NOT the lower-layer request such as {@link  org.apache.http.HttpResponse}.
 * 
 * @author bysong@tudou.com
 *
 */
public interface IRequest extends IMessage {

/*	String getUrl();
	String getRequestType();*/
	
}
