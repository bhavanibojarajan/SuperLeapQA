package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.InputType;
import com.holmusk.SuperLeapQA.model.NumericSelectableInputType;
import com.holmusk.SuperLeapQA.model.TextInputType;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.base.type.DelayType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpActionType extends
    BaseActionType,
    SignUpValidationType,
    RegisterModeActionType,
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
        final SignUpActionType THIS = this;

        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoBToBeOfAge(AGE))
            .flatMap(a -> THIS.rxConfirmDoB())
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link #acceptableAgeRange(UserMode)}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAgeInput(@NotNull UserMode mode) {
        List<Integer> range = acceptableAgeRange(mode);
        int age = CollectionTestUtil.randomElement(range);
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Navigate to the unacceptable age input screen by selecting a DoB that
     * results in an age that does not lie within {
     * @link #acceptableAgeRange(UserMode)}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_unacceptableAgeInput(@NotNull UserMode mode) {
        int age = mode.maxAcceptableAge() + 1;
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
        final SignUpActionType THIS = this;
        return rx_splash_register().flatMap(a -> THIS.rx_register_DoBPicker(mode));
    }

    /**
     * Bridge method that helps navigate from splash screen to unacceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_unacceptableAgeInput(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_unacceptableAgeInput(@NotNull UserMode mode) {
        final SignUpActionType THIS = this;

        return rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rx_DoBPicker_unacceptableAgeInput(mode));
    }

    /**
     * Bridge method that helps navigate from splash screen to acceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_acceptableAgeInput(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_acceptableAgeInput(@NotNull UserMode mode) {
        final SignUpActionType THIS = this;

        return rx_splash_DoBPicker(mode)
            .flatMap(a -> THIS.rx_DoBPicker_acceptableAgeInput(mode));
    }

    /**
     * Bridge method that helps navigate from splash screen to personal info
     * input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_acceptableAgeInput(UserMode)
     * @see #rx_acceptableAgeInput_personalInfoInput()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_personalInfoInput(@NotNull UserMode mode) {
        final SignUpActionType THIS = this;

        return rx_splash_acceptableAgeInput(mode)
            .flatMap(a -> THIS.rx_acceptableAgeInput_personalInfoInput());
    }

    /**
     * Bridge method that helps navigate from splash screen to extra personal
     * info screen. Only applicable to {@link UserMode#TEEN}.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_personalInfoInput(UserMode)
     * @see #rx_personalInfoInputs_extraInfoInputs(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_extraInfoInput(@NotNull final UserMode MODE) {
        final SignUpActionType THIS = this;

        return rx_splash_personalInfoInput(MODE)
            .flatMap(a -> THIS.rx_personalInfoInputs_extraInfoInputs(MODE));
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
            .flatMap(ENGINE::rxClick).map(a -> true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay))
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE
                .rxElementContainingText("ok")
                .flatMap(ENGINE::rxClick)
                .map(BooleanUtil::toTrue);
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
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
        return rxSelectDoB(calendar.getTime());
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link #acceptableAgeRange(UserMode)} should not bring the user to the
     * correct sign up screen. This action assumes the user is in the DoB
     * selection screen, but has not opened the DoB picker yet.
     * @param MODE A {@link UserMode} instance.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see #acceptableAgeRange(UserMode)
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen(UserMode)
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final UserMode MODE,
                                             @NotNull final List<Integer> AGES) {
        final List<Integer> RANGE = acceptableAgeRange(MODE);
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
                                return rxValidateUnacceptableAgeScreen(MODE);
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
        final SignUpActionType THIS = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(2010, 2030));
        final Date DATE = calendar.getTime();

        // When
        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoB(DATE))
            .flatMap(a -> THIS.rxConfirmDoB())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxDoBEditFieldHasDate(DATE));
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
        return rxEditFieldForInput(input).flatMap(engine()::rxClick).map(a -> true);
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
    default Flowable<WebElement> rxEnterInput(@NotNull TextInputType input,
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
    default Flowable<WebElement> rxEnterRandomInput(@NotNull TextInputType input) {
        return rxEnterInput(input, input.randomInput());
    }

    /**
     * Confirm email subscription for future program expansion.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxUnacceptableAgeSubmitButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmUnacceptableAgeInput() {
        final BaseEngine<?> ENGINE = engine();

        return rxUnacceptableAgeSubmitButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
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
        final SignUpActionType THIS = this;
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
                    .flatMap(ENGINE::rxClick)
                    .map(BooleanUtil::toTrue);
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
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectEthnicity(@NotNull final Ethnicity E) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.ETHNICITY)
            .flatMap(a -> ENGINE.rxElementContainingText(E.value()))
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select a {@link CoachPref} for {@link ChoiceInput#COACH_PREFERENCE}.
     * @param CP A {@link CoachPref} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectCoachPref(@NotNull final CoachPref CP) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.COACH_PREFERENCE)
            .flatMap(a -> ENGINE.rxElementContainingText(CP.value()))
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
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
        return rxAcceptableAgeConfirmButton().flatMap(engine()::rxClick).map(a -> true);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(InputType)
     * @see #rxSelectEthnicity(Ethnicity)
     * @see #rxSelectCoachPref(CoachPref)
     * @see #rxSelectNumericInput(NumericSelectableInputType, double)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterRandomAcceptableAgeInputs() {
        final SignUpActionType THIS = this;
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
//        final Height HEIGHT_MODE = CollectionTestUtil.randomElement(Height.values());
//        final Weight WEIGHT_MODE = CollectionTestUtil.randomElement(Weight.values());
        final Height HEIGHT_MODE = Height.CM; // TODO: Randomize when ft bug is fixed
        final Weight WEIGHT_MODE = Weight.KG; // TODO: Randomize when lb bug is fixed
        final double HEIGHT = HEIGHT_MODE.randomSelectableNumericValue();
        final double WEIGHT = WEIGHT_MODE.randomSelectableNumericValue();

        return rxClickInputField(GENDER)
            .flatMap(a -> rxSelectEthnicity(ETH))
            .flatMap(a -> rxSelectCoachPref(CP))

            .flatMap(a -> rxClickInputField(HEIGHT_MODE))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_MODE, HEIGHT))

            .flatMap(a -> rxClickInputField(WEIGHT_MODE))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_MODE, WEIGHT));
    }
    //endregion

    //region Personal Info Input
    /**
     * Navigate from the acceptable age input to the personal info input
     * screen.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomAcceptableAgeInputs()
     * @see #rxConfirmAcceptableAgeInputs()
     */
    @NotNull
    default Flowable<Boolean> rx_acceptableAgeInput_personalInfoInput() {
        final SignUpActionType THIS = this;

        return rxEnterRandomAcceptableAgeInputs()
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs());
    }

    /**
     * Click the submit button to confirm personal info inputs.
     * @return A {@link Flowable} instance.
     * @see #rxPersonalInfoSubmitButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmPersonalInfoInputs() {
        final BaseEngine<?> ENGINE = engine();

        return rxPersonalInfoSubmitButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Toggle the TOC checkbox to be accepted/rejected.
     * @param ACCEPTED A {@link Boolean} value.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxTOCCheckBox()
     * @see BaseEngine#isCheckBoxChecked(WebElement)
     * @see BaseEngine#click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxToggleTOC(final boolean ACCEPTED) {
        final BaseEngine<?> ENGINE = engine();

        return rxTOCCheckBox()
            .flatMap(a -> ENGINE.rxSetCheckBoxState(a, ACCEPTED))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * This method can be used for {@link UserMode#personalInformation()}
     * and {@link UserMode#extraPersonalInformation()}.
     * @param inputs A {@link List} of {@link InputType}.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxEnterRandomInput(TextInputType)
     * @see BaseEngine#rxNavigateBackOnce()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rxEnterRandomPersonalInfoInputs(@NotNull List<InputType> inputs) {
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .fromIterable(inputs)
            .ofType(TextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .concatMap(ENGINE::rxToggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rxEnterRandomPersonalInfoInputs(List)
     * @see BaseEngine#rxHideKeyboard()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rxEnterRandomPersonalInfoInputs(@NotNull UserMode mode) {
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = THIS.engine();

        return rxEnterRandomPersonalInfoInputs(mode.personalInformation())
            .flatMap(a -> ENGINE.rxHideKeyboard())
            .flatMap(a -> THIS.rxToggleTOC(true));
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#TEEN}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomPersonalInfoInputs(List)
     * @see UserMode#extraPersonalInformation()
     */
    @NotNull
    default Flowable<Boolean> rxEnterRandomExtraPersonalInfoInputs(@NotNull UserMode mode) {
        return rxEnterRandomPersonalInfoInputs(mode.extraPersonalInformation());
    }

    /**
     * Confirm additional personal inputs. This is only relevant to
     * {@link UserMode#TEEN}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxConfirmPersonalInfoInputs()
     */
    @NotNull
    default Flowable<Boolean> rxConfirmExtraPersonalInputs(@NotNull UserMode mode) {
        switch (mode) {
            case TEEN:
                return rxConfirmPersonalInfoInputs();

            default:
                return Flowable.just(true);
        }
    }

    /**
     * Watch until the personal info screen is no longer visible.
     * @return A {@link Flowable} instance.
     * @see #rxPersonalInfoSubmitButton()
     * @see BaseEngine#rxWatchUntilNoLongerVisible(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxWatchPersonalInfoScreenUntilNoLongerVisible() {
        final BaseEngine<?> ENGINE = engine();

        return rxPersonalInfoSubmitButton()
            .flatMap(ENGINE::rxWatchUntilNoLongerVisible)
            .onErrorReturnItem(true);
    }

    /**
     * Navigate from the personal info input screen to the extra info input
     * screen. Only applicable to {@link UserMode#TEEN}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomPersonalInfoInputs(UserMode)
     * @see #rxConfirmPersonalInfoInputs()
     * @see UserMode#TEEN
     */
    @NotNull
    default Flowable<Boolean> rx_personalInfoInputs_extraInfoInputs(@NotNull UserMode mode) {
        switch (mode) {
            case TEEN:
                final SignUpActionType THIS = this;

                return rxEnterRandomPersonalInfoInputs(mode)
                    .flatMap(a -> THIS.rxConfirmPersonalInfoInputs());

            default:
                return Flowable.just(true);
        }
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
        final SignUpActionType THIS = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();

        return rxOpenDoBPicker()
            .flatMap(a -> THIS.rxSelectDoB(DATE))
            .flatMap(a -> THIS.rxConfirmDoB())
            .flatMap(a -> THIS.rxNavigateBackWithBackButton())
            .flatMap(a -> THIS.rxDoBEditFieldHasDate(DATE));
    }

    /**
     * Check if either the {@link TextInput#PHONE} or {@link TextInput#EMAIL}
     * input is required, assuming the user is already in the unacceptable age
     * input screen.
     * @param input A {@link TextInput} instance. Should be either
     * {@link TextInput#EMAIL} or {@link TextInput#PHONE}.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #rxConfirmUnacceptableAgeInput()
     * @see #rxWatchUntilProgressBarNoLongerVisible()
     * @see #rxConfirmUnacceptableAgeInputCompleted()
     */
    @NotNull
    default Flowable<Boolean> rxCheckUnacceptableAgeInputRequired(@NotNull TextInput input) {
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return rxEnterRandomInput(TextInput.NAME)
            .flatMap(a -> THIS.rxEnterRandomInput(input))
            .flatMap(a -> ENGINE.rxHideKeyboard())
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rxWatchUntilProgressBarNoLongerVisible())
            .flatMap(a -> THIS.rxValidateUnacceptableAgeInputConfirmation())
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInputCompleted());
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
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        long delay = unacceptableAgeInputConfirmDelay();

        return rxEnterRandomInput(TextInput.NAME)
            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.EMAIL))
            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.PHONE))
            .flatMap(a -> THIS.rxConfirmUnacceptableAgeInput())
            .flatMap(a -> THIS.rxValidateUnacceptableAgeInputConfirmation())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> THIS.rxUnacceptableAgeInputOkButton())
            .flatMap(ENGINE::rxClick).map(a -> true)
            .flatMap(a -> THIS.rxValidateWelcomeScreen());
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
    default Flowable<Boolean> rxEnterAndValidateAcceptableAgeInputs() {
        final SignUpActionType THIS = this;
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

        return rxClickInputField(Gender.MALE)
            .flatMap(a -> THIS.rxClickInputField(Gender.FEMALE))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))
            .flatMap(a -> THIS.rxSelectCoachPref(CP))

            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CM, HEIGHT_CM))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_STR))
            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_FT_STR))

            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.FT, HEIGHT_FT))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_STR))
            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_CM_STR))

            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.KG, WEIGHT_KG))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_STR))
            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_LB_STR))

            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.LB, WEIGHT_LB))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_STR))
            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_KG_STR));
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
    default Flowable<Boolean> rxValidateAcceptableAgeEmptyInputErrors(
        @NotNull final UserMode MODE
    ) {
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (ENGINE.platform().equals(Platform.IOS)) {
            return Flowable.just(true);
        }

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final double HEIGHT_CM = Height.CM.randomSelectableNumericValue();
        final double HEIGHT_FT = Height.FT.randomSelectableNumericValue();
        final double WEIGHT_KG = Weight.KG.randomSelectableNumericValue();
        final double WEIGHT_LB = Weight.LB.randomSelectableNumericValue();
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rxConfirmAcceptableAgeInputs()
            .flatMap(a -> THIS.rxIsShowingError(GENDER.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxClickInputField(GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CM.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CM, HEIGHT_CM))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.FT.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.FT, HEIGHT_FT))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.KG.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.KG, WEIGHT_KG))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.LB.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.LB, WEIGHT_LB))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.ETHNICITY.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.COACH_PREFERENCE.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectCoachPref(CP));
    }

    /**
     * Enter random inputs and validate that the input views can be properly
     * interacted with.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(TextInputType)
     * @see #rxEditFieldForInput(InputType)
     * @see BaseEngine#rxToggleNextOrDoneInput(WebElement)
     * @see BaseEngine#rxTogglePasswordMask(WebElement)
     * @see BaseEngine#isShowingPassword(WebElement)
     * @see RxUtil#error()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidatePersonalInfoInputs(@NotNull UserMode mode) {
        final SignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable.fromIterable(mode.personalInformation())
            .ofType(TextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .concatMap(ENGINE::rxToggleNextOrDoneInput)

            .concatMap(a -> THIS.rxEnterRandomInput(TextInput.PASSWORD))
            .concatMap(a -> THIS.rxEditFieldForInput(TextInput.PASSWORD))
            .concatMap(a -> ENGINE.rxToggleNextOrDoneInput(a).flatMap(b ->
                    ENGINE.rxTogglePasswordMask(a)
            ))
            .filter(ENGINE::isShowingPassword)
            .switchIfEmpty(RxUtil.error())
            .map(BooleanUtil::toTrue);
    }
    //endregion
}
