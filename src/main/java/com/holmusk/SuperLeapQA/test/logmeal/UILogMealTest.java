package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 29/5/17.
 */
public final class UILogMealTest extends UIBaseTest implements UILogMealTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UILogMealTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
