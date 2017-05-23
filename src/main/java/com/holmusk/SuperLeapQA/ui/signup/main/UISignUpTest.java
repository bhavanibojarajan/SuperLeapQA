package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerTestHelperType;
import com.holmusk.SuperLeapQA.ui.signup.invalidage.InvalidAgeTestHelperType;
import com.holmusk.SuperLeapQA.ui.signup.personalinfo.PersonalInfoTestHelperType;
import com.holmusk.SuperLeapQA.ui.signup.validage.ValidAgeTestHelperType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/13/17.
 */
public class UISignUpTest extends UIBaseTest implements
    DOBPickerTestHelperType,
    ValidAgeTestHelperType,
    InvalidAgeTestHelperType,
    PersonalInfoTestHelperType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UISignUpTest(int index) {
        super(index);
    }

    /**
     * This test checks that {@link UserMode#TEEN_U18} will see
     * {@link Screen#EXTRA_PERSONAL_INFO}, while {@link UserMode#TEEN_A18} will
     * not. It uses a custom {@link DataProvider} that provides only
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#EXTRA_PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #guarantorSpecificUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = true)
    @Test(dataProvider = "guarantorSpecificUserModeProvider")
    public void test_guarantorNeeded_requiresParentInfo(@NotNull final UserMode MODE) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* During the tests, if the current user requires a guarantor (i.e
         * below 18 years-old), we expect the parent information screen to
         * be present */
        rx_navigate(MODE, Screen.SPLASH, Screen.USE_APP_NOW).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
