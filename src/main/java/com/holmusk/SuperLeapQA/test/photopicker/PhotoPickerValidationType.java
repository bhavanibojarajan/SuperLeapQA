package com.holmusk.SuperLeapQA.test.photopicker;

import com.holmusk.SuperLeapQA.model.PhotoPickerMode;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 29/5/17.
 */
public interface PhotoPickerValidationType extends BaseValidationType {
    /**
     * Get the {@link WebElement} that can be used to switch to a
     * {@link PhotoPickerMode}.
     * @param engine {@link Engine} instance.
     * @param mode {@link PhotoPickerMode} instance.
     * @return
     */
    @NotNull
    default Flowable<WebElement> rxe_pickerMode(@NotNull Engine<?> engine,
                                                @NotNull PhotoPickerMode mode) {
        return engine.rxe_containsText(mode.pickerTitle()).firstElement().toFlowable();
    }

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

    /**
     * Get the {@link WebElement} that are holding images, assuming the user
     * is in {@link com.holmusk.SuperLeapQA.model.PhotoPickerMode#LIBRARY}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_imageViews()
     * @see com.holmusk.SuperLeapQA.model.PhotoPickerMode#LIBRARY
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_imageViews(@NotNull Engine<?> engine) {
        return engine.rxe_imageViews();
    }
}
