package org.kgromov.frontend;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.assistant.ChatParticipant;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.kgromov.assistant.ChatParticipant.ASSISTANT;
import static org.kgromov.assistant.ChatParticipant.USER;

@Route("")
@PageTitle("Chat with Open AI document assistant")
@Slf4j
@RequiredArgsConstructor
class ChatView extends VerticalLayout {
    private final ConversationalRetrievalChain conversationalRetrievalChain;
    private final MessageList chat = new MessageList();
    private final MessageInput input = new MessageInput();

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        configureLayout();

        input.addSubmitListener(event -> {
            this.processMessage(USER, event.getValue());

            CompletableFuture.supplyAsync(() -> conversationalRetrievalChain.execute(event.getValue()))
                    .whenComplete((answer, ex) -> this.processMessage(ASSISTANT, answer));
        });
    }

    private void configureLayout() {
        setSizeFull();
        add(chat, input);
        expand(chat);
        input.setWidthFull();
    }

    private void processMessage(ChatParticipant participant, String message) {
        var chatMessage = new MessageListItem(message, Instant.now(), participant.getName(), participant.getAvatar());
        getUI().ifPresent(ui -> ui.access(() -> {
            var chatMessages = new ArrayList<>(chat.getItems());
            chatMessages.add(chatMessage);
            chat.setItems(chatMessages);
        }));
    }
}
