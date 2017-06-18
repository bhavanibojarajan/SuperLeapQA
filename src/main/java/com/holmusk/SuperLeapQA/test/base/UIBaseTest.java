package com.holmusk.SuperLeapQA.test.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
    @NotNull private final CompositeDisposable DISPOSABLE;

    @NotNull private final Engine<?> ENGINE;

    public UIBaseTest(@NotNull Engine<?> engine) {
        FORWARD_NODES = new LinkedList<>();
        BACKWARD_NODES = new LinkedList<>();
        ENGINE = engine;
        DISPOSABLE = new CompositeDisposable();
    }

    @NotNull
    @Override
    public Engine<?> engine() {
        return ENGINE;
    }

    @NotNull
    @Override
    public CompositeDisposable masterDisposable() {
        return DISPOSABLE;
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

    @Override
    @BeforeSuite
    public void beforeSuite() {
        UIBaseTestType.super.beforeSuite();
    }

    @Override
    @AfterSuite
    public void afterSuite() {
        UIBaseTestType.super.afterSuite();
    }

    @Override
    @BeforeClass
    public void beforeClass() {
        UIBaseTestType.super.beforeClass();
    }

    @Override
    @AfterClass
    public void afterClass() {
        UIBaseTestType.super.afterClass();
    }

    @Override
    @BeforeMethod
    public void beforeMethod() {
        UIBaseTestType.super.beforeMethod();
    }

    @Override
    @AfterMethod
    public void afterMethod() {
        UIBaseTestType.super.afterMethod();
    }
}
