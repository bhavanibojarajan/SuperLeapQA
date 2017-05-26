package com.holmusk.SuperLeapQA.ui.signup.personalinfo;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.signup.validage.ValidAgeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoValidationType extends ValidAgeValidationType {
    /**
     * Get the submit button for the personal info screen. Depending on the
     * current {@link UserMode}, the confirm button text may change.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rx_e_personalInfoSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsID("btnNext").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine.rx_containsText(
                "register_title_submit",
                "register_title_next"
            ).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_TOCCheckBox(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsID("ctv_toc").firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the Terms and Condition acceptance label.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_TOCAcceptanceLabel(@NotNull Engine<?> engine) {
        return engine
            .rx_containsText("register_title_readAndAcceptTOC")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate that the personal info input screen contains correct
     * {@link WebElement}.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#personalInfo(PlatformType)
     * @see #rxe_editField(Engine, SLInputType)
     * @see #rx_e_personalInfoSubmit(Engine)
     * @see ObjectUtil#nonNull(Object)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rxv_personalInfoScreen(@NotNull final Engine<?> ENGINE,
                                               @NotNull UserMode mode) {
        final PersonalInfoValidationType THIS = this;
        final PlatformType PLATFORM = ENGINE.platform();

        return Flowable.fromIterable(mode.personalInfo(PLATFORM))
            .flatMap(a -> THIS.rxe_editField(ENGINE, a))
            .concatWith(THIS.rx_e_personalInfoSubmit(ENGINE))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
