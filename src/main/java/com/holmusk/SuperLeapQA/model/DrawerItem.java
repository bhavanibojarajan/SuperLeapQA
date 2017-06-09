package com.holmusk.SuperLeapQA.model;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.AndroidView;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.common.BaseErrorType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.property.base.AttributeType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.Attribute;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 6/10/17.
 */
public enum DrawerItem implements AttributeType<String>, BaseErrorType {
    SETTINGS,
    DIETICIAN_PROFILES,
    ABOUT,
    FEEDBACK,
    SIGN_OUT;

    /**
     * Override this method to provide default implementation.
     * @return {@link String} value.
     * @see AttributeType#value()
     * @see #ABOUT
     * @see #DIETICIAN_PROFILES
     * @see #FEEDBACK
     * @see #SETTINGS
     * @see #SIGN_OUT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public String value() {
        switch (this) {
            case SETTINGS:
                return "drawer_title_settings";

            case DIETICIAN_PROFILES:
                return "drawer_title_dieticianProfile";

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
     * @param platform {@link PlatformType} instance.
     * @return {@link XPath} instance.
     * @see Attributes#containsText(String)
     * @see Attributes#of(PlatformType)
     * @see Attributes#ofClass(String)
     * @see BaseViewType#className()
     * @see CompoundAttribute.Builder#addAttribute(Attribute)
     * @see AndroidView.ViewType#CHECKED_TEXT_VIEW
     * @see Platform#ANDROID
     * @see #NOT_AVAILABLE
     */
    @NotNull
    public XPath drawerItemXP(@NotNull PlatformType platform) {
        String clsName;

        if (platform.equals(Platform.ANDROID)) {
            clsName = AndroidView.ViewType.CHECKED_TEXT_VIEW.className();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        Attributes attrs = Attributes.of(platform);

        CompoundAttribute cAttr = CompoundAttribute.builder()
            .addAttribute(attrs.ofClass(clsName))
            .addAttribute(attrs.containsText(value()))
            .build();

        return XPath.builder().addAttribute(cAttr).build();
    }
}
