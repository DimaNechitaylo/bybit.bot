package com.nechytailo.bybit.bot.bot.routes;

import com.nechytailo.bybit.bot.bot.constants.MessageRoutes;
import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.exception.InvalidEventArgumentException;
import com.nechytailo.bybit.bot.bot.service.impl.BybitServiceImpl;
import com.nechytailo.bybit.bot.bot.validation.EventValidator;
import com.nechytailo.bybit.bot.exception.GeneralServiceException;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@Message
public class AddEventRoute implements Route {

    private static final Logger LOG = LoggerFactory.getLogger(AddEventRoute.class);

    @Autowired
    private EventValidator eventValidator;

    @Autowired
    private BybitServiceImpl bybitService;

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper) {
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(KeyboardButton.builder().text(MessageRoutes.MAIN_MENU).build()))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .replyMarkup(replyKeyboardMarkup)
                .text("Send event data in the required format:\n Description, symbol, side, qty, yyyy-MM-dd HH:mm:ss.\n Example:\n Description of my event, BTCUSDT, BUY, 50, 2024-10-08 21:50:00")
                .build();
        return ResponseList.builder()
                .response(message)
                .build();
    }

    @MessageMapping
    public ResponseEntity handleAddEvent(@NotNull UpdateWrapper wrapper) {
        String messageText = wrapper.getUpdate().getMessage().getText();
        Long userId = wrapper.getUserId();
        Long chatId = wrapper.getChatId();
        LOG.debug("AddEventRoute route message: {}", messageText);
        SendMessage message;
        try {
            TradeEventRequestDto tradeEventRequestDto = eventValidator.processTradeEvent(messageText);
            bybitService.addEvent(tradeEventRequestDto, userId);
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Event " + messageText + " added")
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

    @MessageMapping(MessageRoutes.MAIN_MENU)
    public ResponseEntity df(@NotNull UpdateWrapper wrapper) {
        LOG.debug("AddEventRoute route");
        LOG.debug("++++++++++++++++++++++++++ save new Event");

        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("Main menu from add event")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(MainMenuRoute.class)
                .build();
    }
}