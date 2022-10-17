package sdh.it.mindic.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import sdh.it.mindic.dao.DictionaryDao;
import sdh.it.mindic.dao.LanguageDao;
import sdh.it.mindic.dao.WordDao;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.LanguagesModel;
import sdh.it.mindic.entities.WordsModel;

@Database(entities = {WordsModel.class,
        DictionariesModel.class,
        LanguagesModel.class} ,version = 2,exportSchema = false)
public abstract class DicDatabase extends RoomDatabase {

    public static DicDatabase instance;
    public abstract WordDao wordDao();
    public abstract DictionaryDao dictionaryDao();
    public abstract LanguageDao languageDao();


    public synchronized static DicDatabase getInstance(Context context) {
        if (instance ==null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DicDatabase.class,
                    "DicDataBase").fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }
}
