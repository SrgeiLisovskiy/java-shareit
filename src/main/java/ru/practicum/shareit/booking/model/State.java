package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.StateValidationException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;


    public static State returnState(String stateText){
        try {
            return State.valueOf(stateText);
        }catch (Exception e){
            throw new StateValidationException("Unknown state: " + stateText);
        }
    }
}
