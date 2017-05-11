package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
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
    BaseSignUpValidationType
{
    /**
     * Validate the parent sign-up screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxDoBEditField()
     * @see #rxDoBElements()
     */
    @NotNull
    default Flowable<Boolean> rxValidateParentDoBPickerScreen() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concat(
                ENGINE.rxElementContainingText("parentSignUp_title_whatIsYourChild"),
                ENGINE.rxElementContainingText("parentSignUp_title_dateOfBirth"),
                rxDoBEditField()
            )
            .all(ObjectUtil::nonNull)
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> rxDoBEditField())
            .flatMap(ENGINE::rxClick)
            .flatMap(a -> rxDoBElements())
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .delay(generalDelay(), TimeUnit.MILLISECONDS)

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rxNavigateBackOnce())
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .map(a -> true);
    }
}
