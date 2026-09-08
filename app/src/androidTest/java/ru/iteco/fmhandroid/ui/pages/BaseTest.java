package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.waitDisplayed;

import androidx.test.espresso.Espresso;

import ru.iteco.fmhandroid.R;


public class BaseTest {

    private final int authorizationImageButton = R.id.authorization_image_button;

    // Ждём появления элемента с проверкой видимости
    protected void waitForElement(int id, long timeout) {
        onView(isRoot()).perform(waitDisplayed(id, timeout));
    }

    // Клик с проверкой, что элемент кликабелен
    protected void safeClick(int id) {
        onView(withId(id))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()))
                .perform(click());
        Espresso.onIdle(); // Ждём завершения действия
    }

    // Ввод текста с проверкой
    protected void safeType(int id, String text) {
        onView(withId(id))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()))
                .perform(replaceText(text), closeSoftKeyboard());
        Espresso.onIdle();
    }


    public void logoutIfNeeded() {
        try {
            onView(isRoot()).perform(waitDisplayed(authorizationImageButton, 1000));
            onView(withId(authorizationImageButton))
                    .perform(click());
            onView(withText("Log out")).perform(click());
            Espresso.onIdle();
        } catch (Exception e) {
            Espresso.onIdle();
        }
    }


}










