package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by haipham on 5/13/17.
 */
public enum UserMode implements ValueRangeConverterType<Integer> {
    PARENT,
    TEEN;

    @NotNull
    @Override
    public Converter<Integer> converter() {
        return a -> Double.valueOf(a).intValue();
    }

    /**
     * Get the sign up button's id for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @return A {@link String} value.
     */
    @NotNull
    public String androidSignUpButtonId() {
        switch (this) {
            case PARENT:
                return "btnRegChild";

            case TEEN:
                return "btnRegSelf";

            default:
                return "";
        }
    }

    /**
     * Get the personal information inputs for this {@link UserMode}.
     * @return A {@link List} of {@link InputType}.
     * @see Arrays#asList(Object[])
     * @see Collections#emptyList()
     */
    @NotNull
    public List<InputType> personalInformation() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.CHILD_NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.HOME
                );

            case TEEN:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.HOME
                );

            default:
                return Collections.emptyList();
        }
    }

    /**
     * Get additional personal inputs, esp. for {@link UserMode#TEEN} since
     * users will need to enter parent's information as well.
     * @return A {@link List} of {@link InputType}.
     * @see Arrays#asList(Object[])
     * @see Collections#emptyList()
     */
    @NotNull
    public List<InputType> extraPersonalInformation() {
        switch (this) {
            case TEEN:
                return Arrays.asList(
                    TextInput.PARENT_NAME,
                    TextInput.PARENT_MOBILE,
                    TextInput.PARENT_EMAIL
                );

            default:
                return Collections.emptyList();
        }
    }

    /**
     * Get the minimum acceptable age for the current {@link UserMode}.
     * @return An {@link Integer} value.
     */
    public int minAcceptableAge() {
        switch (this) {
            case PARENT:
                return 5;

            case TEEN:
                return 16;

            default:
                return 0;
        }
    }

    /**
     * Get the maximum acceptable age for the current {@link UserMode}.
     * @return An {@link Integer} value.
     */
    public int maxAcceptableAge() {
        switch (this) {
            case PARENT:
                return 6;

            case TEEN:
                return 19;

            default:
                return 0;
        }
    }

    /**
     * Get the acceptable age range for the current sign up mode.
     * @return A {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minAcceptableAge()
     * @see #maxAcceptableAge()
     */
    @NotNull
    public List<Integer> acceptableAgeRange() {
        int minAge = minAcceptableAge();
        int maxAge = maxAcceptableAge();
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get an age range with lower/upper bounds that are lower/higher than
     * the min/max age by an offset value.
     * @param offset An {@link Integer} value.
     * @return A {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minAcceptableAge()
     * @see #maxAcceptableAge()
     */
    @NotNull
    public List<Integer> ageOffsetFromAcceptableRange(int offset) {
        int minAge = minAcceptableAge() - offset;
        int maxAge = maxAcceptableAge() + offset;
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get the {@link String} representation of the acceptable age range.
     * @return A {@link String} value.
     * @see #acceptableAgeRange()
     * @see #minAcceptableAge()
     * @see #maxAcceptableAge()
     */
    @NotNull
    public String acceptableAgeRangeString() {
        int minAge = minAcceptableAge();
        int maxAge = maxAcceptableAge();
        return String.format("%1$d-%2$d", minAge, maxAge);
    }
}
