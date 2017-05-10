package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum Height {
    FT,
    CM;

    /**
     * Get the localizable title for the current {@link Height}.
     * @return A {@link String} value.
     */
    @NotNull
    public String localizable() {
        switch (this) {
            case FT:
                return "user_title_height_ft";

            case CM:
                return "user_title_height_cm";

            default:
                return "";
        }
    }

    /**
     * Get the view id for {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * locator.
     * @return A {@link String} value.
     */
    @NotNull
    public String androidViewId() {
        switch (this) {
            case FT:
                return "btn_ft";

            case CM:
                return "btn_cm";

            default:
                return "";
        }
    }
}
