package com.holmusk.SuperLeapQA.navigation;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.type.BackwardNavigationType;
import com.holmusk.SuperLeapQA.navigation.type.ForwardNavigationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.navigation.NavigationSupplier;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.List;

/**
 * Created by haipham on 5/21/17.
 */
@SuppressWarnings("WeakerAccess")
public final class ScreenHolder implements
    ScreenType,
    ForwardNavigationType,
    BackwardNavigationType
{
    @NotNull public final Screen SCREEN;
    @NotNull public final UserMode MODE;
    @NotNull public final NavigationSupplier INITIALIZATION;

    @NotNull
    public static ScreenHolder of(@NotNull Engine<?> engine,
                                  @NotNull Screen screen,
                                  @NotNull UserMode mode) {
        return new ScreenHolder(screen, mode, screen.rxa_onInitialized(engine));
    }

    private ScreenHolder(@NotNull Screen screen,
                         @NotNull UserMode mode,
                         @NotNull NavigationSupplier init) {
        SCREEN = screen;
        MODE = mode;
        INITIALIZATION = init;
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
    @Override
    public String toString() {
        return String.format("%s-%s", MODE, SCREEN);
    }

    @NotNull
    @Override
    public NavigationSupplier rxa_onInitialized() {
        return INITIALIZATION;
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link Long} value.
     */
    @Override
    public long animationDelay(@NotNull PlatformType platform) {
        if (platform.equals(Platform.IOS)) {
            return 1500;
        } else {
            return 1000;
        }
    }

    /**
     * Override this to provide default implementation.
     * @param ENGINE {@link Engine} instance. This is necessary because
     *               most of the time the navigation will rely on the
     *               {@link org.openqa.selenium.WebDriver}.
     * @return {@link List} of {@link ScreenType.Direction}
     */
    @NotNull
    @Override
    public List<Direction> forwardAccessible(@NotNull final Engine<?> ENGINE) {
        final ScreenHolder THIS = this;
        PlatformType platform = ENGINE.platform();

        switch (SCREEN) {
            case SPLASH:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WELCOME, MODE),
                        a -> THIS.rxn_splash_welcome(ENGINE),
                        platform
                    )
                );

            case WELCOME:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.LOGIN, MODE),
                        a -> THIS.rxn_welcome_login(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.REGISTER, MODE),
                        a -> THIS.rxn_welcome_register(ENGINE),
                        platform
                    )
                );

            case LOGIN:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.FORGOT_PASSWORD, MODE),
                        a -> THIS.rxn_login_forgotPassword(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.REGISTER, MODE),
                        a -> THIS.rxn_login_register(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DASHBOARD_TUTORIAL, MODE),
                        a -> THIS.rxn_login_tutorial(ENGINE, MODE),
                        platform
                    )
                );

            case FORGOT_PASSWORD:
                return HPIterables.asList();

            case REGISTER:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.SHA, MODE),
                        a -> THIS.rxn_register_sha(ENGINE, MODE),
                        platform
                    )
                );

            case SHA:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DOB, MODE),
                        a -> THIS.rxn_sha_DoBPicker(ENGINE, MODE),
                        platform
                    )
                );

            case DOB:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.INVALID_AGE, MODE),
                        a -> THIS.rxn_DoBPicker_invalidAge(ENGINE, MODE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.VALID_AGE, MODE),
                        a -> THIS.rxn_DoBPicker_validAge(ENGINE, MODE),
                        platform
                    )
                );

            case INVALID_AGE:
                return HPIterables.asList();

            case VALID_AGE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.PERSONAL_INFO, MODE),
                        a -> THIS.rxn_validAge_personalInfo(ENGINE, MODE),
                        platform
                    )
                );

            case PERSONAL_INFO:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.ADDRESS_INFO, MODE),
                        a -> THIS.rxn_personalInfo_addressInfo(ENGINE, MODE),
                        platform
                    )
                );

            case ADDRESS_INFO:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.GUARANTOR_INFO, MODE),
                        a -> THIS.rxn_addressInfo_guarantorInfo(ENGINE),
                        platform
                    )
                );

            case GUARANTOR_INFO:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.USE_APP_NOW, MODE),
                        a -> THIS.rxn_guarantorInfo_useApp(ENGINE, MODE),
                        platform
                    )
                );

            case USE_APP_NOW:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DASHBOARD_TUTORIAL, MODE),
                        a -> THIS.rxn_useApp_tutorial(ENGINE),
                        platform
                    )
                );

            case DASHBOARD_TUTORIAL:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DASHBOARD, MODE),
                        a -> THIS.rxn_tutorial_dashboard(ENGINE),
                        platform
                    )
                );

            case DASHBOARD:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.ADD_CARD, MODE),
                        a -> THIS.rxn_dashboard_addCard(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.SEARCH, MODE),
                        a -> THIS.rxn_dashboard_search(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.SETTINGS, MODE),
                        a -> THIS.rxn_dashboard_settings(ENGINE),
                        platform
                    )
                );

            case SEARCH:
                return HPIterables.asList();

            case ADD_CARD:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.PHOTO_PICKER, MODE),
                        a -> THIS.rxn_addCard_photoPicker(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WEIGHT_VALUE, MODE),
                        a -> THIS.rxn_addCard_weightValue(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.ACTIVITY_VALUE, MODE),
                        a -> THIS.rxn_addCard_activityValue(ENGINE),
                        platform
                    )
                );

            case PHOTO_PICKER:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.MEAL_ENTRY, MODE),
                        a -> THIS.rxn_photoPicker_logMeal(ENGINE),
                        platform
                    )
                );

            case MEAL_ENTRY:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.MEAL_PAGE, MODE),
                        a -> THIS.rxn_logMeal_mealPage(ENGINE),
                        platform
                    )
                );

            case MEAL_PAGE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.CHAT, MODE),
                        a -> THIS.rxn_mealPage_chat(ENGINE),
                        platform
                    )
                );

            case CHAT:
                return HPIterables.asList();

            case WEIGHT_VALUE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WEIGHT_ENTRY, MODE),
                        a -> THIS.rxn_weightValue_weightEntry(ENGINE),
                        platform
                    )
                );

            case WEIGHT_ENTRY:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WEIGHT_PAGE, MODE),
                        a -> THIS.rxn_weightEntry_weightPage(ENGINE),
                        platform
                    )
                );

            case WEIGHT_PAGE:
                return HPIterables.asList();

            case ACTIVITY_VALUE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.ACTIVITY_ENTRY, MODE),
                        a -> THIS.rxn_activityValue_activityEntry(ENGINE),
                        platform
                    )
                );

            case ACTIVITY_ENTRY:
                return HPIterables.asList();

            case SETTINGS:
                return HPIterables.asList();

            default:
                HPLog.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this to provide default implementation.
     * @param ENGINE {@link Engine} instance. This is necessary because
     *               most of the time the navigation will rely on the
     *               {@link org.openqa.selenium.WebDriver}.
     * @return {@link List} of {@link ScreenType.Direction}
     */
    @NotNull
    @Override
    public List<Direction> backwardAccessible(@NotNull final Engine<?> ENGINE) {
        final ScreenHolder THIS = this;
        PlatformType platform = ENGINE.platform();

        switch (SCREEN) {
            case SPLASH:
                return HPIterables.asList();

            case WELCOME:
                return HPIterables.asList();

            case LOGIN:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WELCOME, MODE),
                        a -> THIS.rxn_login_welcome(ENGINE),
                        platform
                    )
                );

            case FORGOT_PASSWORD:
                return HPIterables.asList();

            case REGISTER:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.WELCOME, MODE),
                        a -> THIS.rxn_register_welcome(ENGINE),
                        platform
                    )
                );

            case SHA:
                return HPIterables.asList();

            case DOB:
                return HPIterables.asList();

            case INVALID_AGE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DOB, MODE),
                        a -> THIS.rxn_invalidAge_DOBPicker(ENGINE),
                        platform
                    ),
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.REGISTER, MODE),
                        a -> THIS.rxn_invalidAge_welcome(ENGINE),
                        platform
                    )
                );

            case VALID_AGE:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DOB, MODE),
                        a -> THIS.rxn_validAge_DoB(ENGINE),
                        platform
                    )
                );

            case PERSONAL_INFO:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.VALID_AGE, MODE),
                        a -> THIS.rxn_personalInfo_validAge(ENGINE),
                        platform
                    )
                );

            case ADDRESS_INFO:
                return HPIterables.asList();

            case GUARANTOR_INFO:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.ADDRESS_INFO, MODE),
                        a -> THIS.rxn_guarantorInfo_addressInfo(ENGINE, MODE),
                        platform
                    )
                );

            case USE_APP_NOW:
                return HPIterables.asList();

            case DASHBOARD_TUTORIAL:
                return HPIterables.asList();

            case DASHBOARD:
                return HPIterables.asList();

            case SEARCH:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.DASHBOARD, MODE),
                        a -> THIS.rxn_search_dashboard(ENGINE),
                        platform
                    )
                );

            case ADD_CARD:
                return HPIterables.asList();

            case PHOTO_PICKER:
                return HPIterables.asList();

            case MEAL_ENTRY:
                return HPIterables.asList();

            case MEAL_PAGE:
                return HPIterables.asList();

            case CHAT:
                return HPIterables.asList(
                    new Direction(
                        ScreenHolder.of(ENGINE, Screen.MEAL_PAGE, MODE),
                        a -> THIS.rxn_chat_mealPage(ENGINE),
                        platform
                    )
                );

            case WEIGHT_VALUE:
                return HPIterables.asList();

            case WEIGHT_ENTRY:
                return HPIterables.asList();

            case WEIGHT_PAGE:
                return HPIterables.asList();

            case ACTIVITY_VALUE:
                return HPIterables.asList();

            case ACTIVITY_ENTRY:
                return HPIterables.asList();

            case SETTINGS:
                return HPIterables.asList();

            default:
                HPLog.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
