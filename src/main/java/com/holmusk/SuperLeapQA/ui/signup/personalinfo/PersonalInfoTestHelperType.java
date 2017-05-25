package com.holmusk.SuperLeapQA.ui.signup.personalinfo;

import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextInputType;
import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.model.TextInputType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 19/5/17.
 */
public interface PersonalInfoTestHelperType extends PersonalInfoActionType {
    /**
     * Toggle and check that the password mask works. This is only applicable
     * to {@link org.swiften.xtestkit.mobile.Platform#ANDROID} since on
     * {@link org.swiften.xtestkit.mobile.Platform#IOS} there is no way to
     * reveal the password.
     * @param ENGINE {@link Engine} instance.
     * @param element {@link WebElement} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_togglePasswordMask(WebElement)
     * @see Engine#isShowingPassword(WebElement)
     */
    @NotNull
    default Flowable<?> rx_h_checkPasswordMask(@NotNull final Engine<?> ENGINE,
                                               @NotNull WebElement element) {
        if (element instanceof AndroidEngine) {
            return ENGINE
                .rx_togglePasswordMask(element)
                .filter(ENGINE::isShowingPassword);
        } else {
            return Flowable.just(true);
        }
    }
}
