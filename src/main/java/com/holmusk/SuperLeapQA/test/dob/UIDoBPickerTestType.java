package com.holmusk.SuperLeapQA.test.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.util.LogUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIDoBPickerTestType extends UIBaseTestType, DOBPickerActionType {
    /**
     * This test validates that {@link Screen#DOB} selection works by
     * sequentially selecting DoBs and validating that DoBs that fall out of
     * {@link UserMode#validAgeCategoryRange()} should not bring the user
     * to the correct sign up screen. This action assumes the user is in
     * {@link Screen#DOB}, but has not opened the DoB picker yet.
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18} will
     * be treated the same. This is why we use
     * {@link UserMode#validAgeCategoryRange()} instead of
     * {@link UserMode#validAgeRange()}.
     * @param MODE {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #generalUserModeProvider()
     * @see #engine()
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxa_selectDoBToBeOfAge(Engine, int)
     * @see #rxa_confirmDoB(Engine)
     * @see #rxv_validAgeScreen(Engine)
     * @see #rxv_invalidAgeScreen(Engine, UserMode)
     */
    @SuppressWarnings("unchecked")
    @Test(
        enabled = false,
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_DoBSelection_shouldWork(@NotNull final UserMode MODE) {
        // Setup
        final UIDoBPickerTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final List<Integer> AGES = MODE.offsetFromCategoryValidRange(1, 1);
        final List<Integer> RANGE = MODE.validAgeCategoryRange();
        final int LENGTH = AGES.size();
        TestSubscriber subscriber = CustomTestSubscriber.create();
        LogUtil.printft("Selecting ages from range %s", AGES);

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return THIS.rxa_openDoBPicker(ENGINE)
                        .flatMap(a -> THIS.rxa_selectDoBToBeOfAge(ENGINE, AGE))
                        .flatMap(a -> THIS.rxa_confirmDoB(ENGINE))
                        .flatMap(a -> {
                            if (VALID) {
                                return THIS.rxv_validAgeScreen(ENGINE);
                            } else {
                                return THIS.rxv_invalidAgeScreen(ENGINE, MODE);
                            }
                        })
                        .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.DOB),
            new Repeater().repeat(0)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
