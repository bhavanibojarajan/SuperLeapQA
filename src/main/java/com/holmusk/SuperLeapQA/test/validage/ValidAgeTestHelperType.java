package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.SuperLeapQA.model.ChoiceInput;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.SLNumericChoiceType;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.HMUITestKit.model.HMInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 19/5/17.
 */
public interface ValidAgeTestHelperType extends ValidAgeActionType {
    /**
     * Confirm that 12 {@link Height#INCH} is converted to {@link Height#FT}
     * when we are picking {@link ChoiceInput#HEIGHT}, assuming the user
     * is already in the acceptable age screen.
     * @param ENGINE {@link Engine} instance.
     * @param FT {@link Height#FT} value to be selected.
     * @return {@link Flowable} instance.
     * @see ChoiceInput#HEIGHT
     * @see Engine#platform()
     * @see Height#FT
     * @see Height#INCH
     * @see Height#stringValue(PlatformType, UnitSystem, List)
     * @see UnitSystem#IMPERIAL
     * @see #rxa_selectChoice(Engine, List)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_selectUnitSystemPicker(Engine, HMChoiceType, SLNumericChoiceType)
     * @see #rxv_hasValue(Engine, HMInputType, String)
     */
    @NotNull
    default Flowable<?> rxh_inchToFoot(@NotNull final Engine<?> ENGINE, final int FT) {
        final ValidAgeTestHelperType THIS = this;
        final ChoiceInput C_HEIGHT = ChoiceInput.HEIGHT;
        final Height H_FT = Height.FT;
        PlatformType platform = ENGINE.platform();
        UnitSystem unit = UnitSystem.IMPERIAL;

        final List<Zip<Height,String>> INPUTS = Arrays.asList(
            Zip.of(Height.FT, String.valueOf(FT)),
            Zip.of(Height.INCH, String.valueOf(0))
        );

        final String STR = Height.stringValue(platform, unit, INPUTS);

        return rxa_selectUnitSystemPicker(ENGINE, C_HEIGHT, H_FT)
            .flatMap(a -> THIS.rxa_selectChoice(ENGINE, INPUTS))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(ENGINE))
            .flatMap(a -> THIS.rxv_hasValue(ENGINE, C_HEIGHT, STR));
    }

    /**
     * Recursively check all {@link Height#FT} selectable values and confirm
     * that {@link Height#INCH} to {@link Height#FT} conversion works
     * correctly.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Height#FT
     * @see Height#selectableRange(UserMode)
     * @see #rxh_inchToFoot(Engine, int)
     */
    @NotNull
    default Flowable<?> rxh_inchToFootRecursive(@NotNull final Engine<?> ENGINE,
                                                @NotNull final UserMode MODE) {
        final ValidAgeTestHelperType THIS = this;
        final List<Integer> SELECTABLE = Height.FT.selectableRange(MODE);
        final int LENGTH = SELECTABLE.size();

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    return THIS
                        .rxh_inchToFoot(ENGINE, SELECTABLE.get(INDEX))
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new Repeater().repeat(0);
    }
}
