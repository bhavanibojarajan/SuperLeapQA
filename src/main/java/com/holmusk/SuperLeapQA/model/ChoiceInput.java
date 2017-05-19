package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidChoiceInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements BaseErrorType, SLInputType, AndroidChoiceInputType {
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREFERENCE;

    /**
     * @return A {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #newXPathBuilder()
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    @Override
    public XPath androidViewXPath() {
        final String ID;

        switch (this) {
            case HEIGHT:
                ID = "et_height";
                break;

            case WEIGHT:
                ID = "et_weight";
                break;

            case ETHNICITY:
                ID = "et_eth";
                break;

            case COACH_PREFERENCE:
                ID = "et_coachpref";
                break;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }

        return newXPathBuilder().containsID(ID).build();
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #newXPathBuilder()
     */
    @NotNull
    @Override
    public XPath androidScrollViewPickerXPath() {
        return newXPathBuilder().containsID("select_dialog_listview").build();
    }

    /**
     * @return A {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #newXPathBuilder()
     */
    @Override
    public XPath androidScrollViewItemXPath() {
        return newXPathBuilder().containsID("text1").build();
    }

    /**
     * @return A {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        String error;

        switch (this) {
            case COACH_PREFERENCE:
                error = "register_error_coachPrefNotSet";
                break;

            case ETHNICITY:
                error = "register_error_ethnicityNotSet";
                break;

            default:
                throw new RuntimeException(NOT_IMPLEMENTED);
        }

        return LCFormat.builder().withPattern(error).build();
    }
}
