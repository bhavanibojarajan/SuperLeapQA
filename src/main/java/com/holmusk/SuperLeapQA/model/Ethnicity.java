package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextChoiceInputItemType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.element.property.type.base.AttributeType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

/**
 * Created by haipham on 5/12/17.
 */
public enum Ethnicity implements
    AttributeType<String>,
    BaseErrorType,
    SLInputType,
    SLTextChoiceInputItemType
{
    CHINESE,
    MALAY,
    INDIAN,
    JAPANESE,
    ASIAN_OTHER,
    WHITE_NON_HISPANIC,
    AFRICAN,
    LATINO_HISPANIC,
    OTHERS;

    @NotNull
    @Override
    public String value() {
        switch (this) {
            case CHINESE:
                return "user_title_ethnicity_chinese";

            case MALAY:
                return "user_title_ethnicity_malay";

            case INDIAN:
                return "user_title_ethnicity_indian";

            case JAPANESE:
                return "user_title_ethnicity_japanese";

            case ASIAN_OTHER:
                return "user_title_ethnicity_asianOther";

            case WHITE_NON_HISPANIC:
                return "user_title_ethnicity_whiteNonHispanic";

            case AFRICAN:
                return "user_title_ethnicity_african";

            case LATINO_HISPANIC:
                return "user_title_ethnicity_latinoHispanic";

            case OTHERS:
                return "user_title_ethnicity_others";

            default:
                return "";
        }
    }

    /**
     * @return {@link String} value.
     * @see SLTextChoiceInputItemType#stringValue()
     * @see org.swiften.javautilities.localizer.LocalizerType#localize(String)
     */
    @NotNull
    @Override
    public String stringValue() {
        return Config.TEST_KIT.localize(value());
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.InputType#inputViewXPath(PlatformType)
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
        return XPath.builder(Platform.ANDROID).containsID("text1").build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        return XPath.builder(Platform.IOS).build();
    }

    /**
     * @return {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.builder()
            .withPattern("register_error_ethnicityNotSet")
            .build();
    }
}
