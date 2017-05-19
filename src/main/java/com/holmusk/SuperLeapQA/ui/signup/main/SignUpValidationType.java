package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import org.swiften.xtestkit.base.element.action.input.type.InputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpValidationType extends BaseActionType {
    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param input A {@link InputType} instance.
     * @param <P> Generics parameter.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementWithXPath(XPath...)
     * @see RxUtil#error(String)
     * @see #NOT_IMPLEMENTED
     */
    @NotNull
    default <P extends SLInputType> Flowable<WebElement>
    rx_editFieldForInput(@NotNull P input) {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rx_elementWithXPath(input.androidViewXPath());
        } else {
            return RxUtil.error(NOT_IMPLEMENTED);
        }
    }
}
