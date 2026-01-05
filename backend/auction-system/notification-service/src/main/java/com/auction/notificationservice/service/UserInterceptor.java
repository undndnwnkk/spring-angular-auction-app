package com.auction.notificationservice.service;

import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.UUID;

public class UserInterceptor implements ChannelInterceptor {
    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String userId = accessor.getFirstNativeHeader("userId");
            System.out.println(">>> STOMP CONNECT, userId header = " + userId);
            UserExample test = new UserExample(userId);
            accessor.setUser(test);
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }
}

@EqualsAndHashCode
class UserExample implements Principal {
    private final String uuid;

    public UserExample(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return uuid;
    }
}
