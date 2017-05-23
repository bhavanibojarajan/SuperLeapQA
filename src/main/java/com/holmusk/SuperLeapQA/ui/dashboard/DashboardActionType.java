package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.ui.signup.invalidage.InvalidAgeActionType;
import com.holmusk.SuperLeapQA.ui.signup.personalinfo.PersonalInfoActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardActionType extends
    PersonalInfoActionType,
    InvalidAgeActionType,
    DashboardValidationType
{
    /**
     * Click the Use App Now button.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxUseAppNowButton(Engine)
     * @see Engine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_a_useAppNow(@NotNull final Engine<?> ENGINE) {
        return rxUseAppNowButton(ENGINE).flatMap(ENGINE::rx_click);
    }
}
