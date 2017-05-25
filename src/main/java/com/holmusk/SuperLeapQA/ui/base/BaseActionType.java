package com.holmusk.SuperLeapQA.ui.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.general.type.BaseLocatorErrorType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.model.TextInputType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseActionType extends BaseValidationType, BaseLocatorErrorType {
    /**
     * Navigate backwards by clicking the back button.
     * @return {@link Flowable} instance.
     * @param ENGINE {@link Engine} instance.
     * @see #generalDelay()
     * @see #rxBackButton(Engine)
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_a_clickBackButton(@NotNull final Engine<?> ENGINE) {
        long delay = generalDelay();

        return rxBackButton(ENGINE)
            .flatMap(ENGINE::rx_click).map(BooleanUtil::toTrue)
            .delay(delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_progressBar(Engine)
     * @see Engine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_a_watchProgressBarUntilHidden(@NotNull final Engine<?> ENGINE) {
        return rx_e_progressBar(ENGINE)
            .flatMap(ENGINE::rx_watchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Enter an input for {@link TextInput}.
     * @param input {@link TextInputType} instance.
     * @param TEXT {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)
     * @see Engine#rx_sendKeys(WebElement, String...)
     */
    @NotNull
    default <P extends SLInputType> Flowable<WebElement>
    rx_a_enterInput(@NotNull final Engine<?> ENGINE,
                    @NotNull P input,
                    @NotNull final String TEXT) {
        return rx_e_editField(ENGINE, input).flatMap(a -> ENGINE.rx_sendKeys(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param engine {@link Engine} instance.
     * @param input {@link TextInputType} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterInput(Engine, SLInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default Flowable<WebElement> rx_a_enterRandomInput(@NotNull Engine<?> engine,
                                                       @NotNull SLTextInputType input) {
        return rx_a_enterInput(engine, input, input.randomInput());
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_editField(Engine, SLInputType)   )
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_clickInputField(@NotNull final Engine<?> ENGINE,
                                             @NotNull SLInputType input) {
        return rx_e_editField(ENGINE, input).flatMap(ENGINE::rx_click);
    }

    /**
     * Confirm a text input.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#localizer()
     * @see Engine#rx_withXPath(XPath...)
     * @see Engine#rx_click(WebElement)
     * @see org.swiften.javautilities.localizer.LocalizerType#localize(String)
     * @see XPath.Builder#setClass(String)
     * @see XPath.Builder#containsText(String)
     */
    @NotNull
    default Flowable<?> rx_a_confirmTextInput(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            String done = "input_title_done";
            String localized = ENGINE.localizer().localize(done);

            XPath xPath = XPath.builder(Platform.IOS)
                .setClass(IOSView.ViewType.UI_BUTTON.className())
                .containsText(localized)
                .build();

            return ENGINE.rx_withXPath(xPath).flatMap(ENGINE::rx_click);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Toggle next input for an input-based {@link WebElement}.
     * @param ENGINE {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_isLastInput(WebElement)
     * @see Engine#rx_toggleNextOrDoneInput(WebElement)
     * @see #rx_a_confirmTextInput(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rx_a_toggleNextInputOrDone(@NotNull final Engine<?> ENGINE,
                                                   @NotNull WebElement element) {
        if (ENGINE instanceof AndroidEngine) {
            return ENGINE.rx_toggleNextOrDoneInput(element);
        } else if (ENGINE instanceof IOSEngine) {
            final BaseActionType THIS = this;

            return ENGINE
                .rx_isLastInput(element)
                .flatMap(a -> {
                    if (a) {
                        return THIS.rx_a_confirmTextInput(ENGINE);
                    } else {
                        String id = "ob downArrow";
                        return ENGINE.rx_containsID(id).flatMap(ENGINE::rx_click);
                    }
                });
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the previous input for an input-based {@link WebElement}. This
     * is only relevant for {@link Platform#IOS} since it has up/down arrow
     * buttons.
     * @param ENGINE {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     * @see Engine#rx_click(WebElement)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rx_a_togglePreviousInput(@NotNull final Engine<?> ENGINE,
                                                 @NotNull WebElement element) {
        if (ENGINE instanceof IOSEngine) {
            return ENGINE.rx_containsID("ob upArrow").flatMap(ENGINE::rx_click);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
