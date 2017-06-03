package com.holmusk.SuperLeapQA.navigation.type;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.chat.ChatActionType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.dob.DOBPickerActionType;
import com.holmusk.SuperLeapQA.test.forgotpassword.ForgotPasswordActionType;
import com.holmusk.SuperLeapQA.test.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.test.login.LoginActionType;
import com.holmusk.SuperLeapQA.test.logmeal.LogMealActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import com.holmusk.SuperLeapQA.test.photopicker.PhotoPickerActionType;
import com.holmusk.SuperLeapQA.test.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.test.search.SearchActionType;
import com.holmusk.SuperLeapQA.test.sha.SHAActionType;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import com.holmusk.SuperLeapQA.test.welcome.WelcomeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 6/3/17.
 */
public interface BackwardNavigationType extends
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
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SEARCH}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_cancelSearch(Engine)
     */
    @NotNull
    default Flowable<?> rxn_search_dashboard(@NotNull Engine<?> engine) {
        return rxa_cancelSearch(engine);
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#CHAT}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_dismissChatWindow(Engine)
     */
    @NotNull
    default Flowable<?> rxn_chat_mealPage(@NotNull Engine<?> engine) {
        return rxa_dismissChatWindow(engine);
    }
}
