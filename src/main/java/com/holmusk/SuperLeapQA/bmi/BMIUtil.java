package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.SuperLeapQA.model.Ethnicity;
import com.holmusk.SuperLeapQA.model.Gender;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;

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
     * Check whether {@link BMIParam#bmi()} is within healthy range.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see BMIParam#bmi()
     * @see Zip#A
     * @see Zip#B
     * @see #healthyRange(BMIParam)
     */
    public static boolean withinHealthyRange(@NotNull BMIParam param) {
        Zip<Double,Double> healthyRange = healthyRange(param);
        double bmi = param.bmi();
        return bmi >= healthyRange.A && bmi <= healthyRange.B;
    }

    /**
     * Check if {@link BMIParam#bmi()} is not within healthy range.
     * @param param {@link BMIParam} instance.
     * @return {@link Boolean} value.
     * @see #withinHealthyRange(BMIParam)
     */
    public static boolean notWithinHealthyRange(@NotNull BMIParam param) {
        return !withinHealthyRange(param);
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

        return Zip.of(185d, 23d);
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
