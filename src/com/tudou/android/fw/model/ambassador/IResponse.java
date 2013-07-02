package com.tudou.android.fw.model.ambassador;
/**
 * response received by client.
 * 
 * <pre>
 * <strong>NOTE</strong>:
 * this response is application-domain specific, i.e it's 
 * NOT the lower-layer response such as {@link  org.apache.http.HttpResponse}.
 * 
 * @author bysong@tudou.com
 *
 */
public interface IResponse extends IMessage {
	
	/**
	 * @return id related to request.
	 */
	public long getRequestId();
	
	/**
	 * if a error has occurred.
	 * whenever handle response, caller must check this.
	 * 
	 * @return
	 * 
	 * @see #getError();
	 */
	public boolean inError();
	
	public IRequest getRequest();
	
	/**
	 * @return
	 * 
	 * @see #inError()
	 */
	public IError getError();	
	public void setError(IError e);
	
	public static interface IError {

	    public static final int ERROR_IO_EXCETION = 1;
	    public static final int ERROR_FILE_NOT_FOUND = 2;
	    public static final int ERROR_CLIENT_PROTOCOL_EXCCEPTION = 3;
	    public static final int ERROR_JSON_EXCEPTION = 4;
	    /**
	     * a generic exception.
	     * 
	     * FIXME it's unsafe to dependence this exception.
	     */
	    public static final int ERROR_GENERIC_EXCEPTION = 5;
	    
	    public int getCode();
	    public String getDesc();
	}
}
