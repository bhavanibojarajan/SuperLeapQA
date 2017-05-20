package com.holmusk.SuperLeapQA.general;

import com.holmusk.SuperLeapQA.model.*;
import org.jetbrains.annotations.NotNull;
import static org.mockito.Mockito.*;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.localizer.Localizer;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.navigation.ScreenManagerType;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by haipham on 5/11/17.
 */
public final class GeneralTest {
    @Test
    public void test_emptyInputErrors_shouldBeCorrect() {
        // Setup & When
        LCFormat parent_gender = Gender.FEMALE.emptyInputError(UserMode.PARENT);
        LCFormat teen_gender = Gender.MALE.emptyInputError(UserMode.TEEN_UNDER_18);
        LCFormat parent_ft_height = Height.FT.emptyInputError(UserMode.PARENT);
        LCFormat parent_cm_height = Height.CM.emptyInputError(UserMode.PARENT);
        LCFormat parent_kg_weight = Weight.KG.emptyInputError(UserMode.PARENT);
        LCFormat parent_lb_weight = Weight.LB.emptyInputError(UserMode.PARENT);
        LCFormat teen_ft_height = Height.FT.emptyInputError(UserMode.TEEN_UNDER_18);
        LCFormat teen_cm_height = Height.CM.emptyInputError(UserMode.TEEN_UNDER_18);
        LCFormat teen_kg_weight = Weight.KG.emptyInputError(UserMode.TEEN_UNDER_18);
        LCFormat teen_lb_weight = Weight.LB.emptyInputError(UserMode.TEEN_UNDER_18);
        Localizer localizer = Localizer.builder().addBundle("Strings", Locale.US).build();

        // Then
        LogUtil.println(localizer.localize(parent_gender));
        LogUtil.println(localizer.localize(teen_gender));
        LogUtil.println(localizer.localize(parent_ft_height));
        LogUtil.println(localizer.localize(parent_cm_height));
        LogUtil.println(localizer.localize(parent_kg_weight));
        LogUtil.println(localizer.localize(parent_lb_weight));
        LogUtil.println(localizer.localize(teen_ft_height));
        LogUtil.println(localizer.localize(teen_cm_height));
        LogUtil.println(localizer.localize(teen_kg_weight));
        LogUtil.println(localizer.localize(teen_lb_weight));
    }

    @Test
    public void test_randomTextInput_shouldWork() {
        // Setup & When
        String name = TextInput.NAME.randomInput();
        String phone = TextInput.PHONE.randomInput();
        String email = TextInput.EMAIL.randomInput();

        // Then
        LogUtil.println(name, phone, email);
    }

    @Test
    public void test_birthDayCalculation_shouldBeCorrect() {
        // Setup
        Calendar calendar = Calendar.getInstance();
        LocalDate date1 = LocalDate.of(2017, 5, 19);
        LocalDate date2 = LocalDate.of(2001, 5, 19);

        // When
        long yearDiff = ChronoUnit.YEARS.between(date1, date2);

        // Then
        LogUtil.println(yearDiff);
    }

    @Test
    public void test_userModeAgeRange_shouldBeCorrect() {
        // Setup & When & Then
        LogUtil.println(UserMode.PARENT.acceptableAgeRange());
        LogUtil.println(UserMode.PARENT.acceptableAgeCategoryRangeString());
        LogUtil.println(UserMode.TEEN_UNDER_18.acceptableAgeRange());
        LogUtil.println(UserMode.TEEN_UNDER_18.acceptableAgeCategoryRangeString());
        LogUtil.println(UserMode.TEEN_ABOVE_18.acceptableAgeRange());
        LogUtil.println(UserMode.TEEN_ABOVE_18.acceptableAgeCategoryRangeString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_navigation_shouldWorkCorrectly() {
        // Setup
        final Engine<?> ENGINE = mock(Engine.class);
        final List<ScreenManagerType.Node> NODES = new ArrayList<>();

        ScreenManagerType manager = new ScreenManagerType() {
            @NotNull
            @Override
            public Engine<?> engine() {
                return ENGINE;
            }

            @Override
            public void addNodes(@NotNull List<Node> nodes) {
                NODES.addAll(nodes);
            }

            @NotNull
            @Override
            public List<Node> registeredNodes() {
                return NODES;
            }
        };

        manager.register(Screen.values());

        try {
            // When
            List<ScreenManagerType.Node> n1 = manager.nodes(Screen.SPLASH, Screen.REGISTER);
            List<ScreenManagerType.Node> n2 = manager.nodes(Screen.SPLASH, Screen.SIGN_IN);
            List<ScreenManagerType.Node> n3 = manager.nodes(Screen.SIGN_IN, Screen.REGISTER);
            List<ScreenManagerType.Node> n4 = manager.nodes(Screen.WELCOME, Screen.REGISTER);
            List<ScreenManagerType.Node> n5 = manager.nodes(Screen.SPLASH, Screen.TEEN_DOB_PICKER);
            List<ScreenManagerType.Node> n6 = manager.nodes(Screen.SIGN_IN, Screen.PARENT_DOB_PICKER);
            List<ScreenManagerType.Node> n7 = manager.nodes(Screen.SPLASH, Screen.SIGN_IN, Screen.TEEN_DOB_PICKER);

            // Then
            LogUtil.println(n1);
            LogUtil.println(n2);
            LogUtil.println(n3);
            LogUtil.println(n4);
            LogUtil.println(n5);
            LogUtil.println(n6);
            LogUtil.println(n7);
        } catch (Exception e) {
            LogUtil.println(e);
        }
    }
}
