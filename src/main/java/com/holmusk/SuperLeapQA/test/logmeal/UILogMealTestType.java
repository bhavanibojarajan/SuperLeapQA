package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.HMUITestKit.model.HMInputType;
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
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
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
     * @see CollectionUtil#randomElement(Object[])
     * @see Engine#rxe_containsText(String...)
     * @see Engine#rxv_errorWithPageSource()
     * @see Mood#title()
     * @see Mood#values()
     * @see NumberUtil#randomBetween(int, int)
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#randomInput()
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_MEAL
     * @see Screen#SEARCH
     * @see TextInput#MEAL_DESCRIPTION
     * @see #assertCorrectness(TestSubscriber)
     * @see #defaultUserMode()
     * @see #engine()
     * @see #randomSelectableTime()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_selectMealPhotos(Engine)
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
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMealThenDelete_shouldWork() {
        // Setup
        final UILogMealTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final Date TIME = randomSelectableTime();
        final TextInput DSC_INPUT = TextInput.MEAL_DESCRIPTION;
        final String DESCRIPTION = DSC_INPUT.randomInput();
        final Mood MOOD = CollectionUtil.randomElement(Mood.values());
        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        final Flowable<?> RXV_MEAL_PAGE = Flowable
            .mergeArray(
                ENGINE.rxe_containsText(MOOD.title()),
                ENGINE.rxe_containsText(DESCRIPTION),
                rxv_hasMealTime(ENGINE, TIME)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxa_selectMood(ENGINE, MOOD))
            .flatMap(a -> THIS.rxa_selectMealPhotos(ENGINE))
            .flatMap(a -> THIS.rxa_openMealTimePicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectMealTime(ENGINE, TIME))
            .flatMap(a -> THIS.rxa_confirmMealTime(ENGINE))
            .flatMap(a -> THIS.rxa_input(ENGINE, DSC_INPUT, DESCRIPTION))
            .flatMap(a -> THIS.rxa_confirmMealDescription(ENGINE))
            .flatMap(a -> THIS.rxa_submitMeal(ENGINE))
            .flatMap(a -> THIS.rxa_dismissMealImageTutorial(ENGINE))
            .flatMap(a -> RXV_MEAL_PAGE)

            /* Go back to dashboard to access the search menu */
            .flatMap(a -> THIS.rxa_backToDashboard(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))

            /* Search for the meal and verify that it is present */
            .flatMap(a -> THIS.rxa_search(ENGINE, DESCRIPTION))
            .flatMap(a -> THIS.rxa_openSearchResult(ENGINE, DESCRIPTION))
            .flatMap(a -> RXV_MEAL_PAGE)

            /* Delete the meal and verify that it is no longer searchable */
            .flatMap(a -> THIS.rxa_openEditMenu(ENGINE))
            .flatMap(a -> THIS.rxa_deleteFromMenu(ENGINE))
            .flatMap(a -> THIS.rxa_dashboardAfterSearchAndDelete(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))
            .flatMap(a -> THIS.rxa_search(ENGINE, DESCRIPTION))

            /* At this stage, the meal should have been cleared from the
             * search result. If it is still present, throw an error */
            .flatMap(a -> THIS
                .rxe_searchResult(ENGINE, DESCRIPTION)
                .map(BooleanUtil::toTrue)
                .flatMap(b -> ENGINE.rxv_errorWithPageSource())
                .onErrorReturnItem(true)
            )
            .subscribe(subscriber);

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
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxa_clearSearchBar()
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxv_errorWithPageSource()
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#randomInput()
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#MEAL_PAGE
     * @see Screen#CHAT
     * @see Screen#SEARCH
     * @see TextInput#MEAL_COMMENT
     * @see #assertCorrectness(TestSubscriber)
     * @see #defaultUserMode()
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
            .map(a -> INPUT.randomInput())
            .collect(Collectors.toList());

        UserMode mode = defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_PAGE, Screen.CHAT)
            .flatMap(a -> Flowable.fromIterable(COMMENTS))
            .concatMap(a -> THIS.rxa_postChat(ENGINE, a)
                .flatMap(b -> THIS.rxe_chatMessage(ENGINE, a))
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> THIS.rxa_dismissChatWindow(ENGINE))
            .flatMap(a -> THIS.rxa_backToDashboard(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))
            .flatMap(a -> Flowable.fromIterable(COMMENTS))
            .concatMap(a -> THIS.rxa_search(ENGINE, a)
                .flatMap(b -> THIS.rxe_searchResult(ENGINE, a))
                .flatMap(b -> ENGINE.rxa_clearSearchBar())
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> THIS.rxa_search(ENGINE, COMMENTS.get(0)))
            .flatMap(a -> THIS.rxa_openSearchResult(ENGINE, COMMENTS.get(0)))

            /* When we access the meal page from a comment search result, the
             * scroll view will show the bottom of the page. We need to scroll
             * up to show the edit button */
            .flatMap(a -> THIS.rxa_makeEditButtonVisible(ENGINE))
            .flatMap(a -> THIS.rxa_openEditMenu(ENGINE))
            .flatMap(a -> THIS.rxa_deleteFromMenu(ENGINE))
            .flatMap(a -> THIS.rxa_dashboardAfterSearchAndDelete(ENGINE))
            .flatMap(a -> THIS.rxa_toggleDashboardSearch(ENGINE))
            .flatMap(a -> Flowable.fromIterable(COMMENTS))
            .concatMap(a -> THIS.rxa_search(ENGINE, a)
                .map(BooleanUtil::toTrue)
                .flatMap(b -> ENGINE.rxv_errorWithPageSource())
                .onErrorReturnItem(true)
                .flatMap(b -> ENGINE.rxa_clearSearchBar())
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
