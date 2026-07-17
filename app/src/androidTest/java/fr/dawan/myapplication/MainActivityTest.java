package fr.dawan.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.dawan.myapplication.ui.MainActivity;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void presenceMap() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void epingleClickable() {
        // Très compliqué car interprété comme des pixels sur la map
        // JE dois rappeller et mocker l'action plutôt que le click car pas d'ID dans le layout XML
    }
}