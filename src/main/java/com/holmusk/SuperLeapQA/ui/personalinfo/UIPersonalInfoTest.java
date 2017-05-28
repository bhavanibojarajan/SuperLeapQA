package com.holmusk.SuperLeapQA.ui.personalinfo;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIPersonalInfoTest extends UIBaseTest implements UIPersonalInfoTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIPersonalInfoTest(int index) {
        super(index);
    }
}
