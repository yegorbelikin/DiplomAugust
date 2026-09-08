package ru.iteco.fmhandroid.ui.tests;

import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.descriptionMissionText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.login;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.password;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.privacyLink;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.termsLink;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.titleMissionText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.TestData.version;

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
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.AuthPage;
import ru.iteco.fmhandroid.ui.pages.BaseTest;
import ru.iteco.fmhandroid.ui.pages.MissionPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class OtherPageTests extends BaseTest {


    AuthPage authPage = new AuthPage();
    MissionPage missionPage = new MissionPage();
    AboutPage aboutPage = new AboutPage();
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
    public void goToMissionPage() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        missionPage.openMissionPage();
        missionPage.mathMissionPage();

    }

    @Test
    public void expandMissionCard() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        missionPage.openMissionPage();
        missionPage.scrollToMission(titleMissionText);
        missionPage.matchMissionText(titleMissionText);
        missionPage.openMissionCard(titleMissionText);
        missionPage.swipeUpMissionList();
        missionPage.mathMissionDescription(descriptionMissionText, titleMissionText);
    }

    @Test
    public void checkAboutPageContent() {
        logoutIfNeeded();
        authPage.performLogin(login, password);
        aboutPage.openAboutPage();
        aboutPage.matchAboutPage();
        aboutPage.matchAboutVersion(version);
        aboutPage.matchAboutPrivacyLink(privacyLink);
        aboutPage.matchAboutTermsLink(termsLink);

    }

}


