package sdh.it.mindic.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "tbl_word")
public class WordsModel {
    @PrimaryKey(autoGenerate = true)
    private int word_id;
    private String word;
    private String meanings;
    private String description;
    private String type;
    private Integer Language_id;
    private boolean interested = false;
    private boolean recent = false;

    public WordsModel() {
    }

    public WordsModel(String word, String meanings, Integer language_id) {
        this.word = word;
        this.meanings = meanings;
        this.Language_id = language_id;
    }

    public WordsModel(String word, String meanings, String description, String type, Integer language_id) {
        this.word = word;
        this.meanings = meanings;
        this.description = description;
        this.type = type;
        this.Language_id = language_id;
    }

    public int getWord_id() {
        return word_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeanings() {
        return meanings;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }

    public Integer getLanguage_id() {
        return Language_id;
    }

    public void setLanguage_id(Integer language_id) {
        this.Language_id = language_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }
}
