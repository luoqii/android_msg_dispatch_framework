package com.tudou.android.fw.model.ambassador;


/**
 * factory to create {@link IAmbassador}
 * 
 * @author bysong@tudou.com
 *
 */
public class AmbassadorFactory {
    private static final String TAG = AmbassadorFactory.class.getSimpleName();
    
	private static AmbassadorFactory sInstance;
	
	private IAmbassadorFactory mFactory;
	
	private AmbassadorFactory(){

	}
	
	public static AmbassadorFactory getInstance(){
		if (null == sInstance) {
			sInstance = new AmbassadorFactory();
		}
		
		return sInstance;
	}
	
	public IAmbassador createAmbassador(Policy notUsed){
		if (null != mFactory) {
		    return mFactory.createAmbassador(notUsed);
		}
		
		throw new IllegalStateException("you must set a factory by setFacory().");
	}
	
	public void setFactory(IAmbassadorFactory factory) {
	    mFactory = factory;
	}
	
	public interface Policy {
	    
	}
	
	public interface IAmbassadorFactory{
	    IAmbassador createAmbassador(Policy notUsed);
	}
}
