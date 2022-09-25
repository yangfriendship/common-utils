package me.youzheng.common.packagescan;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PackageScan {

    String[] value();

}
