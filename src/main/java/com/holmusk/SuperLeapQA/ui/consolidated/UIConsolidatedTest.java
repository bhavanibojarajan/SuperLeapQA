package com.holmusk.SuperLeapQA.ui.consolidated;

import com.holmusk.SuperLeapQA.runner.Runner;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTest;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.ui.dashboard.UIDashboardTestType;
import com.holmusk.SuperLeapQA.ui.dob.UIDoBPickerTestType;
import com.holmusk.SuperLeapQA.ui.invalidage.UIInvalidAgeTestType;
import com.holmusk.SuperLeapQA.ui.login.UILoginTestType;
import com.holmusk.SuperLeapQA.ui.logmeal.UILogMealTestType;
import com.holmusk.SuperLeapQA.ui.registermode.UIRegisterModeTestType;
import com.holmusk.SuperLeapQA.ui.personalinfo.UIPersonalInfoTestType;
import com.holmusk.SuperLeapQA.ui.validage.UIValidAgeTestType;
import com.holmusk.SuperLeapQA.ui.welcome.UIWelcomeTestType;
import org.testng.annotations.*;

/**
 * Created by haipham on 4/4/17.
 */
public final class UIConsolidatedTest extends UIBaseTest implements
    UIBaseTestType,
    UIWelcomeTestType,
    UILoginTestType,
    UIRegisterModeTestType,
    UIDoBPickerTestType,
    UIInvalidAgeTestType,
    UIValidAgeTestType,
    UIPersonalInfoTestType,
    UIDashboardTestType,
    UILogMealTestType
{
    @Factory(dataProviderClass = Runner.class, dataProvider = "dataProvider")
    public UIConsolidatedTest(int index) {
        super(index);
    }
}