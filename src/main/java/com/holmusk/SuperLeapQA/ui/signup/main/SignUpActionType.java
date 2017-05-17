package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.InputType;
import com.holmusk.SuperLeapQA.model.NumericSelectableInputType;
import com.holmusk.SuperLeapQA.model.TextInputType;
import com.holmusk.SuperLeapQA.ui.signup.mode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeActionType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.base.type.DelayType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpActionType extends
    BaseActionType,
    SignUpValidationType,
    RegisterModeActionType,
    WelcomeActionType
{
    /**
     * Enter an input for a {@link TextInput}.
     * @param input A {@link TextInputType} instance.
     * @param TEXT A {@link String} value.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see BaseEngine#rxSendKey(WebElement, String...)
     */
    @NotNull
    default Flowable<WebElement> rxEnterInput(@NotNull TextInputType input,
                                              @NotNull final String TEXT) {
        final BaseEngine<?> ENGINE = engine();
        return rxEditFieldForInput(input).flatMap(a -> ENGINE.rxSendKey(a, TEXT));
    }

    /**
     * Enter a random input using {@link TextInputType#randomInput()}.
     * @param input A {@link TextInputType} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEnterInput(TextInputType, String)
     * @see TextInputType#randomInput()
     */
    @NotNull
    default Flowable<WebElement> rxEnterRandomInput(@NotNull TextInputType input) {
        return rxEnterInput(input, input.randomInput());
    }

    /**
     * Perform a click action on an editable field. This is useful when
     * the editable field is showing an error circle that can be shown if
     * clicked (however, this is only applicable to {@link Platform#ANDROID}.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxClickInputField(@NotNull InputType input) {
        return rxEditFieldForInput(input).flatMap(engine()::rxClick).map(a -> true);
    }
}
