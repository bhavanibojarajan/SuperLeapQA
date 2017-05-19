package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardValidationType extends BaseValidationType {
    /**
     * Get the Use App Now button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxUseAppNowButton() {
        return engine().rx_elementContainingText("dashboard_title_useAppNow");
    }

    /**
     * Validate the Use App Now screen after the user finishes sign up.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see #rxUseAppNowButton()
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUseAppNowScreen() {
        final DashboardValidationType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concatArray(
                ENGINE.rx_elementContainingText("dashboard_title_accountReadyToUse"),
                ENGINE.rx_elementContainingText("dashboard_title_rememberCheckEmail"),
                THIS.rxUseAppNowButton()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate that the tutorial screen is present when the user first signs
     * up.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxValidateDashboardTutorialScreen() {
        return engine()
            .rx_elementContainingText("dashboard_title_tapHereToMakeFirstEntry")
            .map(BooleanUtil::toTrue);
    }
}
