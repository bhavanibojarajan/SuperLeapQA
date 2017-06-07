package com.holmusk.SuperLeapQA.navigation;

import com.holmusk.SuperLeapQA.navigation.type.ScreenInitializationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.navigation.ScreenType;

/**
 * Created by haipham on 5/21/17.
 */
public enum Screen implements ScreenInitializationType {
    SPLASH,
    WELCOME,
    LOGIN,
    FORGOT_PASSWORD,
    REGISTER,
    SHA,
    DOB,
    INVALID_AGE,
    VALID_AGE,
    PERSONAL_INFO,
    ADDRESS_INFO,
    GUARANTOR_INFO,
    USE_APP_NOW,
    DASHBOARD_TUTORIAL,
    DASHBOARD,
    SEARCH,
    ADD_CARD,
    PHOTO_PICKER,
    LOG_MEAL,
    MEAL_PAGE,
    CHAT;

    /**
     * Get the {@link Flowable} to perform work once the current {@link Screen}
     * has been fully initialized.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxn_dashboardTutorialInitialized(Engine)
     * @see #rxn_photoPickerInitialized(Engine)
     */
    @NotNull
    public ScreenType.NavigationSupplier rxa_onInitialized(@NotNull Engine<?> engine) {
        switch (this) {
            case DASHBOARD_TUTORIAL:
                return a -> rxn_dashboardTutorialInitialized(engine);

            case PHOTO_PICKER:
                return a -> rxn_photoPickerInitialized(engine);

            default:
                return a -> Flowable.just(true);
        }
    }
}
