package com.holmusk.SuperLeapQA.onboarding.splash;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.engine.base.PlatformEngine;

/**
 * Created by haipham on 5/7/17.
 */

/**
 * Validate splash screen.
 */
public interface OnboardingValidationType extends BaseValidationType {
    /**
     * Validate that all views are present in splash screen.
     * @return A {@link Flowable} instance.
     */
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateViews() {
        PlatformEngine engine = currentEngine();

        return Flowable
            .concat(
                engine.rxElementsContainingText("auth_title_signIn"),
                engine.rxElementsContainingText("auth_title_register")
            )
            .all(ObjectUtil::nonNull)
            .map(a -> true)
            .toFlowable();
    }
}
