package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.string.StringUtil;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 5/10/17.
 */
public enum TextInput implements BaseErrorType, HMTextType {
    CHILD_NAME,
    CHILD_NRIC,
    EMAIL,
    HOME,
    MEAL_COMMENT,
    MEAL_DESCRIPTION,
    MOBILE,
    NAME,
    NRIC,
    PARENT_EMAIL,
    PARENT_MOBILE,
    PARENT_NAME,
    PASSWORD,
    PHONE,
    POSTAL_CODE,
    UNIT_NUMBER;

    /**
     * Override this method to provide default implementation.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} value.
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
     * @see XPath.Builder#addAttribute(Attribute)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath androidInputViewXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(Platform.ANDROID);

        final String ID;

        switch (this) {
            case NAME:
            case PARENT_NAME:
                ID = "et_name";
                break;

            case EMAIL:
            case PARENT_EMAIL:
                ID = "et_email";
                break;

            case PHONE:
                ID = "et_phone";
                break;

            case CHILD_NAME:
                ID = "et_childname";
                break;

            case CHILD_NRIC:
            case NRIC:
                ID = "et_childnric";
                break;

            case MOBILE:
            case PARENT_MOBILE:
                ID = "et_mobile";
                break;

            case PASSWORD:
                ID = "et_password";
                break;

            case HOME:
                ID = "et_home";
                break;

            case UNIT_NUMBER:
                ID = "et_unitnumber";
                break;

            case POSTAL_CODE:
                ID = "et_postalcode";
                break;

            case MEAL_DESCRIPTION:
                ID = "ac_logfood_desc";
                break;

            case MEAL_COMMENT:
                ID = "et_comment";
                break;

            default:
                LogUtil.println(this);
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
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see InputHelperType#localizer()
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(Attribute)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.ViewType#UI_TEXT_FIELD
     * @see IOSView.ViewType#UI_SECURE_TEXT_FIELD
     * @see Platform#IOS
     * @see #iOSShortDescription()
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        Attributes attrs = Attributes.of(Platform.IOS);
        LocalizerType localizer = helper.localizer();

        switch (this) {
            case PASSWORD:
                String cls = IOSView.ViewType.UI_SECURE_TEXT_FIELD.className();
                CompoundAttribute a1 = CompoundAttribute.forClass(cls);
                return XPath.builder().addAttribute(a1).build();

            default:
                String shortDsc = iOSShortDescription();
                String localized = localizer.localize(shortDsc);
                Attribute a2 = attrs.containsText(localized);
                return XPath.builder().addAttribute(a2).build();
        }
    }

    /**
     * Get a short description of the input field, to use with
     * {@link #iOSInputViewXP(InputHelperType)}. This is done so we do not
     * have to search for the input fields by their indexes.
     * @return {@link String} value.
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String iOSShortDescription() {
        switch (this) {
            case NAME:
            case PARENT_NAME:
            case CHILD_NAME:
                return "user_title_abbv_name";

            case PHONE:
            case MOBILE:
            case PARENT_MOBILE:
                return "user_title_abbv_mobile";

            case EMAIL:
            case PARENT_EMAIL:
                return "user_title_abbv_email";

            case CHILD_NRIC:
            case NRIC:
                return "user_title_abbv_nric";

            case PASSWORD:
                return "user_title_abbv_password";

            case HOME:
                return "user_title_abbv_home";

            case POSTAL_CODE:
                return "user_title_abbv_postal";

            case UNIT_NUMBER:
                return "user_title_abbv_unit";

            case MEAL_DESCRIPTION:
                return "mealLog_title_abbv_description";

            case MEAL_COMMENT:
                return "mealLog_title_abbv_comment";

            default:
                LogUtil.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get a random text input.
     * @param helper {@link InputHelperType} instance.
     * @return {@link String} value.
     * @see org.swiften.xtestkit.base.model.TextInputType#randomInput(InputHelperType)
     * @see CollectionUtil#randomElement(Object[])
     * @see StringUtil#randomDigitString(int)
     * @see StringUtil#randomString(int)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public String randomInput(@NotNull InputHelperType helper) {
        switch (this) {
            case CHILD_NAME:
            case NAME:
            case PARENT_NAME:
            case PASSWORD:
                return "testQA-" + StringUtil.randomString(10);

            case CHILD_NRIC:
            case NRIC:
                String suffix = CollectionUtil.randomElement(
                    "A",
                    "B",
                    "C",
                    "D",
                    "E",
                    "F",
                    "G",
                    "H",
                    "I",
                    "Z",
                    "J");

                String digits  = StringUtil.randomDigitString(7);
                return "S" + digits + suffix;

            case MOBILE:
            case PARENT_MOBILE:
            case PHONE:
                return StringUtil.randomDigitString(8);

            case POSTAL_CODE:
                return "139951"; // Blk 71 Ayer Rajah Crescent

            case EMAIL:
            case PARENT_EMAIL:
                return "testQA-" + StringUtil.randomString(10) + "@gmail.com";

            case UNIT_NUMBER:
                int floor = NumberUtil.randomBetween(1, 9);
                int unit = NumberUtil.randomBetween(1, 9);
                return String.format("#%02d-%02d", floor, unit);

            case MEAL_COMMENT:
            case MEAL_DESCRIPTION:
                return StringUtil.randomString(20);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
