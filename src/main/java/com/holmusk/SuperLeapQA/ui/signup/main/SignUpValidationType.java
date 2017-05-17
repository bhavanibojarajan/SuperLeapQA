package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.ui.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.InputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.AndroidView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by haipham on 5/8/17.
 */
public interface SignUpValidationType extends BaseActionType {
    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxEditFieldForInput(@NotNull InputType input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID(input.androidViewId());
        } else {
            return RxUtil.error();
        }
    }
}
