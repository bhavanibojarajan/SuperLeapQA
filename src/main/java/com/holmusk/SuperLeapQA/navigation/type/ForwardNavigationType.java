package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.DrawerItem;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.address.AddressActionType;
import com.holmusk.SuperLeapQA.test.chat.ChatActionType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerActionType;
import com.holmusk.SuperLeapQA.test.forgotpassword.ForgotPasswordActionType;
import com.holmusk.SuperLeapQA.test.guarantorinfo.GuarantorInfoActionType;
import com.holmusk.SuperLeapQA.test.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.test.logactivity.LogActivityActionType;
import com.holmusk.SuperLeapQA.test.login.LoginActionType;
import com.holmusk.SuperLeapQA.test.logmeal.LogMealActionType;
import com.holmusk.SuperLeapQA.test.logweight.LogWeightActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import com.holmusk.SuperLeapQA.test.personalinfo.PersonalInfoActionType;
import com.holmusk.SuperLeapQA.test.photopicker.PhotoPickerActionType;
import com.holmusk.SuperLeapQA.test.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.test.search.SearchActionType;
import com.holmusk.SuperLeapQA.test.settings.SettingActionType;
import com.holmusk.SuperLeapQA.test.sha.SHAActionType;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import com.holmusk.SuperLeapQA.test.welcome.WelcomeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.object.HPObjects;
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
    AddressActionType,
    PersonalInfoActionType,
    GuarantorInfoActionType,
    DashboardActionType,
    SearchActionType,
    PhotoPickerActionType,
    LogMealActionType,
    MealPageActionType,
    ChatActionType,
    LogWeightActionType,
    LogActivityActionType,
    SettingActionType
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
     * @param engine {@link Engine} instance.
     * @param age {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxa_selectDoBToBeOfAge(Engine, int)
     * @see #rxa_confirmDoB(Engine) )
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_ageInput(@NotNull Engine<?> engine, int age) {
        return Flowable.concatArray(
            rxa_openDoBPicker(engine),
            rxa_selectDoBToBeOfAge(engine, age),
            rxa_confirmDoB(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DOB}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#INVALID_AGE}
     * {@link UserMode#maxCategoryValidAge()})
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
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
     * @see #rxn_DoBPicker_ageInput(Engine, int)
     */
    @NotNull
    default Flowable<?> rxn_DoBPicker_validAge(@NotNull Engine<?> engine,
                                               @NotNull UserMode mode) {
        List<Integer> range = mode.validAgeRange();
        int age = HPIterables.randomElement(range);
        return rxn_DoBPicker_ageInput(engine, age);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#VALID_AGE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeValidAgeInputs(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_validAge_personalInfo(@NotNull Engine<?> engine,
                                                  @NotNull UserMode mode) {
        return rxa_completeValidAgeInputs(engine, mode);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADDRESS_INFO}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completePersonalInfo(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_personalInfo_addressInfo(@NotNull Engine<?> engine,
                                                     @NotNull UserMode mode) {
        return rxa_completePersonalInfo(engine, mode);
    }

    /**
     * Only applicable to {@link UserMode#requiresGuarantor()}.
     * If {@link UserMode#requiresGuarantor()} is {@link Boolean#FALSE},
     * this method will go directly to the dashboard.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeAddressInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxn_addressInfo_guarantorInfo(@NotNull Engine<?> engine) {
        return rxa_completeAddressInfo(engine);
    }

    /**
     * Navigate from the extra info screen to the Use App Now screen. Note
     * that the extra info screen only exists for {@link UserMode#TEEN_U18}.
     * For other {@link UserMode} instances, this screen should be the
     * personal info screen.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#USE_APP_NOW}
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeGuarantorInfo(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxn_guarantorInfo_useApp(@NotNull Engine<?> engine,
                                                 @NotNull UserMode mode) {
        return rxa_completeGuarantorInfo(engine, mode);
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
     * @see #rxa_addCard(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxn_addCard_photoPicker(@NotNull final Engine<?> ENGINE) {
        return rxa_addCard(ENGINE, CardType.MEAL);
    }

    /**
     * We skip photo selection by default.
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PHOTO_PICKER}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_ENTRY}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_skipPhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxn_photoPicker_logMeal(@NotNull final Engine<?> ENGINE) {
        return rxe_skipPhoto(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_ENTRY}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_logNewMeal(Engine)
     */
    @NotNull
    default Flowable<?> rxn_logMeal_mealPage(@NotNull Engine<?> engine) {
        return rxa_logNewMeal(engine);
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

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_VALUE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_addCard(Engine, CardType)
     */
    @NotNull
    default Flowable<?> rxn_addCard_weightValue(@NotNull Engine<?> engine) {
        return rxa_addCard(engine, CardType.WEIGHT);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_VALUE}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_ENTRY}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeCSSValue(Engine, HMCSSInputType)
     */
    @NotNull
    default Flowable<?> rxn_weightValue_weightEntry(@NotNull Engine<?> engine) {
        return rxa_completeCSSValue(engine, CSSInput.WEIGHT);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_ENTRY}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_PAGE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeCSSEntry(Engine, HMCSSInputType)
     */
    @NotNull
    default Flowable<?> rxn_weightEntry_weightPage(@NotNull Engine<?> engine) {
        return rxa_completeCSSEntry(engine, CSSInput.WEIGHT);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ACTIVITY_VALUE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_addCard(Engine, CardType)
     * @see CardType#ACTIVITY
     */
    @NotNull
    default Flowable<?> rxn_addCard_activityValue(@NotNull Engine<?> engine) {
        return rxa_addCard(engine, CardType.ACTIVITY);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ACTIVITY_VALUE}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeCSSValue(Engine, HMCSSInputType)
     */
    @NotNull
    default Flowable<?> rxn_activityValue_activityEntry(@NotNull Engine<?> engine) {
        HMCSSInputType input = CSSInput.ACTIVITY;
        return rxa_completeCSSValue(engine, input);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SETTINGS}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_openDrawerAndSelect(Engine, DrawerItem)
     */
    @NotNull
    default Flowable<?> rxn_dashboard_settings(@NotNull Engine<?> engine) {
        return rxa_openDrawerAndSelect(engine, DrawerItem.SETTINGS);
    }
}
