package com.holmusk.SuperLeapQA.runner;

import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.swiften.xtestkit.test.RepeatRunner;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * Created by haipham on 5/7/17.
 */
public final class TestRunner implements RepeatRunner.TestRunner {
    /**
     * This {@link RepeatRunner} must be static to avoid it being recreated
     * upon every test iteration.
     */
    @NotNull
    private static final RepeatRunner RUNNER;

    static {
        RUNNER = RepeatRunner.builder()
            .addListener(Config.TEST_KIT)
            .withParameterConsumer(Config.TEST_KIT)
            .withRetryCount(Config.runCount())
            .withPartitionSize(Config.runCount())
            .withVerboseLevel(0)
            .build();
    }

    @NotNull
    @DataProvider(parallel = true)
    public static Iterator<Object[]> dataProvider() {
        return RUNNER.dataParameters();
    }

    @Test(enabled = false)
    @Override
    @RepeatRunner.TestRunnerMethod
    public void runTests() {
        RUNNER.run();
    }
}
