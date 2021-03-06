package com.holmusk.SuperLeapQA.test.invalidage;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIInvalidAgeTestType extends UIBaseTestType {
    /**
     * Provide data parameters for
     * {@link #test_invalidAgeInput_phoneOrEmail(UserMode, TextInput)}.
     * @return {@link Iterator} instance.
     * @see #test_invalidAgeInput_phoneOrEmail(UserMode, TextInput)
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> userModeInputProvider() {
        return Arrays.asList(
            new Object[] { UserMode.PARENT, TextInput.PHONE },
            new Object[] { UserMode.PARENT, TextInput.EMAIL }
//            new Object[] { UserMode.TEEN_U18, TextInput.PHONE },
//            new Object[] { UserMode.TEEN_U18, TextInput.EMAIL }
        ).iterator();
    }

    /**
     * This test validates that {@link Screen#INVALID_AGE} should only
     * require either {@link TextInput#PHONE} or {@link TextInput#EMAIL},
     * and not both. It sequentially substitutes {@link TextInput} into
     * {@link #rxa_input(Engine, HMInputType, String)}. We do not use
     * {@link DataProvider} with this method because we already have a
     * {@link Factory} for the constructor.
     * @param MODE {@link UserMode} instance.
     * @param INPUT {@link TextInput} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_acknowledgeSubscription(Engine)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_makeNextInputVisible(Engine)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_confirmInvalidAgeInputs(Engine)
     * @see #rxv_invalidAgeInputConfirmed(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIInvalidAgeTestType.class,
        dataProvider = "userModeInputProvider"
    )
    default void test_invalidAgeInput_phoneOrEmail(@NotNull final UserMode MODE,
                                                   @NotNull final TextInput INPUT) {
        // Setup
        final UIInvalidAgeTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.INVALID_AGE),

            Flowable.fromArray(TextInput.NAME, INPUT)
                .concatMap(a -> Flowable.concatArray(
                    THIS.rxa_randomInput(ENGINE, a),
                    THIS.rxa_makeNextInputVisible(ENGINE)
                )),

            rxa_confirmInvalidAgeInputs(ENGINE),
            rxv_invalidAgeInputConfirmed(ENGINE),
            rxa_acknowledgeSubscription(ENGINE)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that filling in {@link Screen#INVALID_AGE}
     * inputs work correctly, by checking that after the submit button is
     * clicked, the user should be brought to the confirm screen and
     * {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_completeInvalidAgeInputs(Engine)
     * @see #rxa_acknowledgeSubscription(Engine)
     * @see #rxv_invalidAgeInputConfirmed(Engine)
     * @see #rxv_welcomeScreen(Engine)
     * @see #rxe_invalidAgeOk(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_invalidAgeInput_shouldWork(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE),
            rxa_completeInvalidAgeInputs(engine),
            rxv_invalidAgeInputConfirmed(engine),
            rxa_acknowledgeSubscription(engine),
            rxv_welcomeScreen(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
