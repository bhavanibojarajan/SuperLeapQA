package com.holmusk.SuperLeapQA.ui.base;

import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLTextType;
import com.holmusk.SuperLeapQA.navigation.type.NavigationType;
import com.holmusk.SuperLeapQA.navigation.type.SLScreenManagerType;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.test.BaseTestType;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * Created by haipham on 5/27/17.
 */
public interface UIBaseTestType extends BaseTestType, NavigationType, SLScreenManagerType {
    @NotNull
    default List<Zip<SLTextType,String>> loginCredentials() {
        return Arrays.asList(
            new Zip<SLTextType,String>(TextInput.EMAIL, "haipham@gmail.com"),
            new Zip<SLTextType,String>(TextInput.PASSWORD, "12345678")
        );
    }

    /**
     * This {@link DataProvider} provides {@link org.swiften.xtestkit.base.Engine}
     * instances for constructor methods.
     * @return {@link Iterator} instance.
     * @see Config#runCount()
     */
    @NotNull
    @DataProvider(parallel = true)
    static Iterator<Object[]> dataProvider() {
        List<Object[]> data = new LinkedList<>();

        for (int i = 0, count = Config.runCount(); i < count; i++) {
            data.add(new Object[] { i });
        }

        return data.iterator();
    }

    /**
     * This {@link DataProvider} provides {@link UserMode} instances that
     * do not care whether a guarantor is required.
     * @return {@link Iterator} instance.
     * @see UserMode#PARENT
     * @see UserMode#TEEN_U18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> generalUserModeProvider() {
        List<Object[]> data = new LinkedList<>();
        UserMode[] modes = new UserMode[] { UserMode.TEEN_U18 };

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
     * @return {@link Iterator} instance.
     * @see UserMode#TEEN_U18
     * @see UserMode#TEEN_A18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> guarantorSpecificUserModeProvider() {
        List<Object[]> data = new LinkedList<>();

        UserMode[] modes = new UserMode[] {
            UserMode.TEEN_U18,
            UserMode.TEEN_A18
        };

        for (UserMode mode : modes) {
            data.add(new Object[] { mode });
        }

        return data.iterator();
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
    }
}
