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
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.platform.XMLAttributeType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

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
     * @see Attribute.Builder#withFormatible(Attribute.Formatible)
     * @see Attribute.Builder#withValue(Object)
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformType)
     * @see Engine#platform()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withAttributes(Attribute[])
     * @see XMLAttributeType#value()
     * @see AndroidXMLAttribute#CONTENT_DESC
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_backButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attributes attrs = Attributes.of(Platform.ANDROID);

            Attribute navUp = Attribute.<String>builder()
                .addAttribute(AndroidXMLAttribute.CONTENT_DESC.value())
                .withValue("Navigate up")
                .withFormatible(new Attributes.ContainsString() {})
                .build();

            Attribute collapse = Attribute.<String>builder()
                .addAttribute(AndroidXMLAttribute.CONTENT_DESC.value())
                .withValue("Collapse")
                .withFormatible(new Attributes.ContainsString() {})
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
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_progressBar(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("pb_general").firstElement().toFlowable();
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
        XPath xPath = item.drawerItemXP(engine);
        return engine.rxe_withXPath(xPath);
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
     * @see Attribute.Builder#withFormatible(Attribute.Formatible)
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see Point#getX()
     * @see XPath.Builder#addAttribute(Attribute)
     * @see WebElement#getLocation()
     * @see IOSView.ViewType#UI_BUTTON
     * @see com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD
     * @see #rxe_backButton(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardBack(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_backButton(engine);
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_BUTTON.className())
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
    default Flowable<WebElement> rxe_edit(@NotNull Engine<?> engine) {
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
     * Get the edit menu delete confirm button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#hasText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(Attribute)
     * @see CompoundAttribute.Builder#withClass(String)
     * @see Engine#localizer()
     * @see Engine#platform()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see AndroidView.ViewType#BUTTON
     * @see IOSView.ViewType#UI_BUTTON
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_menuDeleteConfirm(@NotNull Engine<?> engine) {
        PlatformType platform = engine.platform();
        Attributes attrs = Attributes.of(platform);
        LocalizerType localizer = engine.localizer();
        String localized = localizer.localize("edit_title_delete");

        String btnCls;

        if (engine instanceof AndroidEngine) {
            btnCls = AndroidView.ViewType.BUTTON.className();
        } else if (engine instanceof IOSEngine) {
            btnCls = IOSView.ViewType.UI_BUTTON.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .withClass(btnCls)
            .addAttribute(attrs.hasText(localized))
            .build();

        XPath xPath = XPath.builder().addAttribute(cAttr).build();
        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }
}
