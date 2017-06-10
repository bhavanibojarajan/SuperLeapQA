package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 5/12/17.
 */
public enum CoachPref implements BaseErrorType, HMInputType, HMTextChoiceType.Item {
    MALE,
    FEMALE,
    NO_PREFERENCE;

    /**
     * Get the {@link String} value displayed by the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link CoachPref}.
     * @return {@link String} value.
     * @see #FEMALE
     * @see #MALE
     * @see #NO_PREFERENCE
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case MALE:
                return "user_title_coachPref_male";

            case FEMALE:
                return "user_title_coachPref_female";

            case NO_PREFERENCE:
                return "user_title_coachPref_noPreference";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @return {@link String} value.
     * @see Config#LOCALIZER
     * @see HMTextChoiceType.Item#stringValue()
     * @see org.swiften.javautilities.localizer.LocalizerType#localize(String)
     * @see #title()
     */
    @NotNull
    @Override
    public String stringValue() {
        return Config.LOCALIZER.localize(title());
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(PlatformType)
     * @see #androidInputViewXP()
     * @see #iOSInputViewXP()
     * @see Platform#ANDROID
     * @see Platform#IOS
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
}
