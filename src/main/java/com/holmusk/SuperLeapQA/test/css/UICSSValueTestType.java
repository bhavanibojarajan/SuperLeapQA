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

        return data.iterator();
    }

    /**
     * Log some CSS cards and verify that the previous values are saved the
     * next time the user logs another CSS entry.
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxv_error()
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
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),

            /* For cross-platform reusability, we need to get the CSS value
             * from the CSS entry screen, since on Android it's not possible
             * to get it directly from the scroll view */
            Flowable.concatArray(
                THIS.rxa_navigate(MODE, Screen.DASHBOARD, ENTRY_SCREEN),
                THIS.rxe_selectedCSSValue(ENGINE, INPUT)
                    .flatMap(b -> Flowable.concatArray(
                        THIS.rxa_submitCSSEntry(ENGINE, INPUT),
                        THIS.rxa_backToDashboard(ENGINE),
                        THIS.rxa_navigate(MODE, Screen.DASHBOARD, VALUE_SCREEN),
                        THIS.rxa_submitCSSValue(ENGINE, INPUT),

                        ENGINE.rxe_containsText(b)
                            .firstElement().toFlowable()
                            .switchIfEmpty(ENGINE.rxv_error()),

                        THIS.rxa_dashboardFromCSSEntry(ENGINE)
                    ).all(ObjectUtil::nonNull).toFlowable())
            ).all(ObjectUtil::nonNull).toFlowable().repeat(3)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
