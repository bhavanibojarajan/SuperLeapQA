package com.holmusk.SuperLeapQA.test.search;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.view.BaseViewType;
import org.swiften.xtestkitcomponents.xpath.*;

/**
 * Created by haipham on 1/6/17.
 */
public interface SearchValidationType extends BaseValidationType {
    /**
     * Get the search cancel button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #rxe_backButton(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchCancel(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return rxe_backButton(engine);
        } if (engine instanceof IOSEngine) {
            return engine
                .rxe_containsText("search_title_cancel")
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the search bar.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see BaseViewType#className()
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_ofClass(String...)
     * @see IOSView.ViewType#UI_SEARCH_BAR
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchBar(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine
                .rxe_containsID("search_src_text")
                .firstElement()
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return engine
                .rxe_ofClass(IOSView.ViewType.UI_SEARCH_BAR.className())
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get the result item based on the query {@link String} used to search
     * for it.
     * @param engine {@link Engine} instance.
     * @param query {@link String} value.
     * @return {@link Flowable} instance.
     * @see Attributes#of(PlatformProviderType)
     * @see Attributes#containsID(String)
     * @see Attributes#containsText(String)
     * @see BaseViewType#className()
     * @see CompoundAttribute#forClass(String)
     * @see CompoundAttribute#single(AttributeType)
     * @see Engine#rxe_withXPath(XPath...)
     * @see XPath.Builder#addAttribute(AttributeType)
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see IOSView.ViewType#UI_TABLE_VIEW
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchResult(@NotNull Engine<?> engine,
                                                  @NotNull String query) {
        Attributes attrs = Attributes.of(engine);

        XPath xPath;

        if (engine instanceof AndroidEngine) {
            CompoundAttribute cAttr = CompoundAttribute.builder()
                .addAttribute(attrs.containsID("tv_food_name"))
                .addAttribute(attrs.containsText(query))
                .build();

            xPath = XPath.builder().addAttribute(cAttr).build();
        } else if (engine instanceof IOSEngine) {
            String tblView = IOSView.ViewType.UI_TABLE_VIEW.className();
            String tblCell = IOSView.ViewType.UI_TABLE_VIEW_CELL.className();

            Attribute attr = attrs.containsText(query);

            CompoundAttribute cAttr = CompoundAttribute.single(attr)
                .withClass(IOSView.ViewType.UI_STATIC_TEXT.className());

            xPath = XPath.builder()
                .addAttribute(CompoundAttribute.forClass(tblView))
                .addAttribute(CompoundAttribute.forClass(tblCell))
                .addAttribute(cAttr)
                .build();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }
}
