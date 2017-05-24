package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 23/5/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.ios.IOSView;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides methods to handle text-based choice inputs, e.g.
 * {@link com.holmusk.SuperLeapQA.model.ChoiceInput#ETHNICITY} or
 * {@link com.holmusk.SuperLeapQA.model.ChoiceInput#COACH_PREF}
 */
public interface SLTextChoiceInputType extends SLChoiceInputType, BaseErrorType {
    /**
     * Get all text choice inputs. Usually this interface is implemented by
     * a {@link Enum}, so this method should call {@link Enum} values.
     * @return {@link List} of {@link Object}
     */
    @NotNull
    List<? extends SLTextChoiceInputItemType> allTextChoices();

    /**
     * @param platform {@link PlatformType} instance.
     * @param selected {@link String} value of the selected choice.
     * @return {@link XPath}
     * @see SLChoiceInputType#targetChoiceItemXPath(PlatformType, String)
     */
    @NotNull
    @Override
    default XPath targetChoiceItemXPath(@NotNull PlatformType platform,
                                        @NotNull String selected) {
        return XPath.builder(platform).containsText(selected).build();
    }

    /**
     * @param VALUE {@link String} value.
     * @return {@link Integer} value.
     * @see SLChoiceInputType#numericValue(String)
     * @see #allTextChoices()
     * @see #NOT_AVAILABLE
     */
    @Override
    default double numericValue(@NotNull final String VALUE) {
        List<? extends SLTextChoiceInputItemType> values = allTextChoices();

        Optional<? extends SLTextChoiceInputItemType> input = values
            .stream()
            .filter(a -> a.stringValue().equals(VALUE))
            .findFirst();

        if (input.isPresent()) {
            return values.indexOf(input.get());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param value {@link Integer} value.
     * @return {@link String} value.
     * @see SLChoiceInputType#stringValue(double)
     * @see #allTextChoices()
     */
    @NotNull
    @Override
    default String stringValue(double value) {
        return allTextChoices().get((int)value).toString();
    }

    /**
     * @return Return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType#choicePickerScrollViewXPath(PlatformType)
     * @see #androidScrollViewPickerXPath()
     * @see #iOSScrollViewPickerXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerScrollViewXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidScrollViewPickerXPath();

            case IOS:
                return iOSScrollViewPickerXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType#choicePickerScrollViewItemXPath(PlatformType)
     * @see #androidScrollViewPickerItemXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerScrollViewItemXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidScrollViewPickerItemXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#atIndex(int)
     * @see XPath.Builder#ofClass(String)
     */
    @NotNull
    default XPath androidScrollViewPickerXPath() {
        return XPath.builder(Platform.ANDROID)
            .containsID("select_dialog_listview")
            .build();
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see Platform#IOS
     * @see XPath.Builder#setClass(String)
     * @see IOSView.ViewType#UI_PICKERWHEEL
     */
    @NotNull
    default XPath iOSScrollViewPickerXPath() {
        return XPath.builder(Platform.IOS)
            .setClass(IOSView.ViewType.UI_PICKERWHEEL.className())
            .build();
    }

    /**
     * Get the scroll view picker item {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#containsID(String)
     * @see XPath.Builder#ofInstance(int)
     */
    @NotNull
    default XPath androidScrollViewPickerItemXPath() {
        return XPath.builder(Platform.ANDROID).containsID("text1").build();
    }
}
