package com.holmusk.SuperLeapQA.test.address;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 7/6/17.
 */
public interface AddressValidationType extends BaseValidationType {
    /**
     * Get the submit button for
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADDRESS_INFO}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_addressSubmit(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("register_title_submit", "register_title_next")
            .firstElement()
            .toFlowable();
    }
}
