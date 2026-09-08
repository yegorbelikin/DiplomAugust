package ru.iteco.fmhandroid.ui.tests;

import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.login;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.newsTitle;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.password;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.timeZone;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthPage;
import ru.iteco.fmhandroid.ui.pages.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NewsPage;
import ru.iteco.fmhandroid.ui.scenario.NewsScenario;


@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsTests extends BaseTest {

    AuthPage authPage = new AuthPage();
    NewsPage newsPage = new NewsPage();
    NewsScenario newsScenario = new NewsScenario();

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @BeforeClass
    public static void disableAnimations() {
        // Принудительно отключаем анимации через ADB shell перед запуском тестов в классе
        InstrumentationRegistry.getInstrumentation().getUiAutomation()
                .executeShellCommand("settings put global window_animation_scale 0");
        InstrumentationRegistry.getInstrumentation().getUiAutomation()
                .executeShellCommand("settings put global transition_animation_scale 0");
        InstrumentationRegistry.getInstrumentation().getUiAutomation()
                .executeShellCommand("settings put global animator_duration_scale 0");
    }

    @Before
    public void registerIdlingResources() { //Подключаемся к “счетчику”
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }


    @After
    public void unregisterIdlingResources() { //Отключаемся от “счетчика”
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }


    Calendar calendar = Calendar.getInstance(timeZone);

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);


    @Test
    public void createNewsWithEmptyCategory() {

        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsPage.setNewsTitle(newsTitle);
        newsPage.setNewsDate(year, month, day);
        newsPage.clickOKInDialog();
        newsPage.setNewsTime(hour, minute);
        newsPage.clickOKInDialog();
        newsPage.setNewsDescription("Тайский массаж");
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();
    }


    @Test
    public void createNewsWithEmptyDate() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsPage.setNewsItemCategory("Массаж");
        newsPage.setNewsTitle(newsTitle);
        newsPage.setNewsTime(hour, minute);
        newsPage.clickOKInDialog();
        newsPage.setNewsDescription("Тайский массаж");
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();
    }


    @Test
    public void createNewsWithEmptyTime() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsPage.setNewsItemCategory("Массаж");
        newsPage.setNewsTitle(newsTitle);
        newsPage.setNewsDate(year, month, day);
        newsPage.clickOKInDialog();
        newsPage.setNewsDescription("Тайский массаж");
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();
    }


    @Test
    public void createNewsWithEmptyDescription() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsPage.setNewsItemCategory("Массаж");
        newsPage.setNewsTitle(newsTitle);
        newsPage.setNewsDate(year, month, day);
        newsPage.clickOKInDialog();
        newsPage.setNewsTime(hour, minute);
        newsPage.clickOKInDialog();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();
    }


    @Test
    public void createNewsWithWrongTime() {
        int wrongHour = calendar.get(Calendar.HOUR_OF_DAY) - 1;
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, wrongHour, minute);
        newsPage.matchPastTime();

    }


    @Test
    public void deleteNews() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.clickToDeleteItem(newsTitle);
        newsPage.clickToOKButtonInDeleteItem();
        newsPage.noMatchNews(newsTitle);
        Espresso.onIdle();
    }

    @Test
    public void deleteNewsWithCancel() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.clickToDeleteItem(newsTitle);
        newsPage.clickToCancelButtonInDeleteItem();
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        Espresso.onIdle();
    }


    @Test
    public void editNews() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editYear = calendar.get(Calendar.YEAR) + 1;
        int editMonth = calendar.get(Calendar.MONTH) + 2;
        int editDay = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        int editHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int editMinute = calendar.get(Calendar.MINUTE) + 1;
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.setNewsItemCategoryWithClear("Зарплата");
        newsPage.setNewsTitle(newsTitle + 1);
        newsPage.setNewsDate(editYear, editMonth, editDay);
        newsPage.clickOKInDialog();
        newsPage.setNewsTime(editHour, editMinute);
        newsPage.clickOKInDialog();
        newsPage.setNewsDescription("Премия");
        newsPage.clickSaveButton();
        newsPage.scrollToNews(newsTitle + 1);
        newsPage.matchNews(newsTitle + 1);
        newsPage.openNewsCard(newsTitle + 1);
        newsPage.matchNewsDescription("Премия", newsTitle + 1);
        Espresso.onIdle();
    }


    @Test
    public void editNewsWithEmptyCategories() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.setNewsEmptyCategory();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();

    }

    @Test
    public void editNewsWithEmptyDate() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.clearNewsDate();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();

    }

    @Test
    public void editNewsWithEmptyTime() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.clearNewsTime();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();

    }

    @Test
    public void editNewsWithEmptyTitle() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.clearNewsTitle();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();

    }


    @Test
    public void editNewsWithEmptyDescription() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickToEditeItem(newsTitle);
        newsPage.clearNewsDescription();
        newsPage.clickSaveButton();
        newsPage.matchEmptyFields();

    }


    @Test
    public void sortNews() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editMonth = calendar.get(Calendar.MONTH) + 2;
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickAddNews();
        newsScenario.create(newsTitle + 1, "Массаж", "Тайский массаж", year, editMonth, day, hour, minute);
        newsPage.scrollToNews(newsTitle + 1);
        newsPage.matchNews(newsTitle + 1);
        String earlyDateText = String.format("%02d.%02d.%d", day, month, year);
        String laterDateText = String.format("%02d.%02d.%d", day, editMonth, year);
        newsPage.checkSortLaterEarly(newsTitle + 1, laterDateText, newsTitle, earlyDateText);
        newsPage.clickSortButton();
        newsPage.scrollToNews(newsTitle + 1);
        newsPage.checkSortLaterEarly(newsTitle, earlyDateText, newsTitle + 1, laterDateText);

    }


    @Test
    public void filterNews() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editMonth = calendar.get(Calendar.MONTH) + 2;

        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.clickAddNews();
        newsScenario.create(newsTitle + 1, "Зарплата", "Тайский массаж", year, editMonth, day, hour, minute);
        newsPage.scrollToNews(newsTitle + 1);
        newsPage.matchNews(newsTitle + 1);
        newsPage.clickFilterNewsButton();
        newsPage.setNewsItemCategoryWithClear("Массаж");
        newsPage.clickFilterButton();
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);
        newsPage.matchMissingNews(newsTitle + 1);

    }

    @Test
    public void createNews() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        newsScenario.goToCreateNews();
        newsScenario.create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        newsPage.scrollToNews(newsTitle);
        newsPage.matchNews(newsTitle);

    }


}


