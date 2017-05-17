package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.mobile.android.AndroidView;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableInputValidationType extends DOBPickerValidationType {
    /**
     * Get the next confirm button for acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxAcceptableAgeConfirmButton() {
        return engine().rxElementContainingText("register_title_next");
    }

    /**
     * Get the back button's title label.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingText(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxAcceptableAgeInputTitleLabel() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingText(
                "parentSignUp_title_enterChildDetails",
                "teenSignUp_title_enterDetails"
            );
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get the scrollable height selector view, assuming the user is already
     * in the height picker window.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingID(String...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxScrollableInputPickerView(@NotNull InputType input) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingID("select_dialog_listview");
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Get all input value items within the scrollable view as emitted by
     * {@link #rxScrollableInputPickerView(InputType)}, assuming the user
     * is already in the picker window.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementsByXPath(ByXPath)
     * @see BaseEngine#newXPathBuilder()
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default Flowable<WebElement> rxPickerItemViews(@NotNull InputType input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (engine instanceof AndroidEngine) {
            XPath xPath = engine.newXPathBuilder()
                .ofClass(AndroidView.ViewType.TEXT_VIEW.className())
                .containsID("text1")
                .build();

            ByXPath byXPath = ByXPath.builder()
                .withXPath(xPath)
                .withError(NO_SUCH_ELEMENT)
                .build();

            return engine.rxElementsByXPath(byXPath);
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see #rxAcceptableAgeConfirmButton()
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateAcceptableAgeScreen() {
        return rxEditFieldForInput(Gender.MALE)
            .flatMap(a -> rxEditFieldForInput(Gender.FEMALE))
            .flatMap(a -> rxEditFieldForInput(ChoiceInput.HEIGHT))
            .flatMap(a -> rxEditFieldForInput(Height.FT))
            .flatMap(a -> rxEditFieldForInput(Height.CM))
            .flatMap(a -> rxEditFieldForInput(ChoiceInput.WEIGHT))
            .flatMap(a -> rxEditFieldForInput(Weight.LB))
            .flatMap(a -> rxEditFieldForInput(Weight.KG))
            .flatMap(a -> rxEditFieldForInput(ChoiceInput.ETHNICITY))
            .flatMap(a -> rxEditFieldForInput(ChoiceInput.COACH_PREFERENCE))
            .flatMap(a -> rxAcceptableAgeConfirmButton())
            .flatMap(a -> rxAcceptableAgeInputTitleLabel())
            .map(BooleanUtil::toTrue);
    }

    /**
     * Check if an editable field has an input.
     * @param INPUT A {@link InputType} instance.
     * @param VALUE A {@link String} value.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     */
    @NotNull
    default Flowable<Boolean> rxEditFieldHasValue(@NotNull final InputType INPUT,
                                                  @NotNull final String VALUE) {
        return rxEditFieldForInput(INPUT)
            .map(engine()::getText)
            .doOnNext(a -> LogUtil.printf("Current value for %s: %s", INPUT, a))
            .filter(a -> a.equals(VALUE))
            .switchIfEmpty(RxUtil.errorF("Value for %s does not equal %s", INPUT, VALUE))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the view that pops up when an error is notified to the user. This
     * only works in specific cases however, so use with care.
     * @param error A {@link LCFormat} value.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxErrorPopup(@NotNull LCFormat error) {
        return engine().rxElementContainingText(error);
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error A {@link LCFormat} value.
     * @return A {@link Flowable} instance.
     * @see #rxErrorPopup(LCFormat)
     */
    @NotNull
    default Flowable<Boolean> rxIsShowingError(@NotNull LCFormat error) {
        return rxErrorPopup(error).map(a -> true);
    }
}
