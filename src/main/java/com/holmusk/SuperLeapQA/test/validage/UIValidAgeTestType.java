package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIValidAgeTestType extends UIBaseTestType, ValidAgeTestHelperType {
    /**
     * Get the {@link UserMode} to be used for BMI check, because not all
     * {@link UserMode} requires such a check.
     * @return {@link Iterator}.
     * @see UserMode#requiresBMICheck()
     * @see UserMode#values()
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> bmiCheckUserModeProvider() {
        List<Object[]> data = new LinkedList<>();
        UserMode[] modes = UserMode.values();

        for (UserMode mode : modes) {
            if (mode.requiresBMICheck()) {
                data.add(new Object[] { mode });
            }
        }

        return data.iterator();
    }

    /**
     * Confirm that when the user selects
     * {@link ChoiceInput#HEIGHT} in
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}, every 12
     * {@link com.holmusk.SuperLeapQA.model.Height#INCH} is converted to
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}.
     * @param mode {@link UserMode} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#VALID_AGE
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxh_inchToFootRecursive(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_12Inch_shouldBeConvertedTo1Foot(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.VALID_AGE),
            rxh_inchToFootRecursive(engine, mode)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Confirm that when BMI calculation fails (i.e. the user is within
     * healthy BMI range), the app should notify and fail the process. We do
     * so by deliberating selecting height/weight so that BMI calculations
     * return a healthy figure.
     * @param mode {@link UserMode} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#VALID_AGE
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_completeValidAgeInputs(Engine, UserMode, boolean)
     * @see #rxv_unqualifiedBMI(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIValidAgeTestType.class,
        dataProvider = "bmiCheckUserModeProvider"
    )
    default void test_BMIOutOfRange_shouldNotifyUser(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.VALID_AGE),
            rxa_completeValidAgeInputs(engine, mode, false),
            rxv_unqualifiedBMI(engine)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
