package com.holmusk.SuperLeapQA.general;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.bmi.BMIParam;
import com.holmusk.SuperLeapQA.bmi.BMIUtil;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.ScreenHolder;
import com.holmusk.SuperLeapQA.navigation.type.SLScreenManagerType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.navigation.ScreenManagerType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by haipham on 5/11/17.
 */
@SuppressWarnings("UndeclaredTests")
public final class GeneralTest {
    @NotNull
    @DataProvider
    public Iterator<Object[]> userModeProvider() {
        List<Object[]> data = new LinkedList<>();

        UserMode[] modes = new UserMode[] {
            UserMode.PARENT,
            UserMode.TEEN_A18,
            UserMode.TEEN_U18
        };

        for (UserMode mode : modes) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
    }

    @Test
    public void test_randomTextInput_shouldWork() {
        // Setup & When & Then
        LogUtil.println(TextInput.NAME.randomInput());
        LogUtil.println(TextInput.PHONE.randomInput());
        LogUtil.println(TextInput.EMAIL.randomInput());
        LogUtil.println(TextInput.UNIT_NUMBER.randomInput());
    }

    @Test
    public void test_choiceInputXP_shouldWork() {
        // Setup & When & Then
        LogUtil.println(ChoiceInput.WEIGHT.inputViewXP(Platform.IOS));
    }

    @Test
    public void test_birthDayCalculation_shouldBeCorrect() {
        // Setup
        LocalDate date1 = LocalDate.of(2017, 6, 5);
        LocalDate date2 = LocalDate.of(2013, 6, 5);

        // When
        long yearDiff = ChronoUnit.YEARS.between(date1, date2);

        // Then
        LogUtil.println(yearDiff);
    }

    @Test
    public void test_userModeAgeRange_shouldBeCorrect() {
        // Setup & When & Then
        LogUtil.println(UserMode.PARENT.validAgeRange());
        LogUtil.println(UserMode.PARENT.validAgeCategoryRangeString());
        LogUtil.println(UserMode.TEEN_U18.validAgeRange());
        LogUtil.println(UserMode.TEEN_U18.validAgeCategoryRangeString());
        LogUtil.println(UserMode.TEEN_A18.validAgeRange());
        LogUtil.println(UserMode.TEEN_A18.validAgeCategoryRangeString());
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "userModeProvider")
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
            List<ScreenManagerType.Node> nodes = manager.multiNodes(mode, start, screen);
            ScreenManagerType.Node first = nodes.get(0);
            ScreenManagerType.Node last = nodes.get(nodes.size() - 1);
            assertEquals(((ScreenHolder)first.S1).SCREEN, start);
            assertEquals(((ScreenHolder)last.S2).SCREEN, screen);
        }
    }

    @Test
    public void test_XPathCreation_shouldWork() {
        // Setup && When && Then
        LogUtil.println(ChoiceInput.COACH_PREF.inputViewXP(Platform.IOS));
        LogUtil.println(Height.CM.choicePickerItemXP(Platform.ANDROID));
        LogUtil.println(Setting.LOCATION.settingXP(Platform.ANDROID));
        LogUtil.println(Setting.LOCATION.settingXP(Platform.IOS));
        LogUtil.println(Setting.UNITS.settingXP(Platform.ANDROID));
        LogUtil.println(Setting.UNITS.settingXP(Platform.IOS));
    }

    @Test
    public void test_bmiRanges_shouldWork() {
        // Setup
        Ethnicity asian = Ethnicity.ASIAN_OTHER;
        Gender male = Gender.FEMALE;
        UserMode userMode = UserMode.TEEN_U18;

        List<Zip<Height,String>> height = Height.random(
            Platform.ANDROID,
            userMode,
            UnitSystem.IMPERIAL);

        List<Zip<Weight,String>> weight = Weight.random(
            Platform.ANDROID,
            userMode,
            UnitSystem.IMPERIAL
        );

        BMIParam param1 = BMIParam.builder()
            .withEthnicity(asian)
            .withGender(male)
            .withHeight(height)
            .withWeight(weight)
            .build();

        // When & Then
        LogUtil.println(BMIUtil.tightestInvalidRange(userMode, param1));
        LogUtil.println(BMIUtil.widestInvalidRange(userMode, param1));
        LogUtil.println(BMIUtil.withinTightestInvalidRange(userMode, param1));
        LogUtil.println(param1.heightM());
        LogUtil.println(param1.weightKG());
        LogUtil.println(param1.bmi());
    }
}
