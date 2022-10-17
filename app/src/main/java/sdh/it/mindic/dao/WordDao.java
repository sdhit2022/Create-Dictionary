package sdh.it.mindic.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import sdh.it.mindic.entities.WordsModel;

@Dao
public interface WordDao {
    @Insert
    void insert(WordsModel word);

    @Delete
    void delete(WordsModel word);

    @Update
    void update(WordsModel word);

    @Query("select * from tbl_word where interested = 1 order by word ")
    LiveData<List<WordsModel>> getAllWords();

    @Transaction
    @Query("select * from tbl_word where interested = 1 order by word ")
    List<WordsModel> getAllInterestedWords();

    @Query("select * from tbl_word where recent = 1 order by word ")
    List<WordsModel> getAllRecentWords();

    @Query ("select * from tbl_word WHERE  language_id = :lan_id order by word")
    LiveData<List<WordsModel>> getAllWordsByDic(Integer lan_id);

    @Query ("delete from tbl_word WHERE  language_id = :lan_id")
    void deleteAllWordsByDic(Integer lan_id);

    @Transaction
    @Query("SELECT * FROM tbl_word where language_id = :lan_id ")
    List<WordsModel> getWordsByDic(Integer lan_id);





}
