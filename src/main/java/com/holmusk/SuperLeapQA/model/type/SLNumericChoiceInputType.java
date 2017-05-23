package com.holmusk.SuperLeapQA.model.type;

import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.util.type.ValueRangeConverterType;

import java.util.List;
import java.util.Optional;

/**
 * Created by haipham on 18/5/17.
 */
public interface SLNumericChoiceInputType extends SLChoiceInputType, ValueRangeConverterType<Integer> {
    /**
     * Get an error message format that can be used with
     * {@link #minSelectableNumericValue(UserMode)} and
     * {@link #maxSelectableNumericValue(UserMode)} to create an error message.
     * @return {@link String} value.
     */
    @NotNull
    String emptyInputErrorFormat();

    /**
     * We provide a default implementation to make use of max and min values.
     * @param mode {@link UserMode} instance.
     * @return {@link org.swiften.javautilities.localizer.LCFormat} value.
     * @see SLInputType#emptyInputError(UserMode)
     * @see #emptyInputErrorFormat()
     * @see #minSelectableNumericValue(UserMode)
     * @see #maxSelectableNumericValue(UserMode)
     * @see #stringValue(double)
     */
    @NotNull
    @Override
    default LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.builder()
            .withPattern(emptyInputErrorFormat())
            .addArgument(stringValue(minSelectableNumericValue(mode)))
            .addArgument(stringValue(maxSelectableNumericValue(mode)))
            .build();
    }

    @NotNull
    @Override
    default Converter<Integer> converter() {
        return a -> (int)a;
    }

    /**
     * Get the minimum numeric value that can be selected.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    int minSelectableNumericValue(@NotNull UserMode mode);

    /**
     * Get the maximum numeric value that can be selected.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    int maxSelectableNumericValue(@NotNull UserMode mode);

    /**
     * Get the difference between two consecutive numeric values.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    default int selectableNumericValueStep(@NotNull UserMode mode) {
        return 1;
    }

    /**
     * Limit the number of elements within {@link List} of numeric range
     * return by {@link #selectableNumericRange(UserMode)}.
     * @return {@link Integer} value.
     */
    @NotNull
    default Optional<Integer> numericRangeLimit() {
        return Optional.of(20);
    }

    /**
     * Get the selectable numeric range.
     * @param mode {@link UserMode} instance.
     * @return {@link List} of {@link Integer}.
     * @see #numericRangeLimit()
     * @see #valueRange(Number, Number, Number)
     * @see #minSelectableNumericValue(UserMode)
     * @see #maxSelectableNumericValue(UserMode)
     * @see #selectableNumericValueStep(UserMode)
     */
    @NotNull
    default List<Integer> selectableNumericRange(@NotNull UserMode mode) {
        List<Integer> values = valueRange(
            minSelectableNumericValue(mode),
            maxSelectableNumericValue(mode),
            selectableNumericValueStep(mode)
        );

        Optional<Integer> optional = numericRangeLimit();
        int limit = optional.orElseGet(values::size);
        return CollectionUtil.subList(values, 0, limit);
    }

    /**
     * Get a random numeric value from {@link #selectableNumericRange(UserMode)}.
     * @return {@link Integer} value.
     * @see #selectableNumericRange(UserMode)
     * @see CollectionTestUtil#randomElement(List)
     */
    default int randomValue(@NotNull UserMode mode) {
        List<Integer> selectableRange = selectableNumericRange(mode);
        return CollectionTestUtil.randomElement(selectableRange);
    }
}
