package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/19/17.
 */

import org.swiften.xtestkit.base.model.ChoiceInputType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.android.model.AndroidChoiceInputType;
import org.swiften.xtestkit.ios.model.IOSChoiceInputType;

/**
 * {@link ChoiceInputType}
 * for Superleap.
 */
public interface SLChoiceType extends
    AndroidChoiceInputType,
    IOSChoiceInputType,
    BaseErrorType,
    SLInputType {}
