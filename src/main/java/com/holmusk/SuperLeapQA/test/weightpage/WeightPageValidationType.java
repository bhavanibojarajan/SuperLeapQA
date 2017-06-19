package com.holmusk.SuperLeapQA.test.weightpage;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 6/11/17.
 */
public interface WeightPageValidationType extends BaseValidationType {
    default Flowable<WebElement> rxe_prevWeightDisplay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_item_activity_weight_detail_valueleft")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    @NotNull
    default Flowable<WebElement> rxe_weightChangeDisplay(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_item_activity_weight_detail_valuemid")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
