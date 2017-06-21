package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.DrawerItem;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.ios.element.locator.AndroidXMLAttribute;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.common.ClassNameType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.XMLAttributeType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Interfaces that extend this should declare methods that assist with app
 * validation (e.g. make sure all views are present).
 */
public interface BaseValidationType extends BaseErrorType, AppDelayType {
    /**
     * Get the common back button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attribute.Builder#addAttribute(String)
     * @see Attribute.Builder#withFormatible(Formatible)
     * @see Attribute.Builder#withValue(Object)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformProviderType)
     * @see Engine#platform()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withAttributes(Attribute[])
     * @see Formatibles#containsString()
     * @see XMLAttributeType#value()
     * @see AndroidXMLAttribute#CONTENT_DESC
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_backButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attributes attrs = Attributes.of(engine);

            Attribute navUp = Attribute.<String>builder()
                .addAttribute(AndroidXMLAttribute.CONTENT_DESC.value())
                .withValue("Navigate up")
                .withFormatible(Formatibles.containsString())
                .build();

            Attribute collapse = Attribute.<String>builder()
                .addAttribute(AndroidXMLAttribute.CONTENT_DESC.value())
                .withValue("Collapse")
                .withFormatible(Formatibles.containsString())
                .build();

            Attribute btnBack = attrs.containsID("btnBack");

            return engine
                .rxe_withAttributes(btnBack, navUp, collapse)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("ob back", "button back")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the common progress bar.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_progressBar(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.Type.PROGRESS_BAR)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("Loading..", "Indeterminate Progress")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link HMInputType}.
     * @param engine {@link Engine} instance.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_withXPath(XPath...)
     * @see HMInputType#inputViewXP(InputHelperType)
     */
    @NotNull
    default Flowable<WebElement> rxe_editField(@NotNull Engine<?> engine,
                                               @NotNull HMInputType input) {
        return engine
            .rxe_withXPath(input.inputViewXP(engine))
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the current value displayed by {@link WebElement} instance.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT {@link HMInputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#getText(WebElement)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<String> rxe_fieldValue(@NotNull final Engine<?> ENGINE,
                                            @NotNull final HMInputType INPUT) {
        return rxe_editField(ENGINE, INPUT).map(ENGINE::getText)
            .doOnNext(a -> LogUtil.printft("Value for %s: %s", INPUT, a));
    }

    /**
     * Check if an editable field has an input.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT {@link InputType} instance.
     * @param VALUE {@link String} value.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#getText(WebElement)
     * @see Engine#rxv_errorWithPageSource(String)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<?> rxv_fieldHasValue(@NotNull final Engine<?> ENGINE,
                                          @NotNull final HMInputType INPUT,
                                          @NotNull final String VALUE) {
        return rxe_fieldValue(ENGINE, INPUT)
            .filter(a -> a.toLowerCase().equals(VALUE.toLowerCase()))
            .switchIfEmpty(ENGINE.rxv_errorWithPageSource(String.format(
                "Value for %s does not equal %s", INPUT, VALUE)))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the confirm button for numeric choice inputs
     * (e.g. {@link com.holmusk.SuperLeapQA.model.Height} and
     * {@link com.holmusk.SuperLeapQA.model.Weight}).
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_numericChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("btnDone")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("input_title_done")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the confirm button for text choice inputs (e.g.
     * {@link com.holmusk.SuperLeapQA.model.ChoiceInput#ETHNICITY} or
     * {@link com.holmusk.SuperLeapQA.model.ChoiceInput#COACH_PREF}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_textChoiceConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("input_title_done")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }


    /**
     * Get the drawer toggle {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attribute.Builder#addAttribute(String)
     * @see Attribute.Builder#withValue(Object)
     * @see Engine#rxe_withAttributes(Attribute[])
     * @see Engine#rxe_containsID(String...)
     * @see XMLAttributeType#value()
     * @see AndroidXMLAttribute#CONTENT_DESC
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_drawerToggle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attribute attr = Attribute.<String>builder()
                .addAttribute(AndroidXMLAttribute.CONTENT_DESC.value())
                .withValue("Open navigation drawer")
                .build();

            return engine.rxe_withAttributes(attr).firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("button hamburger menu")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the {@link WebElement} that corresponds to {@link DrawerItem}.
     * @param engine {@link Engine} instance.
     * @param item {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see DrawerItem#drawerItemXP(Engine)
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    default Flowable<WebElement> rxe_drawerItem(@NotNull Engine<?> engine,
                                                @NotNull DrawerItem item) {
        XPath xpath = item.drawerItemXP(engine);
        return engine.rxe_withXPath(xpath);
    }

    /**
     * Validate the drawer in the
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see DrawerItem#values()
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_drawerItem(Engine, DrawerItem)
     */
    @NotNull
    default Flowable<?> rxv_drawer(@NotNull final Engine<?> ENGINE) {
        final BaseValidationType THIS = this;

        return Flowable
            .fromArray(DrawerItem.values())
            .flatMap(a -> THIS.rxe_drawerItem(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if the drawer is open.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isTrue(boolean)
     * @see #rxv_drawer(Engine)
     */
    @NotNull
    default Flowable<Boolean> rxv_isDrawerOpen(@NotNull Engine<?> engine) {
        return rxv_drawer(engine).map(BooleanUtil::isTrue).onErrorReturnItem(false);
    }

    /**
     * Get the button that we can click to get back to
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}. This is
     * usually used when we are in
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_PAGE}, for e.g.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attribute.Builder#addAttribute(String)
     * @see Attribute.Builder#withValue(Object)
     * @see Attribute.Builder#withFormatible(Formatible)
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see Formatibles#containsString()
     * @see Point#getX()
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see WebElement#getLocation()
     * @see IOSView.Type#UI_BUTTON
     * @see #NOT_AVAILABLE
     * @see #rxe_backButton(Engine)
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardBack(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_backButton(engine).firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.Type.UI_BUTTON)
                .filter(a -> a.getLocation().getX() == 0)
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the edit {@link WebElement} that can be used to alter/delete content.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_editToggle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("action_menu")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("button edit")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the menu delete button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_menuDelete(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("edit_title_delete")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the image tutorial dismiss {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealImageTutorialDismiss(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealPage_title_gotIt")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the edit menu delete confirm button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#hasText(String)
     * @see Attributes#of(PlatformProviderType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see CompoundAttribute.Builder#withClass(ClassNameType)
     * @see Engine#localizer()
     * @see Engine#platform()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see AndroidView.Type#BUTTON
     * @see IOSView.Type#UI_BUTTON
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_menuDeleteConfirm(@NotNull Engine<?> engine) {
        Attributes attrs = Attributes.of(engine);
        LocalizerType localizer = engine.localizer();
        String localized = localizer.localize("edit_title_delete");

        ClassNameType btnCls;

        if (engine instanceof AndroidEngine) {
            btnCls = AndroidView.Type.BUTTON;
        } else if (engine instanceof IOSEngine) {
            btnCls = IOSView.Type.UI_BUTTON;
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .withClass(btnCls)
            .addAttribute(attrs.hasText(localized))
            .build();

        XPath xpath = XPath.builder().addAttribute(cAttr).build();
        return engine.rxe_withXPath(xpath).firstElement().toFlowable();
    }
}
