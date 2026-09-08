package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
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

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import ru.iteco.fmhandroid.R;

public class AuthPage extends BaseTest {


    private final int loginInput = R.id.login_text_input_layout;
    private final int passwordInput = R.id.password_text_input_layout;
    private final int enterButton = R.id.enter_button;
    private final int mainMenuImageButton = R.id.main_menu_image_button;
    private final int authError = ru.iteco.fmhandroid.R.string.error;
    private final int authEmptyError = ru.iteco.fmhandroid.R.string.empty_login_or_password;


    public void performLogin(String login, String password) {

        setLogin(login);
        setPassword(password);
        clickEnter();
        Espresso.onIdle();
        setMainMenuImageButton();

    }

    public void setMainMenuImageButton() {
        waitForElement(mainMenuImageButton, 5000);
        ViewInteraction imageButton = onView(withId(mainMenuImageButton));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void setLogin(String login) {
        onView(isRoot()).perform(waitDisplayed(loginInput, 10000));
        ViewInteraction loginField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(loginInput))));
        loginField.check(matches(isDisplayed()));
        loginField.perform(replaceText(login), closeSoftKeyboard());

    }

    public void setPassword(String password) {
        ViewInteraction passwordField = onView(
                allOf(isAssignableFrom(EditText.class),
                        isDescendantOfA(withId(passwordInput))));
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(replaceText(password), closeSoftKeyboard());
    }

    public void clickEnter() {
        safeClick(enterButton);
    }

    public void checkingError() {
        onView(withText(authError))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));

    }

    public void checkingEmptyError() {
        onView(withText(authEmptyError))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }
}
