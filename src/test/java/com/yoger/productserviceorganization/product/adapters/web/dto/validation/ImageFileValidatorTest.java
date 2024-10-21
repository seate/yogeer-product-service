package com.yoger.productserviceorganization.product.adapters.web.dto.validation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class ImageFileValidatorTest {
    private ImageFileValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ImageFileValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void whenFileIsValidImageAndNotEmpty_thenIsValid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");

        ValidImage annotation = mock(ValidImage.class);
        when(annotation.emptyable()).thenReturn(false);
        validator.initialize(annotation);

        boolean result = validator.isValid(file, context);

        assertThat(result).isTrue();
    }

    @Test
    public void whenFileHasInvalidContentType_thenIsInvalid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");

        ValidImage annotation = mock(ValidImage.class);
        when(annotation.emptyable()).thenReturn(false);
        validator.initialize(annotation);

        boolean result = validator.isValid(file, context);

        assertThat(result).isFalse();
    }

    @Test
    public void whenFileIsEmptyAndEmptyableTrue_thenIsValid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        ValidImage annotation = mock(ValidImage.class);
        when(annotation.emptyable()).thenReturn(true);
        validator.initialize(annotation);

        boolean result = validator.isValid(file, context);

        assertThat(result).isTrue();
    }

    @Test
    public void whenFileIsEmptyAndEmptyableFalse_thenIsInvalid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        ValidImage annotation = mock(ValidImage.class);
        when(annotation.emptyable()).thenReturn(false);
        validator.initialize(annotation);

        boolean result = validator.isValid(file, context);

        assertThat(result).isFalse();
    }
}