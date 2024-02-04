package org.kgromov.frontend;

import org.kgromov.assistant.ChatParticipant;

import java.time.Instant;

record Message(ChatParticipant participant, String text, Instant time) {
}
