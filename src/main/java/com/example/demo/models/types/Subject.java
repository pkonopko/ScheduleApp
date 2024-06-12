package com.example.demo.models.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subject {
    MATH("Math"),
    PHYSICS("Physics"),
    IT("It"),
    CHEMISTRY("Chemistry"),
    BIOLOGY("Biology"),
    ENGLISH("English"),
    SPANISH("Spanish"),
    GERMAN("German");
    private final String subject;
}
