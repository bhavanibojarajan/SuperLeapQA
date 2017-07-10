package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkitcomponents.common.ErrorProviderType;

/**
 * Created by haipham on 29/5/17.
 */
public enum PhotoPickerMode implements ErrorProviderType {
    CAMERA,
    LIBRARY,
    RECENT;

    /**
     * Get the title {@link String} to locate the picker mode switcher.
     * @return {@link String} value.
     */
    @NotNull
    public String title() {
        switch (this) {
            case CAMERA:
                return "photo_picker_title_camera";

            case LIBRARY:
                return "photo_picker_title_library";

            case RECENT:
                return "photo_picker_title_recent";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
