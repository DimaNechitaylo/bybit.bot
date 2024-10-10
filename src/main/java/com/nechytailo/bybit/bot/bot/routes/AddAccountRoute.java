package com.nechytailo.bybit.bot.bot.routes;

import com.nechytailo.bybit.bot.bot.constants.MessageRoutes;
import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.service.impl.BybitServiceImpl;
import com.nechytailo.bybit.bot.bot.validation.AccountValidator;
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
public class AddAccountRoute implements Route {

    private static final Logger LOG = LoggerFactory.getLogger(AddAccountRoute.class);

    @Autowired
    private AccountValidator accountValidator;

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
                .text("Send event data in the required format:\n API key, API secret, proxy host, proxy port, proxy login, proxy password.\n Example:\n VlaWGp7YMTGc3VdQQg, xCKd81GVT8Z5Du2FZMcPhYaQJSKdHIo3gpwX, 181.3.213.31, 8080, login, pass")
                .build();
        return ResponseList.builder()
                .response(message)
                .build();
    }

    @MessageMapping
    public ResponseEntity handleGreeting(@NotNull UpdateWrapper wrapper) {
        String messageText = wrapper.getUpdate().getMessage().getText();
        LOG.debug("AddAccountRoute route message: {}", messageText);
        LOG.debug("++++++++++++++++++++++++++ save new account");
        SendMessage message;
        try {
            AccountDto accountDto = accountValidator.processAccount(messageText);
            bybitService.addAccount(accountDto);

            message = SendMessage.builder()
                    .chatId(wrapper.getChatId())
                    .text("Account " + messageText + " added")
                    .build();
        } catch (GeneralServiceException e) {
            message = SendMessage.builder()
                    .chatId(wrapper.getChatId())
                    .text("Error: " + e.getErrorType().getErrorCode() + " " + e.getMessage())
                    .build();
        } catch (Exception ex) { //TODO
            ex.printStackTrace();
            message = SendMessage.builder()
                    .chatId(wrapper.getChatId())
                    .text("Error "+ex.getMessage())
                    .build();
        }

        return ResponseEntity.builder()
                .response(message)
                .build();
    }

    @MessageMapping(MessageRoutes.MAIN_MENU)
    public ResponseEntity df(@NotNull UpdateWrapper wrapper) {
        LOG.debug("AddAccountRoute route");

        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("Main menu from AddAccountRoute")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(MainMenuRoute.class)
                .build();
    }
}