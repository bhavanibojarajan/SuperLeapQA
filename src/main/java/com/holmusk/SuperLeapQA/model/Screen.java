package com.holmusk.SuperLeapQA.model;

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
public enum Screen implements ScreenType, BaseErrorType {
    SPLASH,
    WELCOME,
    SIGN_IN,
    REGISTER,
    PARENT_DOB_PICKER,
    TEEN_DOB_PICKER;

    /**
     * @param engine {@link Engine} instance. This is necessary because
     *               most of the time the navigation will rely on the
     *               {@link org.openqa.selenium.WebDriver}.
     * @return {@link List} of {@link ScreenType.Direction}
     *
     */
    @NotNull
    @Override
    public List<Direction> accessibleFromHere(@NotNull Engine<?> engine) {
        switch (this) {
            case SPLASH:
                return CollectionUtil.asList(
                    new Direction(WELCOME, a -> Flowable.empty())
                );

            case WELCOME:
                return CollectionUtil.asList(
                    new Direction(SIGN_IN, a -> Flowable.empty()),
                    new Direction(REGISTER, a -> Flowable.empty())
                );

            case SIGN_IN:
                return CollectionUtil.asList(
                    new Direction(REGISTER, a -> Flowable.empty())
                );

            case REGISTER:
                return CollectionUtil.asList(
                    new Direction(WELCOME, a -> Flowable.empty()),
                    new Direction(PARENT_DOB_PICKER, a -> Flowable.empty()),
                    new Direction(TEEN_DOB_PICKER, a -> Flowable.empty())
                );

            case PARENT_DOB_PICKER:
                return CollectionUtil.asList(
                    new Direction(REGISTER, a -> Flowable.empty())
                );

            case TEEN_DOB_PICKER:
                return CollectionUtil.asList(
                    new Direction(REGISTER, a -> Flowable.empty())
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
