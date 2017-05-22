package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
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
    COACH_PREF;

    /**
     * @return {@link XPath} value.
     * @see AndroidInputType#androidViewXPath()
     * @see #xPathBuilder()
     * @see #NOT_AVAILABLE
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

            case COACH_PREF:
                ID = "et_coachpref";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return xPathBuilder().containsID(ID).build();
    }

    /**
     * @return {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #xPathBuilder()
     */
    @NotNull
    @Override
    public XPath androidScrollViewPickerXPath() {
        return xPathBuilder().containsID("select_dialog_listview").build();
    }

    /**
     * @return {@link XPath} value.
     * @see AndroidChoiceInputType#androidScrollViewItemXPath()
     * @see #xPathBuilder()
     */
    @Override
    public XPath androidScrollViewItemXPath() {
        return xPathBuilder().containsID("text1").build();
    }

    /**
     * @return {@link String} value.
     * @see SLInputType#emptyInputError(UserMode)
     */
    @NotNull
    @Override
    public LCFormat emptyInputError(@NotNull UserMode mode) {
        String error;

        switch (this) {
            case COACH_PREF:
                error = "register_error_coachPrefNotSet";
                break;

            case ETHNICITY:
                error = "register_error_ethnicityNotSet";
                break;

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }

        return LCFormat.builder().withPattern(error).build();
    }
}
