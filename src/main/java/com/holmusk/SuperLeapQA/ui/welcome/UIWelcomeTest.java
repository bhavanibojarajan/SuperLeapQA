package com.holmusk.SuperLeapQA.ui.welcome;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIWelcomeTest extends UIBaseTest implements UIWelcomeTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIWelcomeTest(int index) {
        super(index);
    }
}
