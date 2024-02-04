package org.kgromov.assistant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatParticipant {
    USER("User", "https://cdn-icons-png.flaticon.com/512/9187/9187532.png"),
    ASSISTANT("Assistant", "https://cdn-icons-png.flaticon.com/512/3558/3558860.png");

    private final String name;
    private final String avatar;
}
