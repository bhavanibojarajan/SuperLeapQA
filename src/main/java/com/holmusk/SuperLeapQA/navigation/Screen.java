package com.holmusk.SuperLeapQA.navigation;

import com.holmusk.SuperLeapQA.navigation.type.ScreenInitializationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.navigation.NavigationSupplier;

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
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxn_dashboardTutorialInitialized(Engine)
     * @see #rxn_photoPickerInitialized(Engine)
     */
    @NotNull
    public NavigationSupplier rxa_onInitialized(@NotNull final Engine<?> ENGINE) {
        final Screen THIS = this;

        switch (this) {
            case USE_APP_NOW:
                return a -> THIS.rxn_useAppNowInitialized(ENGINE);

            case DASHBOARD_TUTORIAL:
                return a -> THIS.rxn_dashboardTutorialInitialized(ENGINE);

            case PHOTO_PICKER:
                return a -> THIS.rxn_photoPickerInitialized(ENGINE);

            default:
                return a -> Flowable.just(true);
        }
    }
}
