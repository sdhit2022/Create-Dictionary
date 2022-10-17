package sdh.it.mindic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import sdh.it.mindic.entities.LanguagesModel;

@Dao
public interface LanguageDao {
    @Insert
    void insert(LanguagesModel languagesModel);

    @Transaction
    @Query("select * from tbl_languages ")
    List<LanguagesModel> getAllLanguage();

    @Transaction
    @Query("select code from tbl_languages where language= :language")
    String getLanguageCode(String language);

}
