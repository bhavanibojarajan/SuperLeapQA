package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.HMUITestKit.model.HMInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.ios.IOSView;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender implements BaseErrorType, HMInputType {
    MALE,
    FEMALE;

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(PlatformType)
     * @see #androidInputViewXP()
     * @see #iOSInputViewXP()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXP(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXP();

            case IOS:
                return iOSInputViewXP();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath androidInputViewXP() {
        final String ID;

        switch (this) {
            case MALE:
                ID = "btn_male";
                break;

            case FEMALE:
                ID = "btn_female";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return XPath.builder(Platform.ANDROID)
            .containsID(ID)
            .addAnyClass()
            .build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see BaseViewType#className()
     * @see Platform#IOS
     * @see IOSView.ViewType#UI_BUTTON
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#containsText(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath iOSInputViewXP() {
        String text;

        switch (this) {
            case MALE:
                text = "user_title_gender_male";
                break;

            case FEMALE:
                text = "user_title_gender_female";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        String localized = Config.TEST_KIT.localize(text);

        return XPath.builder(Platform.IOS)
            .addClass(IOSView.ViewType.UI_BUTTON.className())
            .containsText(localized)
            .build();
    }
}