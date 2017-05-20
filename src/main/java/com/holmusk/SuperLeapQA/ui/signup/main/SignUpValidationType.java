package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpValidationType extends BaseActionType {
    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param engine {@link Engine} instance.
     * @param input A {@link InputType} instance.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see Engine#rx_withXPath(XPath...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default <P extends SLInputType> Flowable<WebElement>
    rx_e_editField(@NotNull Engine<?> engine, @NotNull P input) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rx_withXPath(input.androidViewXPath())
                .firstElement()
                .toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }
}
