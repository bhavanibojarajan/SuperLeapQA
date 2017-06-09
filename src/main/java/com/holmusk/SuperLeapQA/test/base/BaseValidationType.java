package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.DrawerItem;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
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
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_backButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attributes attrs = Attributes.of(Platform.ANDROID);

            Attribute navUp = Attribute.<String>builder()
                .addAttribute("content-desc")
                .withValue("Navigate up")
                .withFormatible(new Attributes.ContainsString() {})
                .build();

            Attribute collapse = Attribute.<String>builder()
                .addAttribute("content-desc")
                .withValue("Collapse")
                .withFormatible(new Attributes.ContainsString() {})
                .build();

            Attribute btnBack = attrs.containsID("btnBack");

            return engine
                .rxe_withAttributes(btnBack, navUp, collapse)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine.rxe_containsID("ob back").firstElement().toFlowable();
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
     * {@link InputType}.
     * @param engine {@link Engine} instance.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#platform()
     * @see Engine#rxe_withXPath(XPath...)
     * @see HMInputType#inputViewXP(PlatformType)
     */
    @NotNull
    default Flowable<WebElement> rxe_editField(@NotNull Engine<?> engine,
                                               @NotNull HMInputType input) {
        PlatformType platform = engine.platform();

        return engine
            .rxe_withXPath(input.inputViewXP(platform))
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
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_drawerToggle(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            Attribute attr = Attribute.<String>builder()
                .addAttribute("content-desc")
                .withValue("Open navigation drawer")
                .build();

            return engine.rxe_withAttributes(attr).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the {@link WebElement} that corresponds to {@link DrawerItem}.
     * @param engine {@link Engine} instance.
     * @param item {@link DrawerItem} instance.
     * @return {@link Flowable} instance.
     * @see DrawerItem#drawerItemXP(PlatformType)
     * @see Engine#platform()
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    default Flowable<WebElement> rxe_drawerItem(@NotNull Engine<?> engine,
                                                @NotNull DrawerItem item) {
        PlatformType platform = engine.platform();
        XPath xPath = item.drawerItemXP(platform);
        return engine.rxe_withXPath(xPath);
    }

    /**
     * Check if the drawer is open.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see DrawerItem#values()
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_drawerItem(Engine, DrawerItem)
     */
    @NotNull
    default Flowable<Boolean> rxv_isDrawerOpen(@NotNull final Engine<?> E) {
        final BaseValidationType THIS = this;

        return Flowable
            .fromArray(DrawerItem.values())
            .flatMap(a -> THIS.rxe_drawerItem(E, a))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .onErrorReturnItem(false);
    }
}
