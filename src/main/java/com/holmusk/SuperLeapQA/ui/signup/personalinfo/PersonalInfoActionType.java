package com.holmusk.SuperLeapQA.ui.signup.personalinfo;

import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.ui.signup.validage.ValidAgeActionType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoActionType extends PersonalInfoValidationType, ValidAgeActionType {
    /**
     * Click the submit button to confirm personal info inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_personalInfoSubmit(Engine)
     * @see Engine#rxa_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rxa_confirmPersonalInfo(@NotNull final Engine<?> ENGINE) {
        return rxe_personalInfoSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

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
     * @see #rxa_enterRandomInputs(Engine, List)
     * @see #rxa_toggleTC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_enterPersonalInfo(@NotNull final Engine<?> ENGINE,
                                              @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        PlatformType platform = ENGINE.platform();

        return rxa_enterRandomInputs(ENGINE, mode.personalInfo(platform))
            .flatMap(a -> THIS.rxa_toggleTC(ENGINE, true));
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#requiresGuarantor()}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#guarantorInfo(PlatformType)
     * @see UserMode#requiresGuarantor()
     * @see #rxa_enterRandomInput(Engine, SLTextInputType)
     */
    @NotNull
    default Flowable<?> rxa_enterGuarantorInfo(@NotNull Engine<?> engine,
                                               @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            PlatformType platform = engine.platform();
            return rxa_enterRandomInputs(engine, mode.guarantorInfo(platform));
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Confirm additional personal inputs. This is only relevant to
     * {@link UserMode#requiresGuarantor()}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#requiresGuarantor()
     * @see #rxa_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmGuarantorInfo(@NotNull Engine<?> engine,
                                                 @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rxa_confirmPersonalInfo(engine);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Watch until the personal info screen is no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_personalInfoSubmit(Engine)
     * @see Engine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_watchPersonalInfoScreen(@NotNull final Engine<?> ENGINE) {
        return rxe_personalInfoSubmit(ENGINE)
            .flatMap(ENGINE::rx_watchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Open the main website and access the TOC page. This is primarily to
     * check that when the user navigates back to the app, the previous state
     * is saved and restored.
     * On {@link org.swiften.xtestkit.mobile.Platform#ANDROID}, the link
     * text is not a separate view, so we need to manually calculate an
     * approximate position, a press/tap on which will open up the web
     * browser.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_openTOC(@NotNull final Engine<?> ENGINE) {
        return rxe_TCAcceptanceLabel(ENGINE)
            .map(a -> {
                Point point = a.getLocation();
                Dimension size = a.getSize();
                int x = point.getX(), y = point.getY(), h = size.getHeight();
                return new Point(x + 10, y + h - 3);
            })
            .flatMap(a -> ENGINE.rx_tap(a.getX(), a.getY()));
    }
}
