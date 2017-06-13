package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 5/12/17.
 */
public enum Ethnicity implements BaseErrorType, HMInputType, HMTextChoiceType.Item {
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
     * Get {@link String} value as displayed by the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link Ethnicity}.
     * @return {@link String} value.
     * @see #AFRICAN
     * @see #ASIAN_OTHER
     * @see #CHINESE
     * @see #INDIAN
     * @see #JAPANESE
     * @see #MALAY
     * @see #OTHERS
     * @see #WHITE_NON_HISPANIC
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case AFRICAN:
                return "user_title_ethnicity_african";

            case ASIAN_OTHER:
                return "user_title_ethnicity_asianOther";

            case CHINESE:
                return "user_title_ethnicity_chinese";

            case INDIAN:
                return "user_title_ethnicity_indian";

            case LATINO_HISPANIC:
                return "user_title_ethnicity_latinoHispanic";

            case JAPANESE:
                return "user_title_ethnicity_japanese";

            case MALAY:
                return "user_title_ethnicity_malay";

            case OTHERS:
                return "user_title_ethnicity_others";

            case WHITE_NON_HISPANIC:
                return "user_title_ethnicity_whiteNonHispanic";

            default:
                return "";
        }
    }

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link String} value.
     * @see HMTextChoiceType.Item#stringValue(InputHelperType, double)
     * @see InputHelperType#localizer()
     * @see LocalizerType#localize(String)
     * @see #title()
     */
    @NotNull
    @Override
    public String stringValue(@NotNull InputHelperType helper) {
        LocalizerType localizer = helper.localizer();
        return localizer.localize(title());
    }

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} value.
     * @see InputHelperType#platform()
     * @see InputType#inputViewXP(InputHelperType)
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #androidInputViewXP(InputHelperType)
     * @see #iOSInputViewXP(InputHelperType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXP(@NotNull InputHelperType helper) {
        PlatformType platform = helper.platform();

        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXP(helper);

            case IOS:
                return iOSInputViewXP(helper);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see Platform#ANDROID
     */
    @NotNull
    private XPath androidInputViewXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(Platform.ANDROID);
        Attribute attribute = attrs.containsID("text1");
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see CompoundAttribute#empty()
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
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
