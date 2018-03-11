package com.shouxin.alerm.Datasource;


import java.lang.annotation.*;

/**
 * Created by lilianwei on 2017/6/29.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}