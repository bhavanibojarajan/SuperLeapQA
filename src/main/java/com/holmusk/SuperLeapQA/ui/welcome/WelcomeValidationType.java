package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.param.UnidirectionParam;
import org.swiften.xtestkit.base.type.DurationType;

/**
 * Created by haipham on 5/7/17.
 */
public interface WelcomeValidationType extends BaseValidationType {
    /**
     * Get the register button on the welcome screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_welcomeRegister(@NotNull Engine<?> engine) {
        return engine.rxe_containsText("welcome_title_register").firstElement().toFlowable();
    }

    /**
     * Get the sign in button on the welcome screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_signIn(@NotNull Engine<?> engine) {
        return engine.rxe_containsText("welcome_title_signIn").firstElement().toFlowable();
    }

    /**
     * Validate that all views are present in splash screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_signIn(Engine)
     * @see #rxe_welcomeRegister(Engine)
     * @see ObjectUtil#nonNull(Object)
     * @see BooleanUtil#toTrue(Object)
     */
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_welcomeScreen(@NotNull Engine<?> engine) {
        return Flowable
            .concat(rxe_signIn(engine), rxe_welcomeRegister(engine))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate the swipeable splash screens.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rx_swipeGenericLR(DurationType)
     * @see Engine#rxSwipeGenericRL(DurationType)
     */
    @NonNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_welcomeSwipes(@NotNull final Engine<?> ENGINE) {
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
                        .flatMap(ENGINE::rxe_containsText)
                        .all(ObjectUtil::nonNull)
                        .toFlowable()

                        /* Swipe once from right to left */
                        .flatMap(a -> ENGINE.rxSwipeGenericRL(
                            UnidirectionParam.builder()
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
            .rx_swipeGenericLR(UnidirectionParam.builder()
                .withTimes(LENGTH)
                .withDuration(0)
                .build())
            .flatMap(a -> new CheckScreen().checkScreen(0));
    }
}
