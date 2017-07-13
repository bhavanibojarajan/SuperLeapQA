package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.dashboard.DashboardActionType;
import com.holmusk.SuperLeapQA.test.mealpage.MealPageActionType;
import com.holmusk.SuperLeapQA.test.search.SearchActionType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * Test that meal logging works as expected, by following the logging
     * process and posting a meal onto the server. Afterwards, we can verify
     * whether the information we entered is correctly stored.
     * We then search for the meal from {@link Screen#SEARCH}.
     * Finally, we delete the meal to clean up and verify that it is no longer
     * searchable from {@link Screen#SEARCH}.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #randomSelectableTime()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_selectMealPhotos(Engine)
     * @see #rxa_openMealDescription(Engine) 
     * @see #rxa_input(Engine, HMInputType, String)
     * @see #rxa_confirmMealDescription(Engine)
     * @see #rxa_selectMood(Engine, Mood)
     * @see #rxa_openMealTimePicker(Engine)
     * @see #rxa_selectMealTime(Engine, Date)
     * @see #rxa_confirmMealTime(Engine)
     * @see #rxa_submitMeal(Engine)
     * @see #rxa_dismissMealImageTutorial(Engine)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxa_dashboardAfterSearchAndDelete(Engine)
     * @see #rxv_hasMealTime(Engine, Date)
     * @see #rxv_searchResultMustBeEmpty(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMealThenDelete_shouldWork() {
        // Setup
        Engine<?> engine = engine();
        Date time = randomSelectableTime();
        TextInput dscInput = TextInput.MEAL_DESCRIPTION;
        String description = dscInput.randomInput(engine);
        Mood mood = HPIterables.randomElement(Mood.values());
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        Flowable<?> rxv_mealPage = Flowable
            .mergeArray(
                engine.rxe_containsText(mood.title()),
                engine.rxe_containsText(description),
                rxv_hasMealTime(engine, time)
            )
            .all(HPObjects::nonNull)
            .toFlowable();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_ENTRY),
            rxa_selectMood(engine, mood),
            rxa_selectMealPhotos(engine),
            rxa_openMealTimePicker(engine),
            rxa_selectMealTime(engine, time),
            rxa_confirmMealTime(engine),
            rxa_openMealDescription(engine),
            rxa_input(engine, dscInput, description),
            rxa_confirmMealDescription(engine),
            rxa_submitMeal(engine),
            rxa_dismissMealImageTutorial(engine),
            rxv_mealPage,

            /* Go back to dashboard to access the search menu */
            rxa_backToDashboard(engine),
            rxa_toggleDashboardSearch(engine),

            /* Search for the meal and verify that it is present */
            rxa_search(engine, description),
            rxa_openSearchResult(engine, description),
            rxv_mealPage,

            /* Delete the meal and verify that it is no longer searchable */
            rxa_openEditMenu(engine),
            rxa_deleteFromMenu(engine),
            rxa_dashboardAfterSearchAndDelete(engine),
            rxa_toggleDashboardSearch(engine),
            rxa_search(engine, description),

            /* At this stage, the meal should have been cleared from the
             * search result. If it is still present, throw an error */
            rxe_searchResult(engine, description)
                .map(HPBooleans::toTrue)
                .flatMap(b -> engine.rxv_error())
                .onErrorReturnItem(true)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Test that after the user deletes a meal log (after navigating to it
     * from {@link Screen#SEARCH}), when {@link Screen#MEAL_PAGE} dismisses,
     * the search result view is refreshed to reflect the deletion. Only
     * applicable to {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_confirmMealTime(Engine)
     * @see #rxa_submitMeal(Engine)
     * @see #rxa_dismissMealImageTutorial(Engine)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_toggleDashboardSearch(Engine)
     * @see #rxa_search(Engine, String)
     * @see #rxa_openSearchResult(Engine, String)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxv_searchResultMustBeEmpty(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMealThenDelete_shouldRefreshSearchResult() {
        // Setup
        final Engine<?> ENGINE = engine();

        if (!(ENGINE instanceof AndroidEngine)) {
            return;
        }

        HMTextType dscInput = TextInput.MEAL_DESCRIPTION;
        String description = dscInput.randomInput(ENGINE);
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_ENTRY),
            rxa_input(ENGINE, dscInput, description),
            rxa_confirmMealDescription(ENGINE),
            rxa_submitMeal(ENGINE),
            rxa_dismissMealImageTutorial(ENGINE),

            /* Go back to dashboard to access the search menu */
            rxa_backToDashboard(ENGINE),
            rxa_toggleDashboardSearch(ENGINE),

            /* Search for the meal and verify that it is present */
            rxa_search(ENGINE, description),
            rxa_openSearchResult(ENGINE, description),

            /* Delete the meal and verify that the search result view is empty */
            rxa_openEditMenu(ENGINE),
            rxa_deleteFromMenu(ENGINE),
            rxv_searchResultMustBeEmpty(ENGINE)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Test the meal logging works as expected, by logging a meal then posting
     * comments on the chat page.
     * We then verify that the messages can be searched from
     * {@link Screen#SEARCH}, but will not be so after we delete the meal from
     * the database.
     * @see Engine#rxa_clearSearchBar()
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_postChat(Engine, String)
     * @see #rxa_dismissChatWindow(Engine)
     * @see #rxa_backToDashboard(Engine)
     * @see #rxa_toggleDashboardSearch(Engine)
     * @see #rxa_search(Engine, String)
     * @see #rxa_openSearchResult(Engine, String)
     * @see #rxa_openEditMenu(Engine)
     * @see #rxa_deleteFromMenu(Engine)
     * @see #rxa_dashboardAfterSearchAndDelete(Engine)
     * @see #rxe_chatMessage(Engine, String)
     * @see #rxe_searchResult(Engine, String)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMealWithComments_shouldWork() {
        // Setup
        final Engine<?> ENGINE = engine();

        /* If we are not testing on iOS, there is no need to test this method
         * since we cannot search for comments from the search bar on other
         * platforms */
        if (!(ENGINE instanceof IOSEngine)) {
            return;
        }

        final UILogMealTestType THIS = this;
        final TextInput INPUT = TextInput.MEAL_COMMENT;

        final List<String> COMMENTS = IntStream
            .range(0, 3).boxed()
            .map(a -> INPUT.randomInput(ENGINE))
            .collect(Collectors.toList());

        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_PAGE, Screen.CHAT),

            Flowable.fromIterable(COMMENTS)
                .concatMap(b -> Flowable.concat(
                    THIS.rxa_postChat(ENGINE, b),
                    THIS.rxe_chatMessage(ENGINE, b)
                )),

            rxa_dismissChatWindow(ENGINE),
            rxa_backToDashboard(ENGINE),
            rxa_toggleDashboardSearch(ENGINE),

            Flowable.fromIterable(COMMENTS)
                .concatMap(b -> Flowable.concat(
                    THIS.rxa_search(ENGINE, b),
                    THIS.rxe_searchResult(ENGINE, b),
                    ENGINE.rxa_clearSearchBar()
                )),

            rxa_search(ENGINE, COMMENTS.get(0)),
            rxa_openSearchResult(ENGINE, COMMENTS.get(0)),

            /* When we access the meal page from a comment search result, the
             * scroll view will show the bottom of the page. We need to scroll
             * up to show the edit button */
            rxa_makeEditButtonVisible(ENGINE),
            rxa_openEditMenu(ENGINE),
            rxa_deleteFromMenu(ENGINE),
            rxa_dashboardAfterSearchAndDelete(ENGINE),
            rxa_toggleDashboardSearch(ENGINE),

            Flowable.fromIterable(COMMENTS)
                .concatMap(b -> Flowable.concatArray(
                    THIS.rxa_search(ENGINE, b),
                    THIS.rxv_searchResultMustBeEmpty(ENGINE),
                    ENGINE.rxa_clearSearchBar())
                )
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
