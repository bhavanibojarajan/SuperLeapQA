package com.holmusk.SuperLeapQA.test.weightpage;

import com.holmusk.SuperLeapQA.test.base.BaseValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haipham on 6/11/17.
 */
public interface WeightPageValidationType extends BaseValidationType {
    /**
     * Check whether a weight log has a particular {@link Date}.
     * @param engine {@link Engine} instance.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rxe_containsText(String...)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<Boolean> rxv_weightHasDate(@NotNull Engine<?> engine,
                                                @NotNull Date date) {
        String format;

        if (engine instanceof IOSEngine) {
            format = "m/dd/YY h:mm a";
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);

        return engine
            .rxe_containsText(dateString)
            .firstElement()
            .toFlowable()
            .map(BooleanUtil::toTrue)
            .onErrorReturnItem(false);
    }
}
