package com.holmusk.SuperLeapQA.onboarding.common;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.InputType;
import com.holmusk.SuperLeapQA.model.type.NumericSelectableInputType;
import com.holmusk.SuperLeapQA.model.type.TextInputType;
import com.holmusk.SuperLeapQA.onboarding.register.RegisterActionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.base.type.DelayType;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface BaseSignUpActionType extends
    BaseActionType,
    BaseSignUpValidationType,
    PlatformErrorType,
    RegisterActionType,
    WelcomeActionType
{
    //region Bridged Navigation
    /**
     * Navigate to the appropriate screen, based on an age value.
     * @param AGE An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxOpenDoBPicker()
     * @see #rxSelectDoBToBeOfAge(int)
     * @see #rxConfirmDoB()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_inputScreenForAge(final int AGE) {
        return rxOpenDoBPicker()
            .concatWith(rxSelectDoBToBeOfAge(AGE))
            .concatWith(rxConfirmDoB())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link #acceptableAgeRange()}.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAgeInput() {
        List<Integer> range = acceptableAgeRange();
        int age = CollectionTestUtil.randomElement(range);
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within {@link #acceptableAgeRange()}.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_unacceptableAgeInput() {
        int age = maxAcceptableAge() + 1;
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Bridge method that helps navigate from splash screen to sign up.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_register()
     * @see #rx_register_DoBPicker(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_DoBPicker(@NotNull UserMode mode) {
        return rx_splash_register()
            .concatWith(rx_register_DoBPicker(mode))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method that helps navigate from splash screen to unacceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_unacceptableAgeInput()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_unacceptableAgeInput(@NotNull UserMode mode) {
        return rx_splash_DoBPicker(mode)
            .concatWith(rx_DoBPicker_unacceptableAgeInput())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Bridge method that helps navigate from splash screen to acceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_acceptableAgeInput()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_acceptableAgeInput(@NotNull UserMode mode) {
        return rx_splash_DoBPicker(mode)
            .concatWith(rx_DoBPicker_acceptableAgeInput())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
    //endregion

    //region DoB Picker
    /**
     * Navigate to the sign up screen from register screen, assuming the user
     * is already on the register screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxSignUpButton(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_register_DoBPicker(@NotNull UserMode mode) {
        return rxSignUpButton(mode)
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true);
    }

    /**
     * Open the DoB dialog in the sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     * @see #rxDoBEditField()
     * @see BaseEngine#rxClick(WebElement)
     * @see BaseEngine#rxImplicitlyWait(DelayType)
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditField()
            .flatMap(ENGINE::rxClick)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .concatWith(ENGINE.rxImplicitlyWait(this::generalDelay))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE.rxElementContainingText("ok").flatMap(ENGINE::rxClick);
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param DATE A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxSelectDate(DateType)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoB(@NotNull final Date DATE) {
        return engine().rxSelectDate(() -> DATE);
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param age An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoBToBeOfAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);

        /* We need to add 1 to the day field to avoid the birthday. E.g.
         * if the current date is 10/05/2017 and we want the user to be 4
         * years-old, we need to select 11/05/2013 - any lower than that then
         * the user would be 5+ years-old */
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return rxSelectDoB(calendar.getTime());
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link #acceptableAgeRange()} should not bring the user to the correct
     * sign up screen. This action assumes the user is in the DoB selection
     * screen, but has not opened the DoB picker yet.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see #acceptableAgeRange()
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen()
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final List<Integer> AGES) {
        final List<Integer> RANGE = acceptableAgeRange();
        final int LENGTH = AGES.size();

        class IterateDoBs {
            @NotNull
            @SuppressWarnings("WeakerAccess")
            Flowable<Boolean> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return rxOpenDoBPicker()
                        .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
                        .flatMap(a -> rxConfirmDoB())
                        .flatMap(a -> {
                            if (VALID) {
                                return rxValidateAcceptableAgeScreen();
                            } else {
                                return rxValidateUnacceptableAgeScreen();
                            }
                        })
                        .flatMap(a -> rxNavigateBackWithBackButton())
                        .flatMap(a -> new IterateDoBs().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new IterateDoBs().repeat(0);
    }

    /**
     * Select a random DoB, assuming the user is already in the DoB selection
     * screen.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     * @see #rxConfirmDoB()
     * @see #rxNavigateBackWithBackButton()
     * @see #rxDoBEditFieldHasDate(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectRandomDoB() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(2010, 2030));
        final Date DATE = calendar.getTime();

        // When
        return rxOpenDoBPicker()
            .concatWith(rxSelectDoB(DATE))
            .concatWith(rxConfirmDoB())
            .concatWith(rxNavigateBackWithBackButton())
            .concatWith(rxDoBEditFieldHasDate(DATE))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
    //endregion

    //region Common Input Actions
    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxClickInputField(@NotNull InputType input) {
        return rxEditFieldForInput(input).flatMap(engine()::rxClick);
    }
    //endregion

    //region Unacceptable Age Input
    /**
     * Enter an input for a {@link TextInput}.
     * @param input A {@link TextInputType} instance.
     * @param TEXT A {@link String} value.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see BaseEngine#rxSendKey(WebElement, String...)
     */
    @NotNull
    default Flowable<Boolean> rxEnterInput(@NotNull TextInputType input,
                                           @NotNull final String TEXT) {
        final BaseEngine<?> ENGINE = engine();
        return rxEditFieldForInput(input).flatMap(a -> ENGINE.rxSendKey(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param input A {@link TextInputType} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterInput(TextInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default Flowable<Boolean> rxEnterRandomInput(@NotNull TextInputType input) {
        return rxEnterInput(input, input.randomInput());
    }

    /**
     * Confirm email subscription for future program expansion.
     * @return A {@link Flowable} instance.
     * @see #rxUnacceptableAgeSubmitButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmUnacceptableAgeInput() {
        return rxUnacceptableAgeSubmitButton().flatMap(engine()::rxClick);
    }
    //endregion

    //region Acceptable Age Input
    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link NumericSelectableInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectNumericInput(
        @NotNull final NumericSelectableInputType MODE,
        final double NUMERIC_VALUE)
    {
        final BaseSignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final String HEIGHT_STR = MODE.stringValue(NUMERIC_VALUE);

        final TextParam TEXT_PARAM = TextParam.builder()
            .withText(HEIGHT_STR)
            .withRetries(0)
            .build();

        SwipeRepeatComparisonType repeater = new SwipeRepeatComparisonType() {
            @NotNull
            @Override
            public Flowable<Integer> rxInitialDifference(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .map(a -> a - NUMERIC_VALUE)
                    .map(Double::intValue);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .filter(a -> a > NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .filter(a -> a < NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollViewChildItems() {
                return THIS.rxPickerItemViews(ChoiceInput.HEIGHT);
            }

            @Override
            public double elementSwipeRatio() {
                return 0.7d;
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxShouldKeepSwiping() {
                return ENGINE
                    .rxElementWithText(TEXT_PARAM)
                    .filter(a -> ENGINE.getText(a).equals(HEIGHT_STR))
                    .flatMap(ENGINE::rxClick);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollableViewToSwipe() {
                return rxScrollableInputPickerView(ChoiceInput.HEIGHT);
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxSwipeOnce(@NotNull SwipeType param) {
                return ENGINE.rxSwipeOnce(param);
            }
        };

        return repeater.rxRepeatSwipe();
    }

    /**
     * Select an {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxClickInputField(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectEthnicity(@NotNull final Ethnicity E) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.ETHNICITY)
            .flatMap(a -> ENGINE.rxElementContainingText(E.value()))
            .flatMap(ENGINE::rxClick);
    }

    /**
     * Select a {@link CoachPref} for {@link ChoiceInput#COACH_PREFERENCE}.
     * @param CP A {@link CoachPref} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxClickInputField(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectCoachPref(@NotNull final CoachPref CP) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.COACH_PREFERENCE)
            .flatMap(a -> ENGINE.rxElementContainingText(CP.value()))
            .flatMap(ENGINE::rxClick);
    }

    /**
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxAcceptableAgeConfirmButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmAcceptableAgeInputs() {
        return rxAcceptableAgeConfirmButton().flatMap(engine()::rxClick);
    }
    //endregion

    //region Convenience Validation Methods
    /**
     * Check that the DoB dialog has correct elements.
     * @return A {@link Flowable} instance.
     * @see #rxOpenDoBPicker()
     * @see #rxSelectDoB(Date)
     * @see #rxNavigateBackWithBackButton()
     * @see #rxDoBEditFieldHasDate(Date)
     */
    @NotNull
    default Flowable<Boolean> rxCheckDoBDialogHasCorrectElements() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();

        return rxOpenDoBPicker()
            .concatWith(rxSelectDoB(DATE))
            .concatWith(rxConfirmDoB())
            .concatWith(rxNavigateBackWithBackButton())
            .concatWith(rxDoBEditFieldHasDate(DATE))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Check if the {@link TextInput#PHONE} input is required, assuming the
     * user is already in the unacceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #rxConfirmUnacceptableAgeInput()
     * @see #unacceptableAgeInputConfirmDelay()
     */
    @NotNull
    default Flowable<Boolean> rxCheckUnacceptableAgePhoneInputIsRequired() {
        return Flowable
            .concat(
                rxEnterRandomInput(TextInput.NAME),
                rxEnterRandomInput(TextInput.EMAIL),
                rxConfirmUnacceptableAgeInput()
            )
            .delay(unacceptableAgeInputConfirmDelay(), TimeUnit.MILLISECONDS)

            /* We can validate unacceptable age screen - if views are present
             * it means the app has not navigated to the confirmation screen
             * yet (which is what we want since we expect there to be input
             * errors) */
            .concatWith(rxValidateUnacceptableAgeScreen())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #unacceptableAgeInputConfirmDelay()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateUnacceptableAgeInputs() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concatArray(
                rxEnterRandomInput(TextInput.NAME),
                rxEnterRandomInput(TextInput.EMAIL),
                rxEnterRandomInput(TextInput.PHONE),
                rxConfirmUnacceptableAgeInput(),
                rxValidateUnacceptableAgeInputConfirmation()
            )
            .delay(unacceptableAgeInputConfirmDelay(), TimeUnit.MILLISECONDS)
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .flatMap(a -> rxUnacceptableAgeInputOkButton())
            .flatMap(ENGINE::rxClick)
            .concatWith(rxValidateWelcomeScreen())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(InputType)
     * @see #rxEditFieldHasValue(InputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterRandomAcceptableAgeInputs() {
        final double HEIGHT_CM = Height.CM.randomSelectableNumericValue();
        final double HEIGHT_FT = Height.FT.randomSelectableNumericValue();
        final String HEIGHT_CM_STR = Height.CM.stringValue(HEIGHT_CM);
        final String HEIGHT_CM_FT_STR = Height.CM.ftString(HEIGHT_CM);
        final String HEIGHT_FT_STR = Height.FT.stringValue(HEIGHT_FT);
        final String HEIGHT_FT_CM_STR = Height.FT.cmString(HEIGHT_FT);
        final double WEIGHT_KG = Weight.KG.randomSelectableNumericValue();
        final double WEIGHT_LB = Weight.LB.randomSelectableNumericValue();
        final String WEIGHT_KG_STR = Weight.KG.stringValue(WEIGHT_KG);
        final String WEIGHT_KG_LB_STR = Weight.KG.lbString(WEIGHT_KG);
        final String WEIGHT_LB_STR = Weight.LB.stringValue(WEIGHT_LB);
        final String WEIGHT_LB_KG_STR = Weight.LB.kgString(WEIGHT_LB);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return Flowable
            .concatArray(
                rxClickInputField(Gender.MALE),
                rxClickInputField(Gender.FEMALE),
                rxSelectEthnicity(ETH),
                rxSelectCoachPref(CP),

                rxClickInputField(Height.CM)
                    .concatWith(rxClickInputField(ChoiceInput.HEIGHT))
                    .concatWith(rxSelectNumericInput(Height.CM, HEIGHT_CM))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_STR))
                    .concatWith(rxClickInputField(Height.FT))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_FT_STR))
                    .all(BooleanUtil::isTrue)
                    .toFlowable(),

                rxClickInputField(Height.FT)
                    .concatWith(rxClickInputField(ChoiceInput.HEIGHT))
                    .concatWith(rxSelectNumericInput(Height.FT, HEIGHT_FT))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_STR))
                    .concatWith(rxClickInputField(Height.CM))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_CM_STR))
                    .all(BooleanUtil::isTrue)
                    .toFlowable(),

                rxClickInputField(Weight.KG)
                    .concatWith(rxClickInputField(ChoiceInput.WEIGHT))
                    .concatWith(rxSelectNumericInput(Weight.KG, WEIGHT_KG))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_STR))
                    .concatWith(rxClickInputField(Weight.LB))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_LB_STR))
                    .all(BooleanUtil::isTrue)
                    .toFlowable(),

                rxClickInputField(Weight.LB)
                    .concatWith(rxClickInputField(ChoiceInput.WEIGHT))
                    .concatWith(rxSelectNumericInput(Weight.LB, WEIGHT_LB))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_STR))
                    .concatWith(rxClickInputField(Weight.KG))
                    .concatWith(rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_KG_STR))
                    .all(BooleanUtil::isTrue)
                    .toFlowable()
            )
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxConfirmAcceptableAgeInputs()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxConfirmAcceptableAgeEmptyInputErrors(
        @NotNull final UserMode MODE
    ) {
        final BaseEngine<?> ENGINE = engine();
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());

        return Flowable
            .concatArray(
                /* At this stage, the gender error message should be shown */
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(GENDER.emptySignUpInputError(MODE)),
                rxClickInputField(GENDER),

                /* At this stage, the height error message should be shown */
                rxClickInputField(Height.CM),
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(Height.CM.emptySignUpInputError(MODE)),

                /* At this stage, the height error message should be shown */
                rxClickInputField(Height.FT),
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(Height.FT.emptySignUpInputError(MODE)),

                /* At this stage, the weight error message should be shown */
                rxClickInputField(Weight.KG),
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(Weight.KG.emptySignUpInputError(MODE)),

                /* At this stage, the weight error message should be shown */
                rxClickInputField(Weight.LB),
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(Weight.LB.emptySignUpInputError(MODE)),

                /* At this stage, the ethnicity error message should be shown */
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(ChoiceInput.ETHNICITY.emptySignUpInputError(MODE)),

                /* At this stage, the coach pref error message should be shown */
                rxConfirmAcceptableAgeInputs(),
                rxIsShowingError(ChoiceInput.COACH_PREFERENCE.emptySignUpInputError(MODE))
            )
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
    //endregion
}
