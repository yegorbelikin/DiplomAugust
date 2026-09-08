package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import ru.iteco.fmhandroid.R;

public class MissionPage extends BaseTest {
    private final int ourMissionButton = R.id.our_mission_image_button;
    private final int ourMissionTitle = R.id.our_mission_title_text_view;
    private final int ourMissionList = R.id.our_mission_item_list_recycler_view;
    private final int ourMissionTitleText = R.id.our_mission_item_title_text_view;
    private final int ourMissionCard = R.id.our_mission_item_material_card_view;


    public void openMissionPage() {
        onView(withId(ourMissionButton)).perform(click());
        Espresso.onIdle();

    }

    public void mathMissionPage() {
        ViewInteraction imageButton = onView(withId(ourMissionTitle));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    public void scrollToMission(String titleMission) {
        onView(withId(ourMissionList))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(titleMission))));
    }

    public void matchMissionText(String titleMission) {
        ViewInteraction textView = onView(
                allOf(withId(ourMissionTitleText), withText(titleMission),
                        withParent(withParent(withId(ourMissionCard))),
                        isDisplayed()));
        textView.check(matches(withText(titleMission)));
        Espresso.onIdle();
    }

    public void openMissionCard(String titleMission) {
        onView(allOf(
                withId(ourMissionCard),
                hasDescendant(withText(titleMission)),
                isDisplayingAtLeast(90)
        )).perform(click());
    }

    public void swipeUpMissionList() {
        onView(withId(ourMissionList))
                .perform(swipeUp());
        Espresso.onIdle();

    }

    public void mathMissionDescription(String descript, String titleMission) {
        onView(allOf(
                withText(descript),
                isDescendantOfA(allOf(
                        withId(ourMissionCard),
                        hasDescendant(withText(titleMission)))),
                isDisplayed()))
                .check(matches(isDisplayed()));
        Espresso.onIdle();
    }


}