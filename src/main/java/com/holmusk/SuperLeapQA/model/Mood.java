package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkitcomponents.common.BaseErrorType;

/**
 * Created by haipham on 5/30/17.
 */
public enum Mood implements BaseErrorType {
    EXCITED,
    RELAXED,
    TIRED,
    STRESSED,
    SICK;

    /**
     * The title of the current {@link Mood}.
     * @return {@link String} value.
     */
    @NotNull
    public String moodTitle() {
        switch (this) {
            case EXCITED:
                return "mood_title_excited";

            case RELAXED:
                return "mood_title_relaxed";

            case TIRED:
                return "mood_title_tired";

            case STRESSED:
                return "mood_title_stressed";

            case SICK:
                return "mood_title_sick";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
