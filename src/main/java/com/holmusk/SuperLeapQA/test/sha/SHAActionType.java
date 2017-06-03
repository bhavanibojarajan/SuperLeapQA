package com.holmusk.SuperLeapQA.test.sha;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 6/3/17.
 */
public interface SHAActionType extends SHAValidationType {
    /**
     * Either indicate yes or no for SHA status. This is only relevant for
     * {@link UserMode#requiresGuarantor()}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param participating {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see UserMode#requiresGuarantor()
     * @see #rxe_shaYes(Engine)
     * @see #rxe_shaNo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_setSHAStatus(@NotNull final Engine<?> ENGINE,
                                         @NotNull UserMode mode,
                                         boolean participating) {
        if (mode.requiresGuarantor()) {
            Flowable<WebElement> rxe_element;

            if (participating) {
                rxe_element = rxe_shaYes(ENGINE);
            } else {
                rxe_element = rxe_shaNo(ENGINE);
            }

            return rxe_element.flatMap(ENGINE::rxa_click);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Set SHA status so the current user passes the test.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_setSHAStatus(Engine, UserMode, boolean)
     */
    @NotNull
    default Flowable<?> rxa_passSHA(@NotNull Engine<?> engine, @NotNull UserMode mode) {
        return rxa_setSHAStatus(engine, mode, false);
    }
}
