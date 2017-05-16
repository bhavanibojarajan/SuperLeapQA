package com.holmusk.SuperLeapQA.dashboard.common;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import io.reactivex.Flowable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;

/**
 * Created by haipham on 5/16/17.
 */
public interface BaseDashboardValidationType extends BaseValidationType {
    /**
     * Get the Use App Now button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxUseAppNowButton() {
        return engine().rxElementContainingText("dashboard_title_useAppNow");
    }

    /**
     * Validate the Use App Now screen after the user finishes sign up.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxUseAppNowButton()
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    default Flowable<Boolean> rxValidateUseAppNowScreen() {
        BaseEngine<?> engine = engine();

        return Flowable
            .concat(
                engine.rxElementContainingText("dashboard_title_accountReadyToUse"),
                engine.rxElementContainingText("dashboard_title_rememberToCheckEmail"),
                rxUseAppNowButton()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate that the tutorial screen is present when the user first signs
     * up.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingText(String)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxValidateDashboardTutorialScreen() {
        return engine()
            .rxElementContainingText("dashboard_title_tapHereToMakeFirstEntry")
            .map(BooleanUtil::toTrue);
    }
}
