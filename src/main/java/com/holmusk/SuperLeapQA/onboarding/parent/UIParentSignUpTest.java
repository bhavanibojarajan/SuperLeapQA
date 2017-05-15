package com.holmusk.SuperLeapQA.onboarding.parent;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.onboarding.common.BaseSignUpValidationType;
import com.holmusk.SuperLeapQA.onboarding.common.UICommonSignUpTest;
import com.holmusk.SuperLeapQA.onboarding.register.RegisterActionType;
import com.holmusk.SuperLeapQA.onboarding.welcome.WelcomeActionType;
import com.holmusk.SuperLeapQA.runner.TestRunner;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by haipham on 5/10/17.
 */
public final class UIParentSignUpTest extends UICommonSignUpTest implements
    WelcomeActionType,
    RegisterActionType,
    ParentSignUpActionType,
    ParentSignUpValidationType
{
    @Factory(
        dataProviderClass = TestRunner.class,
        dataProvider = "dataProvider"
    )
    public UIParentSignUpTest(int index) {
        super(index);
    }

    /**
     * @return An {@link Integer} value.
     * @see BaseSignUpValidationType#minAcceptableAge()
     */
    @Override
    public int minAcceptableAge() {
        return 5;
    }

    /**
     * @return An {@link Integer} value.
     * @see BaseSignUpValidationType#maxAcceptableAge()
     */
    @Override
    public int maxAcceptableAge() {
        return 6;
    }

    /**
     * @return A {@link UserMode} instance.
     * @see UICommonSignUpTest#signUpMode()
     */
    @NotNull
    protected UserMode signUpMode() {
        return UserMode.PARENT;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_parentDoBPickerScreen_shouldContainCorrectElements() {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rx_splash_DoBPicker(UserMode.PARENT)
            .concatWith(rxValidateParentDoBPickerScreen())
            .concatWith(rxNavigateBackWithBackButton())
            .concatWith(rxValidateRegisterScreen())
            .all(BooleanUtil::isTrue)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}