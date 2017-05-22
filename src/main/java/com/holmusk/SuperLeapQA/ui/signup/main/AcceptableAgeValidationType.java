package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLChoiceInputType;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLNumericInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import org.swiften.xtestkit.mobile.android.AndroidView;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeValidationType extends DOBPickerValidationType {
    /**
     * Get the next confirm button for acceptable age input screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_acceptableAgeConfirm(@NotNull Engine<?> engine) {
        return engine.rx_containsText("register_title_next").firstElement().toFlowable();
    }

    /**
     * Get the back button's title label.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_acceptableAgeInputTitle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsText(
                "parentSignUp_title_enterChildDetails",
                "teenSignUp_title_enterDetails"
            ).firstElement().toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scrollable height selector view, assuming the user is already
     * in the height picker window.
     * @param input {@link ChoiceInput} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_withXPath(XPath...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default <P extends SLChoiceInputType> Flowable<WebElement>
    rx_e_scrollableChoicePicker(@NotNull Engine<?> engine, @NotNull P input) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_withXPath(input.androidScrollViewPickerXPath())
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get all input value items within the scrollable view as emitted by
     * {@link #rx_e_scrollableChoicePicker(Engine, SLChoiceInputType)},
     * assuming the user is already in the picker window.
     * @param engine {@link Engine} instance.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_byXPath(ByXPath)
     * @see Engine#xPathBuilder()
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default <P extends SLChoiceInputType & SLNumericInputType> Flowable<WebElement>
    rx_e_pickerItemViews(@NotNull Engine<?> engine, @NotNull P input) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_withXPath(input.androidScrollViewItemXPath())
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get a {@link ByXPath} instance that will be used to query for the
     * numeric picker item. This instance is dependent on the
     * {@link org.swiften.xtestkit.base.type.PlatformType} of the currently
     * active {@link Engine}.
     * On {@link org.swiften.xtestkit.mobile.Platform#ANDROID}, the middle
     * {@link WebElement} of the number picker is an
     * {@link AndroidView.ViewType#EDIT_TEXT}, while the rest are
     * {@link AndroidView.ViewType#BUTTON}. We are interested only in the
     * middle {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @param input {@link SLChoiceInputType} instance.
     * @param value {@link String} value that should be displayed by the item.
     * @return {@link ByXPath} instance.
     * @see Engine#xPathBuilder()
     * @see XPath.Builder#hasText(String)
     * @see XPath.Builder#ofClass(String)
     * @see XPath.Builder#ofInstance(int)
     * @see SLChoiceInputType#androidPickerItemIndex()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default ByXPath e_pickerItemQuery(@NotNull Engine<?> engine,
                                      @NotNull SLChoiceInputType input,
                                      @NotNull String value) {
        XPath.Builder xPathBuilder = engine.xPathBuilder().hasText(value);

        if (engine instanceof AndroidEngine) {
            xPathBuilder
                .ofClass(AndroidView.ViewType.EDIT_TEXT.className())
                .ofInstance(input.androidPickerItemIndex());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        XPath xPath = xPathBuilder.build();
        return ByXPath.builder().withXPath(xPath).withRetries(1).build();
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see #rx_e_acceptableAgeConfirm(Engine)
     * @see #rx_e_acceptableAgeInputTitle(Engine)
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_acceptableAgeScreen(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rx_e_editField(engine, Gender.MALE),
                rx_e_editField(engine, Gender.FEMALE),
                rx_e_editField(engine, ChoiceInput.HEIGHT),
                rx_e_editField(engine, Height.FT),
                rx_e_editField(engine, Height.CM),
                rx_e_editField(engine, ChoiceInput.WEIGHT),
                rx_e_editField(engine, Weight.LB),
                rx_e_editField(engine, Weight.KG),
                rx_e_editField(engine, ChoiceInput.ETHNICITY),
                rx_e_editField(engine, ChoiceInput.COACH_PREF),
                rx_e_acceptableAgeConfirm(engine),
                rx_e_acceptableAgeInputTitle(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if an editable field has an input.
     * @param INPUT {@link InputType} instance.
     * @param VALUE {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see Engine#getText(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default <P extends SLInputType> Flowable<?>
    rx_v_editFieldHasValue(@NotNull final Engine<?> ENGINE,
                           @NotNull final P INPUT,
                           @NotNull final String VALUE) {
        return rx_e_editField(ENGINE, INPUT)
            .map(ENGINE::getText)
            .doOnNext(a -> LogUtil.printfThread("Current value for %s: %s", INPUT, a))
            .filter(a -> a.equals(VALUE))
            .switchIfEmpty(RxUtil.errorF("Value for %s does not equal %s", INPUT, VALUE))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for numeric choice inputs (e.g. {@link Height}
     * and {@link Weight}).
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_numericChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsID("btnDone").firstElement().toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Get the view that pops up when an error is notified to the user. This
     * only works in specific cases however, so use with care.
     * @param error {@link LCFormat} value.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rx_e_errorPopup(@NotNull Engine<?> engine,
                                                 @NotNull LCFormat error) {
        return engine.rx_containsText(error).firstElement().toFlowable();
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error {@link LCFormat} value.
     * @return {@link Flowable} instance.
     * @see #rx_e_errorPopup(Engine, LCFormat)
     */
    @NotNull
    default Flowable<?> rxIsShowingError(@NotNull Engine<?> engine,
                                         @NotNull LCFormat error) {
        return rx_e_errorPopup(engine, error);
    }
}
