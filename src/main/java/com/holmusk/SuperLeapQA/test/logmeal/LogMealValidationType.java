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
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.javautilities.protocol.ClassNameType;
import org.swiften.xtestkitcomponents.xpath.AttributeType;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
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
     * @see Mood#title()
     */
    @NotNull
    default Flowable<WebElement> rxe_mood(@NotNull Engine<?> engine,
                                          @NotNull Mood mood) {
        return engine
            .rxe_containsText(mood.title())
            .firstElement()
            .toFlowable();
    }

    /**
     * Get the meal time display text view.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see CompoundAttribute.Builder#withClass(ClassNameType)
     * @see CompoundAttribute.Builder#withIndex(Integer)
     * @see Engine#rxe_withXPath(XPath...)
     * @see Engine#rxe_containsID(String...)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see IOSView.Type#UI_STATIC_TEXT
     * @see IOSView.Type#UI_TABLE_VIEW_CELL
     */
    @NotNull
    default Flowable<WebElement> rxe_mealTime(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("tv_logfood_time")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            CompoundAttribute first = CompoundAttribute.builder()
                .withClass(IOSView.Type.UI_TABLE_VIEW_CELL)
                .withIndex(3)
                .build();

            CompoundAttribute second = CompoundAttribute.builder()
                .withClass(IOSView.Type.UI_STATIC_TEXT)
                .withIndex(2)
                .build();

            XPath xpath = XPath.builder()
                .addAttribute(first)
                .addAttribute(second)
                .build();

            return engine.rxe_withXPath(xpath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the location switch.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see AndroidView.Type#SWITCH
     * @see IOSView.Type#UI_SWITCH
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_mealLocSwitch(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_ofClass(AndroidView.Type.SWITCH)
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.Type.UI_SWITCH)
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
     * @see CompoundAttribute.Builder#withClass(ClassNameType)
     * @see CompoundAttribute.Builder#withIndex(Integer)
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_withXPath(XPath...)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see IOSView.Type#UI_BUTTON
     * @see IOSView.Type#UI_TABLE_VIEW_CELL
     * @see Platform#IOS
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_photoPicker(@NotNull Engine<?> engine, int index) {
        if (engine instanceof AndroidEngine) {
            String id = String.format("photo%d", index);
            return engine.rxe_containsID(id).firstElement().toFlowable();
        } else if (engine instanceof IOSEngine) {
            CompoundAttribute first = CompoundAttribute.builder()
                .withClass(IOSView.Type.UI_TABLE_VIEW_CELL)
                .withIndex(1)
                .build();

            CompoundAttribute second = CompoundAttribute.builder()
                .withClass(IOSView.Type.UI_BUTTON)
                .withIndex(1)
                .build();

            XPath xpath = XPath.builder()
                .addAttribute(first)
                .addAttribute(second)
                .build();

            return engine.rxe_withXPath(xpath).firstElement().toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the confirm button for meal time selection.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_mealTime(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_mealTimeConfirm(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            /* If testing on Android, clicking on the meal time label should
             * dismiss the time picker */
            return rxe_mealTime(engine);
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("mealLog_title_save")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#MEAL_ENTRY}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Config#MAX_PHOTO_COUNT
     * @see Mood#values()
     * @see ObjectUtil#nonNull(Object)
     * @see TextInput#MEAL_DESCRIPTION
     * @see #rxe_editField(Engine, HMInputType)
     * @see #rxe_mealCancel(Engine)
     * @see #rxe_mealConfirm(Engine)
     * @see #rxe_mealLocSwitch(Engine)
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
                rxe_mealLocSwitch(ENGINE),
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
