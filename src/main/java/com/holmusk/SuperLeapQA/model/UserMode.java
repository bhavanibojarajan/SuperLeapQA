package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.util.ValueRangeConverterType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum UserMode implements ErrorProviderType, ValueRangeConverterType<Integer> {
    PARENT,
    TEEN_U18,
    TEEN_A18;

    /**
     * Get the default {@link UserMode}.
     * @return {@link UserMode} instance.
     */
    @NotNull
    public static UserMode defaultUserMode() {
        return PARENT;
    }

    /**
     * Get the default {@link UserMode} that is {@link UserMode#isTeen()}.
     * @return {@link UserMode} instance.
     */
    @NotNull
    public static UserMode defaultTeenUserMode() {
        return TEEN_A18;
    }

    @NotNull
    @Override
    public Converter<Integer> converter() {
        return a -> Double.valueOf(a).intValue();
    }

    /**
     * Check if the current {@link UserMode} is in the teen category.
     * @return {@link Boolean} instance.
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
     * Check whether the current {@link UserMode} requires BMI check.
     * @return {@link Boolean} value.
     * @see #isTeen()
     */
    public boolean requiresBMICheck() {
        return isTeen();
    }

    /**
     * Check if the current {@link UserMode} is in the parent category.
     * @return {@link Boolean} instance.
     */
    public boolean isParent() {
        return this.equals(PARENT);
    }

    /**
     * Get the default login credentials for the current {@link UserMode}.
     * @return {@link List} of {@link Tuple}.
     */
    @NotNull
    public List<Tuple<HMTextType,String>> loginCredentials() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    Tuple.of(TextInput.EMAIL, "haipham-parent@gmail.com"),
                    Tuple.of(TextInput.PASSWORD, "12345678")
                );

            case TEEN_A18:
            case TEEN_U18:
                return Arrays.asList(
                    Tuple.of(TextInput.EMAIL, "haipham-teen@gmail.com"),
                    Tuple.of(TextInput.PASSWORD, "12345678")
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the {@link HMTextType} inputs for the current {@link UserMode}.
     * When the user is {@link UserMode#isParent()}, he/she also needs to
     * enter the child's name and NRIC to be verified.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link HMTextType}.
     */
    @NotNull
    public List<HMTextType> validAgeInfo(@NotNull PlatformType platform) {
        if (isParent()) {
            return HPIterables.asList(
                TextInput.CHILD_NAME,
                TextInput.CHILD_NRIC
            );
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Get the personal information inputs for this {@link UserMode}.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link HMInputType}.
     * @see #androidPersonalInfo()
     * @see #iOSPersonalInfo()
     */
    @NotNull
    public List<HMTextType> personalInfo(@NotNull PlatformType platform) {
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
     * Get personal info {@link HMInputType} for
     * {@link Platform#ANDROID}.
     * @return {@link List} of {@link HMInputType}.
     */
    @NotNull
    private List<HMTextType> androidPersonalInfo() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD
                );

            case TEEN_U18:
            case TEEN_A18:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.NRIC,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get personal info {@link HMInputType} for
     * {@link Platform#IOS}.
     * @return {@link List} of {@link HMInputType}.
     */
    private List<HMTextType> iOSPersonalInfo() {
        switch (this) {
            case PARENT:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD
                );

            case TEEN_U18:
            case TEEN_A18:
                return Arrays.asList(
                    TextInput.NAME,
                    TextInput.NRIC,
                    TextInput.MOBILE,
                    TextInput.EMAIL,
                    TextInput.PASSWORD
                );

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get additional personal inputs, esp. for {@link UserMode#TEEN_U18} since
     * users will need to enter parent's information as well.
     * @param platform {@link PlatformType} instance.
     * @return {@link List} of {@link HMInputType}.
     */
    @NotNull
    public List<HMTextType> guarantorInfo(@NotNull PlatformType platform) {
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
     * Get {@link DashboardMode#ACTIVITY} keyword to be searched, because
     * each {@link UserMode} group has a different activity metric.
     * @return {@link String} value.
     */
    @NotNull
    public String dashboardActivityKeyword() {
        switch (this) {
            case PARENT:
                return "dashboard_activity_title_mins";

            case TEEN_A18:
            case TEEN_U18:
                return "dashboard_activity_title_steps";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
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
     * @see #minValidAge()
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
     * @param lower {@link Integer} value.
     * @param upper {@link Integer} value.
     * @return {@link List} of {@link Integer}.
     * @see #valueRange(Number, Number, Number)
     * @see #minCategoryValidAge()
     * @see #maxCategoryValidAge()
     */
    @NotNull
    public List<Integer> offsetFromCategoryValidRange(int lower, int upper) {
        int minAge = minCategoryValidAge() - lower;
        int maxAge = maxCategoryValidAge() + upper;
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
