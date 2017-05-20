package com.holmusk.SuperLeapQA.ui.base;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.kit.*;
import org.swiften.xtestkit.kit.param.AfterClassParam;
import org.swiften.xtestkit.kit.param.AfterParam;
import org.swiften.xtestkit.kit.param.BeforeClassParam;
import org.swiften.xtestkit.kit.param.BeforeParam;
import org.testng.annotations.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 4/4/17.
 */
public class UIBaseTest implements BaseActionType, BaseValidationType {
    @NotNull
    @DataProvider(parallel = true)
    public static Iterator<Object[]> dataProvider() {
        List<Object[]> data = new LinkedList<>();

        for (int i = 0, count = Config.runCount(); i < count; i++) {
            data.add(new Object[] { i });
        }

        return data.iterator();
    }

    @NotNull private final TestKit TEST_KIT;

    private final int INDEX;

    public UIBaseTest(int index) {
        LogUtil.printfThread("Init new test with index %d", index);
        INDEX = index;
        TEST_KIT = Config.TEST_KIT;
    }

    /**
     * This {@link DataProvider} provides {@link UserMode} instances that
     * do not care whether a guarantor is required.
     * @return A {@link Iterator} instance.
     * @see UserMode#PARENT
     * @see UserMode#TEEN_UNDER_18
     */
    @NotNull
    @DataProvider
    public Iterator<Object[]> generalUserModeProvider() {
        List<Object[]> data = new LinkedList<>();
        UserMode[] modes = new UserMode[] { UserMode.TEEN_UNDER_18};

        for (UserMode mode : modes) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
    }

    /**
     * This {@link DataProvider} provides {@link UserMode} for tests that
     * specifically request for guarantor information. For example, personal
     * information input tests will require {@link UserMode#requiresGuarantor()}
     * to decide whether the parent info screen is required.
     * @return A {@link Iterator} instance.
     * @see UserMode#TEEN_UNDER_18
     * @see UserMode#TEEN_ABOVE_18
     */
    @NotNull
    @DataProvider
    public Iterator<Object[]> guarantorSpecificUserModeProvider() {
        List<Object[]> data = new LinkedList<>();

        UserMode[] modes = new UserMode[] {
            UserMode.TEEN_UNDER_18,
            UserMode.TEEN_ABOVE_18
        };

        for (UserMode mode : modes) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
    }

    //region BaseTestType
    @NotNull
    @Override
    public TestKit testKit() {
        return TEST_KIT;
    }

    @Override
    public int currentIndex() {
        return INDEX;
    }
    //endregion

    protected long currentThread() {
        return Thread.currentThread().getId();
    }

    @BeforeSuite
    public void beforeSuite() {
        TEST_KIT.beforeSuite();
    }

    @AfterSuite
    public void afterSuite() {
        TEST_KIT.afterSuite();
    }

    @BeforeClass
    public void beforeClass() {
        /* Calling beforeClass() here ensures that each Engine will
         * only start the test environment once */
        TEST_KIT.beforeClass(beforeClassParam());
    }

    @AfterClass
    public void afterClass() {
        TEST_KIT.afterClass(afterClassParam());
    }

    @BeforeMethod
    public void beforeMethod() {
        TEST_KIT.before(beforeParam());
    }

    @AfterMethod
    public void afterMethod() {
        TEST_KIT.after(afterParam());
    }

    @NotNull
    private BeforeClassParam beforeClassParam() {
        return BeforeClassParam.builder().withIndex(INDEX).build();
    }

    @NotNull
    private AfterClassParam afterClassParam() {
        return AfterClassParam.builder().withIndex(INDEX).build();
    }

    @NotNull
    private BeforeParam beforeParam() {
        return BeforeParam.builder().withIndex(INDEX).build();
    }

    @NotNull
    private AfterParam afterParam() {
        return AfterParam.builder().withIndex(INDEX).build();
    }

    /**
     * Common method to check correctness of completed operations.
     * @param subscriber The {@link TestSubscriber} instance that received
     *                   all notifications.
     */
    protected void assertCorrectness(@NotNull TestSubscriber subscriber) {
        subscriber.assertSubscribed();
        subscriber.assertNoErrors();
        subscriber.assertComplete();
    }
}