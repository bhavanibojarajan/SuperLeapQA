package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import com.holmusk.SuperLeapQA.model.SLTextInputType;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoActionType extends
    PersonalInfoValidationType, AcceptableAgeActionType
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
        final PersonalInfoActionType THIS = this;
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
        final PersonalInfoActionType THIS = this;
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
     * @see #rx_confirmExtraPersonalInfo(UserMode)
     * @see #rxWatchProgressBarUntilHidden()
     * @see #rxWatchPersonalInfoScreenUntilHidden()
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_extraInfo_useApp(@NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;

        return rxEnterExtraPersonalInfo(MODE)
            .flatMap(a -> THIS.rx_confirmExtraPersonalInfo(MODE))

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
     * @see BaseEngine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmPersonalInfo() {
        final BaseEngine<?> ENGINE = engine();

        return rxPersonalInfoSubmitButton()
            .flatMap(ENGINE::rx_click)
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
            .flatMap(a -> ENGINE.rx_setCheckBoxState(a, ACCEPTED))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * This method can be used for {@link UserMode#personalInformation()}
     * and {@link UserMode#extraPersonalInformation()}.
     * @param inputs A {@link List} of {@link InputType}.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxEnterRandomInput(SLTextInputType)
     * @see BaseEngine#rx_navigateBackOnce()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_enterPersonalInfo(@NotNull List<SLInputType> inputs) {
        final PersonalInfoActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .fromIterable(inputs)
            .ofType(SLTextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .flatMap(ENGINE::rx_toggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rx_enterPersonalInfo(List)
     * @see BaseEngine#rx_hideKeyboard()
     * @see #rxToggleTOC(boolean)
     */
    @NotNull
    default Flowable<Boolean> rx_enterPersonalInfo(@NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        final BaseEngine<?> ENGINE = THIS.engine();

        return rx_enterPersonalInfo(mode.personalInformation())
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rxToggleTOC(true));
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#requiresGuarantor()}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_enterPersonalInfo(List)
     * @see UserMode#extraPersonalInformation()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<Boolean> rxEnterExtraPersonalInfo(@NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rx_enterPersonalInfo(mode.extraPersonalInformation());
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
    default Flowable<Boolean> rx_confirmExtraPersonalInfo(@NotNull UserMode mode) {
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
     * @see BaseEngine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxWatchPersonalInfoScreenUntilHidden() {
        final BaseEngine<?> ENGINE = engine();

        return rxPersonalInfoSubmitButton()
            .flatMap(ENGINE::rx_watchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Navigate from the personal info input screen to the extra info input
     * screen. Only applicable to {@link UserMode#requiresGuarantor()}.
     * If {@link UserMode#requiresGuarantor()} is {@link Boolean#FALSE},
     * this method will go directly to the dashboard.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_enterPersonalInfo(UserMode)
     * @see #rxConfirmPersonalInfo()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<Boolean> rx_personalInfo_extraInfo(@NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        return rx_enterPersonalInfo(mode).flatMap(a -> THIS.rxConfirmPersonalInfo());
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
            .flatMap(a -> ENGINE.rx_tap(a.getX(), a.getY()));
    }
}
