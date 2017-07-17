package com.holmusk.SuperLeapQA.test.photopicker;

import com.holmusk.SuperLeapQA.model.PhotoPickerMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.type.AndroidSDK;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.Random;

/**
 * Created by haipham on 29/5/17.
 */
public interface PhotoPickerActionType extends BaseActionType, PhotoPickerValidationType {
    /**
     * Switch to a {@link PhotoPickerMode}. We also accept any potential
     * permission alert just in case.
     * On {@link org.swiften.xtestkit.mobile.Platform#ANDROID}, specifically
     * with {@link AndroidSDK#isAtLeastM()}, the first click on the
     * {@link WebElement} corresponding to the provided {@link PhotoPickerMode}
     * may not bring the user immediately to the appropriate screen (but
     * instead the user needs to accept a permission dialog, then click on
     * that {@link WebElement} again).
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link PhotoPickerMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_pickerMode(Engine, PhotoPickerMode)
     */
    @NotNull
    default Flowable<?> rxa_selectPickerMode(@NotNull final Engine<?> ENGINE,
                                             @NotNull final PhotoPickerMode MODE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_pickerMode(ENGINE, MODE)
                .flatMap(ENGINE::rxa_click)
                .flatMap(b -> ENGINE.rxa_acceptAlert())
                .onErrorReturnItem(true)
                .repeat(2)
                .all(HPObjects::nonNull)
                .toFlowable();
        } else if (ENGINE instanceof IOSEngine) {
            return rxe_pickerMode(ENGINE, MODE).flatMap(ENGINE::rxa_click);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Randomly select a photo from {@link PhotoPickerMode#LIBRARY}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_imageViews(Engine)
     */
    @NotNull
    default Flowable<?> rxa_selectRandomPhoto(@NotNull final Engine<?> ENGINE) {
        return Flowable.zip(
            rxe_imageViews(ENGINE).count()
                .map(Long::intValue)
                .map(new Random()::nextInt)
                .toFlowable(),

            rxe_imageViews(ENGINE).toList().toFlowable(),
            (a, b) -> ENGINE.rxa_click(b.get(a))
        ).flatMap(a -> a);
    }

    /**
     * Select randomly a number of photos from {@link PhotoPickerMode#LIBRARY}.
     * @param ENGINE {@link Engine} instance.
     * @param PHOTO_COUNT {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rxa_selectPickerMode(Engine, PhotoPickerMode)
     * @see #rxa_selectRandomPhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxa_selectLibraryPhotos(@NotNull final Engine<?> ENGINE,
                                                final int PHOTO_COUNT) {
        final PhotoPickerActionType THIS = this;

        return rxa_selectPickerMode(ENGINE, PhotoPickerMode.LIBRARY)
            .flatMap(a -> Flowable.range(0, PHOTO_COUNT)
                .concatMap(b -> THIS.rxa_selectRandomPhoto(ENGINE))
                .all(HPObjects::nonNull)
                .toFlowable());
    }

    /**
     * Confirm photo selection.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_usePhoto(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPhoto(@NotNull final Engine<?> ENGINE) {
        return rxe_usePhoto(ENGINE).flatMap(ENGINE::rxa_click);
    }
}
