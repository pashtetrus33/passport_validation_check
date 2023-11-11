package com.example.passport_check;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;

public class HelloController implements Initializable {

    private final String today = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

    @FXML
    private TextField age;
    @FXML
    private TextField ageOnIssueDate;
    @FXML
    private TextField result;
    @FXML
    private TextField issueDate;
    @FXML
    private TextField birthday;
    @FXML
    private Label todayLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayLabel.setText(today);
        result.setEditable(false);
        age.setEditable(false);
        ageOnIssueDate.setEditable(false);
    }

    @FXML
    protected void onHelloButtonClick() {

        String birthdayString = birthday.getText();
        String issueDateString = issueDate.getText();
        age.clear();
        ageOnIssueDate.clear();
        result.clear();
        int ageValue = -1;
        int ageOnIssueDateValue = -1;
        if (birthdayString.isEmpty() || issueDateString.isEmpty()) {
            result.setText("Указаны не все даты!");
            result.setStyle("-fx-background-color: red");
        } else {
            ageValue = calculateAge(null, birthdayString);
            ageOnIssueDateValue = calculateAge(issueDateString, birthdayString);
        }


        if (ageValue >= 0 && ageOnIssueDateValue >= 0) {
            age.setText("Возраст: " + ageValue);
            ageOnIssueDate.setText("Возраст на дату выдачи: " + ageOnIssueDateValue);

            if (ageOnIssueDateValue >= 45 || ageOnIssueDateValue >= 20 && ageValue <= 44 || ageOnIssueDateValue >= 14 && ageValue <= 19) {
                age.clear();
                age.setText("Возраст: " + ageValue);
                ageOnIssueDate.clear();
                ageOnIssueDate.setText("Возраст на дату выдачи: " + ageOnIssueDateValue);
                result.setText("Паспорт действительный");
                result.setStyle("-fx-background-color: green");
            } else if (ageValue < 14) {
                result.setText("Возраст меньше 14 лет!");
                result.setStyle("-fx-background-color: red");

            } else if (ageOnIssueDateValue < 14) {
                result.setText("Возраст на дату выдачи меньше 14 лет!");
                result.setStyle("-fx-background-color: red");

            }else {
                result.setText("Паспорт недействительный!!!");
                result.setStyle("-fx-background-color: red");
            }
        }
    }


    public int calculateAge(String issueString, String birthdayString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate = null;
        LocalDate currentDate = LocalDate.now();
        try {
            birthDate = LocalDate.parse(birthdayString, formatter);
            if (issueString != null) {
                currentDate = LocalDate.parse(issueString, formatter);
            }
        } catch (DateTimeParseException e) {
            result.setText("Дата(ы) указана(ы) некорректно!");
            result.setStyle("-fx-background-color: red");
            return -1;
        }

        if (birthDate.getMonth().getValue() == 2 && Integer.parseInt(birthdayString.substring(0, 2)) >= 30) {
            result.setText("в феврале всегда меньше 29 дней!");
            result.setStyle("-fx-background-color: red");
            return -1;
        } else if (birthDate.getYear() % 4 != 0 && birthDate.getMonth().getValue() == 2 && Integer.parseInt(birthdayString.substring(0, 2)) > 28) {
            result.setText(birthDate.getYear() + " - год не високосный в феврале 28 дней!");
            result.setStyle("-fx-background-color: red");
            return -1;
        }

        if (birthDate.isAfter(LocalDate.now()) || currentDate.isAfter(LocalDate.now())) {
            result.setText("Ваша(ы) дата(ы) из будущего :)");
            result.setStyle("-fx-background-color: red");
            return -1;
        }

        if (birthDate.isAfter(currentDate)) {
            result.setText("Дата выдачи раньше даты рождения :))");
            result.setStyle("-fx-background-color: red");
            return -1;
        }

        return Period.between(birthDate, currentDate).getYears();
    }
}

