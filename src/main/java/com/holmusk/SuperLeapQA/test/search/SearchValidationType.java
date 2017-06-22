package com.holmusk.SuperLeapQA.test.search;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.ios.IOSView;
import org.swiften.javautilities.protocol.ClassNameType;
import org.swiften.xtestkitcomponents.platform.PlatformProviderType;
import org.swiften.xtestkitcomponents.xpath.AttributeType;
import org.swiften.xtestkitcomponents.xpath.Attributes;
import org.swiften.xtestkitcomponents.xpath.CompoundAttribute;
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
     * @see Engine#rxe_containsID(String...)
     * @see Engine#rxe_ofClass(ClassNameType[])
     * @see IOSView.Type#UI_SEARCH_BAR
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
                .rxe_ofClass(IOSView.Type.UI_SEARCH_BAR)
                .firstElement()
                .toFlowable();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Get all search results.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Attributes#containsID(String)
     * @see Attributes#of(PlatformProviderType)
     * @see CompoundAttribute#forClass(ClassNameType)
     * @see CompoundAttribute.Builder#addAttribute(AttributeType)
     * @see Engine#rxe_withXPath(XPath...)
     * @see XPath.Builder#addAttribute(CompoundAttribute)
     * @see IOSView.Type#UI_TABLE_VIEW
     * @see IOSView.Type#UI_TABLE_VIEW_CELL
     * @see IOSView.Type#UI_STATIC_TEXT
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchResults(@NotNull Engine<?> engine) {
        Attributes attrs = Attributes.of(engine);

        XPath xpath;

        if (engine instanceof AndroidEngine) {
            CompoundAttribute cAttr = CompoundAttribute.builder()
                .addAttribute(attrs.containsID("tv_food_name"))
                .build();

            xpath = XPath.builder().addAttribute(cAttr).build();
        } else if (engine instanceof IOSEngine) {
            xpath = XPath.builder()
                .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_TABLE_VIEW))
                .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_TABLE_VIEW_CELL))
                .addAttribute(CompoundAttribute.forClass(IOSView.Type.UI_STATIC_TEXT))
                .build();
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        return engine.rxe_withXPath(xpath);
    }

    /**
     * Get the result item based on the query {@link String} used to search
     * for it.
     * @param ENGINE {@link Engine} instance.
     * @param QUERY {@link String} value.
     * @return {@link Flowable} instance.
     * @see Engine#getText(WebElement)
     * @see #rxe_searchResults(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_searchResult(@NotNull final Engine<?> ENGINE,
                                                  @NotNull final String QUERY) {
        return rxe_searchResults(ENGINE)
            .filter(a -> ENGINE.getText(a).equals(QUERY))
            .firstElement()
            .toFlowable();
    }
    /**
     * Verify that the search result is empty.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see NumberUtil#isZero(Number)
     * @see #rxe_searchResults(Engine)
     */
    @NotNull
    default Flowable<Boolean> rxv_emptySearchResult(@NotNull Engine<?> engine) {
        return rxe_searchResults(engine).count()
            .map(NumberUtil::isZero)
            .toFlowable()
            .onErrorReturnItem(true);
    }

    /**
     * Check if the search result is empty. Throw an error otherwise.
     * @param E {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxv_errorWithPageSource()
     * @see #rxv_emptySearchResult(Engine)
     */
    @NotNull
    default Flowable<?> rxv_searchResultMustBeEmpty(@NotNull final Engine<?> E) {
        return rxv_emptySearchResult(E)
            .flatMap(a -> {
                if (a) {
                    return Flowable.just(true);
                } else {
                    return E.rxv_errorWithPageSource();
                }
            });
    }
}
