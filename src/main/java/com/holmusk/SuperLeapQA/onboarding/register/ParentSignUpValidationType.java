package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface ParentSignUpValidationType extends
    BaseValidationType,
    CommonSignUpValidationType
{
    /**
     * Validate the parent sign-up screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxValidateParentSignUpScreen() {
        final BaseEngine<?> ENGINE = currentEngine();

        return Flowable
            .concat(
                ENGINE.rxElementContainingText("parentSignUp_title_whatIsYourChild"),
                ENGINE.rxElementContainingText("parentSignUp_title_dateOfBirth"),
                rxDoBEditableField()
            )
            .all(ObjectUtil::nonNull)
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> rxDoBEditableField())
            .flatMap(ENGINE::rxClick)
            .flatMap(a -> rxDoBElements())
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> rxValidateDoBDatePicker())
            .delay(10000, TimeUnit.MILLISECONDS)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rxNavigateBackOnce())
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .map(a -> true);
    }
}
