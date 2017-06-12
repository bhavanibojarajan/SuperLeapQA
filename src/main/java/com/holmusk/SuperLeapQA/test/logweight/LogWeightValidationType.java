package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 9/6/17.
 */
public interface LogWeightValidationType extends HMCircleScrollValidationType {
    /**
     * Override this method to provide default implementation.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see HMCircleScrollValidationType#rxe_scrollableCSSView(Engine)
     * @see Engine#rxe_containsID(String...)
     * @see IOSView.ViewType#UI_TABLE_VIEW
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @Override
    default Flowable<WebElement> rxe_scrollableCSSView(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("bis_add_weight")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_TABLE_VIEW.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight submit {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightValueSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("action_done")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("weightLog_title_next")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight entry submit {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_weightValueSubmit(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightEntrySubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_weightValueSubmit(engine);
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("weightLog_title_save")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight display {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see CompoundAttribute#withIndex(Integer)
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_TABLE_VIEW
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightDisplay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_add_weight_tag_weightvalue")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            String tbv = IOSView.ViewType.UI_TABLE_VIEW.className();
            String tbc = IOSView.ViewType.UI_TABLE_VIEW_CELL.className();
            String stc = IOSView.ViewType.UI_STATIC_TEXT.className();

            XPath xPath = XPath.builder()
                .addAttribute(CompoundAttribute.forClass(tbv))
                .addAttribute(CompoundAttribute.forClass(tbc).withIndex(1))
                .addAttribute(CompoundAttribute.forClass(stc))
                .build();

            return engine.rxe_withXPath(xPath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight time display {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see CompoundAttribute#single(Attribute)
     * @see Engine#localizer()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see LocalizerType#localize(String)
     * @see XPath.Builder#followingSibling(CompoundAttribute, CompoundAttribute)
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightTime(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_add_weight_tag_set_time")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            LocalizerType localizer = engine.localizer();
            String localized = localizer.localize("weightLog_title_dateTime");
            String stc = IOSView.ViewType.UI_STATIC_TEXT.className();
            Attributes attrs = Attributes.of(Platform.IOS);
            Attribute sta = attrs.containsText(localized);

            XPath xPath = XPath.builder()
                .followingSibling(
                    CompoundAttribute.forClass(stc),
                    CompoundAttribute.single(sta)
                )
                .build();

            return engine.rxe_withXPath(xPath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight location switch {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see AndroidView.ViewType#SWITCH
     * @see IOSView.ViewType#UI_SWITCH
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightLocSwitch(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.ViewType.SWITCH.className())
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_SWITCH.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT_VALUE}
     * weight selection screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_scrollableCSSView(Engine)
     * @see #rxe_weightValueSubmit(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_logWeightValue(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("css_title_dragTheCircle"),
                rxe_scrollableCSSView(engine),
                rxe_weightValueSubmit(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT_VALUE}
     * weight entry screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_weightDisplay(Engine)
     * @see #rxe_weightEntrySubmit(Engine)
     * @see #rxe_weightLocSwitch(Engine)
     * @see #rxe_weightTime(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_logWeightEntry(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_weightDisplay(engine),
                rxe_weightTime(engine),
                rxe_weightLocSwitch(engine),
                rxe_weightEntrySubmit(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if the weight time picker is visible on the screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see WebElement#isDisplayed()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<Boolean> rxv_weightTimePickerOpen(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("date_title_today")
                .firstElement()
                .toFlowable()
                .map(WebElement::isDisplayed)
                .onErrorReturnItem(false);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the currently displayed weight.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#getText(WebElement)
     * @see #rxe_weightDisplay(Engine)
     */
    @NotNull
    default Flowable<String> rxe_selectedWeight(@NotNull final Engine<?> ENGINE) {
        return rxe_weightDisplay(ENGINE)
            .map(ENGINE::getText)
            .doOnNext(a -> LogUtil.printft("Selected weight: %s", a));
    }
}
