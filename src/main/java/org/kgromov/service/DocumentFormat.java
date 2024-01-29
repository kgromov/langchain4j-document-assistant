package org.kgromov.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum DocumentFormat {
    PDF(Set.of("pdf")),
    TXT(Set.of("txt, text"));

    private final Set<String> extensions;

    public static Optional<DocumentFormat> from(String extension) {
        return Stream.of(values())
                .filter(format -> format.getExtensions().contains(extension.toLowerCase()))
                .findFirst();
    }
}