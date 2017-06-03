package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 29/5/17.
 */
public final class UILogMealTest extends UIBaseTest implements UILogMealTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UILogMealTest(int index) {
        super(index);
    }
}
