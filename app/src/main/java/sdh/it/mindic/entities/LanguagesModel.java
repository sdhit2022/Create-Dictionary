package sdh.it.mindic.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_languages")
public class LanguagesModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String language;
    private String code;

    public LanguagesModel() {
    }

    public LanguagesModel(String language, String code) {
        this.language = language;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
