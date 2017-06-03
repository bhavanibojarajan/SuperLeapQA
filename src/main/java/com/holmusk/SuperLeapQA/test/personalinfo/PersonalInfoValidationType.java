package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoValidationType extends ValidAgeValidationType {
    /**
     * Get the submit button for the personal info screen. Depending on the
     * current {@link UserMode}, the confirm button text may change.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rxe_personalInfoSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("btnNext").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine.rxe_containsText(
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
     * @see IOSView.ViewType#UI_BUTTON
     * @see Platform#IOS
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see XPath.Builder#addClass(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_TCCheckBox(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("ctv_toc").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            XPath xPath = XPath.builder(Platform.IOS)
                .addClass(IOSView.ViewType.UI_BUTTON.className())
                .build();

            /* Getting the T&C button this way is admittedly prone to failure,
             * but there is hardly a better way, since the page source for
             * iOS is not very descriptive when it comes to the button's
             * unchecked state */
            return engine
                .rxe_withXPath(xPath).toList()
                .map(a -> a.get(a.size() - 2))
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the Terms and Condition acceptance label.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_TCAcceptanceLabel(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_readAndAcceptTOC")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate that the personal info input screen contains correct
     * {@link WebElement}.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see UserMode#personalInfo(PlatformType)
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_personalInfoSubmit(Engine)
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
            .concatWith(THIS.rxe_personalInfoSubmit(ENGINE))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
