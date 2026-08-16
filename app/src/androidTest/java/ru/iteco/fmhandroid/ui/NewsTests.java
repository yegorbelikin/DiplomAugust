package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
import java.util.TimeZone;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsTests extends BaseTest {

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
    String newsTitle = generateRandomTitle();
    TimeZone timeZone = TimeZone.getTimeZone("Asia/Yekaterinburg");
    Calendar calendar = Calendar.getInstance(timeZone);

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    @Test
    public void createNews() {
        onView(isRoot()).perform(waitFor(500));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        onView(isRoot()).perform(waitFor(500));
    }

    @Test
    public void createNewsWithEmptyCategory() {
        onView(isRoot()).perform(waitFor(500));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_title_text_input_edit_text)).perform(click())
                .perform(replaceText(newsTitle), closeSoftKeyboard());
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_description_text_input_edit_text)).perform(clickWithDelay(200))
                .perform(replaceText("Тайский массаж"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(clickWithDelay(200));
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));
    }


    @Test
    public void createNewsWithEmptyDate() {
        onView(isRoot()).perform(waitFor(500));
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.news_item_title_text_input_edit_text)).perform(clickWithDelay(200))
                .perform(replaceText(newsTitle), closeSoftKeyboard());
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));

        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));

        onView(withId(R.id.news_item_description_text_input_edit_text)).perform(clickWithDelay(200))
                .perform(replaceText("Тайский массаж"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(clickWithDelay(200));
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));
    }


    @Test
    public void createNewsWithEmptyTime() {
        onView(isRoot()).perform(waitFor(500));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.news_item_title_text_input_edit_text)).perform(clickWithDelay(200))
                .perform(replaceText(newsTitle), closeSoftKeyboard());

        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(clickWithDelay(200));
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(clickWithDelay(200));
        onView(withId(R.id.news_item_description_text_input_edit_text)).perform(clickWithDelay(200))
                .perform(replaceText("Тайский массаж"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(clickWithDelay(200));
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));

    }


    @Test
    public void createNewsWithEmptyDescription() {
        onView(isRoot()).perform(waitFor(500));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        create(newsTitle, "Массаж", "", year, month, day, hour, minute);
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));

    }


    @Test
    public void createNewsWithWrongTime() {
        onView(isRoot()).perform(waitFor(500));
        int wrongHour = calendar.get(Calendar.HOUR_OF_DAY) - 1;
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, wrongHour, minute);
        onView(withText("Установленное время уже прошло"))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));
    }


    @Test
    public void deleteNews() {
        onView(isRoot()).perform(waitFor(500));
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(newsTitle))));
        onView(allOf(
                withId(R.id.delete_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()
        )).perform(click());
        onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .perform(click());
        onView(withText(newsTitle)).check(doesNotExist());
        onView(isRoot()).perform(waitFor(500));
    }


    @Test
    public void editNews() {
        onView(isRoot()).perform(waitFor(500));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editYear = calendar.get(Calendar.YEAR) + 1;
        int editMonth = calendar.get(Calendar.MONTH) + 2;
        int editDay = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        int editHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int editMinute = calendar.get(Calendar.MINUTE) + 1;
        logoutIfNeeded();
        performLogin(login, password);
        onView(withId(R.id.all_news_text_view)).perform(clickWithDelay(200));
        onView(withId(R.id.edit_news_material_button)).perform(clickWithDelay(200));
        onView(withId(R.id.add_news_image_view)).perform(clickWithDelay(200));        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(clickWithDelay(200));
        create(newsTitle + 1, "Зарплата", "Премия", editYear, editMonth, editDay, editHour, editMinute);
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle + 1))));
        ViewInteraction editTitle = onView(
                allOf(withId(R.id.news_item_title_text_view),
                        withText(newsTitle + 1),
                        isDisplayed()));
        editTitle.check(matches(withText(newsTitle + 1)));
        onView(allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(
                        withId(R.id.news_item_title_text_view),
                        withText(newsTitle + 1))),
                isDisplayed()))
                .perform(clickWithDelay(200));
        onView(isRoot()).perform(waitFor(1000));
        onView(allOf(
                withText("Премия"),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle + 1)))),
                isDisplayed()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));
    }
}




