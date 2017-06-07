package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.property.base.AttributeType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

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

    /**
     * Override this method to provide default implementation.
     * @return {@link String} value.
     * @see AttributeType#value()
     * @see #NOT_AVAILABLE
     */
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
     * Override this method to provide default implementation.
     * @return {@link String} value.
     * @see HMTextChoiceType.Item#stringValue()
     * @see #value()
     */
    @NotNull
    @Override
    public String stringValue() {
        return value();
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(PlatformType)
     * @see #androidInputViewXP()
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
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see Platform#ANDROID
     * @see XPath.Builder#addAttribute(Attribute)
     */
    @NotNull
    private XPath androidInputViewXP() {
        Attributes attrs = Attributes.of(Platform.ANDROID);
        Attribute attribute = attrs.containsID("text1");
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see CompoundAttribute#empty()
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     */
    @NotNull
    private XPath iOSInputViewXP() {
        return XPath.builder().addAttribute(CompoundAttribute.empty()).build();
    }

    /**
     * Check if the current {@link Ethnicity} is Asia-based.
     * @return {@link Boolean} value.
     * @see #ASIAN_OTHER
     * @see #CHINESE
     * @see #INDIAN
     * @see #JAPANESE
     * @see #MALAY
     */
    public boolean isAsian() {
        switch (this) {
            case ASIAN_OTHER:
            case CHINESE:
            case INDIAN:
            case JAPANESE:
            case MALAY:
                return true;

            default:
                return false;
        }
    }
}
