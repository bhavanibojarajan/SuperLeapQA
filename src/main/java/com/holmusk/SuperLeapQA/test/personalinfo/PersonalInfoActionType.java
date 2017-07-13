package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;

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
     * @see HPObjects#nonNull(Object)
     * @see UserMode#personalInfo(PlatformType)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_toggleTC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_enterPersonalInfo(@NotNull Engine<?> engine,
                                              @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        PlatformType platform = engine.platform();

        return Flowable
            .concatArray(
                rxa_randomInputs(engine, mode.personalInfo(platform)),
                rxa_toggleTC(engine, true)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Click the submit button to confirm personal info inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see HPObjects#nonNull(Object)
     * @see #personalInfoProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_personalInfoSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPersonalInfo(@NotNull final Engine<?> ENGINE) {
        return Flowable.concatArray(
            rxe_personalInfoSubmit(ENGINE).flatMap(ENGINE::rxa_click),
            Flowable.timer(personalInfoProgressDelay(ENGINE), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(ENGINE)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter and confirm personal info.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see HPObjects#nonNull(Object)
     * @see #rxa_enterPersonalInfo(Engine, UserMode)
     * @see #rxa_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completePersonalInfo(@NotNull Engine engine,
                                                 @NotNull UserMode mode) {
        return Flowable
            .concatArray(
                rxa_enterPersonalInfo(engine, mode),
                rxa_confirmPersonalInfo(engine)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
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
     * @see Engine#rxa_tap(Point)
     * @see Dimension#getHeight()
     * @see Point#getX()
     * @see Point#getY()
     * @see WebElement#getLocation()
     * @see WebElement#getSize()
     * @see #webViewDelay(Engine)
     * @see #rxe_TCAcceptanceLabel(Engine)
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
            .flatMap(ENGINE::rxa_tap)
            .delay(webViewDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
