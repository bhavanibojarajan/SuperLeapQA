package com.holmusk.SuperLeapQA.ui.base;

/**
 * Created by haipham on 5/7/17.
 */

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.model.InputType;

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
     * @see Engine#rx_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxBackButton(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsID("btnBack").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine.rx_containsID("ob back").firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the common probress bar.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_progressBar(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_containsID("pb_general").firstElement().toFlowable();
        } else {
            return RxUtil.error();
        }
    }

    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param engine {@link Engine} instance.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#platform()
     * @see Engine#rx_withXPath(XPath...)
     * @see SLInputType#inputViewXPath(PlatformType)
     */
    @NotNull
    default Flowable<WebElement> rxe_editField(@NotNull Engine<?> engine,
                                               @NotNull SLInputType input) {
        PlatformType platform = engine.platform();

        return engine
            .rx_withXPath(input.inputViewXPath(platform))
            .firstElement()
            .toFlowable();
    }
}
