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
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
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
     * @see #rxa_selectChoice(Engine, List)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_selectUnitSystemPicker(Engine, HMChoiceType, SLNumericChoiceType)
     * @see #rxv_fieldHasValue(Engine, HMInputType, String)
     */
    @NotNull
    default Flowable<?> rxh_inchToFoot(@NotNull final Engine<?> ENGINE, final int FT) {
        final ChoiceInput cHeight = ChoiceInput.HEIGHT;
        final Height hFeet = Height.FT;
        PlatformType platform = ENGINE.platform();
        UnitSystem unit = UnitSystem.IMPERIAL;

        List<Tuple<Height,String>> inputs = Arrays.asList(
            Tuple.of(Height.FT, String.valueOf(FT)),
            Tuple.of(Height.INCH, String.valueOf(0))
        );

        final String hString = Height.stringValue(platform, unit, inputs);

        return Flowable.concatArray(
            rxa_selectUnitSystemPicker(ENGINE, cHeight, hFeet),
            rxa_selectChoice(ENGINE, inputs),
            rxa_confirmNumericChoice(ENGINE),
            rxv_fieldHasValue(ENGINE, cHeight, hString)
        ).all(HPObjects::nonNull).toFlowable();
    }

    /**
     * Recursively check all {@link Height#FT} selectable values and confirm
     * that {@link Height#INCH} to {@link Height#FT} conversion works
     * correctly.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
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
