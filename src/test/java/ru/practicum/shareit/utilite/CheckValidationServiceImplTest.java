package ru.practicum.shareit.utilite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CheckValidationServiceImplTest {
    @Autowired
    private CheckValidationService checkValidation;

    @Test
    void checkPageSize() {
        assertThrows(ValidationException.class, () -> checkValidation.checkPageSize(0, 0));
        assertThrows(ValidationException.class, () -> checkValidation.checkPageSize(-1, 0));
        assertThrows(ValidationException.class, () -> checkValidation.checkPageSize(0, -1));
    }
}