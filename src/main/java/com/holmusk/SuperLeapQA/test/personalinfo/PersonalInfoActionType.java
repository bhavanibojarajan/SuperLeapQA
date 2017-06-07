package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoActionType extends PersonalInfoValidationType, ValidAgeActionType {
    /**
     * Toggle the TOC checkbox to be accepted/rejected.
     * @param ENGINE {@link Engine} instance.
     * @param ACCEPTED {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #rxe_TCCheckBox(Engine)
     * @see Engine#toggleCheckBox(WebElement, boolean)
     * @see Engine#click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_toggleTC(@NotNull final Engine<?> ENGINE,
                                     final boolean ACCEPTED) {
        return rxe_TCCheckBox(ENGINE).flatMap(a -> ENGINE.toggleCheckBox(a, ACCEPTED));
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_hideKeyboard()
     * @see UserMode#personalInfo(PlatformType)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_toggleTC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_enterPersonalInfo(@NotNull final Engine<?> ENGINE,
                                              @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        PlatformType platform = ENGINE.platform();

        return rxa_randomInputs(ENGINE, mode.personalInfo(platform))
            .flatMap(a -> THIS.rxa_toggleTC(ENGINE, true));
    }

    /**
     * Click the submit button to confirm personal info inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_personalInfoSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPersonalInfo(@NotNull final Engine<?> ENGINE) {
        return rxe_personalInfoSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter and confirm personal info.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #personalInfoProgressDelay(Engine)
     * @see #rxa_enterPersonalInfo(Engine, UserMode)
     * @see #rxa_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completePersonalInfo(@NotNull final Engine ENGINE,
                                                 @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;

        return rxa_enterPersonalInfo(ENGINE, mode)
            .flatMap(a -> THIS.rxa_confirmPersonalInfo(ENGINE))
            .delay(personalInfoProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Open the main website and access the TOC page. This is primarily to
     * check that when the user navigates back to the app, the previous state
     * is saved and restored.
     * On {@link Platform#ANDROID}, the link
     * text is not a separate view, so we need to manually calculate an
     * approximate position, a press/tap on which will open up the web
     * browser.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_openTC(@NotNull final Engine<?> ENGINE) {
        return rxe_TCAcceptanceLabel(ENGINE)
            .map(a -> {
                Point point = a.getLocation();
                Dimension size = a.getSize();
                int x = point.getX(), y = point.getY(), h = size.getHeight();
                return new Point(x + 10, y + h - 3);
            })
            .flatMap(a -> ENGINE.rxa_tap(a.getX(), a.getY()));
    }
}
