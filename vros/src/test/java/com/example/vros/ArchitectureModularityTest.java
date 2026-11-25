package com.example.vros;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArchitectureModularityTest {

    @Test
    void verifyModuleBoundariesAndGenerateDocumentation() throws IOException {
        Path srcRoot = Paths.get("src/main/java/com/example/vros");
        Map<String, Set<String>> dependencies = new LinkedHashMap<>();
        List<String> modules = List.of("shared", "fleet", "customer", "security", "routing");

        for (String mod : modules) {
            dependencies.put(mod, new TreeSet<>());
            Path modPath = srcRoot.resolve(mod);
            if (!Files.exists(modPath)) continue;

            try (Stream<Path> files = Files.walk(modPath)) {
                files.filter(p -> p.toString().endsWith(".java")).forEach(file -> {
                    try {
                        for (String line : Files.readAllLines(file)) {
                            if (line.startsWith("import com.example.vros.")) {
                                String importedPkg = line.substring("import com.example.vros.".length()).split("\\.")[0];
                                if (!importedPkg.equals(mod) && modules.contains(importedPkg)) {
                                    dependencies.get(mod).add(importedPkg);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        // Regulile arhitecturale ale monolitului modular:
        assertTrue(dependencies.get("shared").isEmpty(), "Modulul 'shared' nu trebuie să depindă de alte module!");
        assertFalse(dependencies.get("fleet").contains("routing"), "Modulul 'fleet' nu trebuie să depindă de 'routing'!");
        assertFalse(dependencies.get("customer").contains("routing"), "Modulul 'customer' nu trebuie să depindă de 'routing'!");
        assertFalse(dependencies.get("customer").contains("fleet"), "Modulul 'customer' trebuie să fie decuplat de 'fleet'!");

        // Generare automată diagramă arhitecturală PlantUML în target/vros-modules.puml
        StringBuilder puml = new StringBuilder("@startuml\ntitle VROS Modular Monolith Architecture\n");
        for (String mod : modules) {
            puml.append("component [").append(mod).append("]\n");
        }
        dependencies.forEach((from, targets) -> {
            for (String to : targets) {
                puml.append("[").append(from).append("] --> [").append(to).append("]\n");
            }
        });
        puml.append("@enduml\n");

        Path out = Paths.get("target/vros-modules.puml");
        Files.createDirectories(out.getParent());
        Files.writeString(out, puml.toString());
        System.out.println("Diagrama arhitecturală a fost generată în: " + out.toAbsolutePath());
    }
}
