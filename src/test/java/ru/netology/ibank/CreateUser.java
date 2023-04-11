package ru.netology.ibank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUser {
    private String login;
    private String password;
    private String status;
}
