package com.holmusk.SuperLeapQA.test.consolidated;

import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.css.UICSSValueTestType;
import com.holmusk.SuperLeapQA.test.dashboard.UIDashboardTestType;
import com.holmusk.SuperLeapQA.test.dob.UIDoBPickerTestType;
import com.holmusk.SuperLeapQA.test.invalidage.UIInvalidAgeTestType;
import com.holmusk.SuperLeapQA.test.logactivity.UILogActivityTestType;
import com.holmusk.SuperLeapQA.test.login.UILoginTestType;
import com.holmusk.SuperLeapQA.test.logmeal.UILogMealTestType;
import com.holmusk.SuperLeapQA.test.logweight.UILogWeightTestType;
import com.holmusk.SuperLeapQA.test.personalinfo.UIPersonalInfoTestType;
import com.holmusk.SuperLeapQA.test.validage.UIValidAgeTestType;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Factory;

/**
 * Created by haipham on 4/4/17.
 */

/**
 * This class takes care of all tests that are not validation-based, i.e.
 * those not found in
 * {@link UIScreenValidationTest}.
 */
@SuppressWarnings("UndeclaredTests")
public final class UILogicValidationTest extends UIBaseTest implements
    UIBaseTestType,
    UILoginTestType,
    UIDoBPickerTestType,
    UIInvalidAgeTestType,
    UIValidAgeTestType,
    UIPersonalInfoTestType,
    UIDashboardTestType,
    UILogMealTestType,
    UILogWeightTestType,
    UILogActivityTestType,
    UICSSValueTestType
{
    @Factory(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "engineProvider"
    )
    public UILogicValidationTest(@NotNull Engine<?> engine) {
        super(engine);
    }
}