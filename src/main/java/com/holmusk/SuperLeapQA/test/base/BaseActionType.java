package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.HMUITestKit.test.datetime.HMDateTimeActionType;
import com.holmusk.SuperLeapQA.model.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.HPReactives;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.choice.ChoiceParam;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.base.model.TextInputType;
import org.swiften.xtestkit.base.param.DirectionParam;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.coordinate.RLPoint;
import org.swiften.xtestkitcomponents.direction.Direction;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 * This is the base action interface that contains common action methods that
 * should not be declared in the action intefaces for specific screens.
 */
public interface BaseActionType extends BaseValidationType, HMDateTimeActionType {
    /**
     * Navigate backwards by clicking the back button.
     * @return {@link Flowable} instance.
     * @param engine {@link Engine} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_backButton(Engine)
     */
    @NotNull
    default Flowable<?> rxa_clickBackButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_backButton(engine)
                .compose(engine.clickFn())
                .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
        } else if (engine instanceof IOSEngine) {
            /* Click on the coordinates directly to avoid flakiness */
            return rxe_backButton(engine)
                .compose(engine.tapMiddleFn())
                .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_progressBar(Engine)
     */
    @NotNull
    default Flowable<?> rxa_watchProgressBar(@NotNull Engine<?> engine) {
        return engine.rxa_watchUntilHidden(rxe_progressBar(engine));
    }

    /**
     * Enter an input for {@link TextInput}.
     * @param input {@link TextInputType} instance.
     * @param text {@link String} value.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<Boolean> rxa_input(@NotNull Engine<?> engine,
                                        @NotNull HMInputType input,
                                        @NotNull String text) {
        return rxe_editField(engine, input).compose(engine.sendValueFn(text));
    }

    /**
     * Enter inputs for a {@link List} of {@link HMTextType}.
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link HMTextType}.
     * @return {@link Flowable} instance.
     * @see #rxa_input(Engine, HMInputType, String)
     * @see #rxa_makeNextInputVisible(Engine)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default Flowable<?> rxa_inputs(@NotNull final Engine<?> ENGINE,
                                   @NotNull List<Tuple<HMTextType,String>> inputs) {
        final BaseActionType THIS = this;

        return Flowable.fromIterable(inputs)
            .concatMap(a -> Flowable.concatArray(
                THIS.rxa_input(ENGINE, a.A, a.B),
                THIS.rxa_makeNextInputVisible(ENGINE)
            )).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput(InputHelperType)}.
     * @param engine {@link Engine} instance.
     * @param input {@link TextInputType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_input(Engine, HMInputType, String)
     */
    @NotNull
    default Flowable<Boolean> rxa_randomInput(@NotNull Engine<?> engine,
                                              @NotNull HMTextType input) {
        return rxa_input(engine, input, input.randomInput(engine));
    }

    /**
     * Enter random inputs for a {@link List} of {@link HMTextType}.
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link HMTextType}.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_makeNextInputVisible(Engine)
     */
    @NotNull
    default Flowable<?> rxa_randomInputs(@NotNull final Engine<?> ENGINE,
                                         @NotNull List<HMTextType> inputs) {
        final BaseActionType THIS = this;

        return Flowable.fromIterable(inputs)
            .concatMap(a -> Flowable.concatArray(
                THIS.rxa_randomInput(ENGINE, a),
                THIS.rxa_makeNextInputVisible(ENGINE)
            )).all(HPObjects::nonNull).toFlowable();
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
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<Boolean> rxa_clickInput(@NotNull Engine<?> engine,
                                             @NotNull HMInputType input) {
        return rxe_editField(engine, input).compose(engine.clickFn());
    }

    /**
     * Confirm a text input.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     */
    @NotNull
    default Flowable<?> rxa_confirmTextInput(@NotNull final Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            String done = "input_title_done";
            String localized = engine.localizer().localize(done);
            Attributes attrs = Attributes.of(engine);

            Attribute attribute = attrs.containsText(localized);

            CompoundAttribute cAttr = CompoundAttribute.single(attribute)
                .withClass(IOSView.Type.UI_BUTTON);

            XPath xpath = XPath.builder().addAttribute(cAttr).build();
            return engine.rxe_withXPath(xpath).compose(engine.clickFn());
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Make the next input field visible on the screen, by toggling next input
     * or dismissing the keyboard.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_makeNextInputVisible(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            /* Since sending key events is rather flaky, we can instead hide
             * the keyboard to expose the next input field */
            return engine.rxa_hideKeyboard();
        } else if (engine instanceof IOSEngine) {
            return rxa_confirmTextInput(engine);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the next input for an input-based {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_toggleNextInput(@NotNull Engine<?> engine,
                                            @NotNull WebElement element) {
        /* In this case, each input will have up/down keyboard accessories
         * that we can use to navigate to the previous/next input fields */
        return engine.rxe_containsID("ob downArrow").compose(engine.clickFn());
    }

    /**
     * Toggle the previous input for an input-based {@link WebElement}. This
     * is only relevant for {@link Platform#IOS} since it has up/down arrow
     * buttons.
     * @param engine {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_togglePreviousInput(@NotNull Engine<?> engine,
                                                @NotNull WebElement element) {
        if (engine instanceof IOSEngine) {
            return engine.rxe_containsID("ob upArrow").compose(engine.clickFn());
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
     */
    @NotNull
    default Flowable<?> rxa_selectChoice(@NotNull Engine<?> engine,
                                         @NotNull HMChoiceType input,
                                         @NotNull String selected) {
        HPLog.printft("Selecting %s for %s", selected, input);

        /* Localize the selected choice just in case */
        String localized = engine.localizer().localize(selected);

        return engine.rxa_selectChoice(ChoiceParam.builder()
            .withGeneralMode()
            .withInput(input)
            .withSelectedChoice(localized)
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
     * @param inputs {@link List} of {@link Tuple} instances.
     * @return {@link Flowable} instance.
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends HMChoiceType> Flowable<?> rxa_selectChoice(
        @NotNull final Engine<?> ENGINE,
        @NotNull List<Tuple<P,String>> inputs
    ) {
        final BaseActionType THIS = this;

        return Flowable.fromIterable(inputs)
            .concatMap(a -> THIS.rxa_selectChoice(ENGINE, a.A, a.B))
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Scroll to the bottom of the screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    default Flowable<?> rxa_scrollToBottom(@NotNull final Engine<?> ENGINE) {
        final DirectionParam PARAM = DirectionParam.builder()
            .withDirection(Direction.DOWN_UP)
            .withStartRatio(0.1d)
            .withEndRatio(0.9d)
            .withTimes(1)
            .build();

        return ENGINE.rxe_window().flatMap(a -> ENGINE.rxa_swipeGeneric(a, PARAM));
    }

    /**
     * Open the drawer.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_drawerToggle(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openDrawer(@NotNull Engine<?> engine) {
        return rxe_drawerToggle(engine).compose(engine.clickFn());
    }

    /**
     * Close the drawer.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     */
    @NotNull
    default Flowable<?> rxa_closeDrawer(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return ENGINE.rxe_window()
                .firstElement()
                .toFlowable()
                .map(a -> ENGINE.coordinate(a, RLPoint.MAX, RLPoint.MID))
                .map(a -> a.moveBy(-20, -20))
                .compose(ENGINE.tapPointFn());
        } else if (ENGINE instanceof IOSEngine) {
            return ENGINE
                .rxe_containsID("reward close")
                .firstElement()
                .toFlowable()
                .compose(ENGINE.clickFn());
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Toggle the drawer open/closed, but first check whether it is already
     * in the specified state.
     * @param ENGINE {@link Engine} instance.
     * @param OPEN {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxa_closeDrawer(Engine)
     * @see #rxa_openDrawer(Engine)
     * @see #rxv_isDrawerOpen(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleDrawer(@NotNull final Engine<?> ENGINE,
                                         final boolean OPEN) {
        final BaseActionType THIS = this;

        return rxv_isDrawerOpen(ENGINE)
            .filter(a -> a != OPEN)
            .flatMap(a -> {
                if (OPEN) {
                    return THIS.rxa_openDrawer(ENGINE);
                } else {
                    return THIS.rxa_closeDrawer(ENGINE);
                }
            })
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)
            .defaultIfEmpty(true);
    }

    /**
     * Select {@link DrawerItem}.
     * @param engine {@link Engine} instance.
     * @param item {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_drawerItem(Engine, DrawerItem)
     */
    @NotNull
    default Flowable<?> rxa_selectDrawerItem(@NotNull Engine<?> engine,
                                             @NotNull DrawerItem item) {
        return rxe_drawerItem(engine, item).compose(engine.clickFn());
    }

    /**
     * Open the drawer and select {@link DrawerItem}.
     * @param ENGINE {@link Engine} instance.
     * @param ITEM {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_selectDrawerItem(Engine, DrawerItem)
     * @see #rxa_toggleDrawer(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_openDrawerAndSelect(@NotNull final Engine<?> ENGINE,
                                                @NotNull final DrawerItem ITEM) {
        final BaseActionType THIS = this;

        return THIS
            .rxa_toggleDrawer(ENGINE, true)
            .flatMap(a -> THIS.rxa_selectDrawerItem(ENGINE, ITEM));
    }

    /**
     * Navigate back to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_dashboardBack(Engine)
     */
    @NotNull
    default Flowable<?> rxa_backToDashboard(@NotNull Engine<?> engine) {
        HPLog.printlnt("Going back to dashboard");
        return rxe_dashboardBack(engine).compose(engine.clickFn());
    }

    /**
     * Dismiss the meal image tutorial. However, if this is not the first time
     * the user is logging a meal and there is no such tutorial, simply swallow
     * the error.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_mealImageTutDismiss(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissMealImageTutorial(@NotNull Engine<?> engine) {
        return rxe_mealImageTutDismiss(engine)
            .compose(engine.clickFn())
            .map(HPBooleans::toTrue)
            .onErrorReturnItem(true);
    }

    /**
     * Toggle edit mode, then delay for a while for the menu to fully appear.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_editToggle(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openEditMenu(@NotNull Engine<?> engine) {
        return rxe_editToggle(engine)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }

    /**
     * Delete some content from the item menu.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #contentDeleteProgressDelay(Engine)
     * @see #rxe_menuDelete(Engine)
     * @see #rxe_menuDeleteConfirm(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_deleteFromMenu(@NotNull Engine<?> engine) {
        return HPReactives
            .concatDelayEach(
                generalDelay(engine),
                rxe_menuDelete(engine).compose(engine.clickFn()),
                rxe_menuDeleteConfirm(engine).compose(engine.clickFn())
            )
            .all(HPObjects::nonNull)
            .toFlowable()
            .delay(contentDeleteProgressDelay(engine), TimeUnit.MILLISECONDS);
    }
}
