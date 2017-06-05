package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.SLNumericChoiceType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.Weight;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.choice.ChoiceParam;
import org.swiften.xtestkit.base.element.choice.ChoiceType;
import org.swiften.xtestkit.base.element.locator.type.BaseLocatorErrorType;
import org.swiften.xtestkit.base.model.ChoiceInputType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.model.TextInputType;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.util.Arrays;
import java.util.List;
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
     * @see #generalDelay(Engine)
     * @see #rxe_backButton(Engine)
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxa_clickBackButton(@NotNull final Engine<?> ENGINE) {
        return rxe_backButton(ENGINE)
            .flatMap(ENGINE::rxa_click).map(BooleanUtil::toTrue)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_watchUntilHidden(WebElement)
     * @see #rxe_progressBar(Engine)
     */
    @NotNull
    default Flowable<Boolean> rxa_watchProgressBar(@NotNull final Engine<?> ENGINE) {
        return rxe_progressBar(ENGINE)
            .flatMap(ENGINE::rxa_watchUntilHidden)
            .onErrorReturnItem(true);
    }

    /**
     * Enter an input for {@link TextInput}.
     * @param input {@link TextInputType} instance.
     * @param TEXT {@link String} value.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)
     * @see Engine#rxa_type(WebElement, String...)
     */
    @NotNull
    default Flowable<WebElement> rxa_input(@NotNull final Engine<?> ENGINE,
                                           @NotNull HMInputType input,
                                           @NotNull final String TEXT) {
        return rxe_editField(ENGINE, input).flatMap(a -> ENGINE.rxa_type(a, TEXT));
    }

    /**
     * Enter inputs for a {@link List} of {@link HMTextType}.
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link HMTextType}.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_input(Engine, HMInputType, String)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default Flowable<?> rxa_inputs(@NotNull final Engine<?> ENGINE,
                                   @NotNull List<Zip<HMTextType,String>> inputs) {
        final BaseActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> THIS.rxa_input(ENGINE, a.A, a.B))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param engine {@link Engine} instance.
     * @param input {@link TextInputType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_input(Engine, HMInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default Flowable<WebElement> rxa_randomInput(@NotNull Engine<?> engine,
                                                 @NotNull HMTextType input) {
        return rxa_input(engine, input, input.randomInput());
    }

    /**
     * Enter random inputs for a {@link List} of {@link HMTextType}.
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link HMTextType}.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     */
    @NotNull
    default Flowable<?> rxa_randomInputs(@NotNull final Engine<?> ENGINE,
                                         @NotNull List<HMTextType> inputs) {
        final BaseActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> THIS.rxa_randomInput(ENGINE, a))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Same as above, but uses a varargs of {@link HMTextType}.
     * @param engine {@link Engine} instance.
     * @param inputs Varargs of {@link HMTextType}.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_randomInputs(@NotNull Engine<?> engine,
                                         @NotNull HMTextType...inputs) {
        return rxa_randomInputs(engine, Arrays.asList(inputs));
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)   )
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_clickInputField(@NotNull final Engine<?> ENGINE,
                                            @NotNull HMInputType input) {
        return rxe_editField(ENGINE, input).flatMap(ENGINE::rxa_click);
    }

    /**
     * Confirm a text input.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#localizer()
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_BUTTON
     * @see Platform#IOS
     * @see org.swiften.javautilities.localizer.LocalizerType#localize(String)
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#containsText(String)
     */
    @NotNull
    default Flowable<?> rxa_confirmTextInput(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            String done = "input_title_done";
            String localized = ENGINE.localizer().localize(done);

            XPath xPath = XPath.builder(Platform.IOS)
                .addClass(IOSView.ViewType.UI_BUTTON.className())
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
     * @see Engine#rxv_isLastInput(WebElement)
     * @see Engine#rxa_toggleNextOrFinishInput(WebElement)
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

    /**
     * Select a value, assuming the user is in the value selection picker.
     * @param engine {@link Engine} instance.
     * @param input {@link SLNumericChoiceType} instance.
     * @param selected {@link String} value.
     * @return {@link Flowable} instance.
     * @see ChoiceParam.Builder#withGeneralMode()
     * @see ChoiceParam.Builder#withInput(ChoiceInputType)
     * @see ChoiceParam.Builder#withSelectedChoice(String)
     * @see Engine#rxa_selectChoice(ChoiceType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectChoice(@NotNull Engine<?> engine,
                                         @NotNull HMChoiceType input,
                                         @NotNull String selected) {
        LogUtil.printfThread("Selecting %s for %s", selected, input);

        return engine.rxa_selectChoice(ChoiceParam.builder()
            .withGeneralMode()
            .withInput(input)
            .withSelectedChoice(selected)
            .build());
    }

    /**
     * Select values for a set {@link SLNumericChoiceType}. For e.g., this is
     * useful when we want to select {@link Height} or {@link Weight} based
     * on different units of measurement (metric/imperial), since the app
     * requires a combination of two inputs from two
     * {@link SLNumericChoiceType} (e.g. {@link Height#CM} and
     * {@link Height#CM_DEC}).
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link Zip} instances.
     * @return {@link Flowable} instance.
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends HMChoiceType> Flowable<?> rxa_selectChoice(
        @NotNull final Engine<?> ENGINE,
        @NotNull List<Zip<P,String>> inputs
    ) {
        final BaseActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> THIS.rxa_selectChoice(ENGINE, a.A, a.B))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}