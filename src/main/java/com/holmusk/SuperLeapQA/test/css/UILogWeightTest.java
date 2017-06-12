package com.holmusk.SuperLeapQA.test.css;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 9/6/17.
 */
public final class UILogWeightTest extends UIBaseTest implements UILogWeightTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UILogWeightTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
