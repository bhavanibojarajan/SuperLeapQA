package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.element.property.base.AttributeType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.base.model.InputType;

/**
 * Created by haipham on 5/12/17.
 */
public enum Ethnicity implements
    AttributeType<String>,
    BaseErrorType,
    HMInputType,
    HMTextChoiceType.Item
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
     * @see InputType#inputViewXP(PlatformType)
     * @see #androidInputViewXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXP(@NotNull PlatformType platform) {
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
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXPath() {
        return XPath
            .builder(Platform.ANDROID)
            .containsID("text1")
            .addAnyClass()
            .build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see XPath.Builder#addAnyClass()
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        return XPath.builder(Platform.IOS).addAnyClass().build();
    }
}
