package com.holmusk.SuperLeapQA.general;

import com.holmusk.SuperLeapQA.model.*;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.localizer.LocalizationFormat;
import org.swiften.javautilities.localizer.Localizer;
import org.swiften.javautilities.log.LogUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

/**
 * Created by haipham on 5/11/17.
 */
public final class GeneralTest {
    @Test
    public void test_heightModeToFt_shouldWork() {
        // Setup
        double heightInCM = 52.0;

        // When
        String ftString = Height.CM.ftString(heightInCM);

        // Then
        Assert.assertTrue(ftString.matches("\\d+'\\d+\""));
    }

    @Test
    public void test_heightModeToCm_shouldWork() {
        // Setup
        double heightInFt = 1.64d;

        // When
        String cmString = Height.FT.cmString(heightInFt);

        // Then
        Assert.assertTrue(cmString.matches("\\d+\\.\\d+ cm"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_heightValueFromString_shouldWork() {
        // Setup
        List<Double> cms = Height.CM.selectableNumericRange();

        for (Double cmVal : cms) {
            String cmString = Height.CM.stringValue(cmVal);

            // When
            double cm = Height.CM.numericValue(cmString);

            // Then
            LogUtil.println(cm);
            Assert.assertTrue(cms.contains(cm));
        }
    }

    @Test
    public void test_weightModeToLB_shouldWork() {
        // Setup
        double weightInKg = 100;

        // When
        String lbString = Weight.KG.lbString(weightInKg);

        // Then
        Assert.assertTrue(lbString.matches("\\d+ lbs"));
    }

    @Test
    public void test_weightValueFromString_shouldWork() {
        // Setup
        List<Double> kgs = Weight.KG.selectableNumericRange();
        List<Double> lbs = Weight.LB.selectableNumericRange();
        List<Zipped<Double,Double>> allZipped = CollectionUtil.zip(kgs, lbs);

        for (Zipped<Double,Double> zipped : allZipped) {
            // When
            Assert.assertNotNull(zipped.FIRST);
            Assert.assertNotNull(zipped.SECOND);
            String kgString = Weight.KG.kgString(zipped.FIRST);
            String lbString = Weight.LB.lbString(zipped.SECOND);

            // Then
            double kg = Weight.KG.numericValue(kgString);
            double lb = Weight.LB.numericValue(lbString);
            LogUtil.println(kg, lb);
        }
    }

    @Test
    public void test_emptyInputErrors_shouldBeCorrect() {
        // Setup & When
        LocalizationFormat parent_gender = Gender.FEMALE.emptySignUpInputError(UserMode.PARENT);
        LocalizationFormat teen_gender = Gender.MALE.emptySignUpInputError(UserMode.TEEN);
        LocalizationFormat parent_ft_height = Height.FT.emptySignUpInputError(UserMode.PARENT);
        LocalizationFormat parent_cm_height = Height.CM.emptySignUpInputError(UserMode.PARENT);
        LocalizationFormat parent_kg_weight = Weight.KG.emptySignUpInputError(UserMode.PARENT);
        LocalizationFormat parent_lb_weight = Weight.LB.emptySignUpInputError(UserMode.PARENT);
        LocalizationFormat teen_ft_height = Height.FT.emptySignUpInputError(UserMode.TEEN);
        LocalizationFormat teen_cm_height = Height.CM.emptySignUpInputError(UserMode.TEEN);
        LocalizationFormat teen_kg_weight = Weight.KG.emptySignUpInputError(UserMode.TEEN);
        LocalizationFormat teen_lb_weight = Weight.LB.emptySignUpInputError(UserMode.TEEN);
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
}
