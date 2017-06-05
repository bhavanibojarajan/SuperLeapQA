package com.holmusk.SuperLeapQA.model;

import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 5/13/17.
 */
public enum ChoiceInput implements HMTextChoiceType {
    HEIGHT,
    WEIGHT,
    ETHNICITY,
    COACH_PREF;

    /**
     * @return {@link List} of {@link HMTextChoiceType}.
     * @see HMTextChoiceType#allTextChoices()
     * @see Ethnicity#values()
     * @see CoachPref#values()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public List<? extends HMTextChoiceType.Item> allTextChoices() {
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
     * @see InputType#inputViewXP(PlatformType)
     * @see #androidInputViewXP()
     * @see #iOSInputViewXP()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    public XPath inputViewXP(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidInputViewXP();

            case IOS:
                return iOSInputViewXP();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    private XPath androidInputViewXP() {
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

        return XPath
            .builder(Platform.ANDROID)
            .containsID(ID)
            .addAnyClass()
            .build();
    }

    /**
     * Get {@link XPath} for the input view for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see IOSView.ViewType#UI_TEXT_FIELD
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see IOSView.ViewType#UI_TABLE_VIEW
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#setIndex(int)
     * @see XPath.Builder#addChildXPath(XPath)
     */
    @NotNull
    private XPath iOSInputViewXP() {
        Platform platform = Platform.IOS;

        /* We need to add 2 to the index because the first cell is the Gender
         * picker, which is also a UITableViewCell. We want to skip this cell.
         * And also, the XPath index is 1-based */
        int index = Arrays.asList(values()).indexOf(this) + 2;

        XPath textFieldXPath = XPath.builder(platform)
            .addClass(IOSView.ViewType.UI_TEXT_FIELD.className())
            .build();

        XPath cellXPath = XPath.builder(platform)
            .addClass(IOSView.ViewType.UI_TABLE_VIEW_CELL.className())
            .setIndex(index)
            .build();

        return XPath.builder(platform)
            .addClass(IOSView.ViewType.UI_TABLE_VIEW.className())
            .addChildXPath(cellXPath)
            .addChildXPath(textFieldXPath)
            .build();
    }


}
