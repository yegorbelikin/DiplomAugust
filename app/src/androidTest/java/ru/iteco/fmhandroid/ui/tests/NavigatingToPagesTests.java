package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.waitFor;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
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
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.BaseTest;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NavigatingToPagesTests extends BaseTest {

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

    String login = "login2";
    String password = "password2";
    String version = "1.0.0";
    String privacyLink = "https://vhospice.org/#/privacy-policy/";
    String termsLink = "https://vhospice.org/#/terms-of-use";
    String titleMissionText = "Служение человеку с теплом, любовью и заботой";
    String descriptionMissionText = "\"Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл.\"";


    @Test
    public void GoToMissionPage() {
        logoutIfNeeded();
        performLogin(login, password);
        openMissionPage();
        ViewInteraction imageButton = onView(withId(R.id.our_mission_title_text_view));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    @Test
    public void expandMissionCard() {
        logoutIfNeeded();
        performLogin(login, password);
        openMissionPage();
        onView(withId(R.id.our_mission_item_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(titleMissionText))));
        ViewInteraction textView = onView(
                allOf(withId(R.id.our_mission_item_title_text_view), withText(titleMissionText),
                        withParent(withParent(withId(R.id.our_mission_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(titleMissionText)));
        Espresso.onIdle();
        onView(allOf(
                withId(R.id.our_mission_item_material_card_view),
                hasDescendant(withText(titleMissionText)),
                isDisplayingAtLeast(90)
        )).perform(click());
        onView(withId(R.id.our_mission_item_list_recycler_view))
                .perform(swipeUp());
        Espresso.onIdle();
        onView(allOf(
                withText(descriptionMissionText),
                isDescendantOfA(allOf(
                        withId(R.id.our_mission_item_material_card_view),
                        hasDescendant(withText(titleMissionText)))),
                isDisplayed()))
                .check(matches(isDisplayed()));
        Espresso.onIdle();
    }

    @Test
    public void GoToNewsPage() {
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.main_menu_image_button)).perform(click());
        onData(anything())
                .inAdapterView(isAssignableFrom(android.widget.AdapterView.class))
                .atPosition(1).perform(click());
        ViewInteraction imageButton = onView(withId(R.id.news_list_swipe_refresh));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }


    @Test
    public void checkAboutPageContent() {
        logoutIfNeeded();
        performLogin(login, password);
        openAboutPage();
        ViewInteraction imageButton = onView(withId(R.id.about_version_title_text_view));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.onIdle();
        ViewInteraction versionView = onView(
                allOf(withId(R.id.about_version_value_text_view), withText(version),
                        isDisplayed()));
        versionView.check(matches(withText(version)));
        ViewInteraction privacyView = onView(
                allOf(withId(R.id.about_privacy_policy_value_text_view), withText(privacyLink),
                        isDisplayed()));
        privacyView.check(matches(withText(privacyLink)));
        ViewInteraction termsView = onView(
                allOf(withId(R.id.about_terms_of_use_value_text_view), withText(termsLink),
                        isDisplayed()));
        termsView.check(matches(withText(termsLink)));


    }

}
