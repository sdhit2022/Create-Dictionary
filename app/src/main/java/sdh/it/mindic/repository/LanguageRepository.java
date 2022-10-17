package sdh.it.mindic.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import sdh.it.mindic.dao.LanguageDao;
import sdh.it.mindic.db.DicDatabase;
import sdh.it.mindic.entities.LanguagesModel;

public class LanguageRepository {
    private LanguageDao languageDao;

    public LanguageRepository(Application application) {
        DicDatabase db = DicDatabase.getInstance(application);
        languageDao=db.languageDao();
    }

    public void insert(LanguagesModel language){new LanguageRepository.InsertLanguageAsyncTask(languageDao).execute(language);}
    public List<LanguagesModel> getAllLanguages(){return languageDao.getAllLanguage();}
    public String getLanCode(String lan){return languageDao.getLanguageCode(lan);}


    private static class InsertLanguageAsyncTask extends AsyncTask<LanguagesModel,Void,Void> {
        private LanguageDao languageDao;

        public InsertLanguageAsyncTask(LanguageDao languageDao) {
            this.languageDao = languageDao;
        }

        @Override
        protected Void doInBackground(LanguagesModel... languageModels) {
            languageDao.insert(languageModels[0]);
            return null;
        }
    }
}
