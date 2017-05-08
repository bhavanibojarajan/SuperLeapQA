package com.holmusk.SuperLeapQA.onboarding.welcome;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.engine.base.BaseEngine;
import org.swiften.xtestkit.engine.base.param.UnidirectionalSwipeParam;

/**
 * Created by haipham on 5/7/17.
 */

/**
 * Validate splash screen.
 */
public interface WelcomeValidationType extends BaseValidationType {
    /**
     * Get the register button on the welcome screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxWelcomeRegisterButton() {
        return currentEngine().rxElementContainingText("welcome_title_register");
    }

    /**
     * Get the sign in button on the welcome screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxWelcomeSignInButton() {
        return currentEngine().rxElementContainingText("welcome_title_signIn");
    }

    /**
     * Validate that all views are present in splash screen.
     * @return A {@link Flowable} instance.
     */
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateWelcomeScreen() {
        return Flowable
            .concat(rxWelcomeSignInButton(), rxWelcomeRegisterButton())
            .all(ObjectUtil::nonNull)
            .map(a -> true)
            .toFlowable();
    }

    /**
     * Validate the swipeable splash screens.
     * @return A {@link Flowable} instance.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateSwipes() {
        final BaseEngine<?> ENGINE = currentEngine();

        final String[][] MESSAGES = new String[][] {
            {
                "welcome_title_oneLiner",
                "welcome_title_getOnRightTrack"
            },
            {
                "welcome_title_enjoyFavoriteFood",
                "welcome_title_guideThroughSmallImprovements"
            },
            {
                "welcome_title_realLifeProfessionals",
                "welcome_title_healthExpertsByYourSide"
            }
        };

        final int LENGTH = MESSAGES.length;

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
                        /* Swipe once from right to left */
                        .flatMap(a -> ENGINE.rxSwipeGenericRL(
                            UnidirectionalSwipeParam.builder()
                                .withTimes(1)
                                .withDuration(0)
                                .build()
                        ))
                        .flatMap(a -> new CheckScreen().checkScreen(INDEX + 1));
                } else {
                    return Flowable.empty();
                }
            }
        }

        return ENGINE
            .rxSwipeGenericLR(
                UnidirectionalSwipeParam.builder()
                    .withTimes(LENGTH)
                    .withDuration(0)
                    .build()
            )
            .flatMap(a -> new CheckScreen().checkScreen(0).map(b -> true));
    }
}
