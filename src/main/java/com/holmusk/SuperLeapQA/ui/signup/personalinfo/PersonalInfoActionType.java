package com.holmusk.SuperLeapQA.ui.signup.personalinfo;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.ui.signup.validage.ValidAgeActionType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.model.InputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoActionType extends PersonalInfoValidationType, ValidAgeActionType {
    /**
     * Click the submit button to confirm personal info inputs.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_personalInfoSubmit(Engine)
     * @see Engine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_a_confirmPersonalInfo(@NotNull final Engine<?> ENGINE) {
        return rx_e_personalInfoSubmit(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Toggle the TOC checkbox to be accepted/rejected.
     * @param ENGINE {@link Engine} instance.
     * @param ACCEPTED {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #rx_e_TOCCheckBox(Engine)
     * @see Engine#toggleCheckBox(WebElement, boolean)
     * @see Engine#click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_toggleTOC(@NotNull final Engine<?> ENGINE,
                                       final boolean ACCEPTED) {
        return rx_e_TOCCheckBox(ENGINE).flatMap(a -> ENGINE.toggleCheckBox(a, ACCEPTED));
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * This method can be used for {@link UserMode#personalInformation()}
     * and {@link UserMode#extraPersonalInformation()}.
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link InputType}.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterRandomInput(Engine, SLTextInputType)
     * @see Engine#rx_navigateBackOnce()
     * @see #rx_a_toggleTOC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rx_a_enterPersonalInfo(@NotNull final Engine<?> ENGINE,
                                               @NotNull List<SLInputType> inputs) {
        final PersonalInfoActionType THIS = this;
        return Flowable
            .fromIterable(inputs)
            .ofType(SLTextInputType.class)
            .concatMap(a -> THIS.rx_a_enterRandomInput(ENGINE, a))
            .flatMap(ENGINE::rx_toggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#personalInformation()
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see Engine#rx_hideKeyboard()
     * @see #rx_a_toggleTOC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rx_a_enterPersonalInfo(@NotNull final Engine<?> ENGINE,
                                               @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;

        return rx_a_enterPersonalInfo(ENGINE, mode.personalInformation())
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rx_a_toggleTOC(ENGINE, true));
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#requiresGuarantor()}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterPersonalInfo(Engine, List)
     * @see UserMode#extraPersonalInformation()
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<?> rx_a_enterExtraPersonalInfo(@NotNull Engine<?> engine,
                                                    @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rx_a_enterPersonalInfo(engine, mode.extraPersonalInformation());
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
     * @see #rx_a_confirmPersonalInfo(Engine)
     * @see UserMode#requiresGuarantor()
     */
    @NotNull
    default Flowable<?> rx_a_confirmExtraPersonalInfo(@NotNull Engine<?> engine,
                                                      @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return rx_a_confirmPersonalInfo(engine);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Watch until the personal info screen is no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_personalInfoSubmit(Engine)
     * @see Engine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_watchPersonalInfoScreen(@NotNull final Engine<?> ENGINE) {
        return rx_e_personalInfoSubmit(ENGINE)
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
    default Flowable<?> rx_a_OpenTOC(@NotNull final Engine<?> ENGINE) {
        return rx_e_TOCAcceptanceLabel(ENGINE)
            .map(a -> {
                Point point = a.getLocation();
                Dimension size = a.getSize();
                int x = point.getX(), y = point.getY(), h = size.getHeight();
                return new Point(x + 10, y + h - 3);
            })
            .flatMap(a -> ENGINE.rx_tap(a.getX(), a.getY()));
    }
}
