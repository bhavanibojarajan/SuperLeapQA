package com.holmusk.SuperLeapQA.onboarding.splash;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.engine.base.PlatformEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/7/17.
 */

/**
 * Validate splash screen.
 */
public interface OnboardingValidationType extends BaseValidationType {
    /**
     * Validate that all views are present in splash screen.
     * @return A {@link Flowable} instance.
     */
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateViews() {
        PlatformEngine engine = currentEngine();

        return Flowable
            .concat(
                engine.rxElementsContainingText("auth_title_signIn"),
                engine.rxElementsContainingText("auth_title_register"),
                rxValidateSplashScreens(engine)
            )
            .all(ObjectUtil::nonNull)
            .map(a -> true)
            .toFlowable();
    }

    /**
     * Validate the swipeable splash screens.
     * @return A {@link Flowable} instance.
     */
    @NonNull
    default Flowable<Boolean> rxValidateSplashScreens(@NonNull final PlatformEngine ENGINE) {
        final String[][] MESSAGES = new String[][] {
            {
                "splash_title_oneLiner",
                "splash_title_getOnRightTrack"
            },
            {
                "splash_title_enjoyFavoriteFood",
                "splash_title_guideThroughSmallImprovements"
            },
            {
                "splash_title_realLifeProfessionals",
                "splash_title_healthExpertsByYourSide"
            }
        };

        final int LENGTH = MESSAGES.length;
        final long DELAY = splashSwipeScreenDelay();

        class CheckScreen {
            @NonNull
            @SuppressWarnings("WeakerAccess")
            Flowable<WebElement> checkScreen(final int INDEX) {
                if (INDEX < LENGTH) {
                    String[] messages = MESSAGES[INDEX];

                    return Flowable.fromArray(messages)
                        .flatMap(ENGINE::rxElementContainingText)
                        .all(ObjectUtil::nonNull)
                        .toFlowable()
                        .delay(DELAY, TimeUnit.MILLISECONDS)
                        .flatMap(a -> new CheckScreen().checkScreen(INDEX + 1));
                } else {
                    return Flowable.empty();
                }
            }
        }

        return new CheckScreen().checkScreen(0).map(a -> true);
    }
}
