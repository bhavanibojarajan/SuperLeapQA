package com.holmusk.SuperLeapQA.test.consolidated;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 6/4/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIScreenValidationTest extends UIBaseTest implements
    UIScreenValidationTestType
{
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UIScreenValidationTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
