package com.holmusk.SuperLeapQA.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by haipham on 18/5/17.
 */

/**
 * Mark a {@link ElementType#METHOD} as {@link GuarantorAware} to
 * distinguish between
 * {@link com.holmusk.SuperLeapQA.model.UserMode#TEEN_UNDER_18} and
 * {@link com.holmusk.SuperLeapQA.model.UserMode#TEEN_ABOVE_18}.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface GuarantorAware {
    public boolean value();
}
