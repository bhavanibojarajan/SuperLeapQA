package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.ios.IOSView;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender implements BaseErrorType, SLInputType {
    MALE,
    FEMALE;

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXPath(PlatformType)
     * @see #androidInputViewXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXPath();

            case IOS:
                return iOSInputViewXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXPath() {
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

        return XPath.builder(Platform.ANDROID).containsID(ID).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see XPath.Builder#setClass(String)
     * @see XPath.Builder#containsText(String)
     * @see IOSView.ViewType#UI_BUTTON
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath iOSInputViewXPath() {
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
            .setClass(IOSView.ViewType.UI_BUTTON.className())
            .containsText(localized)
            .build();
    }

    /**
     * @return {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        String error;

        switch (mode) {
            case PARENT:
                error = "parentSignUp_error_genderNotSelected";
                break;

            case TEEN_U18:
                error = "teenSignUp_error_genderNotSelected";
                break;

            default:
                error = "";
                break;
        }

        return LCFormat.builder().withPattern(error).build();
    }
}
