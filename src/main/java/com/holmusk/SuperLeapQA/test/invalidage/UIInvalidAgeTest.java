package com.holmusk.SuperLeapQA.test.invalidage;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIInvalidAgeTest extends UIBaseTest implements UIInvalidAgeTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UIInvalidAgeTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
