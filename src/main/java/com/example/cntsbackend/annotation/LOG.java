package com.example.cntsbackend.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface LOG {


    /**
     * 模块名称
     */
    String moduleName() default "defaultModuleName-mm";

    /**
     * 模块版本
     */
    String moduleVersion() default "version is not defined";


}
