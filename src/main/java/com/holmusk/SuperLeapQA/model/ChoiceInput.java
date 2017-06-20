package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.ViewType;
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
    COACH_PREF;

    /**
     * Override this method to provide default implementation.
     * @return {@link List} of {@link HMTextChoiceType}.
     * @see HMTextChoiceType#allTextChoices()
     * @see CollectionUtil#asList(Object[])
     * @see CoachPref#values()
     * @see CollectionUtil#asList(Object[])
     * @see Ethnicity#values()
     * @see Gender#values()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public List<? extends HMTextChoiceType.Item> allTextChoices() {
        switch (this) {
            case GENDER:
                return CollectionUtil.asList(Gender.values());

            case ETHNICITY:
                return CollectionUtil.asList(Ethnicity.values());

            case COACH_PREF:
                return CollectionUtil.asList(CoachPref.values());

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
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
     * @see Platform#ANDROID
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see #COACH_PREF
     * @see #ETHNICITY
     * @see #HEIGHT
     * @see #WEIGHT
     * @see #NOT_AVAILABLE
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
     * @see Attributes#of(PlatformType)
     * @see Axes#followingSibling(AttributeType)
     * @see CompoundAttribute#forClass(String)
     * @see CompoundAttribute#withIndex(Integer)
     * @see Engine#localizer()
     * @see LocalizerType#localize(String)
     * @see ViewType#className()
     * @see Platform#IOS
     * @see IOSView.Type#UI_STATIC_TEXT
     * @see IOSView.Type#UI_TEXT_FIELD
     * @see #iOSTitleDescription()
     * @see #values()
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        LocalizerType localizer = helper.localizer();
        Platform platform = Platform.IOS;
        String title = iOSTitleDescription();
        String localized = localizer.localize(title);
        String st = IOSView.Type.UI_STATIC_TEXT.className();
        String tf = IOSView.Type.UI_TEXT_FIELD.className();
        Attributes attrs = Attributes.of(platform);
        Attribute stAttr = attrs.containsText(localized);
        CompoundAttribute tfCAttr = CompoundAttribute.forClass(tf);

        return XPath.builder()
            .addAttribute(CompoundAttribute.forClass(st).addAttribute(stAttr))
            .addAttribute(Axes.followingSibling(tfCAttr))
            .build();
    }

    /**
     * Get the title description to be used with
     * {@link #iOSInputViewXP(InputHelperType)}.
     * @return {@link String} value.
     * @see #COACH_PREF
     * @see #ETHNICITY
     * @see #GENDER
     * @see #HEIGHT
     * @see #WEIGHT
     */
    @NotNull
    private String iOSTitleDescription() {
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

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
