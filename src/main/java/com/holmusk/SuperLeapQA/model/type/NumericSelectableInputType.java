package com.holmusk.SuperLeapQA.model.type;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.List;

/**
 * Created by haipham on 5/12/17.
 */

/**
 * This interface provides methods to convert between numeric input values
 * and their {@link String} equivalents.
 */
public interface NumericSelectableInputType extends ValueRangeConverterType<Double> {
    @NotNull
    @Override
    default Converter<Double> converter() {
        return a -> Double.valueOf(String.format("%.2f", a));
    }

    /**
     * Convert a {@link String} value into a numeric value.
     * @param value A {@link Double} value.
     * @return A {@link Double} value.
     */
    double numericValue(@NotNull String value);

    /**
     * Get the numeric value's {@link String} representation.
     * @param value A {@link Double} value.
     * @return A {@link String} value.
     */
    @NotNull
    String stringValue(double value);

    /**
     * Get the minimum numeric value that can be selected.
     * @return A {@link Double} value.
     */
    double minSelectableNumericValue();

    /**
     * Get the maximum numeric value that can be selected.
     * @return A {@link Double} value.
     */
    double maxSelectableNumericValue();

    /**
     * Get the difference between two consecutive numeric values.
     * @return A {@link Double} value.
     */
    double selectableNumericValueStep();

    /**
     * Get the selectable numeric range.
     * @return A {@link List} of {@link Double}.
     * @see #valueRange(Number, Number, Number)
     * @see #minSelectableNumericValue()
     * @see #maxSelectableNumericValue()
     * @see #selectableNumericValueStep()
     */
    @NotNull
    default List<Double> selectableNumericRange() {
        return valueRange(
            minSelectableNumericValue(),
            maxSelectableNumericValue(),
            selectableNumericValueStep()
        );
    }

    /**
     * Get a random numeric value from {@link #selectableNumericRange()}.
     * @return A {@link Double} value.
     * @see #selectableNumericRange()
     */
    default double randomSelectableNumericValue() {
        List<Double> selectableRange = selectableNumericRange();
        return CollectionTestUtil.randomElement(selectableRange);
    }
}
