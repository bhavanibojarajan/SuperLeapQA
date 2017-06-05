package com.holmusk.SuperLeapQA.test.logmeal;

import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.config.Config;
import com.holmusk.SuperLeapQA.model.Mood;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 29/5/17.
 */
public interface LogMealValidationType extends BaseValidationType {
    /**
     * Get the meal log cancel button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_mealCancel(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("btnClose")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("mealLog_title_cancel")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the meal log confirm button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealConfirm(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealLog_title_submit")
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the {@link WebElement} that corresponds to a particular {@link Mood}.
     * @param engine {@link Engine} instance.
     * @param mood {@link Mood} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see Mood#moodTitle()
     */
    @NotNull
    default Flowable<WebElement> rxe_mood(@NotNull Engine<?> engine,
                                          @NotNull Mood mood) {
        return engine
            .rxe_containsText(mood.moodTitle())
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the meal time display text view.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_withXPath(XPath...)
     * @see Engine#rxe_containsID(String...)
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see Platform#IOS
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#setIndex(XPath.AtIndex)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealTime(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_logfood_time")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            Platform platform = Platform.IOS;

            XPath child = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_STATIC_TEXT.className())
                .setIndex(2)
                .build();

            XPath parent = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_TABLE_VIEW_CELL.className())
                .setIndex(3)
                .addChildXPath(child)
                .build();

            return engine.rxe_withXPath(parent).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the location switch.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see AndroidView.ViewType#SWITCH
     * @see BaseViewType#className()
     * @see Engine#rxe_ofClass(String...)
     * @see IOSView.ViewType#UI_SWITCH
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_locationSwitch(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.ViewType.SWITCH.className())
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_SWITCH.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the button to open the photo picker.
     * @param engine {@link Engine} instance.
     * @param index {@link Integer} value representing the current pick index.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_BUTTON
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see Platform#IOS
     * @see XPath.Builder#addClass(String)
     * @see XPath.Builder#setIndex(int)
     */
    @NotNull
    default Flowable<WebElement> rxe_photoPicker(@NotNull Engine<?> engine, int index) {
        if (engine instanceof AndroidEngine) {
            String id = String.format("photo%d", index);
            return engine.rxe_containsID(id).firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            Platform platform = Platform.IOS;

            XPath child = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_BUTTON.className())
                .setIndex(1)
                .build();

            XPath parent = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_TABLE_VIEW_CELL.className())
                .setIndex(1)
                .addChildXPath(child)
                .build();

            return engine.rxe_withXPath(parent).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the confirm button for meal time selection.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     */
    @NotNull
    default Flowable<WebElement> rxe_mealTimeConfirm(@NotNull Engine<?> engine) {
        return engine
            .rxe_containsText("mealLog_title_save")
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Config#MAX_PHOTO_COUNT
     * @see Mood#values()
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#MEAL_DESCRIPTION
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_mealCancel(Engine)
     * @see #rxe_mealConfirm(Engine)
     * @see #rxe_locationSwitch(Engine)
     * @see #rxe_photoPicker(Engine, int)
     * @see #rxe_mood(Engine, Mood)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_mealLog(@NotNull final Engine<?> ENGINE) {
        final LogMealValidationType THIS = this;

        return Flowable
            .mergeArray(
                rxe_mealCancel(ENGINE),
                rxe_mealConfirm(ENGINE),
                rxe_locationSwitch(ENGINE),
                rxe_editField(ENGINE, TextInput.MEAL_DESCRIPTION),

                Flowable.fromArray(Mood.values())
                    .flatMap(a -> THIS.rxe_mood(ENGINE, a)),

                Flowable.range(0, Config.MAX_PHOTO_COUNT)
                    .map(a -> a + 1)
                    .flatMap(a -> rxe_photoPicker(ENGINE, a))
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}