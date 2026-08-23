package ru.iteco.fmhandroid.ui.data;

public class DataHelper {
    public static final String generateRandomTitle() {
        int randomNumber = (int) (Math.random() * 10000);
        return "Massage " + randomNumber;
    }
}
