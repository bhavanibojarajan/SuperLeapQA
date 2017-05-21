package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.navigation.NavigationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 5/21/17.
 */
public enum Screen {
    SPLASH,
    WELCOME,
    SIGN_IN,
    FORGOT_PASSWORD,
    REGISTER,
    DOB_PICKER,
    UNACCEPTABLE_AGE,
    ACCEPTABLE_AGE,
    PERSONAL_INFO,
    EXTRA_PERSONAL_INFO,
    USE_APP_NOW,
    DASHBOARD_TUTORIAL,
    DASHBOARD;
}
