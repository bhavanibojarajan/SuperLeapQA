package com.holmusk.SuperLeapQA.test.sha;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;

/**
 * Created by haipham on 6/3/17.
 */
public interface SHAActionType extends SHAValidationType {
    /**
     * Either indicate yes or no for SHA status. This is only relevant for
     * {@link UserMode#isTeen()}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param PARTICIPATING {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxa_click(WebElement)
     * @see UserMode#isTeen()
     * @see #rxe_shaYes(Engine)
     * @see #rxe_shaNo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_setSHAStatus(@NotNull final Engine<?> ENGINE,
                                         @NotNull UserMode mode,
                                         final boolean PARTICIPATING) {
        final SHAActionType THIS = this;

        return Flowable.just(mode)
            .filter(UserMode::isTeen)
            .flatMap(a -> PARTICIPATING ?
                THIS.rxe_shaYes(ENGINE) : THIS.rxe_shaNo(ENGINE))
            .flatMap(ENGINE::rxa_click)
            .map(BooleanUtil::toTrue)
            .defaultIfEmpty(true);
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
