package com.holmusk.SuperLeapQA.ui.dashboard;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIDashboardTest extends UIBaseTest implements UIDashboardTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIDashboardTest(int index) {
        super(index);
    }
}
