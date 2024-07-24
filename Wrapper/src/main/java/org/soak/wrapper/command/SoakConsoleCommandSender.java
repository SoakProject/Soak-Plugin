package org.soak.wrapper.command;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.jetbrains.annotations.NotNull;
import org.soak.exception.NotImplementedException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;

import java.util.UUID;

public class SoakConsoleCommandSender extends SoakCommandSender implements ConsoleCommandSender {

    public SoakConsoleCommandSender() {
        this(Sponge.systemSubject());
    }

    public SoakConsoleCommandSender(SystemSubject subject) {
        this(subject, subject);
    }

    public SoakConsoleCommandSender(SystemSubject subject, Audience audience) {
        super(subject, audience);
    }

    @Override
    public boolean isConversing() {
        throw NotImplementedException.createByLazy(Conversable.class, "isConversing");
    }

    @Override
    public void acceptConversationInput(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Conversable.class, "acceptConversationInput", String.class);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation arg0) {
        throw NotImplementedException.createByLazy(Conversable.class, "beginConversation", Conversation.class);
    }

    @Override
    public void abandonConversation(@NotNull Conversation arg0, @NotNull ConversationAbandonedEvent arg1) {
        throw NotImplementedException.createByLazy(Conversable.class,
                "abandonConversation",
                Conversation.class,
                ConversationAbandonedEvent.class);
    }

    @Override
    public void abandonConversation(@NotNull Conversation arg0) {
        throw NotImplementedException.createByLazy(Conversable.class, "abandonConversation", Conversation.class);
    }

    @Override
    public void sendRawMessage(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Conversable.class, "sendRawMessage", String.class);
    }

    @Override
    public void sendRawMessage(UUID arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(Conversable.class, "sendRawMessage", UUID.class, String.class);
    }
}
