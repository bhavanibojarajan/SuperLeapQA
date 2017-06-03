package com.holmusk.SuperLeapQA.test.dob;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIDoBPickerTest extends UIBaseTest implements UIDoBPickerTestType {
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIDoBPickerTest(int index) {
        super(index);
    }
}
