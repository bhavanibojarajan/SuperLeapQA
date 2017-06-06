package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.model.TextInputType;
import org.swiften.xtestkit.mobile.Platform;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIPersonalInfoTestType extends UIBaseTestType, PersonalInfoActionType {
    /**
     * This {@link DataProvider} is used to check for either/or input
     * requirement when the user is entering guarantor information.
     * @return {@link Iterator} instance.
     * @see TextInput#PARENT_NAME
     * @see TextInput#PARENT_EMAIL
     * @see TextInput#PARENT_MOBILE
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> parentPersonalInfoProvider() {
        return Arrays.asList(
            new Object[] {
                Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_EMAIL)
            },
            new Object[] {
                Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_MOBILE)
            }
        ).iterator();
    }

    /**
     * This test validates that the password mask, when clicks, reveals the
     * password. It is only applicable to
     * {@link Platform#ANDROID} since on
     * {@link Platform#IOS} there is no way to
     * reveal the password content.
     * @see Engine#isShowingPassword(WebElement)
     * @see Engine#rxa_togglePasswordMask(WebElement)
     * @see Engine#rxv_errorWithPageSource()
     * @see Screen#SPLASH
     * @see Screen#PERSONAL_INFO
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_confirmTextInput(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_togglePasswordMask_shouldWork() {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.just(ENGINE)
            .filter(a -> a instanceof AndroidEngine)
            .flatMap(a -> THIS.rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO))
            .flatMap(a -> THIS.rxa_randomInput(ENGINE, TextInput.PASSWORD))
            .flatMap(a -> THIS.rxa_confirmTextInput(ENGINE)
                .flatMap(b -> ENGINE.rxa_togglePasswordMask(a))
                .filter(ENGINE::isShowingPassword)
                .switchIfEmpty(ENGINE.rxv_errorWithPageSource())
            )
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TOC and opens up
     * the Web browser, {@link Screen#PERSONAL_INFO} inputs are saved and then
     * restored when the user gets back to the app. This is more relevant for
     * {@link Platform#ANDROID}.
     * @see BooleanUtil#isTrue(boolean)
     * @see HMTextType#randomInput()
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalDelay(Engine)
     * @see #webViewDelay(Engine)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_input(Engine, HMInputType, String)
     * @see #rxa_openTC(Engine)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     * @see #rxv_hasValue(Engine, HMInputType, String)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_leavePersonalInfo_shouldSaveState() {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final UserMode MODE = UserMode.PARENT;
        final Map<String,String> INPUTS = new HashMap<>();
        List<HMTextType> info = MODE.personalInfo(PLATFORM);

        final List<HMTextType> TEXT_INFO = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(HMTextType.class::cast)
            .collect(Collectors.toList());

        TEXT_INFO.forEach(a -> INPUTS.put(a.toString(), a.randomInput()));
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.just(ENGINE)
            .filter(a -> a instanceof AndroidEngine)
            .flatMap(a -> THIS.rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO))
            .concatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rxa_input(ENGINE, a, INPUTS.get(a.toString())))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))

            /* We use toList here because we want to intercept the empty
             * event as well */
            .toList()
            .filter(CollectionUtil::isNotEmpty)
            .toFlowable()

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            .flatMap(a -> THIS.rxe_editField(ENGINE, TextInput.PASSWORD))
            .flatMap(ENGINE::rxa_togglePasswordMask)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)

            .flatMap(a -> THIS.rxa_openTC(ENGINE))
            .delay(webViewDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxa_navigateBackOnce())
            .flatMapIterable(a -> TEXT_INFO)
            .flatMap(a -> THIS.rxv_hasValue(ENGINE, a, INPUTS.get(a.toString())))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the TOC checkbox has to be ticked before the
     * user continues any further. The check happens in
     * {@link Screen#PERSONAL_INFO}.
     * @param MODE {@link UserMode} instance.
     * @see Engine#rxa_hideKeyboard()
     * @see Screen#SPLASH
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see #assertCorrectness(TestSubscriber)
     * @see #generalUserModeProvider()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmPersonalInfo(Engine)
     * @see #rxv_personalInfoScreen(Engine, UserMode)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_requireTOCAccepted_toProceed(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final List<HMTextType> INFO = MODE.personalInfo(PLATFORM);
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rxa_randomInputs(ENGINE, INFO))
            .flatMap(a -> THIS.rxa_confirmPersonalInfo(ENGINE))
            .delay(2000, TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxv_personalInfoScreen(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#GUARANTOR_INFO} for
     * parents/guarantors should only require either
     * {@link TextInput#PARENT_MOBILE} or {@link TextInput#PARENT_EMAIL}.
     * This test is only applicable for {@link UserMode#TEEN_U18}, so we use
     * {@link DataProvider} that provides {@link InputType}.
     * @param INPUTS {@link List} of {@link InputType}.
     * @see Screen#SPLASH
     * @see Screen#GUARANTOR_INFO
     * @see UserMode#TEEN_U18
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #parentPersonalInfoProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     * @see #rxe_progressBar(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIPersonalInfoTestType.class,
        dataProvider = "parentPersonalInfoProvider"
    )
    default void test_parentInfo_phoneOrEmail(@NotNull final List<HMTextType> INPUTS) {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.TEEN_U18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.GUARANTOR_INFO)
            .flatMap(a -> THIS.rxa_randomInputs(ENGINE, INPUTS))
            .flatMap(a -> THIS.rxa_confirmGuarantorInfo(ENGINE, MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rxe_progressBar(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link UserMode#TEEN_U18} will see
     * {@link Screen#GUARANTOR_INFO}, while {@link UserMode#TEEN_A18} will
     * not. It uses a custom {@link DataProvider} that provides only
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#SPLASH
     * @see Screen#GUARANTOR_INFO
     * @see Screen#USE_APP_NOW
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #guarantorSpecificUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "guarantorSpecificUserModeProvider"
    )
    default void test_guarantorNeeded_requiresParentInfo(@NotNull final UserMode MODE) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* During the tests, if the current user requires a guarantor (i.e
         * below 18 years-old), we expect the parent information screen to
         * be present */
        rxa_navigate(MODE, Screen.SPLASH, Screen.USE_APP_NOW).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
