package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.StateValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class StateTest {

    @Test
    void returnState() {
        String stateStr = "Unknown";
        String finalStateStr = stateStr;
        assertThrows(StateValidationException.class, () -> State.returnState(finalStateStr));

        stateStr = "ALL";
        State stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.ALL);

        stateStr = "CURRENT";
        stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.CURRENT);

        stateStr = "PAST";
        stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.PAST);

        stateStr = "FUTURE";
        stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.FUTURE);

        stateStr = "REJECTED";
        stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.REJECTED);

        stateStr = "WAITING";
        stateTest = State.returnState(stateStr);
        assertEquals(stateTest, State.WAITING);
    }
}