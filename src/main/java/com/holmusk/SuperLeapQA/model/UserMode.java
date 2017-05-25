package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.util.ValueRangeConverterType;

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
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link SLInputType}.
     * @see Platform#ANDROID
     * @see Platform#IOS
     * @see #androidPersonalInfo()
     * @see #iOSPersonalInfo()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public List<SLInputType> personalInfo(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidPersonalInfo();

            case IOS:
                return iOSPersonalInfo();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get personal info {@link SLInputType} for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @return {@link List} of {@link SLInputType}.
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private List<SLInputType> androidPersonalInfo() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.CHILD_NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.POSTAL_CODE,
                    TextInput.UNIT_NUMBER
                );

            case TEEN_U18:
            case TEEN_A18:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.HOME,
                    TextInput.POSTAL_CODE,
                    TextInput.UNIT_NUMBER
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get personal info {@link SLInputType} for
     * {@link org.swiften.xtestkit.mobile.Platform#IOS}.
     * @return {@link List} of {@link SLInputType}.
     * @see #NOT_AVAILABLE
     */
    private List<SLInputType> iOSPersonalInfo() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.CHILD_NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.POSTAL_CODE,
                    TextInput.UNIT_NUMBER
                );

            case TEEN_U18:
            case TEEN_A18:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD,
                    TextInput.POSTAL_CODE,
                    TextInput.UNIT_NUMBER
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get additional personal inputs, esp. for {@link UserMode#TEEN_U18} since
     * users will need to enter parent's information as well.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link SLInputType}.
     * @see Arrays#asList(Object[])
     * @see Collections#emptyList()
     */
    @NotNull
    public List<SLInputType> extraInfo(@NotNull PlatformType platform) {
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
    public int minValidAge() {
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
    public int maxValidAge() {
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
     * category. This is different from {@link #minValidAge()} because
     * {@link #TEEN_U18} and {@link #TEEN_A18} technically have
     * the same {@link #minValidAge()} and {@link #maxValidAge()}
     * from the application's perspective.
     * @return {@link Integer} value.
     * @see #maxValidAge()
     */
    public int minCategoryValidAge() {
        switch (this) {
            case PARENT:
                return minValidAge();

            case TEEN_A18:
            case TEEN_U18:
                return TEEN_U18.minValidAge();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the maximum acceptable age for the current {@link UserMode}'s
     * category. This is different from {@link #maxValidAge()} because
     * {@link #TEEN_U18} and {@link #TEEN_A18} technically have
     * the same {@link #minValidAge()} and {@link #maxValidAge()}
     * from the application's perspective.
     * @return {@link Integer} value.
     * @see #maxValidAge()
     */
    public int maxCategoryValidAge() {
        switch (this) {
            case PARENT:
                return maxValidAge();

            case TEEN_A18:
            case TEEN_U18:
                return TEEN_A18.maxValidAge();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the acceptable age range for the current {@link UserMode}.
     * @return {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minValidAge()
     * @see #maxValidAge()
     */
    @NotNull
    public List<Integer> validAgeRange() {
        int minAge = minValidAge();
        int maxAge = maxValidAge();
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get the acceptable age category range for the current {@link UserMode}.
     * @return {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minCategoryValidAge()
     * @see #maxCategoryValidAge()
     */
    @NotNull
    public List<Integer> validAgeCategoryRange() {
        int minAge = minCategoryValidAge();
        int maxAge = maxCategoryValidAge();
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
     * @see #minCategoryValidAge()
     * @see #maxCategoryValidAge()
     */
    @NotNull
    public List<Integer> offsetFromCategoryValidRange(int offset) {
        int minAge = minCategoryValidAge() - offset;
        int maxAge = maxCategoryValidAge() + offset;
        return valueRange(minAge, maxAge + 1, 1);
    }

    /**
     * Get the {@link String} representation of the acceptable age range.
     * Note that this uses the {@link #minCategoryValidAge()} and
     * {@link #maxCategoryValidAge()} because {@link #TEEN_U18}
     * and {@link #TEEN_A18} are technically in the same category.
     * @return {@link String} value.
     * @see #validAgeRange()
     * @see #minCategoryValidAge()
     * @see #maxCategoryValidAge()
     */
    @NotNull
    public String validAgeCategoryRangeString() {
        int minAge = minCategoryValidAge();
        int maxAge = maxCategoryValidAge();
        return String.format("%1$d-%2$d", minAge, maxAge);
    }

    /**
     * Get the register butto text for the current {@link UserMode} for
     * {@link Screen#REGISTER}.
     * @return {@link String} value.
     * @see Screen#REGISTER
     */
    @NotNull
    public String registerButtonText() {
        switch (this) {
            case PARENT:
                return "register_title_iAmParent";

            case TEEN_A18:
            case TEEN_U18:
                return "register_title_iAmTeen";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
