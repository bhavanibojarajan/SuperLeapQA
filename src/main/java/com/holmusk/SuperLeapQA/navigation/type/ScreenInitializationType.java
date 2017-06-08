package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 29/5/17.
 */
public interface ScreenInitializationType extends
    DashboardActionType,
    MealPageActionType
{
    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW} after a
     * new sign up, it may display a pop-up asking the use whether he/she
     * wants to change tracker source. We need to dismiss it.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_acceptAlert()
     * @see #rxa_dismissTrackerPopup(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxn_useAppNowInitialized(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxa_dismissTrackerPopup(ENGINE);
        } else if (ENGINE instanceof IOSEngine) {
            final ScreenInitializationType THIS = this;
            return ENGINE.rxa_acceptAlert()
                .flatMap(a -> THIS.rxa_dismissTrackerPopup(ENGINE));
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
     * @see Engine#rxa_acceptAlert()
     */
    @NotNull
    default Flowable<?> rxn_dashboardTutorialInitialized(@NotNull Engine<?> engine) {
        return engine.rxa_acceptAlert();
    }

    /**
     * When the app navigates to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}, two
     * dialogs may appear asking for location, camera and photo permissions.
     * The number of dialogs may depend on the platform/version being tested on.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isTrue(boolean)
     * @see Engine#rxa_acceptAlert()
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
            .all(BooleanUtil::isTrue)
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
}
