package com.holmusk.SuperLeapQA.test.consolidated;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.test.base.UIBaseTest;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.dashboard.UIDashboardTestType;
import com.holmusk.SuperLeapQA.test.dob.UIDoBPickerTestType;
import com.holmusk.SuperLeapQA.test.invalidage.UIInvalidAgeTestType;
import com.holmusk.SuperLeapQA.test.login.UILoginTestType;
import com.holmusk.SuperLeapQA.test.logmeal.UILogMealTestType;
import com.holmusk.SuperLeapQA.test.personalinfo.UIPersonalInfoTestType;
import com.holmusk.SuperLeapQA.test.registermode.RegisterModeActionType;
import com.holmusk.SuperLeapQA.test.validage.UIValidAgeTestType;
import org.testng.annotations.*;

/**
 * Created by haipham on 4/4/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class UIConsolidatedTest extends UIBaseTest implements
    UIBaseTestType,
    UIScreenValidationTestType,
    UILoginTestType,
    UIDoBPickerTestType,
    UIInvalidAgeTestType,
    UIValidAgeTestType,
    UIPersonalInfoTestType,
    UIDashboardTestType,
    UILogMealTestType, RegisterModeActionType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIConsolidatedTest(int index) {
        super(index);
    }
}