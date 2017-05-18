package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import com.holmusk.SuperLeapQA.model.SLTextInputType;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidTextInputType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoInputActionType extends
    PersonalInfoValidationType, AcceptableAgeInputType
{
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to extra personal
     * info screen. Only applicable to {@link UserMode#TEEN_UNDER_18}.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_personalInfo(UserMode)
     * @see #rx_personalInfo_extraInfo(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_extraInfo(@NotNull final UserMode MODE) {
        final PersonalInfoInputActionType THIS = this;
        return rx_splash_personalInfo(MODE).flatMap(a -> THIS.rx_personalInfo_extraInfo(MODE));
    }

    /**
     * Bridge method that helps navigate from the splash screen to the
     * Use App Now screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_extraInfo(UserMode)
     * @see #rx_extraInfo_useApp(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_useApp(@NotNull final UserMode MODE) {
        final PersonalInfoInputActionType THIS = this;
        return rx_splash_extraInfo(MODE).flatMap(a -> THIS.rx_extraInfo_useApp(MODE));
    }
    //endregion

    /**
     * Navigate from the extra info screen to the Use App Now screen. Note
     * that the extra info screen only exists for {@link UserMode#TEEN_UNDER_18}.
     * For other {@link UserMode} instances, this screen should be the
     * personal info screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterExtraPersonalInfo(UserMode)
     * @see #rxConfirmExtraPersonalInfo(UserMode)
     * @see #rxWatchProgressBarUntilHidden()
     * @see #rxWatchPersonalInfoScreenUntilHidden()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_extraInfo_useApp(@NotNull final UserMode MODE) {
        final PersonalInfoInputActionType THIS = this;

        return rxEnterExtraPersonalInfo(MODE)
            .flatMap(a -> THIS.rxConfirmExtraPersonalInfo(MODE))

            /* First progress bar appears immediately after the submit button
             * is clicked */
            .flatMap(a -> THIS.rxWatchProgressBarUntilHidden())

            /* There is a short delay between the first and the second
             * progress bar */
            .flatMap(a -> THIS.rxWatchPersonalInfoScreenUntilHidden())

            /* The second progress bar appears */
            .flatMap(a -> THIS.rxWatchProgressBarUntilHidden());
    }

    /**
     * Click the submit button to confirm personal info inputs.
     * @return A {@link Flowable} instance.
     * @see #rxPersonalInfoSubmitButton()
     * @see BaseEngine#rxClick(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmPersonalInfo() {
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
     * @see #rxEnterRandomInput(AndroidTextInputType)
     * @see BaseEngine#rxNavigateBackOnce()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rxEnterPersonalInfo(@NotNull List<SLInputType> inputs) {
        final PersonalInfoInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .fromIterable(inputs)
            .ofType(SLTextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .flatMap(ENGINE::rxToggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rxEnterPersonalInfo(List)
     * @see BaseEngine#rxHideKeyboard()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rxEnterPersonalInfo(@NotNull UserMode mode) {
        final PersonalInfoInputActionType THIS = this;
        final BaseEngine<?> ENGINE = THIS.engine();

        return rxEnterPersonalInfo(mode.personalInformation())
            .flatMap(a -> ENGINE.rxHideKeyboard())
            .flatMap(a -> THIS.rxToggleTOC(true));
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#requiresGuarantor()}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterPersonalInfo(List)
     * @see UserMode#extraPersonalInformation()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<Boolean> rxEnterExtraPersonalInfo(@NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rxEnterPersonalInfo(mode.extraPersonalInformation());
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Confirm additional personal inputs. This is only relevant to
     * {@link UserMode#requiresGuarantor()}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxConfirmPersonalInfo()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<Boolean> rxConfirmExtraPersonalInfo(@NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rxConfirmPersonalInfo();
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Watch until the personal info screen is no longer visible.
     * @return A {@link Flowable} instance.
     * @see #rxPersonalInfoSubmitButton()
     * @see BaseEngine#rxWatchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxWatchPersonalInfoScreenUntilHidden() {
        final BaseEngine<?> ENGINE = engine();

        return rxPersonalInfoSubmitButton()
            .flatMap(ENGINE::rxWatchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Navigate from the personal info input screen to the extra info input
     * screen. Only applicable to {@link UserMode#requiresGuarantor()}.
     * If {@link UserMode#requiresGuarantor()} is {@link Boolean#FALSE},
     * this method will go directly to the dashboard.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterPersonalInfo(UserMode)
     * @see #rxConfirmPersonalInfo()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<Boolean> rx_personalInfo_extraInfo(@NotNull UserMode mode) {
        final PersonalInfoInputActionType THIS = this;
        return rxEnterPersonalInfo(mode).flatMap(a -> THIS.rxConfirmPersonalInfo());
    }

    /**
     * Enter random inputs and validate that the input views can be properly
     * interacted with.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(AndroidTextInputType)
     * @see #rxEditFieldForInput(AndroidInputType)
     * @see BaseEngine#rxToggleNextOrDoneInput(WebElement)
     * @see BaseEngine#rxTogglePasswordMask(WebElement)
     * @see BaseEngine#isShowingPassword(WebElement)
     * @see RxUtil#error()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidatePersonalInfo(@NotNull UserMode mode) {
        final PersonalInfoInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable.fromIterable(mode.personalInformation())
            .ofType(SLTextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .flatMap(ENGINE::rxToggleNextOrDoneInput)
            .all(ObjectUtil::nonNull)
            .toFlowable()

            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.PASSWORD))
            .flatMap(a -> THIS.rxEditFieldForInput(TextInput.PASSWORD))
            .flatMap(a -> ENGINE.rxToggleNextOrDoneInput(a).flatMap(b ->
                ENGINE.rxTogglePasswordMask(a)
            ))
            .filter(ENGINE::isShowingPassword)
            .switchIfEmpty(RxUtil.error())
            .map(BooleanUtil::toTrue);
    }

    /**
     * Validate that the TOC checkbox has the be checked before the user can
     * proceed to the next screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rxEnterPersonalInfo(List)
     * @see BaseEngine#rxHideKeyboard()
     * @see #rxConfirmPersonalInfo()
     * @see #rxValidatePersonalInfoScreen(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rxValidateTOCCheckedBeforeProceeding(@NotNull final UserMode MODE) {
        final PersonalInfoInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return rxEnterPersonalInfo(MODE.personalInformation())
            .flatMap(a -> ENGINE.rxHideKeyboard())
            .flatMap(a -> THIS.rxConfirmPersonalInfo())
            .flatMap(a -> THIS.rxValidatePersonalInfoScreen((MODE)));
    }

    /**
     * Open the main website and access the TOC page. This is primarily to
     * check that when the user navigates back to the app, the previous state
     * is saved and restored.
     * On {@link org.swiften.xtestkit.mobile.Platform#ANDROID}, the link
     * text is not a separate view, so we need to manually calculate an
     * approximate position, a press/tap on which will open up the web
     * browser.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxOpenTOCWebsite() {
        final BaseEngine<?> ENGINE = engine();

        return rxTOCAcceptanceLabel()
            .map(a -> {
                Point point = a.getLocation();
                Dimension size = a.getSize();
                int x = point.getX(), y = point.getY(), h = size.getHeight();
                return new Point(x + 10, y + h - 3);
            })
            .flatMap(a -> ENGINE.rxTap(a.getX(), a.getY()));
    }

    /**
     * Enter personal info inputs, navigate forward/backward a few times,
     * open the TOC website then back, and finally validate that all inputs
     * are saved and restored. This assumes the use is already in the
     * personal info page.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rxEnterInput(AndroidInputType, String)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxOpenTOCWebsite()
     * @see #rxEditFieldHasValue(AndroidInputType, String)
     */
    @NotNull
    default Flowable<Boolean> rxValidatePersonalInfoStateSaved(@NotNull UserMode mode) {
        final PersonalInfoInputActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final Map<String,String> INPUTS = new HashMap<>();
        List<SLInputType> info = mode.personalInformation();

        final List<SLTextInputType> TEXT_INFO = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(SLTextInputType.class::cast)
            .collect(Collectors.toList());

        TEXT_INFO.forEach(a -> INPUTS.put(a.toString(), a.randomInput()));

        return Flowable
            .fromIterable(TEXT_INFO)
            .concatMap(a -> THIS.rxEnterInput(a, INPUTS.get(a.toString())))
            .flatMap(ENGINE::rxToggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> ENGINE.rxHideKeyboard())

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            .flatMap(a -> THIS.rxEditFieldForInput(TextInput.PASSWORD))
            .flatMap(ENGINE::rxTogglePasswordMask)

            .flatMap(a -> THIS.rxOpenTOCWebsite())
            .delay(webViewDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rxNavigateBackOnce())
            .flatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rxEditFieldHasValue(a, INPUTS.get(a.toString())));
    }
}
