package com.holmusk.SuperLeapQA.navigation;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.dashboard.DashboardTestHelperType;
import com.holmusk.SuperLeapQA.ui.signup.main.AcceptableAgeActionType;
import com.holmusk.SuperLeapQA.ui.signup.main.DOBPickerActionType;
import com.holmusk.SuperLeapQA.ui.signup.main.PersonalInfoActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.List;

/**
 * Created by haipham on 5/21/17.
 */
public interface NavigationType extends DashboardTestHelperType {
    /**
     * Navigate to the parent sign up screen from register screen, assuming
     * the user is already on the register screen.
     * @return A {@link Flowable} instance.
     * @see #rx_e_signUp(Engine, UserMode)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_n_register_DoBPicker(@NotNull Engine<?> ENGINE,
                                                @NotNull UserMode mode) {
        return rx_e_signUp(ENGINE, mode).flatMap(ENGINE::rx_click);
    }

    /**
     * Navigate to the appropriate screen, based on an age value.
     * @param ENGINE {@link Engine} instance.
     * @param AGE An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rx_a_openDoBPicker(Engine)
     * @see #rx_a_selectDoBToBeOfAge(Engine, int)
     * @see #rxConfirmDoB(Engine) )
     */
    @NotNull
    default Flowable<?> rx_n_DoBPicker_inputScreenForAge(@NotNull final Engine<?> ENGINE,
                                                         final int AGE) {
        final DOBPickerActionType THIS = this;

        return rx_a_openDoBPicker(ENGINE)
            .flatMap(a -> THIS.rx_a_selectDoBToBeOfAge(ENGINE, AGE))
            .flatMap(a -> THIS.rxConfirmDoB(ENGINE));
    }

    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link UserMode#acceptableAgeRange()}.
     * @param engine {@link Engine} instance.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_n_DoBPicker_inputScreenForAge(Engine, int)
     */
    @NotNull
    default Flowable<?> rx_n_DoBPicker_acceptableAge(@NotNull Engine<?> engine,
                                                     @NotNull UserMode mode) {
        List<Integer> range = mode.acceptableAgeRange();
        int age = CollectionTestUtil.randomElement(range);
        return rx_n_DoBPicker_inputScreenForAge(engine, age);
    }

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within
     * {@link UserMode#maxCategoryAcceptableAge()})
     * @param engine {@link Engine} instance.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see UserMode#maxCategoryAcceptableAge()
     * @see #rx_n_DoBPicker_inputScreenForAge(Engine, int)
     */
    @NotNull
    default Flowable<?> rx_n_DoBPicker_unacceptableAge(@NotNull Engine<?> engine,
                                                       @NotNull UserMode mode) {
        int age = mode.maxCategoryAcceptableAge() + 1;
        return rx_n_DoBPicker_inputScreenForAge(engine, age);
    }

    /**
     * Navigate from the acceptable age input to the personal info input
     * screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_a_enterAcceptableAgeInputs(Engine, UserMode)
     * @see #rx_a_confirmAcceptableAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rx_n_acceptableAge_personalInfo(@NotNull final Engine<?> ENGINE,
                                                        @NotNull final UserMode MODE) {
        final AcceptableAgeActionType THIS = this;

        return rx_a_enterAcceptableAgeInputs(ENGINE, MODE)
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(ENGINE));
    }

    /**
     * Navigate from the personal info input screen to the extra info input
     * screen. Only applicable to {@link UserMode#requiresGuarantor()}.
     * If {@link UserMode#requiresGuarantor()} is {@link Boolean#FALSE},
     * this method will go directly to the dashboard.
     * @param ENGINE {@link Engine} instance.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see #rx_a_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rx_n_personalInfo_extraInfo(@NotNull final Engine<?> ENGINE,
                                                    @NotNull UserMode mode) {
        final NavigationType THIS = this;

        return rx_a_enterPersonalInfo(ENGINE, mode).flatMap(a ->
            THIS.rx_a_confirmPersonalInfo(ENGINE)
        );
    }

    /**
     * Navigate from the extra info screen to the Use App Now screen. Note
     * that the extra info screen only exists for {@link UserMode#TEEN_UNDER_18}.
     * For other {@link UserMode} instances, this screen should be the
     * personal info screen.
     * @param ENGINE {@link Engine} instance.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_a_enterExtraPersonalInfo(Engine, UserMode)
     * @see #rx_a_confirmExtraPersonalInfo(Engine, UserMode)
     * @see #rx_a_watchProgressBarUntilHidden(Engine)
     * @see #rx_a_watchPersonalInfoScreen(Engine)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<?> rx_n_extraInfo_useApp(@NotNull final Engine<?> ENGINE,
                                              @NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;

        return rx_a_enterExtraPersonalInfo(ENGINE, MODE)
            .flatMap(a -> THIS.rx_a_confirmExtraPersonalInfo(ENGINE, MODE))

            /* First progress bar appears immediately after the submit button
             * is clicked */
            .flatMap(a -> THIS.rx_a_watchProgressBarUntilHidden(ENGINE))

            /* There is a short delay between the first and the second
             * progress bar */
            .flatMap(a -> THIS.rx_a_watchPersonalInfoScreen(ENGINE))

            /* The second progress bar appears */
            .flatMap(a -> THIS.rx_a_watchProgressBarUntilHidden(ENGINE));
    }

    /**
     * Navigate from the Use App Now screen to the Dashboard tutorial screen,
     * assuming the user is already in the Use App Now screen.
     * @param engine {@link Engine} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_a_useAppNow(Engine)
     */
    @NotNull
    default Flowable<?> rx_n_useApp_tutorial(@NotNull Engine<?> engine) {
        return rx_a_useAppNow(engine);
    }

    /**
     * Navigate from the dashboard tutorial to the dashboard, assuming the
     * user is in the tutorial screen (i.e. using for the first time).
     * @param engine {@link Engine} instance.
     * @return A {@link Flowable} instance.
     * @see Engine#rx_navigateBackOnce()
     */
    @NotNull
    default Flowable<?> rx_n_tutorial_dashboard(@NotNull Engine<?> engine) {
        return engine.rx_navigateBackOnce();
    }
}
