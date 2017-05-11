package com.holmusk.SuperLeapQA.general;

import com.holmusk.SuperLeapQA.model.Height;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberTestUtil;
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
        LogUtil.println(ftString);
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
        List<Double> cms = Height.CM.selectableHeightRange();

        for (Double cmVal : cms) {
            String cmString = Height.CM.heightString(cmVal);

            // When
            double cm = Height.CM.heightValue(cmString);

            // Then
            Assert.assertTrue(cms.contains(cm));
        }
    }
}
