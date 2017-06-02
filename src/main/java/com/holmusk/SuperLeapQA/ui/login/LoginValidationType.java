package com.holmusk.SuperLeapQA.ui.login;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 5/26/17.
 */
public interface LoginValidationType extends BaseValidationType {
    /**
     * Get the submit button {@link WebElement}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see AndroidView.ViewType#BUTTON
     * @see BaseViewType#className()
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_BUTTON
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#containsText(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_submit(@NotNull final Engine<?> ENGINE) {
        LocalizerType localizer = ENGINE.localizer();
        String title, clsName;

        if (ENGINE instanceof AndroidEngine) {
            clsName = AndroidView.ViewType.BUTTON.className();
            title = "login_title_signIn";
        } else if (ENGINE instanceof IOSEngine) {
            clsName = IOSView.ViewType.UI_BUTTON.className();
            title = "login_title_submit";
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        XPath xPath = XPath.builder(ENGINE.platform())
            .addClass(clsName)
            .containsText(localizer.localize(title))
            .build();

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
     * @see Engine#localizer()
     * @see IOSView.ViewType#UI_LINK
     * @see LocalizerType#localize(String)
     * @see Platform#IOS
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#containsText(String)
     * @see Engine#rxe_containsText(LCFormat...)
     * @see Engine#rxe_withXPath(XPath...)
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

            XPath xPath = XPath.builder(Platform.IOS)
                .addClass(IOSView.ViewType.UI_LINK.className())
                .containsText(localized)
                .build();

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
     * @see #rxe_editField(Engine, SLInputType)
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
