package com.holmusk.SuperLeapQA.general;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.SLScreenManagerType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.localizer.Localizer;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.navigation.ScreenManagerType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

        for (UserMode mode : UserMode.values()) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
    }

    @Test
    public void test_emptyInputErrors_shouldBeCorrect() {
        // Setup & When
        LCFormat parent_gender = Gender.FEMALE.emptyInputError(UserMode.PARENT);
        LCFormat teen_gender = Gender.MALE.emptyInputError(UserMode.TEEN_U18);
        LCFormat parent_ft_height = Height.FT.emptyInputError(UserMode.PARENT);
        LCFormat parent_cm_height = Height.CM.emptyInputError(UserMode.PARENT);
        LCFormat parent_kg_weight = Weight.KG.emptyInputError(UserMode.PARENT);
        LCFormat parent_lb_weight = Weight.LB.emptyInputError(UserMode.PARENT);
        LCFormat teen_ft_height = Height.FT.emptyInputError(UserMode.TEEN_U18);
        LCFormat teen_cm_height = Height.CM.emptyInputError(UserMode.TEEN_U18);
        LCFormat teen_kg_weight = Weight.KG.emptyInputError(UserMode.TEEN_U18);
        LCFormat teen_lb_weight = Weight.LB.emptyInputError(UserMode.TEEN_U18);
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
        LocalDate date1 = LocalDate.of(2017, 5, 25);
        LocalDate date2 = LocalDate.of(2002, 5, 24);

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
                FN.clear();
                BN.clear();
            }
        };

        manager.registerScreenHolders();

        // When & Then

        /* DoB tests */
        LogUtil.println(manager.multiNodes(mode, Screen.SPLASH, Screen.DOB));
        LogUtil.println(manager.multiNodes(mode, Screen.DOB, Screen.INVALID_AGE));
        LogUtil.println(manager.multiNodes(mode, Screen.INVALID_AGE, Screen.DOB));

        /* Personal info tests */
        LogUtil.println(manager.multiNodes(mode, Screen.SPLASH, Screen.PERSONAL_INFO));

        /* Dashboard tests */
        LogUtil.println(manager.multiNodes(mode, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD));

        /* Log meal tests */
        LogUtil.println(manager.multiNodes(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL));
        LogUtil.println(manager.multiNodes(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_PAGE, Screen.CHAT));
    }

    @Test
    public void test_xPathCreation_shouldWork() {
        // Setup && When && Then
        LogUtil.println(ChoiceInput.COACH_PREF.inputViewXP(Platform.IOS));
        LogUtil.println(Height.CM.choicePickerItemXP(Platform.ANDROID));
    }
}
