package com.holmusk.SuperLeapQA.ui.signup.main;

import org.swiften.xtestkit.base.element.action.input.type.InputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoValidationType extends SignUpValidationType {
    /**
     * Get the submit button for the personal info screen. Depending on the
     * current {@link UserMode}, the confirm button text may change.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingID(String...)
     * @see RxUtil#error(String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rxPersonalInfoSubmitButton() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementsContainingID("btnNext");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingID(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxTOCCheckBox() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingID("ctv_toc");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the Terms and Condition acceptance label.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementsContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxTOCAcceptanceLabel() {
        return engine().rxElementContainingText("register_title_readAndAcceptTOC");
    }

    /**
     * Validate that the personal info input screen contains correct
     * {@link WebElement}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see UserMode#personalInformation()
     * @see #rxEditFieldForInput(AndroidInputType)
     * @see #rxPersonalInfoSubmitButton()
     * @see ObjectUtil#nonNull(Object)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxValidatePersonalInfoScreen(@NotNull UserMode mode) {
        final PersonalInfoValidationType THIS = this;

        return Flowable.fromIterable(mode.personalInformation())
            .flatMap(THIS::rxEditFieldForInput)
            .concatWith(THIS.rxPersonalInfoSubmitButton())
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .map(BooleanUtil::toTrue);
    }
}
