package com.holmusk.SuperLeapQA.bmi;

import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;

import java.util.Collections;
import java.util.List;

/**
 * Created by haipham on 29/6/17.
 */
public final class BMIResult implements ErrorProviderType {
    /**
     * Get new {@link Builder} instance.
     * @return {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Nullable private BMIParam requestParam;
    @NotNull private List<Tuple<Height,String>> height;
    @NotNull private List<Tuple<Weight,String>> weight;

    private BMIResult() {
        height = Collections.emptyList();
        weight = Collections.emptyList();
    }

    /**
     * Get {@link #requestParam}.
     * @return {@link BMIParam} instance.
     */
    @NotNull
    public BMIParam requestParam() {
        if (HPObjects.nonNull(requestParam)) {
            return requestParam;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link #height}.
     * @return {@link List} of {@link Tuple}.
     */
    @NotNull
    public List<Tuple<Height,String>> height() {
        return height;
    }

    /**
     * Get {@link #weight}.
     * @return {@link List} of {@link Tuple}.
     */
    @NotNull
    public List<Tuple<Weight,String>> weight() {
        return weight;
    }

    /**
     * Builder class for {@link BMIResult}.
     */
    public static final class Builder {
        @NotNull private BMIResult RESULT;

        Builder() {
            RESULT = new BMIResult();
        }

        /**
         * Set {@link #requestParam} instance.
         * @param param {@link BMIParam} instance.
         * @return {@link Builder} instance.
         */
        @NotNull
        public Builder withRequestParam(@NotNull BMIParam param) {
            RESULT.requestParam = param;
            return this;
        }

        /**
         * Set {@link #height} instance.
         * @param height {@link List} of {@link Tuple}.
         * @return {@link Builder} instance.
         */
        @NotNull
        public Builder withHeight(@NotNull List<Tuple<Height,String>> height) {
            RESULT.height = height;
            return this;
        }

        /**
         * Set {@link #weight} instance.
         * @param weight {@link List} of {@link Tuple}.
         * @return {@link Builder} instance.
         */
        @NotNull
        public Builder withWeight(@NotNull List<Tuple<Weight,String>> weight) {
            RESULT.weight = weight;
            return this;
        }

        /**
         * Get {@link #RESULT}.
         * @return {@link BMIResult} instance.
         */
        @NotNull
        public BMIResult build() {
            return RESULT;
        }
    }
}
