package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.engine.base.BaseEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface CommonSignUpInteractionType extends
    BaseInteractionType,
    CommonSignUpValidationType
{
    /**
     * Open the DoB dialog in the parent sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBDialog() {
        final BaseEngine<?> ENGINE = currentEngine();

        return rxDoBEditableField()
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay));
    }
}
