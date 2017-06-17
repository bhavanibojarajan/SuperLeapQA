package com.holmusk.SuperLeapQA.test.chat;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;

/**
 * Created by haipham on 1/6/17.
 */
public interface ChatActionType extends BaseActionType, ChatValidationType {
    /**
     * Open the chat window.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see TextInput#MEAL_COMMENT
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<?> rxa_openChatWindow(@NotNull final Engine<?> ENGINE) {
        HMTextType input = TextInput.MEAL_COMMENT;
        return rxe_editField(ENGINE, input).flatMap(ENGINE::rxa_click);
    }

    /**
     * Dismiss the chat window.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_chatListView(Engine)
     */
    @NotNull
    default Flowable<?> rxa_dismissChatWindow(@NotNull final Engine<?> ENGINE) {
        return rxe_chatListView(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Confirm a chat message.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_chatSend(Engine)
     */
    @NotNull
    default Flowable<?> rxa_sendChat(@NotNull final Engine<?> ENGINE) {
        return rxe_chatSend(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Type and send a chat message.
     * @param engine {@link Engine} instance.
     * @param msg {@link String} chat message.
     * @return {@link Flowable} instance.
     * @see TextInput#MEAL_COMMENT
     * @see #rxa_sendChat(Engine)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @NotNull
    default Flowable<?> rxa_postChat(@NotNull Engine<?> engine,
                                     @NotNull String msg) {
        HMTextType input = TextInput.MEAL_COMMENT;

        return Flowable
            .concatArray(rxa_input(engine, input, msg), rxa_sendChat(engine))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Type and send a random chat message.
     * @param engine {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see TextInput#MEAL_COMMENT
     * @see TextInput#randomInput(InputHelperType)
     * @see #rxa_postChat(Engine, String)
     */
    @NotNull
    default Flowable<?> rxa_postRandomChat(@NotNull Engine<?> engine) {
        String message = TextInput.MEAL_COMMENT.randomInput(engine);
        return rxa_postChat(engine, message);
    }
}
