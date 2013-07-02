package com.tudou.android.fw.msgdispatch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * use to annotate a ViewGroup to let msg dispatch framework
 * to automatically dispatch msg to children if child view is
 * an instance or {@link com.tudou.android.fw.msgdispatch.MsgHandler}
 * or annotated as {@link MsgHandler}.
 * 
 * @author bysong@tudou.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MsgHandler {

}
