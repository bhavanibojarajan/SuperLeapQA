package com.holmusk.SuperLeapQA.test.css;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 13/6/17.
 */
public class UICSSValueTest extends UIBaseTest implements UICSSValueTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UICSSValueTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
