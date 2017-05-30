package com.holmusk.SuperLeapQA.ui.photopicker;

import com.holmusk.SuperLeapQA.model.PhotoPickerMode;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.ui.logmeal.LogMealActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

import java.util.Random;

/**
 * Created by haipham on 29/5/17.
 */
public interface PhotoPickerActionType extends BaseActionType, PhotoPickerValidationType {
    /**
     * Switch to a {@link PhotoPickerMode}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link PhotoPickerMode} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_pickerMode(Engine, PhotoPickerMode)
     */
    @NotNull
    default Flowable<?> rxa_selectPickerMode(@NotNull final Engine<?> ENGINE,
                                             @NotNull PhotoPickerMode mode) {
        return rxe_pickerMode(ENGINE, mode).flatMap(ENGINE::rxa_click);
    }

    /**
     * Randomly select a photo from {@link PhotoPickerMode#LIBRARY}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see Long#intValue()
     * @see Random#nextInt(int)
     * @see #rxe_imageViews(Engine)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomPhoto(@NotNull final Engine<?> ENGINE) {
        return Flowable
            .zip(
                rxe_imageViews(ENGINE).count()
                    .map(Long::intValue)
                    .map(new Random()::nextInt)
                    .toFlowable(),

                rxe_imageViews(ENGINE).toList().toFlowable(),
                (a, b) -> ENGINE.rxa_click(b.get(a))
            )
            .flatMap(a -> a);
    }

    /**
     * Select randomly a number of photos from {@link PhotoPickerMode#LIBRARY}.
     * @param ENGINE {@link Engine} instance.
     * @param NUMBER_OF_PHOTOS {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see PhotoPickerMode#LIBRARY
     * @see #rxa_selectPickerMode(Engine, PhotoPickerMode)
     * @see #rxa_selectRandomPhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxa_selectLibraryPhotos(@NotNull final Engine<?> ENGINE,
                                                final int NUMBER_OF_PHOTOS) {
        final PhotoPickerActionType THIS = this;

        return rxa_selectPickerMode(ENGINE, PhotoPickerMode.LIBRARY)
            .flatMap(a -> Flowable.range(0, NUMBER_OF_PHOTOS))
            .concatMap(a -> THIS.rxa_selectRandomPhoto(ENGINE))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Confirm photo selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_usePhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPhoto(@NotNull final Engine<?> ENGINE) {
        return rxe_usePhoto(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
