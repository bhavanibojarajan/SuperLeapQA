package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.card.CardItemHelperType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.test.TestNGUtil;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * Created by haipham on 5/16/17.
 */
public interface UIDashboardTestType extends
    UIBaseTestType,
    DashboardActionType,
    CardItemHelperType
{
    /**
     * Provide {@link CardType} that can be deleted.
     * @return {@link Iterator} instance.
     * @see TestNGUtil#oneFromEach(Object[]...)
     * @see CardType#MEAL
     * @see CardType#WEIGHT
     * @see UserMode#PARENT
     * @see UserMode#TEEN_A18
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> deletableCardProvider() {
        return TestNGUtil.oneFromEach(
            new Object[] {
                UserMode.PARENT
//                UserMode.TEEN_A18
            },

            new Object[] {
//                CardType.MEAL
                CardType.WEIGHT
            }
        ).iterator();
    }

    /**
     * Delete all cards for a particular {@link CardType}.
     * If we want to use this method as an utility method, we should run it
     * with {@link org.swiften.xtestkit.mobile.Platform#ANDROID} since it's
     * much faster.
     * @param mode {@link UserMode} instance.
     * @param card {@link CardType} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#DASHBOARD
     * @see #deletableCardProvider()
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_revealAndDeleteCardItems(Engine, CardType)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIDashboardTestType.class,
        dataProvider = "deletableCardProvider"
    )
    default void test_deleteCards_shouldWork(@NotNull UserMode mode,
                                             @NotNull CardType card) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.DASHBOARD),
            rxa_revealAndDeleteCardItems(engine, card)
        ).all(ObjectUtil::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
