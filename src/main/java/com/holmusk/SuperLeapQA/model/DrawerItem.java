package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.localizer.LocalizerType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/10/17.
 */
public enum DrawerItem implements BaseErrorType {
    SETTINGS,
    DIETITIAN_PROFILES,
    ABOUT,
    FEEDBACK,
    SIGN_OUT;

    /**
     * Get {@link String} value as displayed by the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link DrawerItem}.
     * @return {@link String} value.
     * @see #ABOUT
     * @see #DIETITIAN_PROFILES
     * @see #FEEDBACK
     * @see #SETTINGS
     * @see #SIGN_OUT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String title() {
        switch (this) {
            case SETTINGS:
                return "drawer_title_settings";

            case DIETITIAN_PROFILES:
                return "drawer_title_dietitianProfile";

            case ABOUT:
                return "drawer_title_about";

            case FEEDBACK:
                return "drawer_title_feedback";

            case SIGN_OUT:
                return "drawer_title_signOut";

            default:
                throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the {@link XPath} query that can be used to locate the
     * {@link org.openqa.selenium.WebElement} corresponding to the current
     * {@link DrawerItem}.
     * @param engine {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see Attributes#ofClass(String)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(Attribute)
     * @see Engine#localizer()
     * @see LocalizerType#localize(String)
     * @see AndroidView.ViewType#CHECKED_TEXT_VIEW
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see Platform#ANDROID
     * @see #title()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath drawerItemXP(@NotNull Engine<?> engine) {
        LocalizerType localizer = engine.localizer();
        String clsName;

        if (engine instanceof AndroidEngine) {
            clsName = AndroidView.ViewType.CHECKED_TEXT_VIEW.className();
        } else if (engine instanceof IOSEngine) {
            clsName = IOSView.ViewType.UI_STATIC_TEXT.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        PlatformType platform = engine.platform();
        Attributes attrs = Attributes.of(platform);
        String localized = localizer.localize(title());

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .addAttribute(attrs.ofClass(clsName))
            .addAttribute(attrs.containsText(localized))
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }
}
