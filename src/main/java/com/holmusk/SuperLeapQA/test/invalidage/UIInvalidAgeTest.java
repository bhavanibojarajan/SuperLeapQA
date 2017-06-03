package com.holmusk.SuperLeapQA.test.invalidage;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
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
