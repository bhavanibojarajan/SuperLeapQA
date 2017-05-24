package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 23/5/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.AndroidView;

/**
 * This interface provides input methods for
 * {@link org.swiften.xtestkit.mobile.Platform#ANDROID} number pickers, esp.
 * for {@link com.holmusk.SuperLeapQA.model.Height} and
 * {@link com.holmusk.SuperLeapQA.model.Weight}
 */
public interface SLAndroidNumericPickerInputType {
    /**
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current input.
     * @return {@link Integer} value.
     */
    int androidScrollablePickerIndex();

    /**
     * Get a {@link ByXPath} instance that will be used to query for the
     * numeric picker item.
     * On {@link org.swiften.xtestkit.mobile.Platform#ANDROID}, the middle
     * {@link org.openqa.selenium.WebElement} of the number picker is an
     * {@link AndroidView.ViewType#EDIT_TEXT}, while the rest are
     * {@link AndroidView.ViewType#BUTTON}. We are interested only in the
     * middle {@link org.openqa.selenium.WebElement}.
     * @param selected {@link String} value of the selected choice.
     * @return {@link ByXPath} instance.
     * @see Platform#ANDROID
     * @see AndroidView.ViewType#EDIT_TEXT
     * @see XPath.Builder#ofClass(String)
     * @see XPath.Builder#ofInstance(int)
     */
    @NotNull
    default XPath androidTargetChoiceItemXPath(@NotNull String selected) {
        return XPath.builder(Platform.ANDROID)
            .ofClass(AndroidView.ViewType.EDIT_TEXT.className())
            .containsText(selected)
            .build();
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see AndroidView.ViewType#NUMBER_PICKER
     * @see #androidScrollablePickerIndex()
     * @see XPath.Builder#atIndex(int)
     * @see XPath.Builder#ofClass(String)
     */
    @NotNull
    default XPath androidScrollViewPickerXPath() {
        return XPath.builder(Platform.ANDROID)
            .setClass(AndroidView.ViewType.NUMBER_PICKER.className())
            .build();
    }

    /**
     * Get the scroll view picker item {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see AndroidView.ViewType#NUMBER_PICKER
     * @see #androidScrollablePickerIndex()
     * @see XPath.Builder#containsID(String)
     * @see XPath.Builder#ofInstance(int)
     */
    @NotNull
    default XPath androidScrollViewPickerItemXPath() {
        PlatformType platform = Platform.ANDROID;
        XPath cxp = XPath.builder(platform).build();

        return XPath.builder(platform)
            .setClass(AndroidView.ViewType.NUMBER_PICKER.className())
            .ofInstance(androidScrollablePickerIndex())
            .addChildXPath(cxp)
            .build();
    }
}
