package com.holmusk.SuperLeapQA.ui.logmeal;

import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.ui.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.ui.mealpage.MealPageActionType;
import com.holmusk.SuperLeapQA.ui.search.SearchActionType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 29/5/17.
 */
public interface UILogMealTestType extends
    UIBaseTestType,
    DashboardActionType,
    LogMealActionType,
    MealPageActionType,
    SearchActionType
{
    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * and confirm that all {@link org.openqa.selenium.WebElement} are present.
     * @see com.holmusk.SuperLeapQA.navigation.Screen#SPLASH
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOGIN
     * @see com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(groups = "ValidateScreen")
    default void test_logMeal_isValidScreen() {
        // Setup
        final UILogMealTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxv_mealLog(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Test that meal logging works as expected, by following the logging
     * process and posting a meal onto the server. Afterwards, we can verify
     * whether the information we entered is correctly stored.
     * We then search for the meal from {@link Screen#SEARCH}.
     * Finally, we delete the meal to clean up and verify that it is no longer
     * searchable from {@link Screen#SEARCH}.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_MEAL
     * @see Screen#SEARCH
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #mealLogProgressDelay()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_selectMealPhotos(Engine)
     * @see #rxa_input(Engine, SLInputType, String)
     * @see #rxa_selectMood(Engine, Mood)
     * @see #rxa_openMealTimePicker(Engine)
     * @see #rxa_selectMealTime(Engine, Date)
     * @see #rxa_confirmMealTime(Engine)
     * @see #rxa_submitMeal(Engine)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_openEditMeal(Engine)
     * @see #rxa_deleteMeal(Engine)
     * @see #rxv_hasMealTime(Engine, Date)
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    default void test_logMealThenDelete_shouldWork() {
        // Setup
        final UILogMealTestType THIS = this;
        final Engine<?> ENGINE = engine();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -NumberTestUtil.randomBetween(1, 5));
        final Date TIME = calendar.getTime();
        final TextInput DSC_INPUT = TextInput.MEAL_DESCRIPTION;
        final String DESCRIPTION = DSC_INPUT.randomInput();
        final Mood MOOD = CollectionTestUtil.randomElement(Mood.values());
        TestSubscriber subscriber = CustomTestSubscriber.create();
        UserMode mode = UserMode.PARENT;

        final Flowable<?> RXV_MEAL_PAGE = Flowable
            .mergeArray(
                ENGINE.rxe_containsText(MOOD.moodTitle()),
                ENGINE.rxe_containsText(DESCRIPTION),
                rxv_hasMealTime(ENGINE, TIME)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxa_selectMealPhotos(ENGINE))
            .flatMap(a -> THIS.rxa_input(ENGINE, DSC_INPUT, DESCRIPTION))
            .flatMap(a -> THIS.rxa_selectMood(ENGINE, MOOD))
            .flatMap(a -> THIS.rxa_openMealTimePicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectMealTime(ENGINE, TIME))
            .flatMap(a -> THIS.rxa_confirmMealTime(ENGINE))
            .flatMap(a -> THIS.rxa_submitMeal(ENGINE))
            .delay(mealLogProgressDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> RXV_MEAL_PAGE)

            /* Go back to dashboard to access the search menu */
            .flatMap(a -> THIS.rxa_backToDashboard(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))

            /* Search for the meal and verify that it is present */
            .flatMap(a -> THIS.rxa_search(ENGINE, DESCRIPTION))
            .flatMap(a -> THIS.rxa_openSearchResult(ENGINE, DESCRIPTION))
            .flatMap(a -> RXV_MEAL_PAGE)

            /* Delete the meal and verify that it is no longer searchable */
            .flatMap(a -> THIS.rxa_openEditMeal(ENGINE))
            .flatMap(a -> THIS.rxa_deleteMeal(ENGINE))
            .flatMap(a -> THIS.rxa_backToDashboard(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))
            .flatMap(a -> THIS.rxa_search(ENGINE, DESCRIPTION))

            /* At this stage, the meal should have been cleared from the
             * search result. If it is still present, throw an error */
            .flatMap(a -> THIS
                .rxe_searchResult(ENGINE, DESCRIPTION)
                .map(BooleanUtil::toTrue)
                .flatMap(b -> RxUtil.error())
                .onErrorReturnItem(true)
            )
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
