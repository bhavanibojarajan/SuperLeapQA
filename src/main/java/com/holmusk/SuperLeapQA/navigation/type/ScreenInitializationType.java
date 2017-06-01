package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.ui.base.AppDelayType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 29/5/17.
 */
public interface ScreenInitializationType extends AppDelayType {
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
     * @see #photoPickerScreenDelay()
     */
    @NotNull
    default Flowable<?> rxn_photoPickerInitialized(@NotNull final Engine<?> ENGINE) {
        final TimeUnit UNIT = TimeUnit.MILLISECONDS;

        /* We need some delay for the screen to fully initialize, because
         * camera initialization can be fast or slow depending on the platform
         * being tested */
        return Flowable
            .timer(photoPickerScreenDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> Flowable.range(0, 3))
            .concatMap(a -> ENGINE.rxa_acceptAlert())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
}
