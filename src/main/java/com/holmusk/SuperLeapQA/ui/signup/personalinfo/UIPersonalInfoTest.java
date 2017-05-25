package com.holmusk.SuperLeapQA.ui.signup.personalinfo;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.model.TextInputType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 23/5/17.
 */
public final class UIPersonalInfoTest extends UIBaseTest implements
    NavigationType, PersonalInfoTestHelperType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIPersonalInfoTest(int index) {
        super(index);
    }

    @DataProvider
    public Iterator<Object[]> parentPersonalInfoProvider() {
        List<Object[]> data = new LinkedList<>();

        data.add(new Object[] {
            Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_EMAIL)
        });

        data.add(new Object[] {
            Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_MOBILE)
        });

        return data.iterator();
    }

    /**
     * This test validates that {@link Screen#PERSONAL_INFO} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility and interacting with each of them.
     * @param MODE {@link UserMode} instance.
     * @see Screen#PERSONAL_INFO
     * @see Engine#rx_toggleNextOrDoneInput(WebElement)
     * @see Engine#rx_togglePasswordMask(WebElement)
     * @see Engine#isShowingPassword(WebElement)
     * @see ObjectUtil#nonNull(Object)
     * @see UserMode#personalInfo(PlatformType)
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #generalUserModeProvider()
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see #rx_e_editField(Engine, SLInputType)
     * @see #rx_a_toggleNextInputOrDone(Engine, WebElement)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider", groups = "ValidateScreen")
    public void test_personalInfoScreen_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTest THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rx_v_personalInfoScreen(ENGINE, MODE))
            .observeOn(Schedulers.trampoline())
            .concatMapIterable(a -> MODE.personalInfo(PLATFORM))
            .ofType(SLTextInputType.class)
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, a))
            .flatMap(a -> THIS.rx_a_toggleNextInputOrDone(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, TextInput.PASSWORD))

            /* Toggle the password mask and check that the password is hidden
             * (if applicable) */
            .flatMap(a -> THIS
                .rx_a_toggleNextInputOrDone(ENGINE, a)
                .flatMap(b -> THIS.rx_h_checkPasswordMask(ENGINE, a))
                .switchIfEmpty(RxUtil.error())
            )
            .flatMap(a -> THIS.rx_a_confirmTextInput(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TOC and opens up
     * the Web browser, {@link Screen#PERSONAL_INFO} inputs are saved and then
     * restored when the user gets back to the app. This is more relevant for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @param MODE {@link UserMode} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see org.swiften.xtestkit.mobile.Platform#ANDROID
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_enterInput(Engine, SLInputType, String)
     * @see #rx_a_OpenTOC(Engine)
     * @see #rx_v_editFieldHasValue(Engine, SLInputType, String)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_leavePersonalInfo_shouldSaveState(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTest THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final Map<String,String> INPUTS = new HashMap<>();
        List<SLInputType> info = MODE.personalInfo(PLATFORM);

        final List<SLTextInputType> TEXT_INFO = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(SLTextInputType.class::cast)
            .collect(Collectors.toList());

        TEXT_INFO.forEach(a -> INPUTS.put(a.toString(), a.randomInput()));
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .concatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rx_a_enterInput(ENGINE, a, INPUTS.get(a.toString())))
            .flatMap(ENGINE::rx_toggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> ENGINE.rx_hideKeyboard())

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            .flatMap(a -> THIS.rx_e_editField(ENGINE, TextInput.PASSWORD))
            .flatMap(ENGINE::rx_togglePasswordMask)

            .flatMap(a -> THIS.rx_a_OpenTOC(ENGINE))
            .delay(webViewDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rx_navigateBackOnce())
            .flatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rx_v_editFieldHasValue(ENGINE, a, INPUTS.get(a.toString())))
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
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see Engine#rx_hideKeyboard()
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see #rx_a_confirmPersonalInfo(Engine)
     * @see #rx_v_personalInfoScreen(Engine, UserMode)
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "generalUserModeProvider")
    public void test_requireTOCAccepted_toProceedFurther(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTest THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final List<SLInputType> INFO = MODE.personalInfo(PLATFORM);
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rx_a_enterPersonalInfo(ENGINE, INFO))
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rx_a_confirmPersonalInfo(ENGINE))
            .delay(2000, TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rx_v_personalInfoScreen(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#EXTRA_PERSONAL_INFO} for
     * parents/guarantors should only require either
     * {@link TextInput#PARENT_MOBILE} or {@link TextInput#PARENT_EMAIL}.
     * This test is only applicable for {@link UserMode#TEEN_U18}, so we use
     * {@link DataProvider} that provides {@link InputType}.
     * @param INPUTS {@link List} of {@link InputType}.
     * @see Screen#EXTRA_PERSONAL_INFO
     * @see #engine()
     * @see #rx_navigate(UserMode, Screen...)
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see #rx_a_confirmExtraPersonalInfo(Engine, UserMode)
     * @see #rx_e_progressBar(Engine)
     * @see #parentPersonalInfoProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "parentPersonalInfoProvider")
    public void test_parentInfoScreen_requiresPhoneOrEmail(@NotNull final List<SLInputType> INPUTS) {
        // Setup
        final UIPersonalInfoTest THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.TEEN_U18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_navigate(MODE, Screen.SPLASH, Screen.EXTRA_PERSONAL_INFO)
            .flatMap(a -> THIS.rx_a_enterPersonalInfo(ENGINE, INPUTS))
            .flatMap(a -> THIS.rx_a_confirmExtraPersonalInfo(ENGINE, MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rx_e_progressBar(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
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
