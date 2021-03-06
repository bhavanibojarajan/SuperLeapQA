package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.test.HPTestNGs;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.model.TextInputType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> parentPersonalInfoProvider() {
        return HPTestNGs
            .oneFromEach(
                new Object[] { TextInput.PARENT_NAME },
                new Object[] { TextInput.PARENT_EMAIL, TextInput.PARENT_MOBILE }
            )
            .stream()
            .map(HPIterables::asList)
            .map(a -> new Object[] { a })
            .collect(Collectors.toList())
            .iterator();
    }

    /**
     * This test validates that the password mask, when clicks, reveals the
     * password. It is only applicable to {@link Platform#ANDROID} since on
     * {@link Platform#IOS} there is no way to reveal the password content.
     * @see Engine#isShowingPassword(WebElement)
     * @see Engine#togglePasswordMaskFn() 
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_confirmTextInput(Engine)
     */
    @Test
    @SuppressWarnings({"unchecked", "Convert2MethodRef"})
    default void test_togglePasswordMask_shouldWork() {
        // Setup
        final Engine<?> ENGINE = engine();

        /* If not testing on Android, skip this test */
        if (!(ENGINE instanceof AndroidEngine)) {
            return;
        }

        UserMode mode = UserMode.defaultUserMode();
        TextInput input = TextInput.PASSWORD;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.PERSONAL_INFO),

            rxa_randomInput(ENGINE, TextInput.PASSWORD),
            rxa_confirmTextInput(ENGINE),

            rxe_editField(ENGINE, input).compose(ENGINE.togglePasswordMaskFn()),

            rxe_editField(ENGINE, input)
                .filter(ENGINE::isShowingPassword)
                .switchIfEmpty(ENGINE.rxv_error())
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TC and opens up
     * the Web browser, {@link Screen#PERSONAL_INFO} inputs are saved and then
     * restored when the user gets back to the app. This is more relevant for
     * {@link Platform#ANDROID}.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalDelay(Engine)
     * @see #webViewDelay(Engine)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_input(Engine, HMInputType, String)
     * @see #rxa_openTC(Engine)
     * @see #rxa_makeNextInputVisible(Engine)
     * @see #rxv_fieldHasValue(Engine, HMInputType, String)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_leavePersonalInfo_shouldSaveState() {
        // Setup
        final Engine<?> ENGINE = engine();

        /* If not testing on Android, skip this test */
        if (!(ENGINE instanceof AndroidEngine)) {
            return;
        }

        final UIPersonalInfoTestType THIS = this;
        final PlatformType PLATFORM = ENGINE.platform();
        final UserMode MODE = UserMode.defaultUserMode();
        final Map<String,String> INPUTS = new HashMap<>();
        List<HMTextType> info = MODE.personalInfo(PLATFORM);

        List<HMTextType> textInfo = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(HMTextType.class::cast)
            .collect(Collectors.toList());

        textInfo.forEach(a -> INPUTS.put(a.toString(), a.randomInput(ENGINE)));
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO),

            Flowable.fromIterable(textInfo)
                .concatMap(a -> Flowable.concatArray(
                    THIS.rxa_input(ENGINE, a, INPUTS.get(a.toString())),
                    THIS.rxa_makeNextInputVisible(ENGINE)
                )),

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            rxe_editField(ENGINE, TextInput.PASSWORD)
                .compose(ENGINE.togglePasswordMaskFn())
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS),

            rxa_openTC(ENGINE),
            ENGINE.rxa_navigateBackOnce(),

            Flowable.fromIterable(textInfo).flatMap(a ->
                THIS.rxv_fieldHasValue(ENGINE, a, INPUTS.get(a.toString()))
            )
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the TOC checkbox has to be ticked before the
     * user continues any further. The check happens in
     * {@link Screen#PERSONAL_INFO}.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #generalUserModeProvider()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmPersonalInfo(Engine)
     * @see #rxe_TCCheckBox(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_requireTCAccepted_toProceed(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        PlatformType platform = engine.platform();
        List<HMTextType> info = mode.personalInfo(platform);
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.PERSONAL_INFO),
            rxa_randomInputs(engine, info),
            rxa_confirmPersonalInfo(engine),

            /* If the check box is present, that means we haven't left the
             * current screen yet */
            rxe_TCCheckBox(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

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
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #parentPersonalInfoProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     * @see #rxe_progressBar(Engine)
     * @see #rxn_useAppInitialized(Engine)
     * @see #rxv_useAppNow(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIPersonalInfoTestType.class,
        dataProvider = "parentPersonalInfoProvider"
    )
    default void test_parentInfo_phoneOrEmail(@NotNull final List<HMTextType> INPUTS) {
        // Setup
        Engine<?> engine = engine();
        UserMode MODE = UserMode.TEEN_U18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.GUARANTOR_INFO),
            rxa_randomInputs(engine, INPUTS),
            rxa_confirmGuarantorInfo(engine, MODE),
            rxn_useAppInitialized(engine),
            rxv_useAppNow(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

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
    default void test_guarantorNeeded_requiresGuarantorInfo(@NotNull final UserMode MODE) {
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
