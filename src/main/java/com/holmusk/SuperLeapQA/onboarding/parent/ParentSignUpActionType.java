package com.holmusk.SuperLeapQA.onboarding.parent;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.SignUpMode;
import com.holmusk.SuperLeapQA.onboarding.common.BaseSignUpActionType;
import com.holmusk.SuperLeapQA.onboarding.register.RegisterActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;

/**
 * Created by haipham on 5/8/17.
 */
public interface ParentSignUpActionType extends
    BaseActionType,
    RegisterActionType,
    BaseSignUpActionType,
    ParentSignUpValidationType {}
