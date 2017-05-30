package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.ui.dob.DOBPickerActionType;
import com.holmusk.SuperLeapQA.ui.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.ui.login.LoginActionType;
import com.holmusk.SuperLeapQA.ui.personalinfo.PersonalInfoActionType;
import com.holmusk.SuperLeapQA.ui.photopicker.PhotoPickerActionType;
import com.holmusk.SuperLeapQA.ui.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.ui.validage.ValidAgeActionType;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
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
public interface NavigationType extends
    WelcomeActionType,
    LoginActionType,
    RegisterModeActionType,
    DOBPickerActionType,
    InvalidAgeActionType,
    ValidAgeActionType,
    DashboardActionType,
    PhotoPickerActionType
{
    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SPLASH}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_signIn(Engine)
     */
    @NotNull
    default Flowable<?> rxn_welcome_login(@NotNull final Engine<?> ENGINE) {
        return rxe_signIn(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_welcome(@NotNull Engine<?> engine) {
        return rxa_clickBackButton(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_forgotPassword(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_forgotPassword(@NotNull final Engine<?> ENGINE) {
        return rxe_forgotPassword(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_loginRegister(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_register(@NotNull final Engine<?> ENGINE) {
        return rxe_loginRegister(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Note that a dialog may pop up asking for push notification permission,
     * so we accept by default.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_acceptAlert()
     * @see #loginProgressDelay()
     * @see #rxa_loginWithDefaults(Engine, UserMode)
     * @see #rxa_watchProgressBarUntilHidden(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_tutorial(@NotNull final Engine<?> ENGINE,
                                           @NotNull UserMode mode) {
        return rxa_loginWithDefaults(ENGINE, mode)
            .delay(loginProgressDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_welcomeRegister(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxn_welcome_register(@NotNull final Engine<?> ENGINE) {
        return rxe_welcomeRegister(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_register_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_clickBackButton(ENGINE);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#INVALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
     * @param ENGINE {@link Engine} instance.
     * @param AGE {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxa_selectDoBToBeOfAge(Engine, int)
     * @see #rxa_confirmDoB(Engine) )
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_ageInput(@NotNull final Engine<?> ENGINE,
                                               final int AGE) {
        final DOBPickerActionType THIS = this;

        return rxa_openDoBPicker(ENGINE)
            .flatMap(a -> THIS.rxa_selectDoBToBeOfAge(ENGINE, AGE))
            .flatMap(a -> THIS.rxa_confirmDoB(ENGINE));
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#INVALID_AGE}
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#INVALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_invalidAge_DOBPicker(@NotNull Engine<?> engine) {
        return rxa_clickBackButton(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#INVALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterAndConfirmInvalidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxn_invalidAge_welcome(@NotNull final Engine<?> ENGINE) {
        return rxa_enterAndConfirmInvalidAgeInputs(ENGINE);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#validAgeRange()
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_validAge_DoB(@NotNull final Engine<?> ENGINE) {
        return rxa_clickBackButton(ENGINE);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterAndConfirmPersonalInfo(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_personalInfo_guarantorInfo(@NotNull Engine<?> engine,
                                                       @NotNull UserMode mode) {
        return rxa_enterAndConfirmPersonalInfo(engine, mode);
    }

    /**
     * Navigate from extra personal info screen to the personal info screen.
     * Only applicable if {@link UserMode#requiresGuarantor()} is true.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#requiresGuarantor()
     * @see #rxa_clickBackButton(Engine)
     */
    @NotNull
    default Flowable<?> rxn_guarantorInfo_personalInfo(@NotNull Engine<?> engine,
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW}
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterGuarantorInfo(Engine, UserMode)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     * @see #rxa_watchProgressBarUntilHidden(Engine)
     * @see #rxa_watchPersonalInfoScreen(Engine)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<?> rxn_guarantorInfo_useApp(@NotNull final Engine<?> ENGINE,
                                                 @NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;

        return rxa_enterGuarantorInfo(ENGINE, MODE)
            .flatMap(a -> THIS.rxa_confirmGuarantorInfo(ENGINE, MODE))

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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_useAppNow(Engine)
     */
    @NotNull
    default Flowable<?> rxn_useApp_tutorial(@NotNull Engine<?> engine) {
        return rxa_useAppNow(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_dismissDashboardTutorial(Engine)
     */
    @NotNull
    default Flowable<?> rxn_tutorial_dashboard(@NotNull Engine<?> engine) {
        return rxa_dismissDashboardTutorial(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_openCardAddMenu(Engine)
     */
    @NotNull
    default Flowable<?> rxn_dashboard_addCard(@NotNull Engine<?> engine) {
        return rxa_openCardAddMenu(engine);
    }

    /**
     * We can access the photo picker by logging a new meal.
     * When the user opens the meal-logging screen, a dialog popup may appear
     * asking for camera permission.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see CardType#MEAL
     * @see Engine#rxa_acceptAlert()
     * @see #rxa_addCard(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxn_addCard_photoPicker(@NotNull final Engine<?> ENGINE) {
        return rxa_addCard(ENGINE, CardType.MEAL);
    }

    /**
     * We skip photo selection by default.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_skipPhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxn_photoPicker_logMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_skipPhoto(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
