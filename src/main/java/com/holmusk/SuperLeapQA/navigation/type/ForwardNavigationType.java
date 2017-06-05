package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.chat.ChatActionType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerActionType;
import com.holmusk.SuperLeapQA.test.forgotpassword.ForgotPasswordActionType;
import com.holmusk.SuperLeapQA.test.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.test.login.LoginActionType;
import com.holmusk.SuperLeapQA.test.logmeal.LogMealActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import com.holmusk.SuperLeapQA.test.personalinfo.PersonalInfoActionType;
import com.holmusk.SuperLeapQA.test.photopicker.PhotoPickerActionType;
import com.holmusk.SuperLeapQA.test.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.test.search.SearchActionType;
import com.holmusk.SuperLeapQA.test.sha.SHAActionType;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import com.holmusk.SuperLeapQA.test.welcome.WelcomeActionType;
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
public interface ForwardNavigationType extends
    WelcomeActionType,
    LoginActionType,
    ForgotPasswordActionType,
    RegisterModeActionType,
    SHAActionType,
    DOBPickerActionType,
    InvalidAgeActionType,
    ValidAgeActionType,
    DashboardActionType,
    SearchActionType,
    PhotoPickerActionType,
    LogMealActionType,
    MealPageActionType,
    ChatActionType
{
    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SPLASH}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #splashDelay(Engine)
     */
    @NotNull
    default Flowable<?> rxn_splash_welcome(@NotNull Engine<?> engine) {
        return Flowable.timer(splashDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_loginFromWelcome(Engine)
     */
    @NotNull
    default Flowable<?> rxn_welcome_login(@NotNull Engine<?> engine) {
        return rxa_loginFromWelcome(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#FORGOT_PASSWORD}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_forgotPasswordFromLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_forgotPassword(@NotNull Engine<?> engine) {
        return rxa_forgotPasswordFromLogin(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_registerFromLogin(Engine)
     */
    @NotNull
    default Flowable<?> rxn_login_register(@NotNull Engine<?> engine) {
        return rxa_registerFromLogin(engine);
    }

    /**
     * Note that a dialog may pop up asking for push notification permission,
     * so we accept by default.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD_TUTORIAL}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_loginWithDefaults(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_login_tutorial(@NotNull final Engine<?> ENGINE,
                                           @NotNull UserMode mode) {
        return rxa_loginWithDefaults(ENGINE, mode);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WELCOME}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_registerFromWelcome(Engine)
     */
    @NotNull
    default Flowable<?> rxn_welcome_register(@NotNull Engine<?> engine) {
        return rxa_registerFromWelcome(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#REGISTER}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}
     * @return {@link Flowable} instance.
     * @see #rxa_SHAFromRegister(Engine, UserMode)
     */
    default Flowable<?> rxn_register_sha(@NotNull Engine<?> ENGINE,
                                         @NotNull UserMode mode) {
        return rxa_SHAFromRegister(ENGINE, mode);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * @return {@link Flowable} instance.
     * @see #rxe_signUpMode(Engine, UserMode)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxn_sha_DoBPicker(@NotNull Engine<?> engine,
                                          @NotNull UserMode mode) {
        return rxa_passSHA(engine, mode);
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeValidAgeInputs(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_validAge_personalInfo(@NotNull final Engine<?> ENGINE,
                                                  @NotNull final UserMode MODE) {
        return rxa_completeValidAgeInputs(ENGINE, MODE);
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
     * Navigate from the extra info screen to the Use App Now screen. Note
     * that the extra info screen only exists for {@link UserMode#TEEN_U18}.
     * For other {@link UserMode} instances, this screen should be the
     * personal info screen.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW}
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isTrue(boolean)
     * @see #registerProgressDelay(Engine)
     * @see #rxa_enterGuarantorInfo(Engine, UserMode)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_guarantorInfo_useApp(@NotNull final Engine<?> ENGINE,
                                                 @NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;

        return rxa_enterGuarantorInfo(ENGINE, MODE)
            .flatMap(a -> THIS.rxa_confirmGuarantorInfo(ENGINE, MODE))
            .delay(registerProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SEARCH}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_toggleDashboardSearch(Engine)
     */
    @NotNull
    default Flowable<?> rxn_dashboard_search(@NotNull Engine<?> engine) {
        return rxa_toggleDashboardSearch(engine);
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

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #mealLogProgressDelay(Engine)
     * @see #rxa_logNewMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxn_logMeal_mealPage(@NotNull Engine<?> engine) {
        long delay = mealLogProgressDelay(engine);
        return rxa_logNewMeal(engine).delay(delay, TimeUnit.MILLISECONDS);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#CHAT}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_openChatWindow(Engine)
     */
    @NotNull
    default Flowable<?> rxn_mealPage_chat(@NotNull Engine<?> engine) {
        return rxa_openChatWindow(engine);
    }
}
