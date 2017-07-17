package com.holmusk.SuperLeapQA.test.search;

import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.xtestkit.base.Engine;

import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 1/6/17.
 */
public interface SearchActionType extends BaseActionType, SearchValidationType {
    /**
     * Cancel search.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_searchCancel(Engine)
     */
    @NotNull
    default Flowable<?> rxa_cancelSearch(@NotNull Engine<?> engine) {
        return rxe_searchCancel(engine).compose(engine.clickFn());
    }

    /**
     * Search a query.
     * @param engine {@link Engine} instance.
     * @param query {@link String} value.
     * @return {@link Flowable} instance.
     * @see #searchProgressDelay(Engine)
     * @see #rxe_searchBar(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_search(@NotNull Engine<?> engine, @NotNull String query) {
        return Flowable.concatArray(
            rxe_searchBar(engine).compose(engine.clickFn()),

            rxe_searchBar(engine)
                .compose(engine.sendValueFn(query))
                .delay(searchProgressDelay(engine), TimeUnit.MILLISECONDS)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Locate a search result with a {@link String} query, then click on it
     * to open the search result page, then delay for a while for the page to
     * load completely.
     * @param engine {@link Engine} instance.
     * @param query {@link String} value.
     * @return {@link Flowable} instance.
     * @see #generalDelay(Engine)
     * @see #rxe_searchResult(Engine, String)
     */
    @NotNull
    default Flowable<?> rxa_openSearchResult(@NotNull Engine<?> engine,
                                             @NotNull String query) {
        return rxe_searchResult(engine, query)
            .compose(engine.clickFn())
            .delay(generalDelay(engine), TimeUnit.MILLISECONDS);
    }
}
