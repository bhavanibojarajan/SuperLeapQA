package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements HMTextChoiceType {
    GENDER,
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREF,
    START_WEIGHT;

    /**
     * Get the title {@link String} corresponding to the current
     * {@link ChoiceInput}.
     * @return {@link String} value.
     */
    @NotNull
    public String title() {
        switch (this) {
            case GENDER:
                return "user_title_gender";

            case HEIGHT:
                return "user_title_height";

            case WEIGHT:
                return "user_title_weight";

            case ETHNICITY:
                return "user_title_ethnicity";

            case COACH_PREF:
                return "user_title_coachPref";

            case START_WEIGHT:
                return "user_profile_title_startWeight";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Override this method to provide default implementation.
     * @return {@link List} of {@link HMTextChoiceType}.
     * @see HMTextChoiceType#allTextChoices()
     */
    @NotNull
    @Override
    public List<? extends HMTextChoiceType.Item> allTextChoices() {
        switch (this) {
            case GENDER:
                return HPIterables.asList(Gender.values());

            case ETHNICITY:
                return HPIterables.asList(Ethnicity.values());

            case COACH_PREF:
                return HPIterables.asList(CoachPref.values());

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
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

        final String ID;

        switch (this) {
            case HEIGHT:
                ID = "et_height";
                break;

            case WEIGHT:
                ID = "et_weight";
                break;

            case ETHNICITY:
                ID = "et_eth";
                break;

            case COACH_PREF:
                ID = "et_coachpref";
                break;

            case START_WEIGHT:
                ID = "tv_start_weight";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        Attribute attribute = attrs.containsID(ID);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Axes#followingSibling(AttributeType)
     * @see #title()
     * @see #values()
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        LocalizerType localizer = helper.localizer();
        Platform platform = Platform.IOS;
        String title = title();
        String localized = localizer.localize(title);
        Attributes attrs = Attributes.of(platform);
        Attribute stAttr = attrs.containsText(localized);

        return XPath.builder()
            .addAttribute(CompoundAttribute.builder()
                .withClass(IOSView.Type.UI_STATIC_TEXT)
                .addAttribute(stAttr)
                .build())
            .addAttribute(Axes.followingSibling(CompoundAttribute
                .forClass(IOSView.Type.UI_TEXT_FIELD)))
            .build();
    }
}
