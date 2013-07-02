package com.tudou.android.fw.msgdispatch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * use to ease debugging work.
 * when annotated, a String description will be output.
 * 
 * @author bysong@tudou.com
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Member2String {
}
