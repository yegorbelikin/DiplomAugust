package ru.iteco.fmhandroid.ui.tests;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.BaseTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static ru.iteco.fmhandroid.ui.data.ViewMatcher.waitDisplayed;

import android.widget.EditText;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
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

@LargeTest

@RunWith(AllureAndroidJUnit4.class)

public class AuthTests extends BaseTest {
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

    @Test
    public void validUser() {
        logoutIfNeeded();
        performLogin(login, password);
        onView(isRoot()).perform(waitDisplayed(R.id.main_menu_image_button, 10000));
        ViewInteraction imageButton = onView(withId(R.id.main_menu_image_button));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    @Test
    public void InvalidLoginUser() {
        logoutIfNeeded();
        onView(isRoot()).perform(waitDisplayed(R.id.login_text_input_layout, 10000));
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.login_text_input_layout))));
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText("loggn"), closeSoftKeyboard());
        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.password_text_input_layout))));
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.enter_button)).perform(click());

        onView(withText(ru.iteco.fmhandroid.R.string.error))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    @Test
    public void InvalidPasswordUser() {
        logoutIfNeeded();
        onView(isRoot()).perform(waitDisplayed(R.id.login_text_input_layout, 10000));
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.login_text_input_layout))));
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText(login), closeSoftKeyboard());
        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.password_text_input_layout))));
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(replaceText("pssword"), closeSoftKeyboard());
        onView(withId(R.id.enter_button)).perform(click());
        onView(withText(ru.iteco.fmhandroid.R.string.error))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));

    }


    @Test
    public void EmptyPasswordUser() {
        logoutIfNeeded();
        onView(isRoot()).perform(waitDisplayed(R.id.login_text_input_layout, 10000));
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.login_text_input_layout))));
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText(""), closeSoftKeyboard());
        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.password_text_input_layout))));
               passwordField.check(matches(isDisplayed()));
        passwordField.perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.enter_button)).perform(click());
        onView(withText(ru.iteco.fmhandroid.R.string.empty_login_or_password))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }
}
