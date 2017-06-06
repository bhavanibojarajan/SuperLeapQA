package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.SuperLeapQA.model.Ethnicity;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by haipham on 6/6/17.
 */
public final class BMIUtil {
    /**
     * Get the healthy BMI range based on {@link Ethnicity}, {@link Gender}
     * and age.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see BMIParam#ethnicity()
     * @see Ethnicity#isAsian()
     * @see #asianHealthyRange(BMIParam)
     * @see #otherHealthyRange(BMIParam)
     */
    @NotNull
    public static Zip<Double,Double> healthyRange(@NotNull BMIParam param) {
        Ethnicity ethnicity = param.ethnicity();

        if (ethnicity.isAsian()) {
            return asianHealthyRange(param);
        } else {
            return otherHealthyRange(param);
        }
    }

    /**
     * Get the tightest healthy range, for which no matter what the user's
     * age is, the BMI should be healthy.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see #allHealthyRangesBasedOnAge(UserMode, BMIParam)
     * @see #tightestRange(Collection)
     */
    @NotNull
    public static Zip<Double,Double> tightestHealthyRange(
        @NotNull UserMode mode,
        @NotNull BMIParam param
    ) {
        return tightestRange(allHealthyRangesBasedOnAge(mode, param));
    }

    /**
     * Get the widest healthy range - any number falling out of which should
     * be considered unhealthy BMI.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see #allHealthyRangesBasedOnAge(UserMode, BMIParam)
     * @see #widestRange(Collection)
     */
    @NotNull
    public static Zip<Double,Double> widestHealthyRange(
        @NotNull UserMode mode,
        @NotNull BMIParam param
    ) {
        return widestRange(allHealthyRangesBasedOnAge(mode, param));
    }

    /**
     * Get all available healthy {@link Zip} range based on
     * {@link UserMode#validAgeCategoryRange()}.
     * @param mode {@link UserMode} instance.
     * @param PARAM {@link BMIParam} instance.
     * @return {@link Collection} of {@link Zip}.
     * @see BMIParam.Builder#withAge(int)
     * @see BMIParam.Builder#withBMIParam(BMIParam)
     * @see UserMode#validAgeCategoryRange()
     */
    @NotNull
    private static Collection<Zip<Double,Double>> allHealthyRangesBasedOnAge(
        @NotNull UserMode mode,
        @NotNull final BMIParam PARAM
    ) {
        List<Integer> ages = mode.validAgeCategoryRange();

        return ages.stream()
            .map(a -> BMIParam.builder().withBMIParam(PARAM).withAge(a))
            .map(BMIParam.Builder::build)
            .map(BMIUtil::healthyRange)
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
     * Check whether {@link BMIParam#bmi()} is not within a certain range.
     * @param range {@link Zip} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #withinRange(Zip, BMIParam)
     */
    private static boolean notWithinRange(@NotNull Zip<Double,Double> range,
                                          @NotNull BMIParam param) {
        return !withinRange(range, param);
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within healthy range.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #healthyRange(BMIParam)
     * @see #withinRange(Zip, BMIParam)
     */
    public static boolean withinHealthyRange(@NotNull BMIParam param) {
        Zip<Double,Double> range = healthyRange(param);
        return withinRange(range, param);
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within tightest healthy range.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #tightestHealthyRange(UserMode, BMIParam)
     * @see #withinRange(Zip, BMIParam)
     */
    public static boolean withinTightestHealthyRange(@NotNull UserMode mode,
                                                     @NotNull BMIParam param) {
        return withinRange(tightestHealthyRange(mode, param), param);
    }

    /**
     * Check whether {@link BMIParam#bmi()} is within widest healthy range.
     * @param mode {@link UserMode} instance.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #widestHealthyRange(UserMode, BMIParam)
     * @see #withinRange(Zip, BMIParam)
     */
    public static boolean withinWidestHealthyRange(@NotNull UserMode mode,
                                                   @NotNull BMIParam param) {
        return withinRange(widestHealthyRange(mode, param), param);
    }

    /**
     * Get healthy BMI range for {@link Ethnicity#isAsian()}.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see BMIParam#age()
     * @see BMIParam#gender()
     * @see Gender#MALE
     * @see Gender#FEMALE
     * @see Zip#of(Object, Object)
     */
    @NotNull
    private static Zip<Double,Double> asianHealthyRange(@NotNull BMIParam param) {
        Gender gender = param.gender();
        int age = param.age();

        switch (age) {
            case 4:
            case 5:
            case 6:
            case 7:
                return Zip.of(14d, 17d);

            case 16:
                switch (gender) {
                    case MALE:
                        return Zip.of(17d, 24d);

                    case FEMALE:
                        return Zip.of(17d, 25d);

                    default:
                        break;
                }

            case 17:
                switch (gender) {
                    case MALE:
                        return Zip.of(18d, 25d);

                    case FEMALE:
                        return Zip.of(17d, 25d);

                    default:
                        break;
                }

            case 18:
                switch (gender) {
                    case MALE:
                        return Zip.of(19d, 25d);

                    case FEMALE:
                        return Zip.of(18d, 26d);

                    default:
                        break;
                }

            default:
                break;
        }

        return Zip.of(18d, 23d);
    }

    /**
     * Get healthy BMI range for other {@link Ethnicity}.
     * @param param {@link BMIParam} instance.
     * @return {@link Zip} instance.
     * @see Zip#of(Object, Object)
     */
    @NotNull
    private static Zip<Double,Double> otherHealthyRange(@NotNull BMIParam param) {
        return Zip.of(18.5d, 25d);
    }

    private BMIUtil() {}
}
