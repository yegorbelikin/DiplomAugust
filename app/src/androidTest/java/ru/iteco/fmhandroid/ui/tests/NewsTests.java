package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
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
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.checkNewsIsMissing;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.verifyNewsOrder;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.waitFor;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.Espresso;
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
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.pages.BaseTest;

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
    String newsTitle = DataHelper.generateRandomTitle();
    TimeZone timeZone = TimeZone.getTimeZone("Asia/Yekaterinburg");
    Calendar calendar = Calendar.getInstance(timeZone);

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);


    @Test
    public void createNewsWithEmptyCategory() {

        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        safeType(R.id.news_item_title_text_input_edit_text, newsTitle);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        Espresso.onIdle();
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_description_text_input_edit_text, "Тайский массаж");
        safeClick(R.id.save_button);
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));

    }


    @Test
    public void createNewsWithEmptyDate() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_title_text_input_edit_text, newsTitle);

        Espresso.onIdle();

        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        safeType(R.id.news_item_description_text_input_edit_text, "Тайский массаж");
        safeClick(R.id.save_button);
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    @Test
    public void createNewsWithEmptyTime() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_title_text_input_edit_text, newsTitle);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());

        Espresso.onIdle();

        safeType(R.id.news_item_description_text_input_edit_text, "Тайский массаж");
        safeClick(R.id.save_button);
        Espresso.onIdle();





        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));

    }


    @Test
    public void createNewsWithEmptyDescription() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_title_text_input_edit_text, newsTitle);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        safeClick(R.id.save_button);
        Espresso.onIdle();




        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));

    }


    @Test
    public void createNewsWithWrongTime() {
        int wrongHour = calendar.get(Calendar.HOUR_OF_DAY) - 1;
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, wrongHour, minute);
        onView(withText("Установленное время уже прошло"))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    @Test
    public void deleteNews() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        onView(allOf(
                withId(R.id.delete_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()
        )).perform(click());
        Espresso.onIdle();
        onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .perform(click());
        onView(withText(newsTitle)).check(doesNotExist());
        Espresso.onIdle();
    }

    @Test
    public void deleteNewsWithCancel() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        onView(allOf(
                withId(R.id.delete_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()
        )).perform(click());
        Espresso.onIdle();
        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .perform(click());
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        onView(isRoot()).perform(waitFor(500));
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
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();


        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(click(), clearText(), click());
        onData(allOf(is(instanceOf(String.class)), is("Зарплата")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
        safeType(R.id.news_item_title_text_input_edit_text, newsTitle + 1);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(editYear, editMonth, editDay));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);

        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(editHour, editMinute));
        Espresso.onIdle();
        onView(withId(android.R.id.button1)).perform(click());
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);

        safeType(R.id.news_item_description_text_input_edit_text, "Премия");
        safeClick(R.id.save_button);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle + 1))));
        Espresso.onIdle();
        ViewInteraction editTitle = onView(
                allOf(withId(R.id.news_item_title_text_view),
                        withText(newsTitle + 1),
                        isDisplayed()));
        editTitle.check(matches(withText(newsTitle + 1)));
        Espresso.onIdle();
        onView(allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(
                        withId(R.id.news_item_title_text_view),
                        withText(newsTitle + 1))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
        onView(allOf(
                withText("Премия"),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle + 1)))),
                isDisplayed()))
                .check(matches(isDisplayed()));
        Espresso.onIdle();
    }


    @Test
    public void editNewsWithEmptyCategories() {

        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();

        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click(), clearText(), closeSoftKeyboard());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.save_button)).perform(click());
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(waitFor(500));
    }

    @Test
    public void editNewsWithEmptyDate() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();


        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(clearText());
        onView(withId(R.id.save_button)).perform(click());
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }

    @Test
    public void editNewsWithEmptyTime() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();


        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(clearText());
        onView(withId(R.id.save_button)).perform(click());
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }

    @Test
    public void editNewsWithEmptyTitle() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
        onView(withId(R.id.news_item_title_text_input_edit_text)).perform(clearText());
        onView(withId(R.id.save_button)).perform(click());
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    @Test
    public void editNewsWithEmptyDescription() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction textView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        textView.check(matches(withText(newsTitle)));
        Espresso.onIdle();
        onView(allOf(withId(R.id.edit_news_item_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(newsTitle)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
        onView(withId(R.id.news_item_description_text_input_edit_text)).perform(clearText());
        onView(withId(R.id.save_button)).perform(click());
        Espresso.onIdle();
        onView(withText(ru.iteco.fmhandroid.R.string.empty_fields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    @Test
    public void sortNews() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editMonth = calendar.get(Calendar.MONTH) + 2;

        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction firstTextView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        firstTextView.check(matches(withText(newsTitle)));

        onView(withId(R.id.add_news_image_view)).perform(click());
        Espresso.onIdle();
        create(newsTitle + 1, "Массаж", "Тайский массаж", year, editMonth, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle + 1))));
        Espresso.onIdle();

        ViewInteraction secondTextView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle + 1),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        secondTextView.check(matches(withText(newsTitle + 1)));

        String earlyDateText = String.format("%02d.%02d.%d", day, month, year);
        String laterDateText = String.format("%02d.%02d.%d", day, editMonth, year);

        onView(withId(R.id.news_list_recycler_view))
                .check(verifyNewsOrder(newsTitle + 1, laterDateText, newsTitle, earlyDateText));

        onView(withId(R.id.sort_news_material_button)).perform(click());
        Espresso.onIdle();

        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(newsTitle + 1))));
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .check(verifyNewsOrder(newsTitle, earlyDateText, newsTitle + 1, laterDateText));
    }


    @Test
    public void filterNews() {
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int editMonth = calendar.get(Calendar.MONTH) + 2;

        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        Espresso.onIdle();
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        ViewInteraction firstTextView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        firstTextView.check(matches(withText(newsTitle)));

        onView(withId(R.id.add_news_image_view)).perform(click());
        Espresso.onIdle();
        create(newsTitle + 1, "Зарплата", "Тайский массаж", year, editMonth, day, hour, minute);
        Espresso.onIdle();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle + 1))));
        Espresso.onIdle();

        ViewInteraction secondTextView = onView(
                allOf(withId(R.id.news_item_title_text_view), withText(newsTitle + 1),
                        withParent(withParent(withId(R.id.news_item_material_card_view))),
                        isDisplayed()));
        secondTextView.check(matches(withText(newsTitle + 1)));

        onView(withId(R.id.filter_news_material_button)).perform(click());

        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click(), clearText(), click());
        onData(allOf(is(instanceOf(String.class)), is("Массаж")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.filter_button)).perform(click());

        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(newsTitle))));

        onView(allOf(withId(R.id.news_item_title_text_view), withText(newsTitle)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.news_list_recycler_view))
                .check(checkNewsIsMissing(newsTitle + 1));
    }

    @Test
    public void createNews1() {
        logoutIfNeeded();
        performLogin(login, password);
        safeClick(R.id.all_news_text_view);
        waitForElement(R.id.edit_news_material_button, 5000);
        safeClick(R.id.edit_news_material_button);
        waitForElement(R.id.add_news_image_view, 5000);
        safeClick(R.id.add_news_image_view);
        waitForElement(R.id.news_item_category_text_auto_complete_text_view, 5000);
        create(newsTitle, "Массаж", "Тайский массаж", year, month, day, hour, minute);
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(newsTitle))));
        Espresso.onIdle();
        waitForElement(R.id.news_item_title_text_view, 5000);
        onView(withText(newsTitle)).check(matches(isDisplayed()));
    }


}


