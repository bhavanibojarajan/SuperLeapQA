package com.holmusk.SuperLeapQA.test.personalinfo;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 5/28/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIPersonalInfoTest extends UIBaseTest implements UIPersonalInfoTestType {
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "dataProvider"
    )
    public UIPersonalInfoTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}
