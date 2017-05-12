package com.holmusk.SuperLeapQA.general;

import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.log.LogUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 5/11/17.
 */
public final class GeneralTest {
    @Test
    public void test_heightModeToFt_shouldWork() {
        // Setup
        double heightInCM = 50;

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
}
