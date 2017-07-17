package com.holmusk.SuperLeapQA.test.weightpage;

import com.holmusk.SuperLeapQA.model.WeightProgress;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/11/17.
 */
public interface WeightPageValidationType extends BaseValidationType {
    /**
     * Get the display {@link WebElement} that corresponds to
     * {@link WeightProgress} instance.
     * @param engine {@link Engine} instance.
     * @param type {@link WeightProgress} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_withXPath(XPath...)
     * @see WeightProgress#valueXP(InputHelperType)
     */
    @NotNull
    default Flowable<WebElement> rxe_weightProgressDisplay(
        @NotNull Engine<?> engine,
        @NotNull WeightProgress type
    ) {
        XPath xPath = type.valueXP(engine);
        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }

    /**
     * Get the weight progress value as {@link Double}.
     * @param ENGINE {@link Engine} instance.
     * @param WP {@link WeightProgress} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_weightProgressDisplay(Engine, WeightProgress)
     */
    @NotNull
    default Flowable<Double> rxe_weightProgress(@NotNull final Engine<?> ENGINE,
                                                @NotNull final WeightProgress WP) {
        return rxe_weightProgressDisplay(ENGINE, WP)
            .map(ENGINE::getText)
            .map(Double::valueOf)
            .doOnNext(a -> HPLog.printft("Value for %s is %s", WP, a));
    }
}
