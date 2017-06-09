package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIValidAgeTest extends UIBaseTest implements UIValidAgeTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UIValidAgeTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
