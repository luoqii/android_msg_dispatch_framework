package com.tudou.android.fw.model.ambassador;

/**
 * top message sent or received by client.
 * 
 * @author bysong@tudou.com
 *
 */
public interface IMessage {
	public static final int TRAFIC_TYPE_UNKOWN = -1;
	public static final int TRAFIC_TYPE_SERVER2CLIENT = 1;
	public static final int TRAFIC_TYPE_CLIENT2SERVER = 2;	
	
	/**
	 * @return traffic type
	 * 
	 * @see TYPE_CLIENT2SERVER
	 * @see TYPE_SERVER2CLIENT
	 */
	public int getTrafficType();
	
	// TODO add orignator id.
//	public String getOrignatorId();

	
	/**
	 * @return identification.
	 */
	public long getId();

	/**
	 * @return opaque data associated with this message. 
	 * 
	 * TODO how to avoid type-casting.
	 */
	public Object getData();
	
	/**
	 * @return  code to identify a group messages.
	 */
	public int getCode();
	
	/**
	 * @return human readable description.
	 */
	public String getCodeDescription();
	public long getUTCTimeStamp();
}
