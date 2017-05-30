package com.holmusk.SuperLeapQA.ui.base;

import com.holmusk.SuperLeapQA.config.Config;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.kit.TestKit;
import org.swiften.xtestkit.kit.param.AfterClassParam;
import org.swiften.xtestkit.kit.param.AfterParam;
import org.swiften.xtestkit.kit.param.BeforeClassParam;
import org.swiften.xtestkit.kit.param.BeforeParam;
import org.testng.annotations.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 5/27/17.
 */
public class UIBaseTest implements UIBaseTestType {
    @NotNull private final TestKit TEST_KIT;
    @NotNull private final List<Node> FORWARD_NODES;
    @NotNull private final List<Node> BACKWARD_NODES;

    private final int INDEX;

    public UIBaseTest(int index) {
        LogUtil.printfThread("Init new test with index %d", index);
        FORWARD_NODES = new LinkedList<>();
        BACKWARD_NODES = new LinkedList<>();
        INDEX = index;
        TEST_KIT = Config.TEST_KIT;
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

    //region SLScreenManagerType
    @NotNull
    @Override
    public List<Node> registeredForwardNodes() {
        return FORWARD_NODES;
    }

    @NotNull
    @Override
    public List<Node> registeredBackwardNodes() {
        return BACKWARD_NODES;
    }

    @Override
    public void addForwardNodes(@NotNull List<Node> nodes) {
        FORWARD_NODES.addAll(nodes);
    }

    @Override
    public void addBackwardNodes(@NotNull List<Node> nodes) {
        BACKWARD_NODES.addAll(nodes);
    }

    @Override
    public void clearAllNodes() {
        FORWARD_NODES.clear();
        BACKWARD_NODES.clear();
    }
    //endregion

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
        BeforeClassParam param = BeforeClassParam.builder().withIndex(INDEX).build();
        TEST_KIT.beforeClass(param);
    }

    @AfterClass
    public void afterClass() {
        AfterClassParam param = AfterClassParam.builder().withIndex(INDEX).build();
        TEST_KIT.afterClass(param);
    }

    @BeforeMethod
    public void beforeMethod() {
        registerScreenHolders();
        BeforeParam param = BeforeParam.builder().withIndex(INDEX).build();
        TEST_KIT.beforeMethod(param);
    }

    @AfterMethod
    public void afterMethod() {
        clearAllNodes();
        AfterParam param = AfterParam.builder().withIndex(INDEX).build();
        TEST_KIT.afterMethod(param);
    }
}
