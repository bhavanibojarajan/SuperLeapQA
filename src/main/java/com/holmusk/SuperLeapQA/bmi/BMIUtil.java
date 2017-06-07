package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.SuperLeapQA.model.Ethnicity;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by haipham on 6/6/17.
 */
public final class BMIUtil {
    /**
     * Get the invalid BMI range based on {@link Ethnicity}, {@link Gender}
     * and age.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see BMIParam#ethnicity()
     * @see Ethnicity#isAsian()
     * @see #asianInvalidRange(BMIParam)
     * @see #otherInvalidRange(BMIParam)
     */
    @NotNull
    public static Zip<Double,Double> invalidRange(@NotNull BMIParam param) {
        Ethnicity ethnicity = param.ethnicity();

        if (ethnicity.isAsian()) {
            return asianInvalidRange(param);
        } else {
            return otherInvalidRange(param);
        }
    }

    /**
     * Get the tightest invalid range, for which no matter what the user's
     * age is, the BMI should be invalid.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see #allInvalidRangesBasedOnAge(UserMode, BMIParam)
     * @see #tightestRange(Collection)
     */
    @NotNull
    public static Zip<Double,Double> tightestInvalidRange(
        @NotNull UserMode mode,
        @NotNull BMIParam param
    ) {
        return tightestRange(allInvalidRangesBasedOnAge(mode, param));
    }

    /**
     * Get the widest healthy range - any number falling out of which should
     * be considered valid BMI.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see #allInvalidRangesBasedOnAge(UserMode, BMIParam)
     * @see #widestRange(Collection)
     */
    @NotNull
    public static Zip<Double,Double> widestInvalidRange(
        @NotNull UserMode mode,
        @NotNull BMIParam param
    ) {
        return widestRange(allInvalidRangesBasedOnAge(mode, param));
    }

    /**
     * Get all available invalid {@link Zip} ranges based on
     * {@link UserMode#validAgeRange()}.
     * @param mode {@link UserMode} instance.
     * @param PARAM {@link BMIParam} instance.
     * @return {@link Collection} of {@link Zip}.
     * @see BMIParam.Builder#withAge(int)
     * @see BMIParam.Builder#withBMIParam(BMIParam)
     * @see UserMode#validAgeRange()
     */
    @NotNull
    private static Collection<Zip<Double,Double>> allInvalidRangesBasedOnAge(
        @NotNull UserMode mode,
        @NotNull final BMIParam PARAM
    ) {
        List<Integer> ages = mode.validAgeRange();

        return ages.stream()
            .map(a -> BMIParam.builder().withBMIParam(PARAM).withAge(a))
            .map(BMIParam.Builder::build)
            .map(BMIUtil::invalidRange)
            .collect(Collectors.toList());
    }

    /**
     * Get the range with the highest upper bound and lowest lower bound.
     * @param ranges {@link Collection} of {@link Zip}.
     * @return {@link Zip}.
     * @see Zip#first()
     * @see Zip#second()
     * @see Zip#of(Object, Object)
     */
    @NotNull
    private static Zip<Double,Double> tightestRange(
        @NotNull Collection<Zip<Double,Double>> ranges
    ) {
        Optional<Double> lowerOps = ranges.stream()
            .map(Zip::first)
            .max(Double::compareTo);

        Optional<Double> upperOps = ranges.stream()
            .map(Zip::second)
            .min(Double::compare);

        double lowerBound = lowerOps.orElseThrow(RuntimeException::new);
        double upperBound = upperOps.orElseThrow(RuntimeException::new);
        return Zip.of(lowerBound, upperBound);
    }

    /**
     * Get the range with the lowest lower bound and highest upper bound.
     * @param ranges {@link Collection} of {@link Zip}.
     * @return {@link Zip}.
     * @see Zip#first()
     * @see Zip#second()
     * @see Zip#of(Object, Object)
     */
    @NotNull
    private static Zip<Double,Double> widestRange(
        @NotNull Collection<Zip<Double,Double>> ranges
    ) {
        Optional<Double> lowerOps = ranges.stream()
            .map(Zip::first)
            .min(Double::compareTo);

        Optional<Double> upperOps = ranges.stream()
            .map(Zip::second)
            .max(Double::compare);

        double lowerBound = lowerOps.orElseThrow(RuntimeException::new);
        double upperBound = upperOps.orElseThrow(RuntimeException::new);
        return Zip.of(lowerBound, upperBound);
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within a certain range.
     * @param range {@link Zip} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see BMIParam#bmi()
     * @see Zip#A
     * @see Zip#B
     */
    private static boolean withinRange(@NotNull Zip<Double,Double> range,
                                       @NotNull BMIParam param) {
        double bmi = param.bmi();
        return bmi >= range.A && bmi <= range.B;
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within tightest invalid range.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #tightestInvalidRange(UserMode, BMIParam)
     * @see #withinRange(Zip, BMIParam)
     */
    public static boolean withinTightestInvalidRange(@NotNull UserMode mode,
                                                     @NotNull BMIParam param) {
        return withinRange(tightestInvalidRange(mode, param), param);
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within widest invalid range.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #widestInvalidRange(UserMode, BMIParam)
     * @see #withinRange(Zip, BMIParam)
     */
    public static boolean withinWidestInvalidRange(@NotNull UserMode mode,
                                                   @NotNull BMIParam param) {
        return withinRange(widestInvalidRange(mode, param), param);
    }

    /**
     * Antithesis of {@link #withinWidestInvalidRange(UserMode, BMIParam)}.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #withinWidestInvalidRange(UserMode, BMIParam)
     */
    public static boolean outOfWidestInvalidRange(@NotNull UserMode mode,
                                                  @NotNull BMIParam param) {
        return !withinWidestInvalidRange(mode, param);
    }

    /**
     * Get unacceptable BMI range for {@link Ethnicity#isAsian()}.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see BMIParam#age()
     * @see BMIParam#gender()
     * @see Zip#of(Object, Object)
     * @see Gender#MALE
     * @see Gender#FEMALE
     */
    @NotNull
    @SuppressWarnings({"unused", "UnusedAssignment"})
    private static Zip<Double,Double> asianInvalidRange(@NotNull BMIParam param) {
        Gender gender = param.gender();
        int age = param.age();
        double lower = 18d, upper = 23d;

        switch (age) {
            case 4:
            case 5:
            case 6:
            case 7:
                lower = 14d;
                upper = 17d;
                break;

            case 16:
                switch (gender) {
                    case MALE:
                        lower = 17d;
                        upper = 24d;
                        break;

                    case FEMALE:
                        lower = 17d;
                        upper = 25d;
                        break;

                    default:
                        break;
                }

            case 17:
                switch (gender) {
                    case MALE:
                        lower = 18d;
                        upper = 25d;
                        break;

                    case FEMALE:
                        lower = 17d;
                        upper = 25d;
                        break;

                    default:
                        break;
                }

            case 18:
                switch (gender) {
                    case MALE:
                        lower = 19d;
                        upper = 25d;
                        break;

                    case FEMALE:
                        lower = 18d;
                        upper = 26d;
                        break;

                    default:
                        break;
                }

            default:
                break;
        }

        /* The lower bound is 0 because we do not accept underweight kids */
        return Zip.of(0d, upper);
    }

    /**
     * Get healthy BMI range for other {@link Ethnicity}.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see Zip#of(Object, Object)
     */
    @NotNull
    @SuppressWarnings("unused")
    private static Zip<Double,Double> otherInvalidRange(@NotNull BMIParam param) {
        double lower = 18.5, upper = 25;
        return Zip.of(0d, upper);
    }

    private BMIUtil() {}
}
