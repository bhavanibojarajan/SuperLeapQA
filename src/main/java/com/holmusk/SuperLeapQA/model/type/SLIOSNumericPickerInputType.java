package com.holmusk.SuperLeapQA.model.type;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.ios.IOSView;

/**
 * Created by haipham on 24/5/17.
 */
public interface SLIOSNumericPickerInputType {
    /**
     * Get the index associated with the {@link org.openqa.selenium.WebElement}
     * with which we are selecting a value for the current input.
     * @return {@link Integer} value.
     */
    int iOSScrollablePickerIndex();

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see IOSView.ViewType#UI_PICKERWHEEL
     */
    @NotNull
    default XPath iOSScrollViewPickerXPath() {
        return XPath.builder(Platform.IOS)
            .setClass(IOSView.ViewType.UI_PICKERWHEEL.className())
            .build();
    }
}
