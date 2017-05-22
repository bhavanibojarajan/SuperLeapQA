package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerValidationType extends SignUpActionType {
    /**
     * Validate the acceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    Flowable<?> rx_v_acceptableAgeScreen(@NotNull Engine<?> engine);

    /**
     * Validate the unacceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     */
    @NotNull
    Flowable<?> rx_v_unacceptableAgeScreen(@NotNull Engine<?> engine,
                                           @NotNull UserMode mode);

    /**
     * Get all calendar {@link WebElement} instances.
     ** @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_allCalendarElements()
     */
    @NotNull
    default Flowable<WebElement> rx_e_DoBElements(@NotNull Engine<?> engine) {
        return engine.rx_allCalendarElements();
    }

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
    default Flowable<WebElement> rx_e_DoBEditField(@NotNull Engine<?> engine) {
        if (engine instanceof AndroidEngine) {
            return engine.rx_editable().firstElement().toFlowable();
        } else {
            return RxUtil.error(NOT_AVAILABLE);
        }
    }

    /**
     * Validate that the DoB editable field displays {@link String}
     * representation of {@link Date}, assuming the user is in the
     * pre-DoB picker screen.
     * @param date {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see SimpleDateFormat#format(Date)
     */
    @NotNull
    default Flowable<?> rx_v_DoBEditFieldHasDate(@NotNull Engine<?> engine,
                                                 @NotNull Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String string = formatter.format(date);
        return engine.rx_containsText(string).firstElement().toFlowable();
    }
}
