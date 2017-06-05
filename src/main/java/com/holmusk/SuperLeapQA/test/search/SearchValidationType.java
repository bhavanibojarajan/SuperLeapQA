package com.holmusk.SuperLeapQA.test.search;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.BaseViewType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.swiften.xtestkitcomponents.xpath.XPath;

/**
 * Created by haipham on 1/6/17.
 */
public interface SearchValidationType extends BaseValidationType {
    /**
     * Get the search cancel button.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchCancel(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
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
     * @see Engine#rxe_ofClass(String...)
     * @see IOSView.ViewType#UI_SEARCH_BAR
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchBar(@NotNull Engine<?> engine) {
        if (engine instanceof IOSEngine) {
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
     * @see BaseViewType#className()
     * @see Engine#platform()
     * @see Engine#rxe_withXPath(XPath...)
     * @see IOSView.ViewType#UI_STATIC_TEXT
     * @see IOSView.ViewType#UI_TABLE_VIEW
     * @see IOSView.ViewType#UI_TABLE_VIEW_CELL
     * @see XPath.Builder#containsText(String)
     * @see XPath.Builder#addClass(String)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchResult(@NotNull Engine<?> engine,
                                                  @NotNull String query) {
        PlatformType platform = engine.platform();
        XPath xPath;

        if (engine instanceof IOSEngine) {
            XPath child = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_STATIC_TEXT.className())
                .containsText(query)
                .build();

            XPath parent = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_TABLE_VIEW_CELL.className())
                .addChildXPath(child)
                .build();

            xPath = XPath.builder(platform)
                .addClass(IOSView.ViewType.UI_TABLE_VIEW.className())
                .addChildXPath(parent)
                .build();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        return engine.rxe_withXPath(xPath).firstElement().toFlowable();
    }
}