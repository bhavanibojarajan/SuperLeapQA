package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollActionType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

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
}
