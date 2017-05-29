package com.holmusk.SuperLeapQA.ui.photopicker;

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 29/5/17.
 */
public interface PhotoPickerValidationType {
    /**
     * Get the Use button to confirm a photo.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_usePhoto(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("photo_picker_title_use")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the Skip button to skip the photo picker screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_skipPhoto(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("photo_picker_title_skip")
            .firstElement()
            .toFlowable();
    }
}
