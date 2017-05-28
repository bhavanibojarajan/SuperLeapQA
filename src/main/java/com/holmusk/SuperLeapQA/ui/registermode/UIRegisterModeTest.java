package com.holmusk.SuperLeapQA.ui.registermode;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIRegisterModeTest extends UIBaseTest implements UIRegisterModeTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIRegisterModeTest(int index) {
        super(index);
    }
}
