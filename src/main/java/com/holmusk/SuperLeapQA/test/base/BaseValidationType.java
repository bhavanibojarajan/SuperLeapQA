package com.holmusk.SuperLeapQA.test.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.HMUITestKit.model.HMInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.base.model.InputType;

/**
 * Interfaces that extend this should declare methods that assist with app
 * validation (e.g. make sure all views are present).
 */
public interface BaseValidationType extends BaseErrorType, AppDelayType {
    /**
     * Get the common back button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#platform()
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_backButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("btnBack").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine.rxe_containsID("ob back").firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the common probress bar.
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
}