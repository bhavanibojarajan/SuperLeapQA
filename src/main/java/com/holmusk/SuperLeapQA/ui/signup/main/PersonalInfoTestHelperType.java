package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.SLInputType;
import com.holmusk.SuperLeapQA.model.SLTextInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.input.type.TextInputType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 19/5/17.
 */
public interface PersonalInfoTestHelperType extends PersonalInfoActionType {
    /**
     * Enter random inputs and validate that the input views can be properly
     * interacted with.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterRandomInput(SLTextInputType)
     * @see #rx_editFieldForInput(SLInputType)
     * @see BaseEngine#rx_toggleNextOrDoneInput(WebElement)
     * @see BaseEngine#rx_togglePasswordMask(WebElement)
     * @see BaseEngine#isShowingPassword(WebElement)
     * @see RxUtil#error()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidatePersonalInfo(@NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return Flowable.fromIterable(mode.personalInformation())
            .ofType(SLTextInputType.class)
            .concatMap(THIS::rxEnterRandomInput)
            .flatMap(ENGINE::rx_toggleNextOrDoneInput)
            .all(ObjectUtil::nonNull)
            .toFlowable()

            .flatMap(a -> THIS.rxEnterRandomInput(TextInput.PASSWORD))
            .flatMap(a -> THIS.rx_editFieldForInput(TextInput.PASSWORD))
            .flatMap(a -> ENGINE.rx_toggleNextOrDoneInput(a).flatMap(b ->
                ENGINE.rx_togglePasswordMask(a)
            ))
            .filter(ENGINE::isShowingPassword)
            .switchIfEmpty(RxUtil.error())
            .map(BooleanUtil::toTrue);
    }

    /**
     * Validate that the TOC checkbox has the be checked before the user can
     * proceed to the next screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rx_enterPersonalInfo(List)
     * @see BaseEngine#rx_hideKeyboard()
     * @see #rxConfirmPersonalInfo()
     * @see #rxValidatePersonalInfoScreen(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_validateTOCCheckedBeforeProceeding(@NotNull final UserMode MODE) {
        final PersonalInfoActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        return rx_enterPersonalInfo(MODE.personalInformation())
            .flatMap(a -> ENGINE.rx_hideKeyboard())
            .flatMap(a -> THIS.rxConfirmPersonalInfo())
            .flatMap(a -> THIS.rxValidatePersonalInfoScreen((MODE)));
    }

    /**
     * Enter personal info inputs, navigate forward/backward a few times,
     * open the TOC website then back, and finally validate that all inputs
     * are saved and restored. This assumes the use is already in the
     * personal info page.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see UserMode#personalInformation()
     * @see #rxEnterInput(SLInputType, String)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxOpenTOCWebsite()
     * @see #rx_editFieldHasValue(SLInputType, String)
     */
    @NotNull
    default Flowable<Boolean> rx_validatePersonalInfoStateSaved(@NotNull UserMode mode) {
        final PersonalInfoActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final Map<String,String> INPUTS = new HashMap<>();
        List<SLInputType> info = mode.personalInformation();

        final List<SLTextInputType> TEXT_INFO = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(SLTextInputType.class::cast)
            .collect(Collectors.toList());

        TEXT_INFO.forEach(a -> INPUTS.put(a.toString(), a.randomInput()));

        return Flowable
            .fromIterable(TEXT_INFO)
            .concatMap(a -> THIS.rxEnterInput(a, INPUTS.get(a.toString())))
            .flatMap(ENGINE::rx_toggleNextInput)
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> ENGINE.rx_hideKeyboard())

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            .flatMap(a -> THIS.rx_editFieldForInput(TextInput.PASSWORD))
            .flatMap(ENGINE::rx_togglePasswordMask)

            .flatMap(a -> THIS.rxOpenTOCWebsite())
            .delay(webViewDelay(), TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .flatMap(a -> ENGINE.rx_navigateBackOnce())
            .flatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rx_editFieldHasValue(a, INPUTS.get(a.toString())));
    }
}
