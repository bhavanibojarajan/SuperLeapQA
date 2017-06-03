package com.holmusk.SuperLeapQA.test.forgotpassword;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIForgotPasswordTest extends UIBaseTest implements UIForgotPasswordTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIForgotPasswordTest(int index) {
        super(index);
    }
}
