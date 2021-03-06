package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.protocol.ClassNameProviderType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoValidationType extends ValidAgeValidationType {
    /**
     * Get the submit button for
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}.
     * Depending on the current {@link UserMode}, the confirm button text may
     * change.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rxe_personalInfoSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("btnNext").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            LocalizerType localizer = engine.localizer();
            Attributes attrs = Attributes.of(engine);
            String localized = localizer.localize("register_title_register");

            CompoundAttribute cAttr = CompoundAttribute.builder()
                .addAttribute(attrs.containsText(localized))
                .withClass(IOSView.Type.UI_BUTTON)
                .build();

            XPath xpath = XPath.builder().addAttribute(cAttr).build();
            return engine.rxe_withXPath(xpath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    default Flowable<WebElement> rxe_TCCheckBox(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("ctv_toc").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            ClassNameProviderType param = IOSView.Type.UI_BUTTON;
            CompoundAttribute attribute = CompoundAttribute.forClass(param);
            XPath xpath = XPath.builder().addAttribute(attribute).build();

            /* Getting the T&C button this way is admittedly prone to failure,
             * but there is hardly a better way, since the page source for
             * iOS is not very descriptive when it comes to the button's
             * unchecked state */
            return engine
                .rxe_withXPath(xpath).toList()
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
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_personalInfoSubmit(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_personalInfoScreen(@NotNull final Engine<?> ENGINE,
                                               @NotNull UserMode mode) {
        final PersonalInfoValidationType THIS = this;
        final PlatformType PLATFORM = ENGINE.platform();

        return Flowable.concatArray(
            Flowable
                .fromIterable(mode.personalInfo(PLATFORM))
                .flatMap(a -> THIS.rxe_editField(ENGINE, a)),

            rxe_personalInfoSubmit(ENGINE)
        ).all(HPObjects::nonNull).toFlowable();
    }
}
