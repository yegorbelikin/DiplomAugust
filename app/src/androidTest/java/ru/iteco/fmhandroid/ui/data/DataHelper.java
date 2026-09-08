package ru.iteco.fmhandroid.ui.data;

import java.util.TimeZone;

public class DataHelper {

    public interface TestData {

        String login = "login2";
        String password = "password2";

        String version = "1.0.0";
        String privacyLink = "https://vhospice.org/#/privacy-policy/";
        String termsLink = "https://vhospice.org/#/terms-of-use";
        String titleMissionText = "Служение человеку с теплом, любовью и заботой";
        String descriptionMissionText = "\"Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл.\"";

        String newsTitle = DataHelper.generateRandomTitle();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Yekaterinburg");

    }


    public static final String generateRandomTitle() {
        int randomNumber = (int) (Math.random() * 10000);
        return "Massage " + randomNumber;
    }
}
