package com.holmusk.SuperLeapQA.test.logactivity;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollValidationType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 12/6/17.
 */
public interface LogActivityValidationType extends
    BaseValidationType,
    HMCircleScrollValidationType
{
    /**
     * Validate
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ACTIVITY_ENTRY}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see CSSInput#ACTIVITY
     * @see #rxe_CSSValueDisplay(Engine, HMCSSInputType)
     * @see #rxe_CSSLogTime(Engine, HMCSSInputType)
     * @see #rxe_CSSDetailEntrySubmit(Engine, HMCSSInputType)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_activityEntry(@NotNull Engine<?> engine) {
        HMCSSInputType input = CSSInput.ACTIVITY;

        return Flowable
            .mergeArray(
                rxe_CSSValueDisplay(engine, input),
                rxe_CSSLogTime(engine, input),
                rxe_CSSDetailEntrySubmit(engine, input)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
