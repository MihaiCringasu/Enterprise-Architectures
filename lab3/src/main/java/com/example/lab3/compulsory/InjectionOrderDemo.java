package com.example.lab3.compulsory;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InjectionOrderDemo {

    private final ConstructorDep constructorDep;

    @Autowired
    private FieldDep fieldDep;

    private SetterDep setterDep;
    private MethodDep methodDep;

    // 1. CONSTRUCTOR INJECTION (se execută prima dată, la crearea obiectului)
    @Autowired
    public InjectionOrderDemo(ConstructorDep constructorDep) {
        this.constructorDep = constructorDep;
        System.out.println(">>> PASUL 1: Constructor Injection executat. (FieldDep este încă null? " + (this.fieldDep == null) + ")");
    }

    // 2. FIELD INJECTION (Spring injectează prin reflection imediat după constructor)
    // Nu putem pune un print direct pe câmp, dar îl verificăm în Setter!

    // 3. SETTER INJECTION
    @Autowired
    public void setSetterDep(SetterDep setterDep) {
        this.setterDep = setterDep;
        System.out.println(">>> PASUL 2 & 3: Field Injection s-a terminat deja (FieldDep != null? " + (this.fieldDep != null) + "), acum se execută Setter Injection.");
    }

    // 4. ARBITRARY METHOD INJECTION
    @Autowired
    public void initializeCustomMethod(MethodDep methodDep) {
        this.methodDep = methodDep;
        System.out.println(">>> PASUL 4: Method Injection executat.");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println(">>> @PostConstruct: Toate dependențele au fost injectate cu succes!\n");
    }
}
