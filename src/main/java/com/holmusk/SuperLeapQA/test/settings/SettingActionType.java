package com.holmusk.SuperLeapQA.test.settings;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.Setting;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
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
     * @param engine {@link Engine} instance.
     * @param setting {@link Setting} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_setting(Engine, Setting)
     */
    @NotNull
    default Flowable<?> rxa_toggleSetting(@NotNull Engine<?> engine,
                                          @NotNull Setting setting) {
        return rxe_setting(engine, setting).compose(engine.clickFn());
    }

    /**
     * Change the current {@link UnitSystem}.
     * @param engine {@link Engine} instance.
     * @param unit {@link UnitSystem} instance.
     * @return {@link Flowable} instance.
     * @see #unitSystemChangeDelay(Engine)
     * @see #unitSystemSettingIndex(Engine, UnitSystem)
     */
    @NotNull
    default Flowable<?> rxa_changeUnitSystem(@NotNull final Engine<?> engine,
                                             @NotNull UnitSystem unit) {
        HPLog.printft("Changing unit system to %s", unit);
        int index = unitSystemSettingIndex(engine, unit);

        if (engine instanceof AndroidEngine) {
            String value = String.valueOf(HPBooleans.isTrue(index));
            String onValue = engine.switcherOnValue();
            final boolean ON = value.equals(onValue);

            return engine
                .rxe_ofClass(AndroidView.Type.SWITCH)
                .firstElement()
                .toFlowable()
                .compose(engine.toggleSwitchFn(ON))
                .delay(unitSystemChangeDelay(engine), TimeUnit.MILLISECONDS);
        } else if (engine instanceof IOSEngine) {
            XPath xpath = XPath.builder()
                .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_SEGMENTED_CONTROL))
                .addAttribute(CompoundAttribute.builder()
                    .withClass(IOSView.Type.UI_BUTTON)
                    .withIndex(index + 1)
                    .build())
                .build();

            return engine
                .rxe_withXPath(xpath)
                .firstElement()
                .toFlowable()
                .compose(engine.clickFn())
                .delay(unitSystemChangeDelay(engine), TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }
}
