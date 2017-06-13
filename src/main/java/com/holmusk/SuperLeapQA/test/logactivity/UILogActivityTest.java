package com.holmusk.SuperLeapQA.test.logactivity;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 13/6/17.
 */
public final class UILogActivityTest extends UIBaseTest implements UILogActivityTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UILogActivityTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
