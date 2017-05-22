package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 5/19/17.
 */

import com.holmusk.SuperLeapQA.model.Height;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;

/**
 * {@link org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType}
 * for Superleap.
 */
public interface SLChoiceInputType extends AndroidChoiceInputType, SLInputType {
    /**
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current {@link Height}. This
     * is useful for when there are multiple
     * {@link org.openqa.selenium.WebElement} with the same id.
     * @return {@link Integer} value.
     */
    int androidPickerItemIndex();
}
