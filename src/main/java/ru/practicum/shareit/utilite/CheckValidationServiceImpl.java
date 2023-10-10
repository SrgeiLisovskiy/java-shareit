package ru.practicum.shareit.utilite;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service

public class CheckValidationServiceImpl implements CheckValidationService {
    @Override
    public PageRequest checkPageSize(Integer from, Integer size) {
        if (from == 0 && size == 0) {
            throw new ValidationException("размер и номер страницы не может быть равен нулю ");
        }
        if (size < 0) {
            throw new ValidationException("размер не может быть меньше чем 0");
        }

        if (from < 0) {
            throw new ValidationException("страница не может быть меньше чем 0");
        }
        return PageRequest.of(from / size, size);
    }
}

