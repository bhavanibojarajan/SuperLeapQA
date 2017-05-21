package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum UserMode implements BaseErrorType, ValueRangeConverterType<Integer> {
    PARENT,
    TEEN_U18,
    TEEN_A18;

    @NotNull
    @Override
    public Converter<Integer> converter() {
        return a -> Double.valueOf(a).intValue();
    }

    /**
     * Check if the current {@link UserMode} is in the teen category.
     * @return {@link Boolean} instance.
     * @see #TEEN_U18
     * @see #TEEN_A18
     */
    public boolean isTeen() {
        switch (this) {
            case TEEN_A18:
            case TEEN_U18:
                return true;

            default:
                return false;
        }
    }

    /**
     * Check if the current {@link UserMode} is in the parent category.
     * @return {@link Boolean} instance.
     * @see #PARENT
     */
    public boolean isParent() {
        return this.equals(PARENT);
    }

    /**
     * Get the personal information inputs for this {@link UserMode}.
     * @return {@link List} of {@link SLInputType}.
     */
    @NotNull
    public List<SLInputType> personalInformation() {
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

            case TEEN_U18:
            case TEEN_A18:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.HOME
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get additional personal inputs, esp. for {@link UserMode#TEEN_U18} since
     * users will need to enter parent's information as well.
     * @return {@link List} of {@link SLInputType}.
     * @see Arrays#asList(Object[])
     * @see Collections#emptyList()
     */
    @NotNull
    public List<SLInputType> extraPersonalInformation() {
        switch (this) {
            case TEEN_U18:
            case TEEN_A18:
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
     * Check whether the current use requires guarantor information. This
     * is only applicable for {@link #TEEN_U18}.
     * @return {@link Boolean} value.
     */
    public boolean requiresGuarantor() {
        switch (this) {
            case TEEN_U18:
                return true;

            default:
                return false;
        }
    }

    /**
     * Get the minimum acceptable age for the current {@link UserMode}.
     * @return {@link Integer} value.
     */
    public int minAcceptableAge() {
        switch (this) {
            case PARENT:
                return 5;

            case TEEN_U18:
                return 16;

            case TEEN_A18:
                return 18;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the maximum acceptable age for the current {@link UserMode}.
     * @return {@link Integer} value.
     */
    public int maxAcceptableAge() {
        switch (this) {
            case PARENT:
                return 6;

            case TEEN_U18:
                return 17;

            case TEEN_A18:
                return 19;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the minimum acceptable age for the current {@link UserMode}'s
     * category. This is different from {@link #minAcceptableAge()} because
     * {@link #TEEN_U18} and {@link #TEEN_A18} technically have
     * the same {@link #minAcceptableAge()} and {@link #maxAcceptableAge()}
     * from the application's perspective.
     * @return {@link Integer} value.
     * @see #maxAcceptableAge()
     */
    public int minCategoryAcceptableAge() {
        switch (this) {
            case PARENT:
                return minAcceptableAge();

            case TEEN_A18:
            case TEEN_U18:
                return TEEN_U18.minAcceptableAge();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the maximum acceptable age for the current {@link UserMode}'s
     * category. This is different from {@link #maxAcceptableAge()} because
     * {@link #TEEN_U18} and {@link #TEEN_A18} technically have
     * the same {@link #minAcceptableAge()} and {@link #maxAcceptableAge()}
     * from the application's perspective.
     * @return {@link Integer} value.
     * @see #maxAcceptableAge()
     */
    public int maxCategoryAcceptableAge() {
        switch (this) {
            case PARENT:
                return maxAcceptableAge();

            case TEEN_A18:
            case TEEN_U18:
                return TEEN_A18.maxAcceptableAge();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the acceptable age range for the current {@link UserMode}.
     * @return {@link List} of {@link Integer}.
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
     * Get the acceptable age category range for the current {@link UserMode}.
     * @return {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minCategoryAcceptableAge()
     * @see #maxCategoryAcceptableAge()
     */
    @NotNull
    public List<Integer> acceptableAgeCategoryRange() {
        int minAge = minCategoryAcceptableAge();
        int maxAge = maxCategoryAcceptableAge();
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get an age range with lower/upper bounds that are lower/higher than
     * the min/max age by an offset value. Note that this does not take
     * into account {@link #requiresGuarantor()}, so the ranges for
     * {@link #TEEN_A18} and {@link #TEEN_U18} are the same.
     * @param offset {@link Integer} value.
     * @return {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minCategoryAcceptableAge()
     * @see #maxCategoryAcceptableAge()
     */
    @NotNull
    public List<Integer> offsetFromCategoryAcceptableRange(int offset) {
        int minAge = minCategoryAcceptableAge() - offset;
        int maxAge = maxCategoryAcceptableAge() + offset;
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get the {@link String} representation of the acceptable age range.
     * Note that this uses the {@link #minCategoryAcceptableAge()} and
     * {@link #maxCategoryAcceptableAge()} because {@link #TEEN_U18}
     * and {@link #TEEN_A18} are technically in the same category.
     * @return {@link String} value.
     * @see #acceptableAgeRange()
     * @see #minCategoryAcceptableAge()
     * @see #maxCategoryAcceptableAge()
     */
    @NotNull
    public String acceptableAgeCategoryRangeString() {
        int minAge = minCategoryAcceptableAge();
        int maxAge = maxCategoryAcceptableAge();
        return String.format("%1$d-%2$d", minAge, maxAge);
    }

    /**
     * Produce a random acceptable age.
     * @return {@link Integer} value.
     * @see CollectionTestUtil#randomElement(List)
     * @see #acceptableAgeRange()
     */
    public int randomAcceptableAge() {
        return CollectionTestUtil.randomElement(acceptableAgeRange());
    }

    /**
     * Get the id of the register button for the current {@link UserMode} on
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @return {@link String} value.
     */
    @NotNull
    public String androidRegisterButtonId() {
        switch (this) {
            case PARENT:
                return "btnRegChild";

            case TEEN_A18:
            case TEEN_U18:
                return "btnRegSelf";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
