package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIValidAgeTestType extends UIBaseTestType, ValidAgeTestHelperType {
    /**
     * Confirm that when the user selects
     * {@link ChoiceInput#HEIGHT} in
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}, every 12
     * {@link com.holmusk.SuperLeapQA.model.Height#INCH} is converted to
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}.
     * @param MODE {@link UserMode} instance.
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxh_inchToFootRecursive(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_12Inch_shouldBeConvertedTo1Foot(@NotNull final UserMode MODE) {
        // Setup
        final UIValidAgeTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rxh_inchToFootRecursive(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
