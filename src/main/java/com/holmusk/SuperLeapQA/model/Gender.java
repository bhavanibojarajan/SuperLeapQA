package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.property.base.AttributeType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 5/10/17.
 */
public enum Gender implements BaseErrorType, HMInputType, HMTextChoiceType.Item {
    MALE,
    FEMALE;

    /**
     * Get {@link String} value as displayed by the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link Gender}.
     * @return {@link String} value.
     * @see AttributeType#value()
     * @see #FEMALE
     * @see #MALE
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case MALE:
                return "user_title_gender_male";

            case FEMALE:
                return "user_title_gender_female";

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
     * @see Platform#ANDROID
     * @see Platform#IOS
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
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see Platform#ANDROID
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #FEMALE
     * @see #MALE
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath androidInputViewXP() {
        Attributes attrs = Attributes.of(Platform.ANDROID);

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

        Attribute attribute = attrs.containsID(ID);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute#withClass(String)
     * @see Config#LOCALIZER
     * @see Platform#IOS
     * @see IOSView.ViewType#UI_BUTTON
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath iOSInputViewXP() {
        Attributes attrs = Attributes.of(Platform.IOS);

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

        String localized = Config.LOCALIZER.localize(text);
        Attribute attr = attrs.containsText(localized);

        CompoundAttribute cAttr = CompoundAttribute.single(attr)
            .withClass(IOSView.ViewType.UI_BUTTON.className());

        return XPath.builder().addAttribute(cAttr).build();
    }
}