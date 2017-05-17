package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoValidationType extends SignUpValidationType {
    /**
     * Get the submit button for the personal info screen. Depending on the
     * current {@link UserMode}, the confirm button text may change.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#platform()
     * @see BaseEngine#rxElementContainingID(String...)
     * @see RxUtil#error(String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rxPersonalInfoSubmitButton() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementsContainingID("btnNext");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#platform()
     * @see BaseEngine#rxElementContainingID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxTOCCheckBox() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("ctv_toc");
        } else {
            return RxUtil.error();
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
}
