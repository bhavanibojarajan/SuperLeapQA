package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.HMUITestKit.test.datetime.HMDateTimeActionType;
import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.HPReactives;
import org.swiften.javautilities.util.LogUtil;
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
     * @param ENGINE {@link Engine} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_backButton(Engine)
     */
    @NotNull
    default Flowable<?> rxa_clickBackButton(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof AndroidEngine) {
            return rxe_backButton(ENGINE)
                .flatMap(ENGINE::rxa_click)
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else if (ENGINE instanceof IOSEngine) {
            /* Click on the coordinates directly to avoid flakiness */
            return rxe_backButton(ENGINE)
                .flatMap(ENGINE::rxa_tapMiddle)
                .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
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
     * @param TEXT {@link String} value.
     * @return {@link Flowable} instance.
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<WebElement> rxa_input(@NotNull final Engine<?> ENGINE,
                                           @NotNull HMInputType input,
                                           @NotNull final String TEXT) {
        return rxe_editField(ENGINE, input).flatMap(a -> ENGINE.rxa_sendValue(a, TEXT));
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
    default Flowable<WebElement> rxa_randomInput(@NotNull Engine<?> engine,
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
    default Flowable<WebElement> rxa_clickInput(@NotNull final Engine<?> ENGINE,
                                                @NotNull HMInputType input) {
        return rxe_editField(ENGINE, input).flatMap(ENGINE::rxa_click);
    }

    /**
     * Confirm a text input.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     */
    @NotNull
    default Flowable<?> rxa_confirmTextInput(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            String done = "input_title_done";
            String localized = ENGINE.localizer().localize(done);
            Attributes attrs = Attributes.of(ENGINE);

            Attribute attribute = attrs.containsText(localized);

            CompoundAttribute cAttr = CompoundAttribute.single(attribute)
                .withClass(IOSView.Type.UI_BUTTON);

            XPath xpath = XPath.builder().addAttribute(cAttr).build();
            return ENGINE.rxe_withXPath(xpath).flatMap(ENGINE::rxa_click);
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
     * @param E {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
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
     */
    @NotNull
    default Flowable<?> rxa_selectChoice(@NotNull Engine<?> engine,
                                         @NotNull HMChoiceType input,
                                         @NotNull String selected) {
        LogUtil.printft("Selecting %s for %s", selected, input);

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
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_drawerToggle(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openDrawer(@NotNull final Engine<?> ENGINE) {
        return rxe_drawerToggle(ENGINE).flatMap(ENGINE::rxa_click);
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
                .flatMap(ENGINE::rxa_tap);
        } else if (ENGINE instanceof IOSEngine) {
            return ENGINE
                .rxe_containsID("reward close")
                .firstElement()
                .toFlowable()
                .flatMap(ENGINE::rxa_click);
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
     * @param ENGINE {@link Engine} instance.
     * @param item {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_drawerItem(Engine, DrawerItem)
     */
    @NotNull
    default Flowable<?> rxa_selectDrawerItem(@NotNull final Engine<?> ENGINE,
                                             @NotNull DrawerItem item) {
        return rxe_drawerItem(ENGINE, item).flatMap(ENGINE::rxa_click);
    }

    /**
     * Open the drawer and select {@link DrawerItem}.
     * @param E {@link Engine} instance.
     * @param DI {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_selectDrawerItem(Engine, DrawerItem)
     * @see #rxa_toggleDrawer(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_openDrawerAndSelect(@NotNull final Engine<?> E,
                                                @NotNull final DrawerItem DI) {
        final BaseActionType THIS = this;
        return rxa_toggleDrawer(E, true).flatMap(a -> THIS.rxa_selectDrawerItem(E, DI));
    }

    /**
     * Navigate back to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_dashboardBack(Engine)
     */
    @NotNull
    default Flowable<?> rxa_backToDashboard(@NotNull final Engine<?> ENGINE) {
        LogUtil.printlnt("Going back to dashboard");
        return rxe_dashboardBack(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Dismiss the meal image tutorial. However, if this is not the first time
     * the user is logging a meal and there is no such tutorial, simply swallow
     * the error.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_mealImageTutDismiss(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissMealImageTutorial(@NotNull final Engine<?> E) {
        return rxe_mealImageTutDismiss(E)
            .flatMap(E::rxa_click)
            .map(HPBooleans::toTrue)
            .onErrorReturnItem(true);
    }

    /**
     * Toggle edit mode, then delay for a while for the menu to fully appear.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_editToggle(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openEditMenu(@NotNull final Engine<?> ENGINE) {
        return rxe_editToggle(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Delete some content from the item menu.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #contentDeleteProgressDelay(Engine)
     * @see #rxe_menuDelete(Engine)
     * @see #rxe_menuDeleteConfirm(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_deleteFromMenu(@NotNull final Engine<?> ENGINE) {
        return HPReactives
            .concatDelayEach(
                generalDelay(ENGINE),
                rxe_menuDelete(ENGINE).flatMap(ENGINE::rxa_click),
                rxe_menuDeleteConfirm(ENGINE).flatMap(ENGINE::rxa_click)
            )
            .all(HPObjects::nonNull)
            .toFlowable()
            .delay(contentDeleteProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
