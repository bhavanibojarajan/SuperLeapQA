package com.holmusk.SuperLeapQA.test.settings;

import com.holmusk.SuperLeapQA.model.Setting;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;

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
}
