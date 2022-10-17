package sdh.it.mindic.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sdh.it.mindic.dao.DictionaryDao;
import sdh.it.mindic.db.DicDatabase;
import sdh.it.mindic.entities.DictionariesModel;

public class DictionaryRepository {
    private DictionaryDao dictionaryDao;
    private LiveData<List<DictionariesModel>> allLanguageList;

    public DictionaryRepository(Application application) {
        DicDatabase db = DicDatabase.getInstance(application);
        dictionaryDao =db.dictionaryDao();
        allLanguageList= dictionaryDao.getAllLanguages();
    }

    public void insert(DictionariesModel dictionariesModel){new InsertLanguageAsyncTask(dictionaryDao).execute(dictionariesModel);}
    public void update(DictionariesModel dictionariesModel){new UpdateLanguageAsyncTask(dictionaryDao).execute(dictionariesModel);}
    public void delete(DictionariesModel dictionariesModel){new DeleteLanguageAsyncTask(dictionaryDao).execute(dictionariesModel);}

    public LiveData<List<DictionariesModel>> getAllLanguages(){return allLanguageList;}
    public DictionariesModel getLanguage(int id){return dictionaryDao.getLanguage(id);}


    public static class InsertLanguageAsyncTask extends AsyncTask<DictionariesModel,Void,Void>{
        private DictionaryDao dictionaryDao;

        public InsertLanguageAsyncTask(DictionaryDao dictionaryDao) {
            this.dictionaryDao = dictionaryDao;
        }

        @Override
        protected Void doInBackground(DictionariesModel... dictionariesModels) {
            dictionaryDao.insert(dictionariesModels[0]);
            return null;
        }
    }

    public static class UpdateLanguageAsyncTask extends AsyncTask<DictionariesModel,Void,Void>{
        private DictionaryDao dictionaryDao;

        public UpdateLanguageAsyncTask(DictionaryDao dictionaryDao) {
            this.dictionaryDao = dictionaryDao;
        }

        @Override
        protected Void doInBackground(DictionariesModel... dictionariesModels) {
            dictionaryDao.update(dictionariesModels[0]);
            return null;
        }
    }

    public static class DeleteLanguageAsyncTask extends AsyncTask<DictionariesModel,Void,Void>{
        private DictionaryDao dictionaryDao;

        public DeleteLanguageAsyncTask(DictionaryDao dictionaryDao) {
            this.dictionaryDao = dictionaryDao;
        }

        @Override
        protected Void doInBackground(DictionariesModel... dictionariesModels) {
            dictionaryDao.delete(dictionariesModels[0]);
            return null;
        }
    }
}
