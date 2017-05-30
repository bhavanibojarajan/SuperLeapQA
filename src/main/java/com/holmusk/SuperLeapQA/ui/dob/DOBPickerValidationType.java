package com.holmusk.SuperLeapQA.ui.dob;

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.ui.welcome.WelcomeValidationType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerValidationType extends WelcomeValidationType {
    /**
     * Validate the acceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    Flowable<?> rxv_validAgeScreen(@NotNull Engine<?> engine);

    /**
     * Validate the unacceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    Flowable<?> rxv_invalidAgeScreen(@NotNull Engine<?> engine, @NotNull UserMode mode);

    /**
     * Get the DoB's editable text field. Due to design differences, this
     * {@link WebElement} only appears in
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_editable()
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<WebElement> rxe_DoBEditField(@NotNull Engine<?> engine) {
        return engine.rx_editable().firstElement().toFlowable();
    }

    /**
     * Validate that the DoB editable field displays {@link String}
     * representation of {@link Date}, assuming the user is in the
     * pre-DoB picker screen.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxe_containsText(String...)
     * @see SimpleDateFormat#format(Date)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxv_DoBEditFieldHasDate(@NotNull Engine<?> engine,
                                                @NotNull Date date) {
        String format;

        if (engine instanceof AndroidEngine) {
            format = "dd MMMM YYYY";
        } else if (engine instanceof IOSEngine) {
            format = "MMM d, YYYY";
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String string = formatter.format(date);
        return engine.rxe_containsText(string).firstElement().toFlowable();
    }
}
