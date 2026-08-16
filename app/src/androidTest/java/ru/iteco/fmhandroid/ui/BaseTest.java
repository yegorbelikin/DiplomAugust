package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static ru.iteco.fmhandroid.ui.ViewMatcher.waitDisplayed;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;

import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;

//@RunWith(AllureAndroidJUnit4.class)
public class BaseTest {

    public void logoutIfNeeded() {
        try {
            onView(withId(R.id.authorization_image_button))
                    .perform(click());
            onView(withText("Log out")).perform(click());
        } catch (Exception e) {

        }
    }

    protected void performLogin(String login, String password) {
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.login_text_input_layout))));
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText(login), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(R.id.password_text_input_layout))));
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(replaceText(password), closeSoftKeyboard());

        onView(withId(R.id.enter_button)).perform(click());
        onView(isRoot()).perform(waitDisplayed(R.id.main_menu_image_button, 5000));
    }

    public static ViewAction waitFor(final long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + delay + " milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

    public static ViewAction clickWithDelay(final long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayingAtLeast(90); // элемент должен быть виден
            }

            @Override
            public String getDescription() {
                return "click with delay";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay); // искусственно ждем ДО клика
                view.performClick();
            }
        };
    }


    public String generateRandomTitle() {
        int randomNumber = (int) (Math.random() * 10000);
        return "Massage " + randomNumber;
    }

    public String create(String title, String category, String description, int year, int month, int day, int hour, int minute) {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click(), clearText(), click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.news_item_title_text_input_edit_text)).perform(replaceText(title));
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .perform(replaceText(description), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(clickWithDelay(200));
        return title;
    }
}










