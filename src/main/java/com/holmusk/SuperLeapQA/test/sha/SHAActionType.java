package com.holmusk.SuperLeapQA.test.sha;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.HPBooleans;
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
            .flatMap(a -> {
                if (PARTICIPATING) {
                    return THIS.rxe_shaYes(ENGINE);
                } else {
                    return THIS.rxe_shaNo(ENGINE);
                }
            })
            .compose(ENGINE.clickFn())
            .map(HPBooleans::toTrue)
            .defaultIfEmpty(true)

            /* On iOS, the app's state is saved between different runs, so
             * if we are using DataProvider, this screen may not appear for
             * subsequent parameter injection. As a result, an exception may
             * be thrown - since this screen is not that important, we can
             * swallow the error and let the tests continue */
            .onErrorReturnItem(true);
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
