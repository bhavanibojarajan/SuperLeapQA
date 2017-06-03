package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;

/**
 * Created by haipham on 5/16/17.
 */
public interface DashboardValidationType extends BaseValidationType {
    /**
     * Get the Use App Now button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_useAppNow(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("dashboard_title_useAppNow")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the add card button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_addCard(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("fabMain")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("card Add")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the card selector button from the
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD} menu.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see com.holmusk.SuperLeapQA.navigation.Screen#ADD_CARD
     * @see CardType#androidViewId()
     * @see CardType#cardSelectorText()
     * @see Engine#rxe_withText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_cardSelector(@NotNull Engine<?> engine,
                                                  @NotNull CardType card) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID(card.androidViewId())
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_withText(card.cardSelectorText())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the scroll view that can be swiped to switch between different
     * dashboard modes.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see IOSView.ViewType#UI_SCROLLVIEW
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardModeSwitcher(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_SCROLLVIEW.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the "Log a Meal" button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxe_logMeal(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("dashboard_title_logMeal")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the "Log weight" button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxe_logWeight(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("dashboard_title_logWeight")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the search button on
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#DASHBOARD}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardSearch(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("button search")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate the Use App Now screen after the user finishes sign up.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_useAppNow(Engine)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_useAppNow(@NotNull Engine<?> engine) {
        final DashboardValidationType THIS = this;

        return Flowable
            .mergeArray(
                engine.rxe_containsText("dashboard_title_accountReadyToUse"),
                engine.rxe_containsText("dashboard_title_rememberCheckEmail"),
                THIS.rxe_useAppNow(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate that the tutorial screen is present when the user first signs
     * up.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<?> rxv_dashboardTutorial(@NotNull Engine<?> engine) {
        return engine.rxe_containsText("dashboard_title_tapToMakeFirstEntry");
    }

    /**
     * Validate the dashboard BMI mode.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_dashboardBMI(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("dashboard_bmi_title_bmi"),
                engine.rxe_containsText("dashboard_bmi_title_current"),
                engine.rxe_containsText("dashboard_bmi_title_start"),
                engine.rxe_containsText("dashboard_bmi_title_recommended")
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate the dashboard activity mode.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     * @see UserMode#dashboardActivityKeyword()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_dashboardActivity(@NotNull Engine<?> engine,
                                              @NotNull UserMode mode) {
        return Flowable
            .mergeArray(
                engine.rxe_containsText("dashboard_activity_title_activity"),
                engine.rxe_containsText("dashboard_activity_title_thisWeek"),
                engine.rxe_containsText("dashboard_activity_title_weeklyGoal"),
                engine.rxe_containsText("dashboard_activity_title_today"),
                engine.rxe_containsText(mode.dashboardActivityKeyword())
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Validate the dashboard screen.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_dashboardModeSwitcher(Engine)
     * @see #rxe_logMeal(Engine)
     * @see #rxe_logWeight(Engine)
     */
    @NotNull
    default Flowable<?> rxv_dashboard(@NotNull Engine<?> engine) {
        return Flowable
            .mergeArray(
                rxe_logMeal(engine),
                rxe_logWeight(engine),
                rxe_dashboardModeSwitcher(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
