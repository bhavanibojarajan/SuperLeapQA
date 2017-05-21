package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.ui.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardValidationType extends BaseValidationType {
    /**
     * Get the Use App Now button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxUseAppNowButton(@NotNull Engine<?> engine) {
        return engine
            .rx_containsText("dashboard_title_useAppNow")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate the Use App Now screen after the user finishes sign up.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see #rxUseAppNowButton(Engine)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_v_useAppNowScreen(@NotNull Engine<?> engine) {
        final DashboardValidationType THIS = this;

        return Flowable
            .mergeArray(
                engine.rx_containsText("dashboard_title_accountReadyToUse"),
                engine.rx_containsText("dashboard_title_rememberCheckEmail"),
                THIS.rxUseAppNowButton(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate that the tutorial screen is present when the user first signs
     * up.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     */
    @NotNull
    default Flowable<?> rx_v_dashboardTutorialScreen(@NotNull Engine<?> engine) {
        return engine.rx_containsText("dashboard_title_tapHereToMakeFirstEntry");
    }
}
