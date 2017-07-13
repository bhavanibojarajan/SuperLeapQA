package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 29/5/17.
 */
public interface ScreenInitializationType extends DashboardActionType, MealPageActionType {
    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW} after a
     * new sign up, it may display a pop-up asking the use whether he/she
     * wants to change tracker source. We need to dismiss it.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_dismissTrackerPopup(Engine)
     */
    @NotNull
    default Flowable<?> rxn_useAppInitialized(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxa_dismissTrackerPopup(engine);
        } else if (engine instanceof IOSEngine) {
            return Flowable.concatArray(
                engine.rxa_acceptAlert(),
                rxa_dismissTrackerPopup(engine)
            ).all(HPObjects::nonNull).toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL},
     * it may request for push notification permission. We need to dismiss
     * the dialog here.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxn_useAppInitialized(Engine)
     */
    @NotNull
    default Flowable<?> rxn_dashboardTutorialInitialized(@NotNull Engine<?> engine) {
        return rxn_useAppInitialized(engine);
    }

    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}, two
     * dialogs may appear asking for location, camera and photo permissions.
     * The number of dialogs may depend on the platform/version being tested on.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #photoPickerScreenDelay(Engine)
     */
    @NotNull
    default Flowable<?> rxn_photoPickerInitialized(@NotNull final Engine<?> ENGINE) {
        /* We need some delay for the screen to fully initialize, because
         * camera initialization can be fast or slow depending on the platform
         * being tested */
        return Flowable
            .timer(photoPickerScreenDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> Flowable.range(0, 3))
            .concatMap(a -> ENGINE.rxa_acceptAlert())
            .all(HPBooleans::isTrue)
            .toFlowable();
    }

    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}, first-time
     * users may see a meal image tutorial pop-up. We need to dismiss it.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_dismissMealImageTutorial(Engine)
     */
    @NotNull
    default Flowable<?> rxn_mealPageInitialized(@NotNull Engine<?> engine) {
        return rxa_dismissMealImageTutorial(engine);
    }

    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_ENTRY}, it
     * may prompt the user for location access.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxn_weightEntryInitialized(@NotNull Engine<?> engine) {
        return engine.rxa_acceptAlert();
    }

    /**
     * When the app navigates to {@link com.holmusk.SuperLeapQA.navigation.Screen}
     * that corresponds to {@link CardType}, perform the appropriate actions.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see #rxn_mealPageInitialized(Engine)
     */
    @NotNull
    default Flowable<?> rxn_cardItemPageInitialized(@NotNull Engine<?> engine,
                                                    @NotNull CardType card) {
        switch (card) {
            case MEAL:
                return rxn_mealPageInitialized(engine);

            default:
                return Flowable.just(true);
        }
    }
}
