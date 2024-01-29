package org.kgromov.web;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ConversationalRetrievalChain conversationalRetrievalChain;

    @PostMapping
    public String ask(@RequestBody String question) {
        var answer = conversationalRetrievalChain.execute(question);
        log.debug("Answer is - {}", answer);
        return answer;
    }
}
