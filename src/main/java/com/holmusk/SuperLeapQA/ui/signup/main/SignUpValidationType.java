package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.type.PlatformType;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpValidationType extends BaseActionType {
    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param engine {@link Engine} instance.
     * @param input {@link InputType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#platform()
     * @see Engine#rx_withXPath(XPath...)
     * @see RxUtil#error(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rx_e_editField(@NotNull Engine<?> engine,
                                                @NotNull SLInputType input) {
        PlatformType platform = engine.platform();

        return engine
            .rx_withXPath(input.inputViewXPath(platform))
            .firstElement()
            .toFlowable();
    }
}
