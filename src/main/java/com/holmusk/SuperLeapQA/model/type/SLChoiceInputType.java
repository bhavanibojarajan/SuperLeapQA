package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/19/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType;
import org.swiften.xtestkit.base.type.PlatformType;

/**
 * {@link org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType}
 * for Superleap.
 */
public interface SLChoiceInputType extends ChoiceInputType, SLInputType {
    /**
     * Get the index of the choice picker, depending on the {@link PlatformType}
     * being tested. If there is only one choice picker present on the screen,
     * return 0.
     * @param platform {@link PlatformType} instance.
     * @return {@link Integer} value.
     */
    default int scrollablePickerIndex(@NotNull PlatformType platform) {
        return 0;
    }
}
