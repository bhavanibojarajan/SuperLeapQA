package com.holmusk.SuperLeapQA.test.popup;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 6/19/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIPopupTest extends UIBaseTest implements UIPopupTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UIPopupTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
