package sdh.it.mindic.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_dic")
public class DictionariesModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String main_language_name;
    private String translate_language_name;

    public DictionariesModel(String main_language_name, String translate_language_name) {
        this.main_language_name = main_language_name;
        this.translate_language_name = translate_language_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain_language_name() {
        return main_language_name;
    }

    public void setMain_language_name(String main_language_name) {
        this.main_language_name = main_language_name;
    }

    public String getTranslate_language_name() {
        return translate_language_name;
    }

    public void setTranslate_language_name(String translate_language_name) {
        this.translate_language_name = translate_language_name;
    }
}
