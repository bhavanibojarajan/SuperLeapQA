package com.holmusk.SuperLeapQA.general;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.bmi.BMIParam;
import com.holmusk.SuperLeapQA.bmi.BMIResult;
import com.holmusk.SuperLeapQA.bmi.BMIUtil;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.ScreenHolder;
import com.holmusk.SuperLeapQA.navigation.type.SLScreenManagerType;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.test.HPTestNGs;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.navigation.ScreenManagerType;
import org.swiften.xtestkit.util.TestHelper;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by haipham on 5/11/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class GeneralTest {
    @NotNull
    @DataProvider(parallel = true)
    public static Iterator<Object[]> bmiDataProvider() {
        return HPTestNGs.oneFromEach(
            Platform.values(),
            UserMode.values(),
            UnitSystem.values(),
            Ethnicity.values(),
            Gender.values()
        ).iterator();
    }

    @NotNull
    @DataProvider(parallel = true)
    public static Iterator<Object[]> findBmiDataProvider() {
        return HPTestNGs.oneFromEach(
            Platform.values(),
            UserMode.values(),
            UnitSystem.values(),
            Ethnicity.values(),
            Gender.values(),
            HPIterables.asList(true, false).toArray()
        ).iterator();
    }

    @Test
    public void test_randomTextInput_shouldWork() {
        // Setup
        InputHelperType helper = TestHelper.mockHelper();

        // When & Then
        HPLog.println(TextInput.NAME.randomInput(helper));
        HPLog.println(TextInput.PHONE.randomInput(helper));
        HPLog.println(TextInput.EMAIL.randomInput(helper));
        HPLog.println(TextInput.UNIT_NUMBER.randomInput(helper));
    }

    @Test
    public void test_textInput_shouldCreateCorrectXPath() {
        // Setup
        InputHelperType helper = TestHelper.mockHelper(Platform.IOS);

        // When & Then
        HPLog.printlnt(TextInput.PASSWORD.inputViewXP(helper));
        HPLog.printlnt(TextInput.POSTAL_CODE.inputViewXP(helper));
    }

    @Test
    public void test_birthDayCalculation_shouldBeCorrect() {
        // Setup
        LocalDate date1 = LocalDate.of(2017, 6, 5);
        LocalDate date2 = LocalDate.of(1996, 6, 5);

        // When
        long yearDiff = ChronoUnit.YEARS.between(date1, date2);

        // Then
        HPLog.println(yearDiff);
    }

    @Test
    public void test_userModeAgeRange_shouldBeCorrect() {
        // Setup & When & Then
        HPLog.println(UserMode.PARENT.validAgeRange());
        HPLog.println(UserMode.PARENT.validAgeCategoryRangeString());
        HPLog.println(UserMode.TEEN_U18.validAgeRange());
        HPLog.println(UserMode.TEEN_U18.validAgeCategoryRangeString());
        HPLog.println(UserMode.TEEN_A18.validAgeRange());
        HPLog.println(UserMode.TEEN_A18.validAgeCategoryRangeString());
    }

    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    public void test_navigation_shouldWorkCorrectly(@NotNull UserMode mode) {
        // Setup
        final Engine<?> ENGINE = mock(Engine.class);
        final List<ScreenManagerType.Node> FN = new ArrayList<>();
        final List<ScreenManagerType.Node> BN = new ArrayList<>();
        doReturn(mock(PlatformType.class)).when(ENGINE).platform();
        doReturn(Flowable.just(true)).when(ENGINE).rxa_acceptAlert();

        SLScreenManagerType manager = new SLScreenManagerType() {
            @NotNull
            @Override
            public Engine<?> engine() {
                return ENGINE;
            }

            @Override
            public void addForwardNodes(@NotNull List<Node> nodes) {
                FN.addAll(nodes);
            }

            @Override
            public void addBackwardNodes(@NotNull List<Node> nodes) {
                BN.addAll(nodes);
            }

            @NotNull
            @Override
            public List<Node> registeredForwardNodes() {
                return FN;
            }

            @NotNull
            @Override
            public List<Node> registeredBackwardNodes() {
                return BN;
            }

            @Override
            public void clearAllNodes() {
                FN.clear(); BN.clear();
            }
        };

        manager.registerScreenHolders();
        Screen start = Screen.SPLASH;
        Screen[] screens = Screen.values();

        // When & Then
        for (Screen screen : screens) {
            HPLog.printf("Checking screen %s", screen);
            List<ScreenManagerType.Node> nodes = manager.multiNodes(mode, start, screen);
            ScreenManagerType.Node first = nodes.get(0);
            ScreenManagerType.Node last = nodes.get(nodes.size() - 1);
            assertEquals(((ScreenHolder)first.S1).SCREEN, start);
            assertEquals(((ScreenHolder)last.S2).SCREEN, screen);
            HPLog.printf("Start: %s, end: %s. Route: %s", start, screen, nodes);
        }
    }

    @Test(
        dataProviderClass = GeneralTest.class,
        dataProvider = "bmiDataProvider"
    )
    public void test_bmiRanges_shouldWork(@NotNull Platform platform,
                                          @NotNull UserMode mode,
                                          @NotNull UnitSystem system,
                                          @NotNull Ethnicity ethnicity,
                                          @NotNull Gender gender) {
        // Setup
        List<Tuple<Height,String>> height = Height.random(
            platform, mode, system
        );

        List<Tuple<Weight,String>> weight = Weight.random(
            platform, mode, system
        );

        BMIParam param1 = BMIParam.builder()
            .withEthnicity(ethnicity)
            .withGender(gender)
            .withHeight(height)
            .withWeight(weight)
            .build();

        // When
        Tuple<Double,Double> tightestInvalid = BMIUtil.tightestInvalidRange(mode, param1);
        Tuple<Double,Double> widestInvalid = BMIUtil.widestInvalidRange(mode, param1);
        double bmi = param1.bmi();

        // Then
        assertEquals(tightestInvalid.A, widestInvalid.A);
        assertTrue(widestInvalid.B >= tightestInvalid.B);
    }

    @Test(
        dataProviderClass = GeneralTest.class,
        dataProvider = "findBmiDataProvider"
    )
    @SuppressWarnings("unchecked")
    public void test_findValidBMI_shouldWork(@NotNull PlatformType platform,
                                             @NotNull UserMode mode,
                                             @NotNull UnitSystem unit,
                                             @NotNull Ethnicity ethnicity,
                                             @NotNull Gender gender,
                                             boolean validBMI) {
        // Setup
        BMIParam param = BMIParam.builder()
            .withEthnicity(ethnicity)
            .withGender(gender)
            .build();

        // When
        BMIResult result = BMIUtil.findBMIResult(platform, mode, unit, param, validBMI);

        // Then
        BMIParam nParam = result.requestParam();
        boolean generatedCorrectly;

        if (validBMI) {
            generatedCorrectly = BMIUtil.outOfWidestInvalidRange(mode, nParam);
        } else {
            generatedCorrectly = BMIUtil.withinTightestInvalidRange(mode, nParam);
        }

        if (!generatedCorrectly) {
            HPLog.printft("Current bmi %.2f. Valid: %b", nParam.bmi(), validBMI);
            HPLog.printlnt(result.height(), result.weight());
            HPLog.printft("Widest invalid range: %s", BMIUtil.widestInvalidRange(mode, nParam));
            HPLog.printft("Tightest invalid range: %s", BMIUtil.tightestInvalidRange(mode, nParam));
            fail("BMI was not correctly generated");
        }
    }

    @Test(dataProviderClass = TestHelper.class, dataProvider = "platformProvider")
    public void test_xpathCreation_shouldWork(@NotNull PlatformType platform) {
        // Setup
        InputHelperType helper = TestHelper.mockHelper(platform);

        // When & Then
        HPLog.printlnt(ActivityValue.TODAY.valueXP(helper, UserMode.PARENT));
        HPLog.printlnt(CardType.ACTIVITY.cardTabXP(helper));
        HPLog.printlnt(CardType.ACTIVITY.cardItemXP(helper));
        HPLog.printlnt(ChoiceInput.HEIGHT.inputViewXP(helper));
        HPLog.printlnt(Popup.RATING.dismissXP(helper));
        HPLog.printlnt(Popup.RATING.presenceXP(helper));
        HPLog.printlnt(TextInput.POSTAL_CODE.inputViewXP(helper));
        HPLog.printlnt(Setting.FOOD.settingXP(helper));
        HPLog.printlnt(WeightProgress.PREVIOUS.valueXP(helper));
    }
}
