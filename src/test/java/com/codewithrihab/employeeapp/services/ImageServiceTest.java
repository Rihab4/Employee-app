package com.codewithrihab.employeeapp.services;

import com.codewithrihab.employeeapp.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    private ImageService imageService;


    @BeforeEach
    void setUp() {
        imageService = new ImageService();
    }

    @ParameterizedTest
    @MethodSource("provideMultipartFilesForValidation")
    void testIsValidImage(MultipartFile file, boolean expected) {
        assertEquals(expected, ImageService.isValidImage(file));
    }

    private static Stream<Arguments> provideMultipartFilesForValidation() {
        MultipartFile validJpeg = mock(MultipartFile.class);
        when(validJpeg.isEmpty()).thenReturn(false);
        when(validJpeg.getContentType()).thenReturn("image/jpeg");

        MultipartFile validPng = mock(MultipartFile.class);
        when(validPng.isEmpty()).thenReturn(false);
        when(validPng.getContentType()).thenReturn("image/png");

        MultipartFile invalidType = mock(MultipartFile.class);
        when(invalidType.isEmpty()).thenReturn(false);
        when(invalidType.getContentType()).thenReturn("text/plain");

        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(emptyFile.getContentType()).thenReturn("image/jpeg");

        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(emptyFile, false),
                Arguments.of(invalidType, false),
                Arguments.of(validJpeg, true),
                Arguments.of(validPng, true)
        );
    }


    @Test
    void testSaveImageToStorageCreatesFileSuccessfully() throws IOException {
        String uploadDir = "test-dir";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenReturn(mock(InputStream.class));


        String savedFileName = imageService.saveImageToStorage(uploadDir, file);

        assertNotNull(savedFileName);
        assertTrue(savedFileName.endsWith("_test.jpg"));
    }

    @Test
    void testSaveImageToStorageThrowsIOException() throws IOException {
        String uploadDir = "test-dir";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new IOException("Test exception"));

        IOException exception = assertThrows(IOException.class, () -> {
            imageService.saveImageToStorage(uploadDir, file);
        });

        assertEquals("Test exception", exception.getMessage());

    }
}
