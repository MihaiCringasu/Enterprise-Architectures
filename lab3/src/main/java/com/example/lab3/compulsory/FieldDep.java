package com.example.lab3.compulsory;

import org.springframework.stereotype.Component;

@Component
public class FieldDep {
    public FieldDep() {
        System.out.println("[1] FieldDep instanțiat");
    }
}
