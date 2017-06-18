package com.holmusk.SuperLeapQA.test.dashboard;

import com.holmusk.SuperLeapQA.model.ActivityValue;
import com.holmusk.SuperLeapQA.model.CardType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.XPath;

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
     * @see CardType#cardIconXP(InputHelperType)
     * @see Engine#rxe_withXPath(XPath...)
     * @see Point#getY()
     * @see WebElement#getLocation()
     */
    @NotNull
    default Flowable<WebElement> rxe_cardSelector(@NotNull Engine<?> engine,
                                                  @NotNull CardType card) {
        XPath xPath = card.cardIconXP(engine);

        if (engine instanceof AndroidEngine) {
            return engine.rxe_withXPath(xPath).firstElement().toFlowable();
        } else {
            return engine
                .rxe_withXPath(xPath)
                .sorted((a, b) -> {
                    Point aPoint = a.getLocation();
                    Point bPoint = b.getLocation();
                    return bPoint.getY() - aPoint.getY();
                })
                .firstElement()
                .toFlowable();
        }
    }

    /**
     * Get the items corresponding to {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see CardType#cardItemXP(InputHelperType)
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    default Flowable<WebElement> rxe_cardItems(@NotNull Engine<?> engine,
                                               @NotNull CardType card) {
        XPath xPath = card.cardItemXP(engine);
        return engine.rxe_withXPath(xPath);
    }

    /**
     * Get the first item corresponding to {@link CardType}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see WebElement#getLocation()
     * @see #rxe_cardItems(Engine, CardType)
     */
    @NotNull
    default Flowable<WebElement> rxe_firstCardItem(@NotNull Engine<?> engine,
                                                   @NotNull CardType card) {
        return rxe_cardItems(engine, card)
            .sorted((a, b) -> {
                Point aPoint = a.getLocation();
                Point bPoint = b.getLocation();
                return aPoint.getY() - bPoint.getY();
            })
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the scroll view that can be swiped to switch between different
     * dashboard modes.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see AndroidView.Type#VIEW_PAGER
     * @see IOSView.Type#UI_SCROLL_VIEW
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardModeSwitcher(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.Type.VIEW_PAGER.className())
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.Type.UI_SCROLL_VIEW.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the card tab {@link WebElement}.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_withXPath(XPath...)
     * @see CardType#cardTabXP(InputHelperType)
     */
    @NotNull
    default Flowable<WebElement> rxe_dashboardCardTab(@NotNull Engine<?> engine,
                                                      @NotNull CardType card) {
        XPath xPath = card.cardTabXP(engine);
        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }

    /**
     * Check if the card list view is not empty.
     * @param engine {@link Engine} instance.
     * @param card {@link CardType} instance.
     * @return {@link Flowable} instance.
     * @see NumberUtil#largerThanZero(Number)
     * @see #rxe_cardItems(Engine, CardType)
     */
    @NotNull
    default Flowable<Boolean> rxv_cardListNotEmpty(@NotNull Engine<?> engine,
                                                   @NotNull CardType card) {
        return rxe_cardItems(engine, card)
            .count().toFlowable()
            .doOnNext(a -> LogUtil.printft("%s items currently", a))
            .map(NumberUtil::largerThanZero);
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
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("action_search")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsID("button search")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get {@link com.holmusk.SuperLeapQA.model.DashboardMode#ACTIVITY}
     * value {@link WebElement} corresponding to {@link ActivityValue}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param type {@link ActivityValue} instance.
     * @return {@link Flowable} instance.
     * @see ActivityValue#valueXP(InputHelperType, UserMode)
     * @see Engine#rxe_withXPath(XPath...)
     */
    @NotNull
    default Flowable<WebElement> rxe_activityValueDisplay(
        @NotNull Engine<?> engine,
        @NotNull UserMode mode,
        @NotNull ActivityValue type)
    {
        XPath xPath = type.valueXP(engine, mode);
        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }

    /**
     * Get the currently displayed value for {@link ActivityValue}.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param type {@link ActivityValue} instance.
     * @return {@link Flowable} instance.
     * @see Double#valueOf(String)
     * @see Engine#getText(WebElement)
     * @see #rxe_activityValueDisplay(Engine, UserMode, ActivityValue)
     */
    @NotNull
    default Flowable<Double> rxe_activityValue(@NotNull final Engine<?> ENGINE,
                                               @NotNull UserMode mode,
                                               @NotNull ActivityValue type) {
        return rxe_activityValueDisplay(ENGINE, mode, type)
            .map(ENGINE::getText)
            .map(a -> a.replaceAll("\\.", ""))
            .map(a -> a.replaceAll(",", ""))
            .map(Double::valueOf);
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
     * Validate the dashboard screen's card list view.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxe_dashboardModeSwitcher(Engine)
     * @see #rxe_logMeal(Engine)
     * @see #rxe_logWeight(Engine)
     */
    @NotNull
    default Flowable<?> rxv_cardListView(@NotNull Engine<?> engine) {
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
