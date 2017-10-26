package com.holmusk.SuperLeapQA.model;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;


public enum Institute implements ErrorProviderType, HMInputType, HMTextChoiceType.Item {
    POLYTECHNIC,
    ITE,
    JUNIOR_COLLEGE,
    UNIVERSITY,
    OTHERS;

    /**
     * Get {@link String} value as displayed by the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link Institute}.
     * @return {@link String} value.
     */
    @NotNull
    public String title() {
        switch (this) {
            case POLYTECHNIC:
                return "user_title_institute_polytechnic";

            case ITE:
                return "user_title_institute_ite";

            case JUNIOR_COLLEGE:
                return "user_title_institute_juniorcollege";

            case UNIVERSITY:
                return "user_title_institute_university";

            case OTHERS:
                return "user_title_institute_others";


            default:
                return "";
        }
    }

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
     * @see InputType#inputViewXP(InputHelperType)
     * @see #androidInputViewXP(InputHelperType)
     * @see #iOSInputViewXP(InputHelperType)
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
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        return XPath.builder().addAttribute(CompoundAttribute.empty()).build();
    }

    }



