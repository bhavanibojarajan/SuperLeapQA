package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.view.BaseViewType;

/**
 * Created by haipham on 9/6/17.
 */
public interface LogWeightValidationType extends HMCircleScrollValidationType {
    /**
     * Override this method to provide default implementation.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see HMCircleScrollValidationType#rxe_scrollableCSSView(Engine)
     * @see Engine#rxe_containsID(String...)
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
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight submit {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("action_done")
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
     * @see #rxe_weightSubmit(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightEntrySubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_weightSubmit(engine);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight display {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightDisplay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_add_weight_tag_weightvalue")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the weight time display {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_weightTime(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_add_weight_tag_set_time")
                .firstElement()
                .toFlowable();
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
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightLocSwitch(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.ViewType.SWITCH.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT}
     * weight selection screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_scrollableCSSView(Engine)
     * @see #rxe_weightSubmit(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_logWeightValue(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("css_title_dragTheCircle"),
                rxe_scrollableCSSView(engine),
                rxe_weightSubmit(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT}
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
}
