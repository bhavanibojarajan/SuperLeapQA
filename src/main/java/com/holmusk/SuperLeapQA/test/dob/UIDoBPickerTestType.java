package com.holmusk.SuperLeapQA.test.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIDoBPickerTestType extends UIBaseTestType, DOBPickerTestHelperType {
    /**
     * This test validates that {@link Screen#DOB} selection works by
     * sequentially selecting DoBs from a range of {@link java.util.Date}.
     * Note that this test is not guarantor-aware, so
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same.
     * @param MODE {@link UserMode} instance.
     * @see Screen#DOB
     * @see #engine()
     * @see UserMode#offsetFromCategoryValidRange(int)
     * @see #rxh_validateDoBsRecursive(Engine, UserMode, List)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UIDoBPickerTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        final List<Integer> AGES = MODE.offsetFromCategoryValidRange(2);

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> THIS.rxh_validateDoBsRecursive(ENGINE, MODE, AGES))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
