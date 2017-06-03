package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMNumericChoiceType;
import com.holmusk.SuperLeapQA.model.UserMode;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.xtestkit.base.model.ChoiceInputType;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.android.model.AndroidNumericPickerInputType;
import org.swiften.xtestkit.util.ValueRangeConverterType;

import java.util.List;
import java.util.Optional;

/**
 * Created by haipham on 18/5/17.
 */
public interface SLNumericChoiceType extends HMNumericChoiceType {
    //region Numeric Range
    /**
     * Get the minimum numeric value that can be selected.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    int minSelectableValue(@NotNull UserMode mode);

    /**
     * Get the maximum numeric value that can be selected.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    int maxSelectableValue(@NotNull UserMode mode);

    /**
     * Get the difference between two consecutive numeric values.
     * @param mode {@link UserMode} instance.
     * @return {@link Integer} value.
     */
    default int selectableValueStep(@NotNull UserMode mode) {
        return 1;
    }

    /**
     * Limit the number of elements within {@link List} of numeric range
     * return by {@link #selectableRange(UserMode)}.
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
     * @see #minSelectableValue(UserMode)
     * @see #maxSelectableValue(UserMode)
     * @see #selectableValueStep(UserMode)
     */
    @NotNull
    default List<Integer> selectableRange(@NotNull UserMode mode) {
        List<Integer> values = valueRange(
            minSelectableValue(mode),
            maxSelectableValue(mode),
            selectableValueStep(mode)
        );

        Optional<Integer> optional = numericRangeLimit();
        int limit = optional.orElseGet(values::size);
        return CollectionUtil.subList(values, 0, limit);
    }

    /**
     * Get a random numeric value from {@link #selectableRange(UserMode)}.
     * @return {@link Integer} value.
     * @see #selectableRange(UserMode)
     * @see CollectionTestUtil#randomElement(List)
     */
    default int randomValue(@NotNull UserMode mode) {
        List<Integer> selectableRange = selectableRange(mode);
        return CollectionTestUtil.randomElement(selectableRange);
    }
    //endregion
}
