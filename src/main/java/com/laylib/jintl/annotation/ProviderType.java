package com.laylib.jintl.annotation;

import java.lang.annotation.*;

/**
 * Provider Type
 *
 * @author Lay
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ProviderType {
    String value() default "default";
}
