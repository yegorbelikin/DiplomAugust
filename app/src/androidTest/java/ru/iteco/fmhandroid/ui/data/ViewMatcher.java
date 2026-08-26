package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import ru.iteco.fmhandroid.R;

public class ViewMatcher {
    public static ViewAction waitDisplayed(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> has been displayed during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> matchId = withId(viewId);
                final Matcher<View> matchDisplayed = isDisplayed();

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (matchId.matches(child) && matchDisplayed.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
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



        public static ViewAssertion verifyNewsOrder(
        final String expectedTopTitle, final String expectedTopDate,
        final String expectedBottomTitle, final String expectedBottomDate) {

            return (view, noViewFoundException) -> {
                if (noViewFoundException != null) throw noViewFoundException;

                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) throw new AssertionError("Адаптер RecyclerView пуст");

                int topPosition = -1;
                int bottomPosition = -1;
                int itemCount = adapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.bindViewHolder(holder, i);

                    TextView titleView = holder.itemView.findViewById(R.id.news_item_title_text_view);
                    TextView dateView = holder.itemView.findViewById(R.id.news_item_publication_date_text_view);

                    if (titleView != null && dateView != null) {
                        String currentTitle = titleView.getText().toString();

                        if (currentTitle.equals(expectedTopTitle)) {
                            topPosition = i;
                        }
                        if (currentTitle.equals(expectedBottomTitle)) {
                            bottomPosition = i;
                        }
                    }
                }

                org.junit.Assert.assertTrue("Не найдена новость сверху: " + expectedTopTitle, topPosition != -1);
                org.junit.Assert.assertTrue("Не найдена новость снизу: " + expectedBottomTitle, bottomPosition != -1);

                // Меньший индекс в RecyclerView означает, что элемент находится выше на экране
                org.junit.Assert.assertTrue(
                        "Ошибка сортировки! Новость '" + expectedTopTitle + "' должна быть выше новости '" + expectedBottomTitle + "'",
                        topPosition < bottomPosition
                );
            };
        }


    public static ViewAssertion checkNewsIsMissing(final String titleToDelete) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) throw noViewFoundException;

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) return; // Если список пуст, значит элемент точно отфильтрован

            int itemCount = adapter.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                adapter.bindViewHolder(holder, i);

                TextView titleView = holder.itemView.findViewById(R.id.news_item_title_text_view);
                if (titleView != null) {
                    String currentTitle = titleView.getText().toString();
                    if (currentTitle.equals(titleToDelete)) {
                        org.junit.Assert.fail("Фильтр не сработал! Новость '" + titleToDelete + "' всё ещё присутствует в списке.");
                    }
                }
            }
        };
    }









    }







