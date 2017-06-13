package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

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
     * @see Attributes#of(PlatformProviderType)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see Engine#localizer()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.ViewType#UI_BUTTON
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
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
                .withClass(IOSView.ViewType.UI_BUTTON.className())
                .build();

            XPath xPath = XPath.builder().addAttribute(cAttr).build();
            return engine.rxe_withXPath(xPath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_BUTTON
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_TCCheckBox(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("ctv_toc").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            String clsName = IOSView.ViewType.UI_BUTTON.className();
            CompoundAttribute attribute = CompoundAttribute.forClass(clsName);
            XPath xPath = XPath.builder().addAttribute(attribute).build();

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
