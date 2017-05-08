package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseValidationType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.engine.base.BaseEngine;
import org.swiften.xtestkit.engine.mobile.Platform;
import org.swiften.xtestkit.engine.base.type.PlatformType;

/**
 * Created by haipham on 5/8/17.
 */
public interface RegisterValidationType extends
    BaseValidationType,
    WelcomeValidationType
{
    /**
     * Validate all views present in the register screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateRegisterScreen() {
        final BaseEngine<?> ENGINE = currentEngine();

        return Flowable
            .concatArray(
                ENGINE.rxElementContainingText("register_title_iAmParent"),
                ENGINE.rxElementContainingText("register_title_iRegisterForChild"),
                ENGINE.rxElementContainingText("register_title_iAmTeen"),
                ENGINE.rxElementContainingText("register_title_iRegisterForSelf"),
                ENGINE.rxElementContainingText("register_title_or"),
                ENGINE.rxElementContainingText("register_title_initiativeByHPB"),
                rxValidateBackButtonTitle()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .map(a -> true);
    }

    /**
     * Validate the back button's title.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxValidateBackButtonTitle() {
        BaseEngine<?> engine = currentEngine();
        PlatformType platform = engine.platform();
        Flowable<WebElement> source;

        if (platform.equals(Platform.ANDROID)) {
            String title = "register_title_whichOneBestDescribes";
            source = engine.rxElementContainingText(title);
        } else {
            return Flowable.empty();
        }

        return source.map(a -> true);
    }
}
