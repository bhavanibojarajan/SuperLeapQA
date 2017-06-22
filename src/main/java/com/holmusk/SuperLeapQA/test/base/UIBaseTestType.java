package com.holmusk.SuperLeapQA.test.base;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.type.BackwardNavigationType;
import com.holmusk.SuperLeapQA.navigation.type.ForwardNavigationType;
import com.holmusk.SuperLeapQA.navigation.type.SLScreenManagerType;
import com.holmusk.SuperLeapQA.navigation.type.ScreenInitializationType;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.util.LogUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.javautilities.test.TestNGUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.kit.TestKit;
import org.swiften.xtestkit.test.BaseTestType;
import org.swiften.javautilities.protocol.RetryType;
import org.testng.annotations.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haipham on 5/27/17.
 */
public interface UIBaseTestType extends
    BaseTestType,
    BackwardNavigationType,
    ForwardNavigationType,
    ScreenInitializationType,
    SLScreenManagerType
{
    /**
     * This {@link DataProvider} provides {@link org.swiften.xtestkit.base.Engine}
     * instances for constructor methods.
     * @return {@link Iterator} instance.
     * @see Config#runCount()
     * @see TestKit#engine(int)
     */
    @NotNull
    @DataProvider(parallel = true)
    static Iterator<Object[]> engineProvider() {
        List<Object[]> data = new LinkedList<>();
        TestKit kit = Config.TEST_KIT;

        for (int i = 0, count = Config.runCount(); i < count; i++) {
            data.add(new Object[] { kit.engine(i) });
        }

        return data.iterator();
    }

    /**
     * This {@link DataProvider} provides {@link UserMode} instances that
     * do not care whether a guarantor is required.
     * @return {@link Iterator} instance.
     * @see TestNGUtil#oneFromEach(Object...)
     * @see UserMode#PARENT
     * @see UserMode#TEEN_A18
     * @see UserMode#TEEN_U18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> generalUserModeProvider() {
        return TestNGUtil.oneFromEach(
            UserMode.PARENT,
//            UserMode.TEEN_A18,
            UserMode.TEEN_U18
        ).iterator();
    }

    /**
     * This {@link DataProvider} provides {@link UserMode} for tests that
     * specifically request for guarantor information. For example, personal
     * information input tests will require {@link UserMode#requiresGuarantor()}
     * to decide whether the parent info screen is required.
     * @return {@link Iterator} instance.
     * @see TestNGUtil#oneFromEach(Object...)
     * @see UserMode#TEEN_U18
     * @see UserMode#TEEN_A18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> guarantorSpecificUserModeProvider() {
        return TestNGUtil.oneFromEach(
            UserMode.TEEN_U18,
            UserMode.TEEN_A18
        ).iterator();
    }

    /**
     * Override this method to provide default implementation.
     * @return {@link TestKit} instance.
     * @see Config#TEST_KIT
     */
    @NotNull
    @Override
    default TestKit testKit() {
        return Config.TEST_KIT;
    }

    /**
     * Common method to check correctness of completed operations.
     * @param subscriber The {@link TestSubscriber} instance that received
     *                   all notifications.
     */
    default void assertCorrectness(@NotNull TestSubscriber subscriber) {
        subscriber.assertSubscribed();
        subscriber.assertNoErrors();
        subscriber.assertComplete();
        LogUtil.printft("Test results: %s", RxUtil.nextEvents(subscriber));
    }

    @BeforeSuite
    default void beforeSuite() {
        testKit().beforeSuite();
    }

    @AfterSuite
    default void afterSuite() {
        testKit().afterSuite();
    }

    @BeforeClass
    default void beforeClass() {
        engine().beforeClass(RetryType.DEFAULT);
    }

    @AfterClass
    default void afterClass() {
        engine().afterClass(RetryType.DEFAULT);
    }

    @BeforeMethod
    default void beforeMethod() {
        registerScreenHolders();

        Engine<?> engine = engine();
//        CompositeDisposable disposable = masterDisposable();
//        Popup[] popups = Popup.values();
//        Disposable d1 = engine.rxa_pollAndDismissPopup(popups).subscribe();
//        disposable.add(d1);

        engine.beforeMethod(RetryType.DEFAULT);
    }

    @AfterMethod
    default void afterMethod() {
        clearAllNodes();

//        CompositeDisposable disposable = masterDisposable();
//        disposable.dispose();

        engine().afterMethod(RetryType.DEFAULT);
    }

    /**
     * Get the master {@link CompositeDisposable} for everything aside from
     * tests.
     * @return {@link CompositeDisposable} instance.
     */
    @NotNull CompositeDisposable masterDisposable();
}
