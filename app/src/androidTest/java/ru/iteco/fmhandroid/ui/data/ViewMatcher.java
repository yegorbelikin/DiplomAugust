package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

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
    public static ViewAction waitForText(final String text, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ожидание текста \"" + text + "\" в течение " + timeout + " мс.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                long startTime = System.currentTimeMillis();
                long endTime = startTime + timeout;
                Matcher<View> textMatcher = withText(text);

                do {
                    // Обходим дерево элементов в поиске нужного текста
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (textMatcher.matches(child) && child.isShown()) {
                            return; // Текст найден и отображен, завершаем ожидание
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50); // Пауза 50 мс перед проверкой
                } while (System.currentTimeMillis() < endTime);

                // Если время вышло, генерируем стандартное исключение Espresso
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static void waitUntilToastIsDisplayed(String text, long timeoutMs) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeoutMs;

        // Используем готовый встроенный матчер для Root, который есть в любой версии Espresso
        Matcher<Root> isToastMatcher = Matchers.is(new org.hamcrest.CustomMatcher<Root>("is toast") {
            @Override
            public boolean matches(Object item) {
                if (item instanceof Root) {
                    Root root = (Root) item;
                    int type = root.getWindowLayoutParams().get().type;
                    return type == WindowManager.LayoutParams.TYPE_TOAST;
                }
                return false;
            }
        });

        while (System.currentTimeMillis() < endTime) {
            try {
                // Ищем текст внутри найденного Toast-окна
                onView(withText(text))
                        .inRoot(isToastMatcher)
                        .check(matches(isDisplayed()));
                return; // Успешно нашли, выходим из метода
            } catch (AssertionError | Exception e) {
                // Игнорируем отсутствие элемента во время ожидания
            }
            SystemClock.sleep(100); // Пауза 100 мс перед повтором
        }

        // Если время вышло, генерируем понятную ошибку
        throw new AssertionError("Toast-сообщение '" + text + "' не появилось в течение " + timeoutMs + " мс.");
    }


    public static ViewAction waitForTextSnackbar(final String text, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ожидание текста \"" + text + "\"";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                long startTime = System.currentTimeMillis();
                long endTime = startTime + timeout;
                Matcher<View> textMatcher = withText(text);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (textMatcher.matches(child) && child.isShown()) {
                            return; // Нашли элемент на основном экране
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static void waitForSystemToast(String text, long timeoutMs) {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Ищет текст как подстроку (содержит указанную фразу)
        Pattern pattern = Pattern.compile(".*" + text + ".*", Pattern.CASE_INSENSITIVE);
        boolean isTextFound = device.wait(Until.hasObject(By.text(pattern)), timeoutMs);

        if (!isTextFound) {
            throw new AssertionError("Текст '" + text + "' не обнаружен на экране за " + timeoutMs + " мс.");
        }
    }

    public static void waitForToastOrSnackbar(String text, long timeoutMs) {
        UiDevice device = UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation()
        );
        boolean found = device.wait(
                Until.hasObject(By.text(text)),
                timeoutMs
        );
        if (!found) {
            throw new AssertionError("Сообщение '" + text + "' не найдено");
        }
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







