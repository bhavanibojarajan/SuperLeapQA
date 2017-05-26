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
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxa_clickBackButton(@NotNull final Engine<?> ENGINE) {
        long delay = generalDelay();

        return rxBackButton(ENGINE)
            .flatMap(ENGINE::rxa_click).map(BooleanUtil::toTrue)
            .delay(delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_progressBar(Engine)
     * @see Engine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxa_watchProgressBarUntilHidden(@NotNull final Engine<?> ENGINE) {
        return rxe_progressBar(ENGINE)
            .flatMap(ENGINE::rx_watchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Enter an input for {@link TextInput}.
     * @param input {@link TextInputType} instance.
     * @param TEXT {@link String} value.
     * @param <P> Generics parameter.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, SLInputType)
     * @see Engine#rx_type(WebElement, String...)
     */
    @NotNull
    default <P extends SLInputType> Flowable<WebElement>
    rxa_enterInput(@NotNull final Engine<?> ENGINE,
                   @NotNull P input,
                   @NotNull final String TEXT) {
        return rxe_editField(ENGINE, input).flatMap(a -> ENGINE.rx_type(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param engine {@link Engine} instance.
     * @param input {@link TextInputType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterInput(Engine, SLInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default Flowable<WebElement> rxa_enterRandomInput(@NotNull Engine<?> engine,
                                                      @NotNull SLTextInputType input) {
        return rxa_enterInput(engine, input, input.randomInput());
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, SLInputType)   )
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_clickInputField(@NotNull final Engine<?> ENGINE,
                                            @NotNull SLInputType input) {
        return rxe_editField(ENGINE, input).flatMap(ENGINE::rxa_click);
    }

    /**
     * Confirm a text input.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#localizer()
     * @see Engine#rxe_withXPath(XPath...)
     * @see Engine#rxa_click(WebElement)
     * @see org.swiften.javautilities.localizer.LocalizerType#localize(String)
     * @see XPath.Builder#setClass(String)
     * @see XPath.Builder#containsText(String)
     */
    @NotNull
    default Flowable<?> rxa_confirmTextInput(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            String done = "input_title_done";
            String localized = ENGINE.localizer().localize(done);

            XPath xPath = XPath.builder(Platform.IOS)
                .setClass(IOSView.ViewType.UI_BUTTON.className())
                .containsText(localized)
                .build();

            return ENGINE.rxe_withXPath(xPath).flatMap(ENGINE::rxa_click);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Make the next input field visible on the screen, by toggling next input
     * or dismissing the keyboard.
     * @param E {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_hideKeyboard()
     * @see Engine#rx_isLastInput(WebElement)
     * @see Engine#rx_toggleNextOrFinishInput(WebElement)
     * @see #rxa_confirmTextInput(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_makeNextInputVisible(@NotNull final Engine<?> E,
                                                 @NotNull WebElement element) {
        if (E instanceof AndroidEngine) {
            /* Since sending key events is rather flaky, we can instead hide
             * the keyboard to expose the next input field */
            return E.rxa_hideKeyboard();
        } else if (E instanceof IOSEngine) {
            return rxa_confirmTextInput(E);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the next input for an input-based {@link WebElement}.
     * @param E {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_toggleNextInput(@NotNull final Engine<?> E,
                                            @NotNull WebElement element) {
        /* In this case, each input will have up/down keyboard accessories
         * that we can use to navigate to the previous/next input fields */
        return E.rxe_containsID("ob downArrow").flatMap(E::rxa_click);
    }

    /**
     * Toggle the previous input for an input-based {@link WebElement}. This
     * is only relevant for {@link Platform#IOS} since it has up/down arrow
     * buttons.
     * @param ENGINE {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxa_click(WebElement)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_togglePreviousInput(@NotNull final Engine<?> ENGINE,
                                                @NotNull WebElement element) {
        if (ENGINE instanceof IOSEngine) {
            return ENGINE.rxe_containsID("ob upArrow").flatMap(ENGINE::rxa_click);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
