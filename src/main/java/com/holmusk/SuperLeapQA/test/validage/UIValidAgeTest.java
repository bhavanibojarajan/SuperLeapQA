package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIValidAgeTest extends UIBaseTest implements UIValidAgeTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIValidAgeTest(int index) {
        super(index);
    }
}
