package org.springboot.sample.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义一个Todo注解
 * @author DELL
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Todo {
	
	public enum Priority{
		LOW,
		MEDIUM,
		HIGH
	}
	
	public enum Status{
		STARTED,
		NOT_STARTED
	}
	
	String author() default "YASH";
	
	Priority priority() default Priority.LOW;
	
	Status status() default Status.NOT_STARTED;

}
