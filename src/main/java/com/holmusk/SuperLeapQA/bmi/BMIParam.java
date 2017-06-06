package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.HMUITestKit.model.HMUnitSystemConvertibleType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.Ethnicity;
import com.holmusk.SuperLeapQA.model.Gender;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;

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
    public static final class Builder {
        @NotNull private final BMIParam PARAM;

        Builder() {
            PARAM = new BMIParam();
        }

        /**
         * Set {@link #ethnicity} instance.
         * @param ethnicity {@link Ethnicity} instance.
         * @return The current {@link Builder} instance.
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
         * @return The current {@link Builder} instance.
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
         * @return The current {@link Builder} instance.
         * @see #age
         */
        @NotNull
        public Builder withAge(int age) {
            PARAM.age = age;
            return this;
        }

        /**
         * Set {@link #heightM}.
         * @param unit {@link UnitSystem} instance.
         * @param primary {@link Double} value.
         * @param secondary {@link Double} value.
         * @return The current {@link Builder} instance.
         * @see UnitSystem#IMPERIAL
         * @see UnitSystem#METRIC
         * @see #heightM
         */
        @NotNull
        public Builder withHeight(@NotNull UnitSystem unit,
                                  double primary,
                                  double secondary) {
            switch (unit) {
                case METRIC:
                    PARAM.heightM = (primary + secondary / 10) / 100;
                    break;

                case IMPERIAL:
                    PARAM.heightM = (primary + secondary / 12) * 30.48 / 100;
                    break;

                default:
                    break;
            }

            return this;
        }

        /**
         * Set {@link #heightM} using inputs acquired via randomization.
         * @param inputs {@link List} of {@link Zip}.
         * @param <T> Generics parameter.
         * @return The current {@link Builder} instance.
         * @see HMUnitSystemConvertibleType#unitSystem()
         * @see Zip#A
         * @see Zip#B
         * @see #withHeight(UnitSystem, double, double)
         */
        @NotNull
        public <T extends HMUnitSystemConvertibleType> Builder withHeight(
            @NotNull List<Zip<T,String>> inputs
        ) {
            if (inputs.size() > 1) {
                Zip<T,String> primary = inputs.get(0);
                Zip<T,String> secondary = inputs.get(0);
                T mode = primary.A;
                UnitSystem unit = mode.unitSystem();
                double pValue = Double.valueOf(primary.B);
                double sValue = Double.valueOf(secondary.B);
                return withHeight(unit, pValue, sValue);
            } else {
                return this;
            }
        }

        /**
         * Set {@link #weightKG} using inputs acquired via randomization.
         * @param inputs {@link List} of {@link Zip}.
         * @param <T> Generics parameter.
         * @return The current {@link Builder} instance.
         * @see HMUnitSystemConvertibleType#unitSystem()
         * @see Zip#A
         * @see Zip#B
         * @see #withWeight(UnitSystem, double, double)
         */
        @NotNull
        public <T extends HMUnitSystemConvertibleType> Builder withWeight(
            @NotNull List<Zip<T,String>> inputs
        ) {
            if (inputs.size() > 1) {
                Zip<T,String> primary = inputs.get(0);
                Zip<T,String> secondary = inputs.get(0);
                T mode = primary.A;
                UnitSystem unit = mode.unitSystem();
                double pValue = Double.valueOf(primary.B);
                double sValue = Double.valueOf(secondary.B);
                return withWeight(unit, pValue, sValue);
            } else {
                return this;
            }
        }

        /**
         * Set {@link #weightKG}.
         * @param unit {@link UnitSystem} instance.
         * @param primary {@link Double} value.
         * @param secondary {@link Double} value.
         * @return The current {@link Builder} instance.
         * @see UnitSystem#IMPERIAL
         * @see UnitSystem#METRIC
         * @see #weightKG
         */
        @NotNull
        public Builder withWeight(@NotNull UnitSystem unit,
                                  double primary,
                                  double secondary) {
            switch (unit) {
                case METRIC:
                    PARAM.weightKG = primary + secondary / 10;
                    break;

                case IMPERIAL:
                    PARAM.weightKG = (primary + secondary / 10) * 0.453d;
                    break;

                default:
                    break;
            }

            return this;
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
