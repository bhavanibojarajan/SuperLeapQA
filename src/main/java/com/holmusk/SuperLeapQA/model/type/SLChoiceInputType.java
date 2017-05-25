package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/19/17.
 */

import org.swiften.xtestkit.model.ChoiceInputType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.model.AndroidChoiceInputType;
import org.swiften.xtestkit.model.IOSChoiceInputType;

/**
 * {@link ChoiceInputType}
 * for Superleap.
 */
public interface SLChoiceInputType extends
    AndroidChoiceInputType,
    IOSChoiceInputType,
    BaseErrorType,
    SLInputType {}
