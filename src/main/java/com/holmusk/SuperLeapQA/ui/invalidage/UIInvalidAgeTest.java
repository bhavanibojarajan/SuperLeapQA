package com.holmusk.SuperLeapQA.ui.invalidage;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIInvalidAgeTest extends UIBaseTest implements UIInvalidAgeTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIInvalidAgeTest(int index) {
        super(index);
    }
}
