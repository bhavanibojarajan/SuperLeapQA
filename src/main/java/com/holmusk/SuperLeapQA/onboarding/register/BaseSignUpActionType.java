package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatableSubElementType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatableType;
import org.swiften.xtestkit.base.param.TextParam;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface BaseSignUpActionType extends
    BaseActionType,
    BaseSignUpValidationType,
    PlatformErrorType
{
    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link #acceptableAgeRange()}.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoBToBeOfAge(int)
     * @see #rxConfirmDoB()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAgeInput() {
        List<Integer> range = acceptableAgeRange();
        final int AGE = CollectionTestUtil.randomElement(range);

        return rxOpenDoBPicker()
            .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
            .flatMap(a -> rxConfirmDoB());
    }

    /**
     * Open the DoB dialog in the parent sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     * @see #rxDoBEditableField()
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditableField()
            .flatMap(ENGINE::rxClick)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay));
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE.rxElementContainingText("ok").flatMap(ENGINE::rxClick);
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param DATE A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxSelectDate(DateType)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoB(@NotNull final Date DATE) {
        return engine().rxSelectDate(() -> DATE);
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param age An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoBToBeOfAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);

        /* We need to add 1 to the day field to avoid the birthday. E.g.
         * if the current date is 10/05/2017 and we want the user to be 4
         * years-old, we need to select 11/05/2013 - any lower than that then
         * the user would be 5+ years-old */
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return rxSelectDoB(calendar.getTime());
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link #acceptableAgeRange()} should not bring the user to the correct
     * sign up screen. This action assumes the user is in the DoB selection
     * screen, but has not opened the DoB picker yet.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see #acceptableAgeRange()
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen()
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final List<Integer> AGES) {
        final List<Integer> RANGE = acceptableAgeRange();
        final int LENGTH = AGES.size();

        class IterateDoBs {
            @NotNull
            @SuppressWarnings("WeakerAccess")
            Flowable<Boolean> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return rxOpenDoBPicker()
                        .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
                        .flatMap(a -> rxConfirmDoB())
                        .flatMap(a -> {
                            if (VALID) {
                                return rxValidateAcceptableAgeScreen();
                            } else {
                                return rxValidateUnacceptableAgeScreen();
                            }
                        })
                        .flatMap(a -> rxNavigateBackWithBackButton())
                        .flatMap(a -> new IterateDoBs().repeat(INDEX + 1));
                } else {
                    return Flowable.empty();
                }
            }
        }

        return new IterateDoBs().repeat(0);
    }

    /**
     * Select a {@link Gender}.
     * @param gender A {@link Gender} instance.
     * @return A {@link Flowable} instance.
     * @see #rxGenderPicker(Gender)
     */
    @NotNull
    default Flowable<Boolean> rxSelectGender(@NotNull Gender gender) {
        return rxGenderPicker(gender).flatMap(engine()::rxClick);
    }

    /**
     * Select a {@link Height} mode.
     * @param mode A {@link Height} instance.
     * @return A {@link Flowable} instance.
     * @see #rxHeightModePicker(Height)
     */
    @NotNull
    default Flowable<Boolean> rxSelectHeightMode(@NotNull Height mode) {
        return rxHeightModePicker(mode).flatMap(engine()::rxClick);
    }

    /**
     * Select a height value, assuming the user is in the height selection
     * screen. Be sure to call {@link #rxSelectHeightMode(Height)} and
     * {@link #rxOpenHeightPickerWindow()} before calling this method.
     * @param MODE A {@link Height} instance.
     * @param HEIGHT A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxHeightPickerItemViews()
     */
    @NotNull
    default Flowable<Boolean> rxSelectHeight(@NotNull final Height MODE,
                                             final double HEIGHT) {
        final BaseSignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final String HEIGHT_STR = MODE.heightString(HEIGHT);

        final TextParam TEXT_PARAM = TextParam.builder()
            .withText(HEIGHT_STR)
            .withRetries(0)
            .build();

        SwipeRepeatableType repeater = new SwipeRepeatableSubElementType() {
            @NotNull
            @Override
            public Flowable<?> rxCompareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::heightValue)
                    .filter(a -> a > HEIGHT);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::heightValue)
                    .filter(a -> a < HEIGHT);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollViewChildItems() {
                return THIS.rxHeightPickerItemViews();
            }

            @Override
            public double elementSwipeRatio() {
                return 0.7d;
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxShouldKeepSwiping() {
                return ENGINE
                    .rxElementContainingText(TEXT_PARAM)
                    .flatMap(ENGINE::rxClick);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollableElementToSwipe() {
                return rxScrollableHeightPickerView();
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxSwipeOnce(@NotNull SwipeType param) {
                return ENGINE.rxSwipeOnce(param);
            }
        };

        return repeater.rxRepeatSwipe();
    }

    /**
     * Select a {@link Weight} mode.
     * @param mode A {@link Weight} mode.
     * @return A {@link Flowable} instance.
     * @see #rxWeightModePicker(Weight)
     */
    @NotNull
    default Flowable<Boolean> rxSelectWeightMode(@NotNull Weight mode) {
        return rxWeightModePicker(mode).flatMap(engine()::rxClick);
    }
}
