package com.holmusk.SuperLeapQA.test.settings;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.Setting;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.util.LogUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.javautilities.protocol.ClassNameProviderType;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 6/10/17.
 */
public interface SettingActionType extends BaseActionType, SettingValidationType {
    /**
     * Toggle {@link Setting} by clicking the corresponding
     * {@link org.openqa.selenium.WebElement}.
     * @param ENGINE {@link Engine} instance.
     * @param setting {@link Setting} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_setting(Engine, Setting)
     */
    @NotNull
    default Flowable<?> rxa_toggleSetting(@NotNull final Engine<?> ENGINE,
                                          @NotNull Setting setting) {
        return rxe_setting(ENGINE, setting).flatMap(ENGINE::rxa_click);
    }

    /**
     * Change the current {@link UnitSystem}.
     * @param ENGINE {@link Engine} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#isTrue(Object)
     * @see CompoundAttribute#forClass(ClassNameProviderType)
     * @see CompoundAttribute.Builder#withClass(ClassNameProviderType)
     * @see CompoundAttribute.Builder#withIndex(Integer)
     * @see Engine#switcherOnValue()
     * @see Engine#rxa_toggleSwitch(WebElement, boolean)
     * @see Engine#rxe_ofClass(ClassNameProviderType[])
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see AndroidView.Type#SWITCH
     * @see IOSView.Type#UI_BUTTON
     * @see IOSView.Type#UI_SEGMENTED_CONTROL
     * @see #unitSystemChangeDelay(Engine)
     * @see #unitSystemSettingIndex(Engine, UnitSystem)
     */
    @NotNull
    default Flowable<?> rxa_changeUnitSystem(@NotNull final Engine<?> ENGINE,
                                             @NotNull UnitSystem unit) {
        LogUtil.printft("Changing unit system to %s", unit);
        int index = unitSystemSettingIndex(ENGINE, unit);

        if (ENGINE instanceof AndroidEngine) {
            String value = String.valueOf(BooleanUtil.isTrue(index));
            String onValue = ENGINE.switcherOnValue();
            final boolean ON = value.equals(onValue);

            return ENGINE
                .rxe_ofClass(AndroidView.Type.SWITCH)
                .firstElement()
                .toFlowable()
                .flatMap(a -> ENGINE.rxa_toggleSwitch(a, ON))
                .delay(unitSystemChangeDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else if (ENGINE instanceof IOSEngine) {
            XPath xpath = XPath.builder()
                .addAttribute(CompoundAttribute
                    .forClass(IOSView.Type.UI_SEGMENTED_CONTROL))
                .addAttribute(CompoundAttribute.builder()
                    .withClass(IOSView.Type.UI_BUTTON)
                    .withIndex(index + 1)
                    .build())
                .build();

            return ENGINE
                .rxe_withXPath(xpath)
                .firstElement()
                .toFlowable()
                .flatMap(ENGINE::rxa_click)
                .delay(unitSystemChangeDelay(ENGINE), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
