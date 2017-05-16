package com.holmusk.SuperLeapQA.ui.base;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
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
    @DataProvider
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
        INDEX = index;
        TEST_KIT = Config.TEST_KIT;
    }

    @NotNull
    @DataProvider
    public Iterator<Object[]> userModeProvider() {
        List<Object[]> data = new LinkedList<>();
        UserMode[] modes = new UserMode[] { UserMode.TEEN };

        for (UserMode mode : modes) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
    }

    //region BaseTestType.
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
        /* Calling beforeClass() here ensures that each BaseEngine will
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