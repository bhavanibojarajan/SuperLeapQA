package com.holmusk.SuperLeapQA.test.login;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 5/26/17.
 */
public interface LoginValidationType extends BaseValidationType {
    /**
     * Get the submit button {@link WebElement}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see AndroidView.ViewType#BUTTON
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute#single(Attribute)
     * @see CompoundAttribute#withClass(String)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_BUTTON
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_submit(@NotNull final Engine<?> ENGINE) {
        LocalizerType localizer = ENGINE.localizer();
        PlatformType platform = ENGINE.platform();
        String title, clsName;
        Attributes attrs = Attributes.of(platform);

        if (ENGINE instanceof AndroidEngine) {
            clsName = AndroidView.ViewType.BUTTON.className();
            title = "login_title_signIn";
        } else if (ENGINE instanceof IOSEngine) {
            clsName = IOSView.ViewType.UI_BUTTON.className();
            title = "login_title_submit";
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        Attribute attribute = attrs.containsText(localizer.localize(title));

        CompoundAttribute cAttr = CompoundAttribute
            .single(attribute)
            .withClass(clsName);

        XPath xPath = XPath.builder().addAttribute(cAttr).build();
        return ENGINE.rxe_withXPath(xPath).firstElement().toFlowable();
    }

    /**
     * Get the forgot password button {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_forgotPassword(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("login_title_forgotPassword")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the register button {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute#single(Attribute)
     * @see CompoundAttribute#withClass(String)
     * @see Engine#localizer()
     * @see Engine#rxe_containsText(LCFormat...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_LINK
     * @see LocalizerType#localize(String)
     * @see Platform#IOS
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_loginRegister(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsText("login_title_register")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            LocalizerType localizer = engine.localizer();
            String localized = localizer.localize("login_title_register");
            Attributes attrs = Attributes.of(Platform.IOS);

            Attribute attribute = attrs.containsText(localized);

            CompoundAttribute cAttr = CompoundAttribute.single(attribute)
                .withClass(IOSView.ViewType.UI_LINK.className());

            XPath xPath = XPath.builder().addAttribute(cAttr).build();
            return engine.rxe_withXPath(xPath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOGIN}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#EMAIL
     * @see TextInput#PASSWORD
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_loginRegister(Engine)
     * @see #rxe_submit(Engine)
     * @see #rxe_forgotPassword(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_loginScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_editField(engine, TextInput.EMAIL),
                rxe_editField(engine, TextInput.PASSWORD),
                rxe_loginRegister(engine),
                rxe_submit(engine),
                rxe_forgotPassword(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
