package com.holmusk.SuperLeapQA.test.search;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 1/6/17.
 */
public interface SearchActionType extends BaseActionType, SearchValidationType {
    /**
     * Cancel search.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_searchCancel(Engine)
     */
    @NotNull
    default Flowable<?> rxa_cancelSearch(@NotNull final Engine<?> ENGINE) {
        return rxe_searchCancel(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Search a query.
     * @param ENGINE {@link Engine} instance.
     * @param QUERY {@link String} value.
     * @return {@link Flowable} instance.
     * @see #searchProgressDelay(Engine)
     * @see #rxe_searchBar(Engine)
     */
    @NotNull
    default Flowable<?> rxa_search(@NotNull final Engine<?> ENGINE,
                                   @NotNull final String QUERY) {
        return rxe_searchBar(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .flatMap(a -> ENGINE.rxa_sendValue(a, QUERY))
            .delay(searchProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Locate a search result with a {@link String} query, then click on it
     * to open the search result page, then delay for a while for the page to
     * load completely.
     * @param ENGINE {@link Engine} instance.
     * @param query {@link String} value.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_searchResult(Engine, String)
     */
    @NotNull
    default Flowable<?> rxa_openSearchResult(@NotNull final Engine<?> ENGINE,
                                             @NotNull String query) {
        return rxe_searchResult(ENGINE, query)
            .flatMap(ENGINE::rxa_click)
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
