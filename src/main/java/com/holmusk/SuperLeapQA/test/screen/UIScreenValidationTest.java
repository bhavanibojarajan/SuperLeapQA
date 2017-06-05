package com.holmusk.SuperLeapQA.test.screen;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 6/4/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIScreenValidationTest extends UIBaseTest implements
    UIScreenValidationTestType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIScreenValidationTest(int index) {
        super(index);
    }
}
