package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.test.type.BaseTestType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseInteractionType extends
    AppDelayType,
    BaseTestType,
    BaseValidationType
{
    /**
     * Navigate backwards by clicking the back button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxNavigateBackWithBackButton() {
        return rxBackButton()
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }
}
