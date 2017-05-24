package com.holmusk.SuperLeapQA.navigation;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.ios.IOSEngine;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.List;

/**
 * Created by haipham on 5/21/17.
 */
public final class ScreenHolder implements ScreenType, NavigationType, BaseErrorType {
    @NotNull private final Screen SCREEN;
    @NotNull private final UserMode MODE;

    @NotNull
    public static ScreenHolder of(@NotNull Screen screen,
                                  @NotNull UserMode mode) {
        return new ScreenHolder(screen, mode);
    }

    private ScreenHolder(@NotNull Screen screen, @NotNull UserMode mode) {
        SCREEN = screen;
        MODE = mode;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof ScreenHolder) {
            ScreenHolder holder = (ScreenHolder)o;
            return holder.SCREEN.equals(SCREEN) && holder.MODE.equals(MODE);
        } else {
            return false;
        }
    }

    @NotNull
    public String toString() {
        return String.format("%s-%s", MODE, SCREEN);
    }

    /**
     * @param engine {@link Engine} instance to check for
     * {@link org.swiften.xtestkit.base.type.PlatformType}.
     * @return {@link Long} value.
     * @see ScreenType#animationDelay(Engine)
     */
    @Override
    public long animationDelay(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return 1500;
        } else {
            return 0;
        }
    }

    /**
     * @param ENGINE {@link Engine} instance. This is necessary because
     *               most of the time the navigation will rely on the
     *               {@link org.openqa.selenium.WebDriver}.
     * @return {@link List} of {@link ScreenType.Direction}
     */
    @NotNull
    @Override
    public List<Direction> forwardAccessible(@NotNull final Engine<?> ENGINE) {
        final NavigationType THIS = this;

        switch (SCREEN) {
            case SPLASH:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.WELCOME, MODE),
                        a -> THIS.rx_n_splash_welcome()
                    )
                );

            case WELCOME:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.SIGN_IN, MODE),
                        a -> Flowable.empty()
                    ),
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.REGISTER, MODE),
                        a -> THIS.rx_n_welcome_register(ENGINE)
                    )
                );

            case SIGN_IN:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.REGISTER, MODE),
                        a -> Flowable.empty()
                    )
                );

            case FORGOT_PASSWORD:
                return CollectionUtil.asList();

            case REGISTER:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.DOB_PICKER, MODE),
                        a -> THIS.rx_n_register_DoBPicker(ENGINE, MODE)
                    )
                );

            case DOB_PICKER:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.INVALID_AGE, MODE),
                        a -> THIS.rx_n_DoBPicker_invalidAge(ENGINE, MODE)
                    ),
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.VALID_AGE, MODE),
                        a -> THIS.rx_n_DoBPicker_validAge(ENGINE, MODE)
                    )
                );

            case INVALID_AGE:
                return CollectionUtil.asList();

            case VALID_AGE:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.PERSONAL_INFO, MODE),
                        a -> THIS.rx_n_validAge_personalInfo(ENGINE, MODE)
                    )
                );

            case PERSONAL_INFO:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.EXTRA_PERSONAL_INFO, MODE),
                        a -> THIS.rx_n_personalInfo_extraInfo(ENGINE, MODE)
                    )
                );

            case EXTRA_PERSONAL_INFO:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.USE_APP_NOW, MODE),
                        a -> THIS.rx_n_extraInfo_useApp(ENGINE, MODE)
                    )
                );

            case USE_APP_NOW:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.DASHBOARD_TUTORIAL, MODE),
                        a -> THIS.rx_n_useApp_tutorial(ENGINE)
                    )
                );

            case DASHBOARD_TUTORIAL:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.DASHBOARD, MODE),
                        a -> THIS.rx_n_tutorial_dashboard(ENGINE)
                    )
                );

            case DASHBOARD:
                return CollectionUtil.asList();

            default:
                LogUtil.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param ENGINE {@link Engine} instance. This is necessary because
     *               most of the time the navigation will rely on the
     *               {@link org.openqa.selenium.WebDriver}.
     * @return {@link List} of {@link ScreenType.Direction}
     */
    @NotNull
    @Override
    public List<Direction> backwardAccessible(@NotNull final Engine<?> ENGINE) {
        final NavigationType THIS = this;

        switch (SCREEN) {
            case SPLASH:
                return CollectionUtil.asList();

            case WELCOME:
                return CollectionUtil.asList();

            case SIGN_IN:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.WELCOME, MODE),
                        a -> THIS.rx_n_signIn_welcome(ENGINE)
                    )
                );

            case FORGOT_PASSWORD:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.SIGN_IN, MODE),
                        a -> Flowable.empty()
                    )
                );

            case REGISTER:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.WELCOME, MODE),
                        a -> THIS.rx_n_register_welcome(ENGINE)
                    )
                );

            case DOB_PICKER:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.REGISTER, MODE),
                        a -> Flowable.empty()
                    )
                );

            case INVALID_AGE:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.DOB_PICKER, MODE),
                        a -> THIS.rx_n_invalidAge_DOBPicker(ENGINE)
                    ),
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.REGISTER, MODE),
                        a -> THIS.rx_n_invalidAge_welcome(ENGINE)
                    )
                );

            case VALID_AGE:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.DOB_PICKER, MODE),
                        a -> THIS.rx_n_validAge_welcome(ENGINE)
                    )
                );

            case PERSONAL_INFO:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.VALID_AGE, MODE),
                        a -> THIS.rx_n_personalInfo_validAge(ENGINE)
                    )
                );

            case EXTRA_PERSONAL_INFO:
                return CollectionUtil.asList(
                    new Direction(
                        ENGINE, ScreenHolder.of(Screen.PERSONAL_INFO, MODE),
                        a -> THIS.rx_n_extraInfo_personalInfo(ENGINE, MODE)
                    )
                );

            case USE_APP_NOW:
                return CollectionUtil.asList();

            case DASHBOARD_TUTORIAL:
                return CollectionUtil.asList();

            case DASHBOARD:
                return CollectionUtil.asList();

            default:
                LogUtil.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}