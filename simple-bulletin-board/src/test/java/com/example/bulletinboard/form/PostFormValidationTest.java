package com.example.bulletinboard.form;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PostFormValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        validatorFactory.close();
    }

    @Test
    void rejectsBlankName() {
        PostForm form = new PostForm("", "content");

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Name is required");
    }

    @Test
    void rejectsBlankContent() {
        PostForm form = new PostForm("alice", "");

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Content is required");
    }

    @Test
    void rejectsContentOver100Characters() {
        PostForm form = new PostForm("alice", "a".repeat(101));

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Content must be 100 characters or fewer");
    }
}
