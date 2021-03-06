package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.validage.ValidAgeActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface PersonalInfoActionType extends PersonalInfoValidationType, ValidAgeActionType {
    /**
     * Toggle the TOC checkbox to be accepted/rejected.
     * @param engine {@link Engine} instance.
     * @param accepted {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #rxe_TCCheckBox(Engine)
     */
    @NotNull
    default Flowable<?> rxa_toggleTC(@NotNull Engine<?> engine, boolean accepted) {
        return rxe_TCCheckBox(engine).compose(engine.toggleCheckBoxFn(accepted));
    }

    /**
     * Enter random personal info inputs in order to access the next screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_toggleTC(Engine, boolean)
     */
    @NotNull
    default Flowable<?> rxa_enterPersonalInfo(@NotNull Engine<?> engine,
                                              @NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        PlatformType platform = engine.platform();

        return Flowable
            .concatArray(
                rxa_randomInputs(engine, mode.personalInfo(platform)),
                rxa_toggleTC(engine, true)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Click the submit button to confirm personal info inputs.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #personalInfoProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_personalInfoSubmit(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmPersonalInfo(@NotNull Engine<?> engine) {
        return Flowable.concatArray(
            rxe_personalInfoSubmit(engine).compose(engine.clickFn()),
            Flowable.timer(personalInfoProgressDelay(engine), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(engine)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Enter and confirm personal info.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_enterPersonalInfo(Engine, UserMode)
     * @see #rxa_confirmPersonalInfo(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completePersonalInfo(@NotNull Engine engine,
                                                 @NotNull UserMode mode) {
        return Flowable
            .concatArray(
                rxa_enterPersonalInfo(engine, mode),
                rxa_confirmPersonalInfo(engine)
            )
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Open the main website and access the TOC page. This is primarily to
     * check that when the user navigates back to the app, the previous state
     * is saved and restored.
     * On {@link Platform#ANDROID}, the link
     * text is not a separate view, so we need to manually calculate an
     * approximate position, a press/tap on which will open up the web
     * browser.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #webViewDelay(Engine)
     * @see #rxe_TCAcceptanceLabel(Engine)
     */
    @NotNull
    default Flowable<?> rxa_openTC(@NotNull final Engine<?> ENGINE) {
        return rxe_TCAcceptanceLabel(ENGINE)
            .map(a -> {
                Point point = a.getLocation();
                Dimension size = a.getSize();
                int x = point.getX(), y = point.getY(), h = size.getHeight();
                return new Point(x + 10, y + h - 3);
            })
            .compose(ENGINE.tapPointFn())
            .delay(webViewDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
