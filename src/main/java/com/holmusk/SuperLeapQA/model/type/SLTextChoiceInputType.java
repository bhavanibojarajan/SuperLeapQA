package com.holmusk.SuperLeapQA.model.type;

/**
 * Created by haipham on 23/5/17.
 */

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.model.ChoiceInputType;
import org.swiften.xtestkit.base.element.locator.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.BaseViewType;
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
     * @see SLChoiceInputType#androidTargetChoiceItemXP(String)
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsText(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath androidTargetChoiceItemXP(@NotNull String selected) {
        return XPath.builder(Platform.ANDROID)
            .containsText(selected)
            .addAnyClass()
            .build();
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
     * @see ChoiceInputType#choicePickerXP(PlatformType)
     * @see #androidChoicePickerXP()
     * @see #iOSScrollViewPickerXP()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerXP(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidChoicePickerXP();

            case IOS:
                return iOSScrollViewPickerXP();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} value.
     * @see ChoiceInputType#choicePickerItemXPath(PlatformType)
     * @see #androidChoicePickerItemXP()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default XPath choicePickerItemXPath(@NotNull PlatformType platform) {
        switch ((Platform)platform) {
            case ANDROID:
                return androidChoicePickerItemXP();

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    default XPath androidChoicePickerXP() {
        return XPath.builder(Platform.ANDROID)
            .containsID("select_dialog_listview")
            .addAnyClass()
            .build();
    }

    /**
     * Get the scroll view picker {@link XPath} for {@link Platform#IOS}.
     * @return {@link XPath} instance.
     * @see BaseViewType#className()
     * @see IOSView.ViewType#UI_PICKERWHEEL
     * @see Platform#IOS
     * @see XPath.Builder#addClass(String)
     */
    @NotNull
    default XPath iOSScrollViewPickerXP() {
        return XPath.builder(Platform.IOS)
            .addClass(IOSView.ViewType.UI_PICKERWHEEL.className())
            .build();
    }

    /**
     * Get the scroll view picker item {@link XPath} for {@link Platform#ANDROID}.
     * @return {@link XPath} instance.
     * @see Platform#ANDROID
     * @see XPath.Builder#addAnyClass()
     * @see XPath.Builder#containsID(String)
     */
    @NotNull
    default XPath androidChoicePickerItemXP() {
        return XPath.builder(Platform.ANDROID)
            .containsID("text1")
            .addAnyClass()
            .build();
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
