package com.holmusk.SuperLeapQA.test.logweight;

import com.holmusk.HMUITestKit.model.HMCSSInputType;
import com.holmusk.HMUITestKit.test.circlescrollselector.HMCircleScrollValidationType;
import com.holmusk.SuperLeapQA.model.CSSInput;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.javautilities.protocol.ClassNameType;

/**
 * Created by haipham on 9/6/17.
 */
public interface LogWeightValidationType extends HMCircleScrollValidationType {
    /**
     * Get the weight location switch {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see AndroidView.Type#SWITCH
     * @see IOSView.Type#UI_SWITCH
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_weightLocSwitch(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.Type.SWITCH)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.Type.UI_SWITCH)
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#WEIGHT_VALUE}
     * weight entry screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see CSSInput#WEIGHT
     * @see #rxe_CSSValueDisplay(Engine, HMCSSInputType)
     * @see #rxe_CSSDetailEntrySubmit(Engine, HMCSSInputType)
     * @see #rxe_weightLocSwitch(Engine)
     * @see #rxe_CSSLogTime(Engine, HMCSSInputType)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_weightEntry(@NotNull Engine<?> engine) {
        HMCSSInputType input = CSSInput.WEIGHT;

        return Flowable
            .mergeArray(
                rxe_CSSValueDisplay(engine, input),
                rxe_CSSLogTime(engine, input),
                rxe_weightLocSwitch(engine),
                rxe_CSSDetailEntrySubmit(engine, input)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
