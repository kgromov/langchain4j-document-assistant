package org.kgromov.assistant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum DocumentType {
    PDF(Set.of("pdf")),
    TXT(Set.of("txt, text")),
    EXCEL(Set.of("xls, xlsx")),
    ;

    private final Set<String> extensions;

    public static Optional<DocumentType> from(String extension) {
        return Stream.of(values())
                .filter(format -> format.getExtensions().contains(extension.toLowerCase()))
                .findFirst();
    }
}
