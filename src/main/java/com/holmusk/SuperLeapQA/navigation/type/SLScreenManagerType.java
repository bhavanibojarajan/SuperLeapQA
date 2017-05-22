package com.holmusk.SuperLeapQA.navigation.type;

/**
 * Created by haipham on 5/21/17.
 */

import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.ScreenHolder;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.navigation.ScreenManagerType;
import org.swiften.xtestkit.navigation.ScreenType;

import java.util.Arrays;
import java.util.List;

/**
 * {@link ScreenManagerType} for Superleap.
 */
public interface SLScreenManagerType extends ScreenManagerType {
    /**
     * Convenience to register all available {@link ScreenHolder}.
     * @see ScreenHolder#of(Screen, UserMode)
     */
    default void registerScreenHolders() {
        UserMode[] modes = UserMode.values();
        Screen[] screens = Screen.values();

        ScreenHolder[] holders = Arrays.stream(modes)
            .flatMap(a -> Arrays.stream(screens).map(b -> ScreenHolder.of(b, a)))
            .toArray(ScreenHolder[]::new);

        register(holders);
    }

    /**
     * Convenience method to get {@link ScreenHolder} from {@link Screen}.
     * @param MODE {@link UserMode} instance.
     * @param screens Varargs of {@link Screen}.
     * @return An Array of {@link ScreenHolder}.
     * @see ScreenHolder#of(Screen, UserMode)
     */
    @NotNull
    default ScreenHolder[] holders(@NotNull final UserMode MODE,
                                   @NotNull Screen...screens) {
        return Arrays.stream(screens)
            .map(a -> ScreenHolder.of(a, MODE))
            .toArray(ScreenHolder[]::new);
    }

    /**
     * Convenience method to navigate from one {@link Screen} to another.
     * @param mode {@link UserMode} instance.
     * @param screens Varargs of {@link Screen}.
     * @see #holders(UserMode, Screen...)
     * @see #multipleShortest(ScreenType...)
     */
    default List<Node> nodes(@NotNull UserMode mode, @NotNull Screen...screens) {
        return multipleShortest(holders(mode, screens));
    }

    /**
     * Convenience method to navigate between {@link Screen}.
     * @param mode {@link UserMode} instance.
     * @param screens Varargs of {@link Screen}.
     * @return {@link Flowable} instance.
     * @see #holders(UserMode, Screen...)
     * @see #rx_navigate(Object, ScreenType...)
     */
    @NotNull
    default Flowable<?> rx_navigate(@NotNull UserMode mode,
                                    @NotNull Screen...screens) {
        return rx_navigate(true, holders(mode, screens));
    }
}
