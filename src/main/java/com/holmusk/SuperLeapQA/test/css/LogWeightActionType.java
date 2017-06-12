package com.holmusk.SuperLeapQA.test.css;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollActionType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 9/6/17.
 */
public interface LogWeightActionType extends
    BaseActionType,
    HMCircleScrollActionType,
    LogWeightValidationType
{
    /**
     * Toggle weight location.
     * @param E {@link Engine} instance.
     * @param ON {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_weightLocSwitch(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleWeightLocation(@NotNull final Engine<?> E,
                                                 final boolean ON) {
        return rxe_weightLocSwitch(E).flatMap(a -> E.rxa_toggleSwitch(a, ON));
    }

    /**
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_WEIGHT_ENTRY}
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}. We do not
     * use this with {@link com.holmusk.SuperLeapQA.navigation.ScreenHolder}
     * because the flows for both platforms are different in such a way that
     * it causes {@link StackOverflowError} when we use
     * {@link org.swiften.xtestkit.navigation.ScreenManagerType#multiNodes(ScreenType...)}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_dashboardFromWeightEntry(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxa_clickBackButton(engine);
        } else if (engine instanceof IOSEngine) {
            final LogWeightActionType THIS = this;

            return Flowable.range(0, 2)
                .concatMap(a -> THIS.rxa_clickBackButton(engine))
                .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
