package com.holmusk.SuperLeapQA.test.guarantorinfo;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 6/7/17.
 */
public interface GuarantorInfoActionType extends BaseActionType, GuarantorInfoValidationType {
    /**
     * Confirm additional personal inputs. This is only relevant to
     * {@link UserMode#requiresGuarantor()}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #registerProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_guarantorInfoSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmGuarantorInfo(@NotNull final Engine<?> ENGINE,
                                                 @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            return Flowable.concatArray(
                rxe_guarantorInfoSubmit(ENGINE).flatMap(ENGINE::rxa_click),
                Flowable.timer(registerProgressDelay(ENGINE), TimeUnit.MILLISECONDS),
                rxa_watchProgressBar(ENGINE)
            ).all(HPObjects::nonNull).toFlowable();
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Enter random additional personal info inputs in order to access the
     * next screen. This is only relevant for {@link UserMode#requiresGuarantor()}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInput(Engine, HMTextType)
     */
    @NotNull
    default Flowable<?> rxa_enterGuarantorInfo(@NotNull Engine<?> engine,
                                               @NotNull UserMode mode) {
        if (mode.requiresGuarantor()) {
            PlatformType platform = engine.platform();
            return rxa_randomInputs(engine, mode.guarantorInfo(platform));
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Complete guarantor info inputs.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterGuarantorInfo(Engine, UserMode)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     */
    @NotNull
    default Flowable<?> rxa_completeGuarantorInfo(@NotNull Engine<?> engine,
                                                  @NotNull UserMode mode) {
        return Flowable
            .concatArray(
                rxa_enterGuarantorInfo(engine, mode),
                rxa_confirmGuarantorInfo(engine, mode)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }
}
