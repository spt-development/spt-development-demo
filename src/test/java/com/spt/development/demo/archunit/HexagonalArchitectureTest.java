package com.spt.development.demo.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Test which uses ArchUnit framework to verify compliance with the
 * dependencies flow imposed by Hexagonal architecture: i.e. core must
 * not depend on infrastructure.
 */
class HexagonalArchitectureTest {
    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.spt.development.demo");

    private final DescribedPredicate<JavaClass> areStandard = resideInAnyPackage(
            "",
            "jakarta..",
            "java..",
            "javax..",
            "org.apache.commons.collections4..",
            "org.slf4j..",
            "lombok..");

    private final DescribedPredicate<JavaClass> areCore = resideInAnyPackage("com.spt.development.demo.core..");

    private final DescribedPredicate<JavaClass> areInfrastructure = resideInAnyPackage("com.spt.development.demo.infrastructure..");

    @Test
    void core_should_not_depend_on_infrastructure() {
        noClasses()
                .that(areCore)
                .should()
                .dependOnClassesThat(areInfrastructure)
                .check(classes);
    }

    @Test
    void core_should_only_depend_on_itself_and_standard_classes() {
        classes()
                .that(areCore)
                .should()
                .onlyDependOnClassesThat(areCore.or(areStandard))
                .check(classes);
    }
}