package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import ru.iteco.fmhandroid.R;

public class AboutPage {
    private final int mainMenuButton = R.id.main_menu_image_button;
    private final int aboutVersionTitle = R.id.about_version_title_text_view;
    private final int aboutVersionNext = R.id.about_version_value_text_view;
    private final int aboutPrivacyPolicyText = R.id.about_privacy_policy_value_text_view;
    private final int aboutTermsText = R.id.about_terms_of_use_value_text_view;


    public void openAboutPage() {
        onView(withId(mainMenuButton)).perform(click());
        onData(anything())
                .inAdapterView(isAssignableFrom(android.widget.AdapterView.class))
                .atPosition(2).perform(click());
        Espresso.onIdle();
    }

    public void matchAboutPage() {
        ViewInteraction imageButton = onView(withId(aboutVersionTitle));
        imageButton.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.onIdle();
    }

    public void matchAboutVersion(String version) {
        ViewInteraction versionView = onView(
                allOf(withId(aboutVersionNext), withText(version),
                        isDisplayed()));
        versionView.check(matches(withText(version)));
    }


    public void matchAboutPrivacyLink(String privacyLink) {
        ViewInteraction privacyView = onView(
                allOf(withId(aboutPrivacyPolicyText), withText(privacyLink),
                        isDisplayed()));
        privacyView.check(matches(withText(privacyLink)));
    }

    public void matchAboutTermsLink(String termsLink) {
        ViewInteraction termsView = onView(
                allOf(withId(aboutTermsText), withText(termsLink),
                        isDisplayed()));
        termsView.check(matches(withText(termsLink)));
    }

}
