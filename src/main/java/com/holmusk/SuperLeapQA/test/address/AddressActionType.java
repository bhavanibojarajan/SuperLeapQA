package com.holmusk.SuperLeapQA.test.address;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 7/6/17.
 */
public interface AddressActionType extends BaseActionType, AddressValidationType {
    /**
     * Submit address information.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #addressInfoProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_addressSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitAddress(@NotNull final Engine<?> ENGINE) {
        return Flowable.concatArray(
            rxe_addressSubmit(ENGINE).flatMap(ENGINE::rxa_click),
            Flowable.timer(addressInfoProgressDelay(ENGINE), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(ENGINE)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter address information.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_enterAddressInfo(@NotNull final Engine<?> ENGINE) {
        List<HMTextType> inputs = HPIterables.asList(
            TextInput.POSTAL_CODE,
            TextInput.UNIT_NUMBER
        );

        return rxa_randomInputs(ENGINE, inputs);
    }

    /**
     * Enter and submit address information.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterAddressInfo(Engine)
     * @see #rxa_submitAddress(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeAddressInfo(@NotNull Engine<?> engine) {
        return Flowable.concatArray(
            rxa_enterAddressInfo(engine),
            rxa_submitAddress(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }
}
