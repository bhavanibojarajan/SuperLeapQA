package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.model.TextInputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by haipham on 5/10/17.
 */
public enum TextInput implements BaseErrorType, SLTextInputType {
    NAME,
    EMAIL,
    PHONE,
    CHILD_NAME,
    MOBILE,
    PASSWORD,
    HOME,
    PARENT_NAME,
    PARENT_EMAIL,
    PARENT_MOBILE;

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see InputType#inputViewXPath(PlatformType)
     * @see #androidInputViewXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXPath();

            case IOS:
                return iOSInputViewXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXPath() {
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

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return XPath.builder(Platform.ANDROID).containsID(ID).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see org.swiften.xtestkit.kit.TestKit#localize(String)
     * @see IOSView.ViewType#UI_TEXTFIELD
     * @see #iOSShortDescription()
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        String shortDescription = iOSShortDescription();
        String localized = Config.TEST_KIT.localize(shortDescription);

        return XPath.builder(Platform.IOS)
            .setClass(IOSView.ViewType.UI_TEXTFIELD.className())
            .containsText(localized)
            .build();
    }

    /**
     * Get a short description of the input field, to use with
     * {@link #iOSInputViewXPath()}. This is done so we do not have to search
     * for the input fields by their indexes.
     * @return {@link String} value.
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private String iOSShortDescription() {
        switch (this) {
            case NAME:
                return "input_title_abbv_name";

            case PHONE:
            case MOBILE:
            case PARENT_MOBILE:
                return "input_title_abbv_mobile";

            case EMAIL:
            case PARENT_EMAIL:
                return "input_title_abbv_email";

            default:
                LogUtil.println(this);
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get a random text input.
     * @return {@link String} value.
     * @see TextInputType#randomInput()
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
                String baseName = String.join("", IntStream.range(0, 10)
                    .boxed()
                    .map(a -> IntStream.range(97, 123))
                    .map(IntStream::boxed)
                    .map(a -> a.map(b -> (char)(int)b))
                    .map(Stream::toArray)
                    .map(Arrays::asList)
                    .map(CollectionTestUtil::randomElement)
                    .map(String::valueOf)
                    .toArray(String[]::new));

                return "testQA-" + baseName;

            case PHONE:
            case MOBILE:
            case PARENT_MOBILE:
                return String.join("", IntStream.range(0, 8)
                    .boxed()
                    .map(a -> IntStream.range(0, 10))
                    .map(IntStream::boxed)
                    .map(Stream::toArray)
                    .map(Arrays::asList)
                    .map(CollectionTestUtil::randomElement)
                    .map(String::valueOf)
                    .toArray(String[]::new));

            case EMAIL:
            case PARENT_EMAIL:
                String baseEmail = String.join("", IntStream.range(0, 10)
                    .boxed()
                    .map(a -> IntStream.range(97, 123))
                    .map(IntStream::boxed)
                    .map(a -> a.map(b -> (char)(int)b))
                    .map(Stream::toArray)
                    .map(CollectionTestUtil::randomElement)
                    .map(String::valueOf)
                    .toArray(String[]::new));

                return "testQA-" + baseEmail + "@gmail.com";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @return {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.empty();
    }
}
