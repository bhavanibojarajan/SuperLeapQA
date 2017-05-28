package com.holmusk.SuperLeapQA.ui.login;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UILoginTest extends UIBaseTest implements UILoginTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UILoginTest(int index) {
        super(index);
    }
}
