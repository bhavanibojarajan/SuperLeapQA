package org.swiften.javautilities.string;

import org.swiften.javautilities.object.ObjectUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by haipham on 4/6/17.
 */
public final class StringUtil {
    /**
     * Check if {@link String} is not null and not empty.
     * @param text {@link String} value.
     * @return {@link Boolean} value.
     */
    public static boolean isNotNullOrEmpty(@Nullable String text) {
        return ObjectUtil.nonNull(text) && !text.isEmpty();
    }

    /**
     * Check if {@link String} is either null or empty.
     * @param text {@link String} value.
     * @return {@link Boolean} value.
     */
    public static boolean isNullOrEmpty(@Nullable String text) {
        return ObjectUtil.isNull(text) || text.isEmpty();
    }

    private StringUtil() {}
}
