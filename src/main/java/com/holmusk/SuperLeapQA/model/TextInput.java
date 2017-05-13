package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.InputType;
import com.holmusk.SuperLeapQA.model.type.TextInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.string.StringUtil;
import sun.swing.StringUIClientPropertyKey;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by haipham on 5/10/17.
 */
public enum TextInput implements InputType, TextInputType {
    NAME,
    EMAIL,
    PHONE;

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     * @see InputType#androidViewId()
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case NAME:
                return "et_name";

            case EMAIL:
                return "et_email";

            case PHONE:
                return "et_phone";

            default:
                return "";
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
                return "";
        }
    }
}
