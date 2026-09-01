package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.waitDisplayed;

import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;

import ru.iteco.fmhandroid.R;


public class BaseTest {

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
            onView(isRoot()).perform(waitDisplayed(R.id.authorization_image_button, 1000));
            onView(withId(R.id.authorization_image_button))
                    .perform(click());
            onView(withText("Log out")).perform(click());
            Espresso.onIdle();
        } catch (Exception e) {
            Espresso.onIdle();
        }
    }

    protected void performLogin(String login, String password) {
        onView(isRoot()).perform(waitDisplayed(R.id.login_text_input_layout, 10000));
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.login_text_input_layout))));
        Espresso.onIdle();
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText(login), closeSoftKeyboard());
        Espresso.onIdle();
        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.password_text_input_layout))));
        Espresso.onIdle();
        passwordField.check(matches(isDisplayed()));
        Espresso.onIdle();
        passwordField.perform(replaceText(password), closeSoftKeyboard());
        Espresso.onIdle();
        safeClick(R.id.enter_button);
        onView(isRoot()).perform(waitDisplayed(R.id.main_menu_image_button, 10000));
        Espresso.onIdle();

    }

    public String create(String title, String category, String description, int year, int month, int day, int hour, int minute) {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_title_text_input_edit_text, title);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        safeType(R.id.news_item_description_text_input_edit_text, description);
        safeClick(R.id.save_button);
        Espresso.onIdle();
        return title;
    }

    protected void openAboutPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        onData(anything())
                .inAdapterView(isAssignableFrom(android.widget.AdapterView.class))
                .atPosition(2).perform(click());
        Espresso.onIdle();
    }

    protected void openMissionPage() {
        onView(withId(R.id.our_mission_image_button)).perform(click());
        Espresso.onIdle();

    }
}










