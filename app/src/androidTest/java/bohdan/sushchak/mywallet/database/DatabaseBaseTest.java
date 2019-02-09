package bohdan.sushchak.mywallet.database;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import bohdan.sushchak.mywallet.data.db.MyWalletDatabase;

@RunWith(AndroidJUnit4.class)
public abstract class DatabaseBaseTest {

    MyWalletDatabase db;

    @Before
    public void createDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                MyWalletDatabase.class).build();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }
}
