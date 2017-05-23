package com.holmusk.SuperLeapQA.model;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextChoiceInputItemType;
import com.holmusk.SuperLeapQA.model.type.SLTextChoiceInputType;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.ios.IOSView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements SLTextChoiceInputType {
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREF;

    /**
     * @return {@link List} of {@link SLTextChoiceInputType}.
     * @see SLTextChoiceInputType#allTextChoices()
     * @see Ethnicity#values()
     * @see CoachPref#values()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public List<? extends SLTextChoiceInputItemType> allTextChoices() {
        switch (this) {
            case ETHNICITY:
                return Arrays.asList(Ethnicity.values());

            case COACH_PREF:
                return Arrays.asList(CoachPref.values());

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.InputType#inputViewXPath(PlatformType)
     * @see #androidInputViewXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXPath();

            case IOS:
                return iOSInputViewXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    private XPath androidInputViewXPath() {
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

        return XPath.builder(Platform.ANDROID).containsID(ID).build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see IOSView.ViewType#UI_TEXTFIELD
     * @see IOSView.ViewType#UI_TABLEVIEW_CELL
     * @see IOSView.ViewType#UI_TABLEVIEW
     * @see XPath.Builder#setClass(String)
     * @see XPath.Builder#setIndex(int)
     * @see XPath.Builder#addChildXPath(XPath)
     */
    @NotNull
    private XPath iOSInputViewXPath() {
        Platform platform = Platform.IOS;

        /* We need to add 2 to the index because the first cell is the Gender
         * picker, which is also a UITableViewCell. We want to skip this cell.
         * And also, the XPath index is 1-based */
        int index = Arrays.asList(values()).indexOf(this) + 2;

        XPath textFieldXPath = XPath.builder(platform)
            .setClass(IOSView.ViewType.UI_TEXTFIELD.className())
            .build();

        XPath cellXPath = XPath.builder(platform)
            .setClass(IOSView.ViewType.UI_TABLEVIEW_CELL.className())
            .setIndex(index)
            .build();

        return XPath.builder(platform)
            .setClass(IOSView.ViewType.UI_TABLEVIEW.className())
            .addChildXPath(cellXPath)
            .addChildXPath(textFieldXPath)
            .build();
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
