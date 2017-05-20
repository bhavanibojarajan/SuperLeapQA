package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.element.property.type.base.AttributeType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 5/12/17.
 */
public enum CoachPref implements AttributeType<String>, AndroidInputType, SLInputType {
    MALE,
    FEMALE,
    NO_PREFERENCE;

    @NotNull
    @Override
    public String value() {
        switch (this) {
            case MALE:
                return "user_title_coachPref_male";

            case FEMALE:
                return "user_title_coachPref_female";

            case NO_PREFERENCE:
                return "user_title_coachPref_noPreference";

            default:
                return "";
        }
    }

    /**
     * @return {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #newXPathBuilder()
     */
    @NotNull
    @Override
    public XPath androidViewXPath() {
        return newXPathBuilder().containsID("text1").build();
    }

    /**
     * @return {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        return LCFormat.builder()
            .withPattern("register_error_coachPrefNotSet")
            .build();
    }
}
