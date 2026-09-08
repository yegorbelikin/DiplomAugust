package ru.iteco.fmhandroid.ui.tests;

import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.login;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.password;

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

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthPage;
import ru.iteco.fmhandroid.ui.pages.BaseTest;

@LargeTest

@RunWith(AllureAndroidJUnit4.class)

public class AuthTests extends BaseTest {
    AuthPage authPage = new AuthPage();

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


    @Test
    public void validUser() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        authPage.setMainMenuImageButton();


    }

    @Test
    public void InvalidLoginUser() {
        logoutIfNeeded();
        authPage.setLogin("loggm");
        authPage.setPassword(password);
        authPage.clickEnter();
        authPage.checkingError();
    }


    @Test
    public void InvalidPasswordUser() {
        logoutIfNeeded();
        authPage.setLogin(login);
        authPage.setPassword("pasword");
        authPage.clickEnter();
        authPage.checkingError();

    }


    @Test
    public void EmptyPasswordUser() {
        logoutIfNeeded();
        authPage.setLogin("");
        authPage.setPassword("");
        authPage.clickEnter();
        authPage.checkingEmptyError();
    }
}
