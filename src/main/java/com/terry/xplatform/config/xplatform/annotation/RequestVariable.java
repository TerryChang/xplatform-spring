package com.terry.xplatform.config.xplatform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestVariable {
    
    String name() default "";			// 읽고자하는 변수명 이름을 받아들이는 부분
}
