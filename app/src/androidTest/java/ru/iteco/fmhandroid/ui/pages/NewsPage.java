package ru.iteco.fmhandroid.ui.pages;

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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.checkNewsIsMissing;
import static ru.iteco.fmhandroid.ui.data.ViewMatcher.verifyNewsOrder;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;

public class NewsPage extends BaseTest {

    private final int allNewsTextView = R.id.all_news_text_view;
    private final int editNewsMaterialButton = R.id.edit_news_material_button;
    private final int addNewsImageView = R.id.add_news_image_view;
    private final int newsItemCategory = R.id.news_item_category_text_auto_complete_text_view;
    private final int newsItemTitle = R.id.news_item_title_text_input_edit_text;
    private final int newsItemPublishDate = R.id.news_item_publish_date_text_input_edit_text;

    private final int newsItemTime = R.id.news_item_publish_time_text_input_edit_text;

    private final int newsItemDescription = R.id.news_item_description_text_input_edit_text;
    private final int buttonOK = android.R.id.button1;
    private final int buttonCancel = android.R.id.button2;
    private final int saveButton = R.id.save_button;

    private final int newsList = R.id.news_list_recycler_view;
    private final int newsItemTitleText = R.id.news_item_title_text_view;
    private final int deleteNewsItem = R.id.delete_news_item_image_view;
    private final int newsItemMaterialCard = R.id.news_item_material_card_view;
    private final int errorEmptyFields = ru.iteco.fmhandroid.R.string.empty_fields;
    private final String errorPastTime = "Установленное время уже прошло";
    private final int editNewsItem = R.id.edit_news_item_image_view;

    private final int sortNewsButton = R.id.sort_news_material_button;

    private final int filterNewsButton = R.id.filter_news_material_button;
    private final int filterButton = R.id.filter_button;


    public void clickAllNews() {
        safeClick(allNewsTextView);
    }

    public void clickEditNews() {
        waitForElement(editNewsMaterialButton, 5000);
        safeClick(editNewsMaterialButton);
    }

    public void clickAddNews() {
        waitForElement(addNewsImageView, 5000);
        safeClick(addNewsImageView);
    }


    public void setNewsItemCategory(String category) {
        waitForElement(newsItemCategory, 5000);
        onView(withId(newsItemCategory))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
    }

    public void setNewsItemCategoryWithClear(String category) {
        waitForElement(newsItemCategory, 5000);
        onView(withId(newsItemCategory))
                .perform(click(), clearText(), click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onIdle();
    }

    public void setNewsEmptyCategory() {
        onView(withId(newsItemCategory)).perform(click(), clearText(), closeSoftKeyboard());
    }


    public void setNewsTitle(String title) {
        safeType(newsItemTitle, title);
    }

    public void clearNewsTitle() {
        onView(withId(newsItemTitle)).perform(clearText());
    }


    public void setNewsDate(int year, int month, int day) {
        onView(withId(newsItemPublishDate)).perform(click());
        onView(withClassName(equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        Espresso.onIdle();

    }

    public void clearNewsDate() {
        onView(withId(newsItemPublishDate)).perform(clearText());
    }

    public void clickOKInDialog() {
        onView(withId(buttonOK)).perform(click());
    }

    public void setNewsTime(int hour, int minute) {
        waitForElement(newsItemCategory, 5000);
        onView(withId(newsItemTime)).perform(click());
        onView(withClassName(equalTo(android.widget.TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        Espresso.onIdle();
    }

    public void clearNewsTime() {
        onView(withId(newsItemTime)).perform(clearText());
    }

    public void setNewsDescription(String description) {
        waitForElement(newsItemCategory, 5000);
        safeType(newsItemDescription, description);
    }

    public void clearNewsDescription() {
        onView(withId(newsItemDescription)).perform(clearText());
    }

    public void clickSaveButton() {
        safeClick(saveButton);
    }

    public void scrollToNews(String title) {
        waitForElement(newsList, 5000);
        onView(withId(newsList))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(title))));
        Espresso.onIdle();
    }

    public void matchNews(String title) {
        waitForElement(newsItemTitleText, 5000);
        onView(withText(title)).check(matches(isDisplayed()));
    }

    public void clickToDeleteItem(String title) {
        onView(allOf(
                withId(deleteNewsItem),
                isDescendantOfA(allOf(
                        withId(newsItemMaterialCard),
                        hasDescendant(withText(title)))),
                isDisplayed()
        )).perform(click());
        Espresso.onIdle();
    }

    public void clickToOKButtonInDeleteItem() {
        onView(withId(buttonOK))
                .inRoot(isDialog())
                .perform(click());
    }

    public void noMatchNews(String title) {
        onView(withText(title)).check(doesNotExist());
    }

    public void clickToCancelButtonInDeleteItem() {
        onView(withId(buttonCancel))
                .inRoot(isDialog())
                .perform(click());
        Espresso.onIdle();
    }

    public void matchEmptyFields() {
        onView(withText(errorEmptyFields))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }


    public void matchPastTime() {
        onView(withText(errorPastTime))
                .inRoot(org.hamcrest.Matchers.not(androidx.test.espresso.matcher.RootMatchers.isFocusable()))
                .check(matches(isDisplayed()));
    }

    public void clickToEditeItem(String title) {
        onView(allOf(withId(editNewsItem),
                isDescendantOfA(allOf(
                        withId(newsItemMaterialCard),
                        hasDescendant(withText(title)))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
    }

    public void openNewsCard(String title) {
        onView(allOf(
                withId(newsItemMaterialCard),
                hasDescendant(allOf(
                        withId(newsItemTitleText),
                        withText(title))),
                isDisplayed()))
                .perform(click());
        Espresso.onIdle();
    }

    public void matchNewsDescription(String description, String title) {
        onView(allOf(
                withText(description),
                isDescendantOfA(allOf(
                        withId(newsItemMaterialCard),
                        hasDescendant(withText(title)))),
                isDisplayed()))
                .check(matches(isDisplayed()));
        Espresso.onIdle();
    }

    public void checkSortLaterEarly(String laterTitle, String laterDate, String EarlyTitle, String EarlyDate) {
        onView(withId(newsList))
                .check(verifyNewsOrder(laterTitle, laterDate, EarlyTitle, EarlyDate));
    }

    public void clickSortButton() {
        onView(withId(sortNewsButton)).perform(click());
        Espresso.onIdle();
    }

    public void clickFilterNewsButton() {
        onView(withId(filterNewsButton)).perform(click());
        Espresso.onIdle();
    }

    public void clickFilterButton() {
        onView(withId(filterButton)).perform(click());
    }

    public void matchMissingNews(String title) {
        onView(withId(newsList))
                .check(checkNewsIsMissing(title));
    }
}
