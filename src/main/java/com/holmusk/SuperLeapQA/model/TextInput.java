package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.util.LogUtil;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.string.StringUtil;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.javautilities.protocol.ClassNameType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haipham on 5/10/17.
 */
public enum TextInput implements BaseErrorType, HMTextType {
    CHILD_NAME,
    CHILD_NRIC,
    EMAIL,
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
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see Platform#ANDROID
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
                LogUtil.printlnt(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }

        Attribute attribute = attrs.containsID(ID);
        return XPath.builder().addAttribute(attribute).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @param helper {@link InputHelperType} instance.
     * @return {@link XPath} instance.
     * @see Attribute.Builder#addAttribute(String)
     * @see Attribute.Builder#withJoiner(Joiner)
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see Axes#followingSibling(AttributeType)
     * @see CompoundAttribute#forClass(ClassNameType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(ClassNameType)
     * @see Formatibles#containsString()
     * @see InputHelperType#localizer()
     * @see InputHelperType#platform()
     * @see LocalizerType#localize(String)
     * @see org.swiften.xtestkitcomponents.view.ViewType#className()
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.Type#UI_SECURE_TEXT_FIELD
     * @see IOSView.Type#UI_STATIC_TEXT
     * @see IOSView.Type#UI_TEXT_FIELD
     * @see IOSView.Type#UI_TEXT_VIEW
     * @see Joiner#OR
     * @see #iOSShortDescription()
     */
    @NotNull
    private XPath iOSInputViewXP(@NotNull InputHelperType helper) {
        final PlatformType PLATFORM = helper.platform();
        Attributes attrs = Attributes.of(PLATFORM);
        LocalizerType localizer = helper.localizer();
        String shortDsc = iOSShortDescription();
        String localized = localizer.localize(shortDsc);
        Attribute ctText = attrs.containsText(localized);

        switch (this) {
            /* For these cases, the short texts are displayed by a neighboring
             * static text label */
            case POSTAL_CODE:
            case UNIT_NUMBER:
                return XPath.builder()
                    .addAttribute(CompoundAttribute.builder()
                        .addAttribute(ctText)
                        .withClass(IOSView.Type.UI_STATIC_TEXT)
                        .build())
                    .addAttribute(Axes.followingSibling(CompoundAttribute
                        .forClass(IOSView.Type.UI_TEXT_FIELD)))
                    .build();

            default:
                List<AttributeType> clsAttrs = CollectionUtil
                    .asList(
                        IOSView.Type.UI_SECURE_TEXT_FIELD,
                        IOSView.Type.UI_TEXT_VIEW,
                        IOSView.Type.UI_TEXT_FIELD
                    )
                    .stream()
                    .map(a -> Attribute.<String>builder()
                        .addAttribute(PLATFORM.classAttribute())
                        .withValue(a.className())
                        .withFormatible(Formatibles.containsString())
                        .withJoiner(Joiner.OR)
                        .build())
                    .collect(Collectors.toList());

                AttributeBlock clsBlock = AttributeBlock.builder()
                    .addAttribute(clsAttrs)
                    .withJoiner(Joiner.OR)
                    .build();

                CompoundAttribute cAttr = CompoundAttribute.builder()
                    .addAttribute(clsBlock)
                    .addAttribute(ctText)
                    .build();

                return XPath.builder().addAttribute(cAttr).build();
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
//                String suffix = CollectionUtil.randomElement(
//                    "A",
//                    "B",
//                    "C",
//                    "D",
//                    "E",
//                    "F",
//                    "G",
//                    "H",
//                    "I",
//                    "Z",
//                    "J");
//
//                String digits  = StringUtil.randomDigitString(7);
//                return "S" + digits + suffix;
                return StringUtil.randomDigitString(7);

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
