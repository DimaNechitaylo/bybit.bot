package com.nechytailo.bybit.bot.bot.routes;

import com.nechytailo.bybit.bot.bot.constants.MessageRoutes;
import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.bot.service.BybitService;
import com.nechytailo.bybit.bot.exception.GeneralServiceException;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.exception.NoTradeEventException;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.stream.Collectors;

@Component
@Message
public class MainMenuRoute implements Route {

    private static final Logger LOG = LoggerFactory.getLogger(MainMenuRoute.class);

    @Autowired
    private BybitService bybitService;

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper) {
        ReplyKeyboardMarkup inlineKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(KeyboardButton.builder().text(MessageRoutes.ADD_ACCOUNT).build(),
                        KeyboardButton.builder().text(MessageRoutes.ADD_EVENT).build(),
                        KeyboardButton.builder().text(MessageRoutes.GET_USER_ACCOUNTS).build(),
                        KeyboardButton.builder().text(MessageRoutes.GET_USER_EVENTS).build()))
                        .build();
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .replyMarkup(inlineKeyboardMarkup)
                .text("ViewInitializer MainMenuRoute")
                .build();
        return ResponseList.builder()
                .response(message)
                .build();
    }

    @MessageMapping(MessageRoutes.ADD_ACCOUNT)
    public ResponseEntity handleTest1(@NotNull UpdateWrapper wrapper) {
        LOG.debug("MainMenu handleTest1");
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("fill ACCOUNT data")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(AddAccountRoute.class)
                .build();
    }

    @MessageMapping(MessageRoutes.ADD_EVENT)
    public ResponseEntity handleTest2(@NotNull UpdateWrapper wrapper) {
        LOG.debug("MainMenu handleTest2");
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("fill event data")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(AddEventRoute.class)
                .build();
    }

    @MessageMapping(MessageRoutes.GET_USER_EVENTS)
    public ResponseEntity handleGetUserEvents(@NotNull UpdateWrapper wrapper) {
        LOG.debug("MainMenu handleGetUserEvents");
        Long userId = wrapper.getUserId();
        Long chatId = wrapper.getChatId();
        String userEventsStr = null;
        SendMessage message;
        try {
            userEventsStr = bybitService.getUserEvents(userId).stream()
                    .map(TradeEventResponseDto::toString)
                    .collect(Collectors.joining("\n"));
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Your events:\n\n" + userEventsStr)
                    .build();
        } catch (GeneralServiceException e) {
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Error: " + e.getErrorType().getErrorCode() + " " + e.getMessage())
                    .build();
        } catch (Exception ex) { //TODO
            ex.printStackTrace();
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Error "+ex.getMessage())
                    .build();
        }



        return ResponseEntity.builder()
                .response(message)
                .build();
    }

    @MessageMapping(MessageRoutes.GET_USER_ACCOUNTS)
    public ResponseEntity handleGetUserAccounts(@NotNull UpdateWrapper wrapper) {
        LOG.debug("MainMenu GET_USER_ACCOUNTS");
        Long userId = wrapper.getUserId();
        Long chatId = wrapper.getChatId();
        String userAccountsStr = null;
        SendMessage message;
        try {
            userAccountsStr = bybitService.getUserAccounts(userId).stream()
                    .map(AccountDto::toString)
                    .collect(Collectors.joining("\n"));

            message = SendMessage.builder()
                    .chatId(wrapper.getChatId())
                    .text("Your accounts:\n\n" + userAccountsStr)
                    .build();
        } catch (GeneralServiceException e) {
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Error: " + e.getErrorType().getErrorCode() + " " + e.getMessage())
                    .build();
        } catch (Exception ex) { //TODO
            ex.printStackTrace();
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Error "+ex.getMessage())
                    .build();
        }

        return ResponseEntity.builder()
                .response(message)
                .build();
    }

    @MessageMapping
    public ResponseEntity handleAnotherMessage(@NotNull UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        LOG.debug("MainMenu handleAnotherMessage");
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text(labelsWrapper.getLabel("main.menu"))
                .build();

        return ResponseEntity.builder()
                .response(message)
                .build();
    }
}
