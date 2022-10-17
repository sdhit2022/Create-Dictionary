package sdh.it.mindic.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import sdh.it.mindic.entities.DictionariesModel;

@Dao
public interface DictionaryDao {
    @Insert
    void insert(DictionariesModel dictionariesModel);

    @Delete
    void delete(DictionariesModel dictionariesModel);

    @Update
    void update(DictionariesModel dictionariesModel);

    @Query("select * from tbl_dic order by id")
    LiveData<List<DictionariesModel>> getAllLanguages();

    @Transaction
    @Query("select * from tbl_dic where id= :lanId")
    DictionariesModel getLanguage(int lanId);
}
