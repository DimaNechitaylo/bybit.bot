package com.nechytailo.bybit.bot.bot.routes;

import com.nechytailo.bybit.bot.bot.constants.CustomSessionConstants;
import com.nechytailo.bybit.bot.entity.User;
import com.nechytailo.bybit.bot.service.UserService;
import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.constants.SessionConstants;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Component
@Entrypoint
@Message
public class LandingRoute implements Route {

    private static final Logger LOG = LoggerFactory.getLogger(LandingRoute.class);

    @Autowired
    private UserService userService;

    @MessageMapping
    public ResponseEntity handleGreeting(@NotNull UpdateWrapper wrapper) {
        LOG.debug("First route");
        wrapper.getSession().set(SessionConstants.SESSION_LOCALE_KEY, Locale.forLanguageTag("en"));
        String username = wrapper.getUpdate().getMessage().getFrom().getUserName();
        Long userId = wrapper.getUserId();
        auth(userId, username, wrapper);
        SendMessage message = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("Hi")
                .build();

        return ResponseEntity.builder()
                .response(message)
                .nextRoute(MainMenuRoute.class)
                .build();
    }

    private void auth(Long userId, String username, UpdateWrapper wrapper) {
        userService.getUser(userId)
                .ifPresentOrElse(user -> {
                            LOG.info("User with id: {}, username: {} was found.", userId, username);
                            wrapper.getSession().set(CustomSessionConstants.USERNAME_KEY, user.getUsername());
                            wrapper.getSession().set(CustomSessionConstants.ROLE_KEY, user.getUserRole());
                        },
                        () -> {
                            User user = userService.registerUser(userId, username);
                            LOG.info("A new user with id: {}, username: {} has been registered", userId, username);
                            wrapper.getSession().set(CustomSessionConstants.USERNAME_KEY, user.getUsername());
                            wrapper.getSession().set(CustomSessionConstants.ROLE_KEY, user.getUserRole());
                        });
    }
}
