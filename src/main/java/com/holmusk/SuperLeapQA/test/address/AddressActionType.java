package com.holmusk.SuperLeapQA.test.address;

import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionUtil;
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
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_addressSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_submitAddress(@NotNull final Engine<?> ENGINE) {
        return rxe_addressSubmit(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter address information.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see CollectionUtil#asList(Object[])
     * @see TextInput#POSTAL_CODE
     * @see TextInput#UNIT_NUMBER
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    default Flowable<?> rxa_enterAddressInfo(@NotNull final Engine<?> ENGINE) {
        List<HMTextType> inputs = CollectionUtil.asList(
            TextInput.POSTAL_CODE,
            TextInput.UNIT_NUMBER
        );

        return rxa_randomInputs(ENGINE, inputs);
    }

    /**
     * Enter and submit address information.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #addressInfoProgressDelay(Engine)
     * @see #rxa_enterAddressInfo(Engine)
     * @see #rxa_submitAddress(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeAddressInfo(@NotNull final Engine<?> ENGINE) {
        final AddressActionType THIS = this;

        return rxa_enterAddressInfo(ENGINE)
            .flatMap(a -> THIS.rxa_submitAddress(ENGINE))
            .delay(addressInfoProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
