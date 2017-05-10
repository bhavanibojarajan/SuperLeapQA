package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by haipham on 5/10/17.
 */
public enum  Weight {
    LBS,
    KG;

    /**
     * Get the localizable title for the current {@link Weight}.
     * @return A {@link String} value.
     */
    @NotNull
    public String localizable() {
        switch (this) {
            case LBS:
                return "user_title_weight_lbs";

            case KG:
                return "user_title_weight_kg";

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
            case LBS:
                return "btn_lb";

            case KG:
                return "btn_kg";

            default:
                return "";
        }
    }
}
