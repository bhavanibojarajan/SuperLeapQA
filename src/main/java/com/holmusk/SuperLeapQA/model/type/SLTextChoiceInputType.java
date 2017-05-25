package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 23/5/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.model.ChoiceInputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.ios.IOSView;

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
     * @return {@link List} of {@link Item}
     */
    @NotNull
    List<? extends Item> allTextChoices();

    /**
     * @param selected The selected {@link String} choice.
     * @return {@link XPath} instance.
     * @see SLChoiceInputType#androidTargetChoiceItemXPath(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath androidTargetChoiceItemXPath(@NotNull String selected) {
        return XPath.builder(Platform.ANDROID).containsText(selected).build();
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
        List<? extends Item> values = allTextChoices();

        Optional<? extends Item> input = values
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
     * @see ChoiceInputType#choicePickerXPath(PlatformType)
     * @see #androidChoicePickerXPath()
     * @see #iOSScrollViewPickerXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidChoicePickerXPath();

            case IOS:
                return iOSScrollViewPickerXPath();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see ChoiceInputType#choicePickerItemXPath(PlatformType)
     * @see #androidChoicePickerItemXPath()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerItemXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidChoicePickerItemXPath();

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
    default XPath androidChoicePickerXPath() {
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
    default XPath androidChoicePickerItemXPath() {
        return XPath.builder(Platform.ANDROID).containsID("text1").build();
    }

    interface Item {
        /**
         * Use this {@link String} to locale a {@link Item} instance. This
         * can be helpful when we are calling
         * {@link SLTextChoiceInputType#numericValue(String)}.
         * @return {@link String} value.
         */
        @NotNull
        String stringValue();
    }
}
