package com.holmusk.SuperLeapQA.test.invalidage;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface InvalidAgeActionType extends BaseActionType, InvalidAgeValidationType {
    /**
     * Enter random inputs for unacceptable age screen, then confirm and check
     * that the app shows a confirmation page.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_enterInvalidAgeInputs(@NotNull Engine<?> engine) {
        return rxa_randomInputs(engine,
            TextInput.NAME,
            TextInput.EMAIL,
            TextInput.PHONE);
    }

    /**
     * Confirm email subscription for future program expansion.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #invalidAgeInputProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_invalidAgeSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmInvalidAgeInputs(@NotNull Engine<?> engine) {
        return Flowable.concatArray(
            rxe_invalidAgeSubmit(engine).compose(engine.clickFn()),
            Flowable.timer(invalidAgeInputProgressDelay(engine), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter and confirm invalid age inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterInvalidAgeInputs(Engine)
     * @see #rxa_confirmInvalidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeInvalidAgeInputs(@NotNull Engine<?> engine) {
        return Flowable.concatArray(
            rxa_enterInvalidAgeInputs(engine),
            rxa_confirmInvalidAgeInputs(engine)
        ).all(HPObjects::nonNull).toFlowable();

    }

    /**
     * Press the ok button after unacceptable age inputs have been completed.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_invalidAgeOk(Engine)
     */
    @NotNull
    default Flowable<?> rxa_acknowledgeSubscription(@NotNull Engine<?> engine) {
        return rxe_invalidAgeOk(engine).compose(engine.clickFn());
    }
}
