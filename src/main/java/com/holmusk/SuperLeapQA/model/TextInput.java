package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.string.StringTestUtil;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.model.TextInputType;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 5/10/17.
 */
public enum TextInput implements BaseErrorType, HMTextType {
    NAME,
    EMAIL,
    PHONE,
    CHILD_NAME,
    CHILD_ID,
    MOBILE,
    PASSWORD,
    HOME,
    POSTAL_CODE,
    UNIT_NUMBER,
    PARENT_NAME,
    PARENT_EMAIL,
    PARENT_MOBILE,
    MEAL_DESCRIPTION,
    MEAL_COMMENT;

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXP(PlatformType)
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
     * @see Platform#ANDROID
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath androidInputViewXP() {
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

            case CHILD_ID:
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

            case MEAL_DESCRIPTION:
                ID = "ac_logfood_desc";
                break;

            default:
                LogUtil.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return XPath
            .builder(Platform.ANDROID)
            .containsID(ID)
            .addAnyClass()
            .build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see BaseViewType#className()
     * @see IOSView.ViewType#UI_TEXT_FIELD
     * @see IOSView.ViewType#UI_SECURE_TEXT_FIELD
     * @see Platform#IOS
     * @see org.swiften.xtestkit.kit.TestKit#localize(String)
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#containsText(XPath.ContainsText)
     * @see #iOSShortDescription()
     */
    @NotNull
    private XPath iOSInputViewXP() {
        Platform platform = Platform.IOS;

        switch (this) {
            case PASSWORD:
                return XPath.builder(platform)
                    .addClass(IOSView.ViewType.UI_SECURE_TEXT_FIELD.className())
                    .build();

            default:
                String shortDsc = iOSShortDescription();
                String localized = Config.TEST_KIT.localize(shortDsc);

                return XPath.builder(platform)
                    .containsText(localized)
                    .addAnyClass()
                    .build();
        }
    }

    /**
     * Get a short description of the input field, to use with
     * {@link #iOSInputViewXP()}. This is done so we do not have to search
     * for the input fields by their indexes.
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

            case HOME:
                return "user_title_abbv_home";

            case EMAIL:
            case PARENT_EMAIL:
                return "user_title_abbv_email";

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
     * @return {@link String} value.
     * @see TextInputType#randomInput()
     * @see StringTestUtil#randomDigitString(int)
     * @see StringTestUtil#randomString(int)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public String randomInput() {
        switch (this) {
            case NAME:
            case CHILD_NAME:
            case PARENT_NAME:
            case PASSWORD:
            case HOME:
            case UNIT_NUMBER:
                return "testQA-" + StringTestUtil.randomString(10);

            case CHILD_ID:
                return "G1235695F";

            case PHONE:
            case MOBILE:
            case PARENT_MOBILE:
                return StringTestUtil.randomDigitString(8);

            case POSTAL_CODE:
                return "139951"; // Blk 71 Ayer Rajah Crescent

            case EMAIL:
            case PARENT_EMAIL:
                return "testQA-" + StringTestUtil.randomString(10) + "@gmail.com";

            case MEAL_DESCRIPTION:
            case MEAL_COMMENT:
                return StringTestUtil.randomString(20);

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
