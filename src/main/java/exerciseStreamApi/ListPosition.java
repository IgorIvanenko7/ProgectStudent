package exerciseStreamApi;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ListPosition {

    ENGINEER ("Инженер"),
    ACCOUNTANT ("Бухгалтер"),
    CSECRETARY ("Секретать");

    private final String position ;
}
