package com.holmusk.SuperLeapQA.test.sha;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 6/3/17.
 */
public interface SHAValidationType extends BaseValidationType {
    /**
     * Get the yes button for
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_shaYes(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("sha_title_yes")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the no button for
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#SHA}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_shaNo(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("sha_title_no")
            .firstElement()
            .toFlowable();
    }
}
