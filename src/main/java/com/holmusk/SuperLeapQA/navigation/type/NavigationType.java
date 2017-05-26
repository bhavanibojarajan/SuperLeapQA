package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerActionType;
import com.holmusk.SuperLeapQA.ui.signup.invalidage.InvalidAgeTestHelperType;
import com.holmusk.SuperLeapQA.ui.signup.personalinfo.PersonalInfoActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/21/17.
 */
public interface NavigationType extends DashboardActionType, InvalidAgeTestHelperType {
    /**
     * Wait for splash screen to finish and navigate to welcome screen.
     * @return {@link Flowable} instance.
     * @see #splashDelay()
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rxn_splash_welcome() {
        long delay = splashDelay();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        return Flowable.timer(delay, unit);
    }

    /**
     * Navigate from the sign in screen to the welcome screen, assuming the
     * user is already in the sign in screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_signIn_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_clickBackButton(ENGINE);
    }

    /**
     * Navigate from welcome to register screen, assuming the user is already
     * in the welcome screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_welcomeRegister(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_n_welcome_register(@NotNull final Engine<?> ENGINE) {
        return rx_e_welcomeRegister(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Navigate from the register screen to the welcome screen, assuming the
     * user is already in the register screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_register_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_clickBackButton(ENGINE);
    }

    /**
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return {@link Flowable} instance.
     * @see #rxe_signUp(Engine, UserMode)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxn_register_DoBPicker(@NotNull Engine<?> ENGINE,
                                               @NotNull UserMode mode) {
        return rxe_signUp(ENGINE, mode).flatMap(ENGINE::rxa_click);
    }

    /**
     * Navigate to the appropriate screen, based on an age value.
     * @param ENGINE {@link Engine} instance.
     * @param AGE {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rx_a_openDoBPicker(Engine)
     * @see #rxa_selectDoBToBeOfAge(Engine, int)
     * @see #rxa_confirmDoB(Engine) )
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_ageInput(@NotNull final Engine<?> ENGINE,
                                               final int AGE) {
        final DOBPickerActionType THIS = this;

        return rx_a_openDoBPicker(ENGINE)
            .flatMap(a -> THIS.rxa_selectDoBToBeOfAge(ENGINE, AGE))
            .flatMap(a -> THIS.rxa_confirmDoB(ENGINE));
    }

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within
     * {@link UserMode#maxCategoryValidAge()})
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#maxCategoryValidAge()
     * @see #rxn_DoBPicker_ageInput(Engine, int)
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_invalidAge(@NotNull Engine<?> engine,
                                                 @NotNull UserMode mode) {
        int age = mode.maxCategoryValidAge() + 1;
        return rxn_DoBPicker_ageInput(engine, age);
    }

    /**
     * Navigate from the unacceptable age input screen to the DoB picker
     * screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_invalidAge_DOBPicker(@NotNull Engine<?> engine) {
        return rxa_clickBackButton(engine);
    }

    /**
     * Navigate from the unacceptable age screen to the welcome screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterAndConfirmInvalidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxn_invalidAge_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_enterAndConfirmInvalidAgeInputs(ENGINE);
    }

    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link UserMode#validAgeRange()}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxn_DoBPicker_ageInput(Engine, int)
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_validAge(@NotNull Engine<?> engine,
                                               @NotNull UserMode mode) {
        List<Integer> range = mode.validAgeRange();
        int age = CollectionTestUtil.randomElement(range);
        return rxn_DoBPicker_ageInput(engine, age);
    }

    /**
     * Navigate from the acceptable age screen to the welcome screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterInvalidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxn_validAge_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_enterInvalidAgeInputs(ENGINE);
    }

    /**
     * Navigate from the acceptable age input to the personal info input
     * screen.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterAndConfirmValidAgeInputs(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_validAge_personalInfo(@NotNull final Engine<?> ENGINE,
                                                  @NotNull final UserMode MODE) {
        return rxa_enterAndConfirmValidAgeInputs(ENGINE, MODE);
    }

    /**
     * Navigate from the personal info screen to the acceptable age screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_personalInfo_validAge(@NotNull Engine<?> engine) {
        return rxa_clickBackButton(engine);
    }

    /**
     * Navigate from the personal info input screen to the extra info input
     * screen. Only applicable to {@link UserMode#requiresGuarantor()}.
     * If {@link UserMode#requiresGuarantor()} is {@link Boolean#FALSE},
     * this method will go directly to the dashboard.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterPersonalInfo(Engine, List)
     * @see #rxa_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxn_personalInfo_extraInfo(@NotNull final Engine<?> ENGINE,
                                                   @NotNull UserMode mode) {
        final NavigationType THIS = this;

        return rxa_enterPersonalInfo(ENGINE, mode).flatMap(a ->
            THIS.rxa_confirmPersonalInfo(ENGINE)
        );
    }

    /**
     * Navigate from extra personal info screen to the personal info screen.
     * Only applicable if {@link UserMode#requiresGuarantor()} is true.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#requiresGuarantor()
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_extraInfo_personalInfo(@NotNull Engine<?> engine,
                                                   @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rxa_clickBackButton(engine);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Navigate from the extra info screen to the Use App Now screen. Note
     * that the extra info screen only exists for {@link UserMode#TEEN_U18}.
     * For other {@link UserMode} instances, this screen should be the
     * personal info screen.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterExtraPersonalInfo(Engine, UserMode)
     * @see #rxa_confirmExtraPersonalInfo(Engine, UserMode)
     * @see #rxa_watchProgressBarUntilHidden(Engine)
     * @see #rxa_watchPersonalInfoScreen(Engine)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<?> rxn_extraInfo_useApp(@NotNull final Engine<?> ENGINE,
                                             @NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;

        return rx_a_enterExtraPersonalInfo(ENGINE, MODE)
            .flatMap(a -> THIS.rxa_confirmExtraPersonalInfo(ENGINE, MODE))

            /* First progress bar appears immediately after the submit button
             * is clicked */
            .flatMap(a -> THIS.rxa_watchProgressBarUntilHidden(ENGINE))

            /* There is a short delay between the first and the second
             * progress bar */
            .flatMap(a -> THIS.rxa_watchPersonalInfoScreen(ENGINE))

            /* The second progress bar appears */
            .flatMap(a -> THIS.rxa_watchProgressBarUntilHidden(ENGINE))

            /* On iOS, the app may ask the user for permission to send push
             * notifications. The default flow is to accept - we can test
             * reject flow elsewhere */
            .flatMap(a -> ENGINE.rxa_acceptAlert());
    }

    /**
     * Navigate from the Use App Now screen to the Dashboard tutorial screen,
     * assuming the user is already in the Use App Now screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_useAppNow(Engine)
     */
    @NotNull
    default Flowable<?> rxn_useApp_tutorial(@NotNull Engine<?> engine) {
        return rxa_useAppNow(engine);
    }

    /**
     * Navigate from the dashboard tutorial to the dashboard, assuming the
     * user is in the tutorial screen (i.e. using for the first time).
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_navigateBackOnce()
     */
    @NotNull
    default Flowable<?> rxn_tutorial_dashboard(@NotNull Engine<?> engine) {
        return engine.rxa_navigateBackOnce();
    }
}
