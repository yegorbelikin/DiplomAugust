package ru.iteco.fmhandroid.ui.scenario;

import androidx.test.espresso.Espresso;

import ru.iteco.fmhandroid.ui.pages.NewsPage;

public class NewsScenario {

    NewsPage newsPage = new NewsPage();


    public void goToCreateNews() {
        newsPage.clickAllNews();
        newsPage.clickEditNews();
        newsPage.clickAddNews();
    }

    public String create(String title, String category, String description, int year, int month, int day, int hour, int minute) {
        newsPage.setNewsItemCategory(category);
        newsPage.setNewsTitle(title);
        newsPage.setNewsDate(year, month, day);
        newsPage.clickOKInDialog();
        newsPage.setNewsTime(hour, minute);
        newsPage.clickOKInDialog();
        newsPage.setNewsDescription(description);
        newsPage.clickSaveButton();
        Espresso.onIdle();
        return title;
    }


}
