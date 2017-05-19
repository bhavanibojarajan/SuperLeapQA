package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.param.UnidirectionalSwipeParam;
import org.swiften.xtestkit.base.type.DurationType;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeValidationType extends BaseValidationType {
    /**
     * Get the register button on the welcome screen.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxWelcomeRegisterButton() {
        return engine().rx_elementContainingText("welcome_title_register");
    }

    /**
     * Get the sign in button on the welcome screen.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxWelcomeSignInButton() {
        return engine().rx_elementContainingText("welcome_title_signIn");
    }

    /**
     * Validate that all views are present in splash screen.
     * @return A {@link Flowable} instance.
     * @see #rxWelcomeSignInButton()
     * @see #rxWelcomeRegisterButton()
     * @see ObjectUtil#nonNull(Object)
     * @see BooleanUtil#toTrue(Object)
     */
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateWelcomeScreen() {
        return Flowable
            .concat(rxWelcomeSignInButton(), rxWelcomeRegisterButton())
            .all(ObjectUtil::nonNull)
            .map(BooleanUtil::toTrue)
            .toFlowable();
    }

    /**
     * Validate the swipeable splash screens.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see BaseEngine#rx_swipeGenericLR(DurationType)
     * @see BaseEngine#rxSwipeGenericRL(DurationType)
     */
    @NonNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateSwipes() {
        final BaseEngine<?> ENGINE = engine();

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
            Flowable<Boolean> checkScreen(final int INDEX) {
                if (INDEX < LENGTH) {
                    String[] messages = MESSAGES[INDEX];

                    return Flowable.fromArray(messages)
                        .flatMap(ENGINE::rx_elementContainingText)
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
                    return Flowable.just(true);
                }
            }
        }

        return ENGINE
            .rx_swipeGenericLR(
                UnidirectionalSwipeParam.builder()
                    .withTimes(LENGTH)
                    .withDuration(0)
                    .build()
            )
            .flatMap(a -> new CheckScreen().checkScreen(0));
    }
}
