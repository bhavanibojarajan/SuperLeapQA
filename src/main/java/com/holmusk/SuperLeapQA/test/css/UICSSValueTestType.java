package com.holmusk.SuperLeapQA.test.css;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 13/6/17.
 */
public interface UICSSValueTestType extends UIBaseTestType, CSSValueActionType {
    /**
     * {@link org.testng.annotations.DataProvider} for
     * {@link #test_logCSS_shouldStartFromPreviousCSSValue(UserMode, Screen, Screen, HMCSSInputType)}
     * @return {@link Iterator} instance.
     * @see CSSInput#ACTIVITY
     * @see CSSInput#WEIGHT
     * @see Screen#ACTIVITY_ENTRY
     * @see Screen#ACTIVITY_VALUE
     * @see Screen#WEIGHT_ENTRY
     * @see Screen#WEIGHT_VALUE
     * @see UserMode#PARENT
     * @see UserMode#TEEN_A18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> cssDataProvider() {
        List<Object[]> data = new LinkedList<>();

        data.add(new Object[] {
            UserMode.PARENT,
            Screen.WEIGHT_VALUE,
            Screen.WEIGHT_ENTRY,
            CSSInput.WEIGHT
        });

        UserMode[] modes = new UserMode[] {
            UserMode.PARENT,
            UserMode.TEEN_A18
        };

        for (UserMode mode : modes) {
            data.add(new Object[] {
                mode,
                Screen.ACTIVITY_VALUE,
                Screen.ACTIVITY_ENTRY,
                CSSInput.ACTIVITY
            });
        }

        return data.iterator();
    }

    /**
     * Log some CSS cards and verify that the previous values are saved the
     * next time the user logs another CSS entry.
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxv_errorWithPageSource()
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_submitCSSValue(Engine, HMCSSInputType)
     * @see #rxa_submitCSSEntry(Engine, HMCSSInputType)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxa_dashboardFromCSSEntry(Engine)
     * @see #rxe_selectedCSSValue(Engine, HMCSSInputType)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UICSSValueTestType.class,
        dataProvider = "cssDataProvider"
    )
    default void test_logCSS_shouldStartFromPreviousCSSValue(
        @NotNull final UserMode MODE,
        @NotNull final Screen VALUE_SCREEN,
        @NotNull final Screen ENTRY_SCREEN,
        @NotNull final HMCSSInputType INPUT
    ) {
        // Setup
        final UICSSValueTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final int TRIES = 3;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD)
            .concatMap(a -> Flowable.range(0, TRIES))
            .concatMap(a -> THIS.rxa_navigate(MODE, Screen.DASHBOARD, ENTRY_SCREEN)

                /* For cross-platform reusability, we need to get the CSS value
                 * from the CSS entry screen, since on Android it's not
                 * possible to get it directly from the scroll view */
                .flatMap(b -> THIS.rxe_selectedCSSValue(ENGINE, INPUT))
                .flatMap(b -> THIS.rxa_submitCSSEntry(ENGINE, INPUT)
                    .flatMap(c -> THIS.rxa_backToDashboard(ENGINE))
                    .flatMap(c -> THIS.rxa_navigate(MODE, Screen.DASHBOARD, VALUE_SCREEN))
                    .flatMap(c -> THIS.rxa_submitCSSValue(ENGINE, INPUT))

                    /* The value should have been saved from the previous log */
                    .flatMap(c -> ENGINE.rxe_containsText(b)
                        .firstElement().toFlowable()
                        .switchIfEmpty(ENGINE.rxv_errorWithPageSource()))
                    .flatMap(c -> THIS.rxa_dashboardFromCSSEntry(ENGINE))))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
