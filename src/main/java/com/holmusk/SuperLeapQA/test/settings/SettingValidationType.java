package com.holmusk.SuperLeapQA.test.settings;

import com.holmusk.SuperLeapQA.model.Setting;
import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/10/17.
 */
public interface SettingValidationType extends BaseValidationType {
    /**
     * Get the {@link WebElement} that corresponds to {@link Setting} instance.
     * @param engine {@link Engine} instance.
     * @param setting {@link Setting} instance.
     * @return {@link Flowable} instance.
     * @see Engine#platform()
     * @see Engine#rxe_withXPath(XPath...)
     * @see Setting#settingXP(PlatformType)
     */
    @NotNull
    default Flowable<WebElement> rxe_setting(@NotNull Engine<?> engine,
                                             @NotNull Setting setting) {
        PlatformType platform = engine.platform();
        XPath xPath = setting.settingXP(platform);
        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#SETTINGS}.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see Setting#values()
     * @see #rxe_backButton(Engine)
     * @see #rxe_setting(Engine, Setting)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxv_settings(@NotNull final Engine<?> ENGINE) {
        final SettingValidationType THIS = this;

        return Flowable
            .mergeArray(
                Flowable.fromArray(Setting.values())
                    .flatMap(a -> THIS.rxe_setting(ENGINE, a)),

                rxe_backButton(ENGINE)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
