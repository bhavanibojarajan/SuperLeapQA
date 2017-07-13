package com.holmusk.SuperLeapQA.test.guarantorinfo;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

/**
 * Created by haipham on 6/7/17.
 */
public interface GuarantorInfoValidationType extends BaseValidationType {
    /**
     * Get the submit button for
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#GUARANTOR_INFO}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<WebElement> rxe_guarantorInfoSubmit(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rxe_containsID("btnNext").firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("register_title_submit")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
