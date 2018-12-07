package shein.by.expensecounter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("shein.by.expensecounter", appContext.getPackageName());
    }


    @Test
    public void saveBugdetTest() {
        SharedPreference sf = getSharedPreferences(ConstainsPreference.APP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(ConstainsPreference.YOUR_DEBTS_BUDGET, 0 + "");
        ed.commit();
    }
}

