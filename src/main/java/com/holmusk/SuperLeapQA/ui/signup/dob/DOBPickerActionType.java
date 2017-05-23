package com.holmusk.SuperLeapQA.ui.signup.dob;

import com.holmusk.SuperLeapQA.ui.signup.main.SignUpActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.type.DelayType;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerActionType extends DOBPickerValidationType, SignUpActionType {
    /**
     * Open the DoB dialog in the sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * Due to design differences, only
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID} requires the
     * DoB picker to be opened. On
     * {@link org.swiften.xtestkit.mobile.Platform#IOS}, the picker is visible
     * immediately upon entering the screen.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_DoBEditField(Engine)
     * @see Engine#rx_click(WebElement)
     * @see Engine#rx_implicitlyWait(DelayType)
     */
    @NotNull
    default Flowable<?> rx_a_openDoBPicker(@NotNull Engine<?> ENGINE) {
        return rx_e_DoBEditField(ENGINE)
            .flatMap(ENGINE::rx_click)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmDoB(@NotNull final Engine<?> ENGINE) {
        return ENGINE
            .rx_containsText("ok", "register_title_submit")
            .firstElement()
            .toFlowable()
            .flatMap(ENGINE::rx_click);
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param engine {@link Engine} instance.
     * @param DATE {@link Date} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_selectDate(DateType)
     */
    @NotNull
    default Flowable<?> rx_a_selectDoB(@NotNull Engine<?> engine,
                                       @NotNull final Date DATE) {
        return engine.rx_selectDate(() -> DATE);
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param engine {@link Engine} instance.
     * @param age {@link Integer} value.
     * @return {@link Flowable} instance.
     * @see #rx_a_selectDoB(Engine, Date)
     */
    @NotNull
    default Flowable<?> rx_a_selectDoBToBeOfAge(@NotNull Engine<?> engine, int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return rx_a_selectDoB(engine, calendar.getTime());
    }
}
