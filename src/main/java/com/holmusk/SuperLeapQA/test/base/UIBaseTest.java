package com.holmusk.SuperLeapQA.test.base;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.common.RetryType;
import org.testng.annotations.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 5/27/17.
 */
public class UIBaseTest implements UIBaseTestType {
    @NotNull private final List<Node> FORWARD_NODES;
    @NotNull private final List<Node> BACKWARD_NODES;

    @NotNull private final Engine<?> ENGINE;

    public UIBaseTest(@NotNull Engine<?> engine) {
        FORWARD_NODES = new LinkedList<>();
        BACKWARD_NODES = new LinkedList<>();
        ENGINE = engine;
    }

    @NotNull
    @Override
    public Engine<?> engine() {
        return ENGINE;
    }

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

    @BeforeSuite
    public void beforeSuite() {
        testKit().beforeSuite();
    }

    @AfterSuite
    public void afterSuite() {
        testKit().afterSuite();
    }

    @BeforeClass
    public void beforeClass() {
        engine().beforeClass(RetryType.DEFAULT);
    }

    @AfterClass
    public void afterClass() {
        engine().afterClass(RetryType.DEFAULT);
    }

    @BeforeMethod
    public void beforeMethod() {
        registerScreenHolders();
        engine().beforeMethod(RetryType.DEFAULT);
    }

    @AfterMethod
    public void afterMethod() {
        clearAllNodes();
        engine().afterMethod(RetryType.DEFAULT);
    }
}
