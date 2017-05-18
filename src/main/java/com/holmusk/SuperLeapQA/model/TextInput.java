package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

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
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     * @see AndroidInputType#androidViewId()
     */
    @NotNull
    @Override
    public String androidViewId() {
        switch (this) {
            case NAME:
            case PARENT_NAME:
                return "et_name";

            case EMAIL:
            case PARENT_EMAIL:
                return "et_email";

            case PHONE:
                return "et_phone";

            case CHILD_NAME:
                return "et_childname";

            case MOBILE:
            case PARENT_MOBILE:
                return "et_mobile";

            case PASSWORD:
                return "et_password";

            case HOME:
                return "et_home";

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get a random text input.
     * @return A {@link String} value.
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
                throw new RuntimeException(NOT_IMPLEMENTED);
        }
    }

    /**
     * @return A {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.empty();
    }
}
