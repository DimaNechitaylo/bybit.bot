package com.nechytailo.bybit.bot.bot.routes;

import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Entrypoint
@Message
public class LandingRoute implements Route {

    private static final Logger LOG = LoggerFactory.getLogger(LandingRoute.class);

    @MessageMapping
    public ResponseEntity handleGreeting(@NotNull UpdateWrapper wrapper) {
        LOG.debug("First route");
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("Hi")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(MainMenuRoute.class)
                .build();
    }
}
