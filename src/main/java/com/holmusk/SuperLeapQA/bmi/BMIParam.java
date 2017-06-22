package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.HMUITestKit.model.HMUnitSystemConvertibleType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.Ethnicity;
import com.holmusk.SuperLeapQA.model.Gender;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkitcomponents.common.BaseErrorType;

import java.util.List;

/**
 * Created by haipham on 6/6/17.
 */
public final class BMIParam {
    /**
     * Get {@link Builder} instance.
     * @return {@link Builder} instance.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull private Ethnicity ethnicity;
    @NotNull private Gender gender;
    private double heightM, weightKG;
    private int age;

    BMIParam() {
        ethnicity = Ethnicity.ASIAN_OTHER;
        gender = Gender.MALE;
    }

    /**
     * Get {@link #ethnicity}.
     * @return {@link Ethnicity} instance.
     * @see #ethnicity
     */
    @NotNull
    public Ethnicity ethnicity() {
        return ethnicity;
    }

    /**
     * Get {@link #gender}.
     * @return {@link Gender} instance.
     * @see #gender
     */
    @NotNull
    public Gender gender() {
        return gender;
    }

    /**
     * Get {@link #age}.
     * @return {@link Integer} value.
     * @see #age
     */
    public int age() {
        return age;
    }

    /**
     * Get {@link #heightM}.
     * @return {@link Double} value.
     * @see #heightM
     */
    public double heightM() {
        return heightM;
    }

    /**
     * Get {@link #weightKG}.
     * @return {@link Double} value.
     * @see #weightKG
     */
    public double weightKG() {
        return weightKG;
    }

    /**
     * Calculate the BMI.
     * @return {@link Double} value.
     * @see Math#pow(double, double)
     * @see #heightM()
     * @see #weightKG()
     */
    public double bmi() {
        return weightKG() / Math.pow(heightM(), 2);
    }

    /**
     * Builder class for {@link BMIParam}.
     */
    public static final class Builder implements BaseErrorType {
        @NotNull private final BMIParam PARAM;

        Builder() {
            PARAM = new BMIParam();
        }

        /**
         * Set {@link #ethnicity} instance.
         * @param ethnicity {@link Ethnicity} instance.
         * @return {@link Builder} instance.
         * @see #ethnicity
         */
        @NotNull
        public Builder withEthnicity(@NotNull Ethnicity ethnicity) {
            PARAM.ethnicity = ethnicity;
            return this;
        }

        /**
         * Set {@link #gender} instance.
         * @param gender {@link Gender} instance.
         * @return {@link Builder} instance.
         * @see #gender
         */
        @NotNull
        public Builder withGender(@NotNull Gender gender) {
            PARAM.gender = gender;
            return this;
        }

        /**
         * Set {@link #age}.
         * @param age {@link Integer} value.
         * @return {@link Builder} instance.
         * @see #age
         */
        @NotNull
        public Builder withAge(int age) {
            PARAM.age = age;
            return this;
        }

        /**
         * Set the {@link #heightM} value.
         * @param heightM {@link Double} value.
         * @return {@link Builder} instance.
         * @see #heightM
         */
        @NotNull
        public Builder withHeight(double heightM) {
            PARAM.heightM = heightM;
            return this;
        }

        /**
         * Set the {@link #weightKG} value.
         * @param weightKG {@link Double} value.
         * @return {@link Builder} instance.
         * @see #weightKG
         */
        @NotNull
        public Builder withWeight(double weightKG) {
            PARAM.weightKG = weightKG;
            return this;
        }

        /**
         * Set {@link #heightM}.
         * @param unit {@link UnitSystem} instance.
         * @param primary {@link Double} value.
         * @param secondary {@link Double} value.
         * @return {@link Builder} instance.
         * @see UnitSystem#IMPERIAL
         * @see UnitSystem#METRIC
         * @see #withHeight(UnitSystem, double, double)
         */
        @NotNull
        public Builder withHeight(@NotNull UnitSystem unit,
                                  double primary,
                                  double secondary) {
            switch (unit) {
                case METRIC:
                    return withHeight((primary + secondary / 10) / 100);

                case IMPERIAL:
                    return withHeight((primary + secondary / 12) * 30.48 / 100);

                default:
                    return this;
            }
        }

        /**
         * Set {@link #heightM} using inputs acquired via randomization.
         * @param inputs {@link List} of {@link Zip}.
         * @param <T> Generics parameter.
         * @return {@link Builder} instance.
         * @see HMUnitSystemConvertibleType#unitSystem()
         * @see ObjectUtil#nonNull(Object)
         * @see Zip#A
         * @see Zip#B
         * @see #withHeight(UnitSystem, double, double)
         * @see #NOT_AVAILABLE
         */
        @NotNull
        public <T extends HMUnitSystemConvertibleType> Builder withHeight(
            @NotNull List<Zip<T,String>> inputs
        ) {
            int size = inputs.size();

            if (size > 0) {
                Zip<T,String> primary = inputs.get(0);
                T mode = primary.A;
                UnitSystem unit = mode.unitSystem();
                double primaryValue = Double.valueOf(primary.B);

                double secondaryValue;

                if (size > 1) {
                    Zip<T, String> secondary = inputs.get(1);
                    secondaryValue = Double.valueOf(secondary.B);
                } else {
                    secondaryValue = 0d;
                }

                return withHeight(unit, primaryValue, secondaryValue);
            } else {
                throw new RuntimeException(NOT_AVAILABLE);
            }
        }

        /**
         * Set {@link #weightKG}.
         * @param unit {@link UnitSystem} instance.
         * @param primary {@link Double} value.
         * @param secondary {@link Double} value.
         * @return {@link Builder} instance.
         * @see UnitSystem#IMPERIAL
         * @see UnitSystem#METRIC
         * @see #withWeight(double)
         */
        @NotNull
        public Builder withWeight(@NotNull UnitSystem unit,
                                  double primary,
                                  double secondary) {
            switch (unit) {
                case METRIC:
                    return withWeight(primary + secondary / 10);

                case IMPERIAL:
                    return withWeight((primary + secondary / 10) * 0.453d);

                default:
                    return this;
            }
        }

        /**
         * Set {@link #weightKG} using inputs acquired via randomization.
         * @param inputs {@link List} of {@link Zip}.
         * @param <T> Generics parameter.
         * @return {@link Builder} instance.
         * @see HMUnitSystemConvertibleType#unitSystem()
         * @see ObjectUtil#nonNull(Object)
         * @see Zip#A
         * @see Zip#B
         * @see #withWeight(UnitSystem, double, double)
         * @see #NOT_AVAILABLE
         */
        @NotNull
        public <T extends HMUnitSystemConvertibleType> Builder withWeight(
            @NotNull List<Zip<T,String>> inputs
        ) {
            int size = inputs.size();

            if (size > 0) {
                Zip<T,String> primary = inputs.get(0);
                T mode = primary.A;
                UnitSystem unit = mode.unitSystem();
                double primaryValue = Double.valueOf(primary.B);

                double secondaryValue;

                if (size > 1) {
                    Zip<T, String> secondary = inputs.get(1);
                    secondaryValue = Double.valueOf(secondary.B);
                } else {
                    secondaryValue = 0d;
                }

                return withWeight(unit, primaryValue, secondaryValue);
            } else {
                throw new RuntimeException(NOT_AVAILABLE);
            }
        }

        /**
         * Copy properties from another {@link BMIParam}.
         * @param param {@link BMIParam} instance.
         * @return {@link Builder} instance.
         * @see BMIParam#age()
         * @see BMIParam#ethnicity()
         * @see BMIParam#gender()
         * @see BMIParam#heightM()
         * @see BMIParam#weightKG()
         * @see #withAge(int)
         * @see #withEthnicity(Ethnicity)
         * @see #withGender(Gender)
         * @see #withHeight(double)
         * @see #withWeight(double)
         */
        @NotNull
        public Builder withBMIParam(@NotNull BMIParam param) {
            return this
                .withHeight(param.heightM())
                .withWeight(param.weightKG())
                .withAge(param.age())
                .withEthnicity(param.ethnicity())
                .withGender(param.gender());
        }

        /**
         * Get {@link #PARAM}.
         * @return {@link BMIParam} instance.
         * @see #PARAM
         */
        @NotNull
        public BMIParam build() {
            return PARAM;
        }
    }
}
