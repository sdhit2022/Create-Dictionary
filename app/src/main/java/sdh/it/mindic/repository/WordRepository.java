package sdh.it.mindic.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sdh.it.mindic.dao.WordDao;
import sdh.it.mindic.db.DicDatabase;
import sdh.it.mindic.entities.WordsModel;

public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<WordsModel>> allWords;
    private LiveData<List<WordsModel>> allWordsByDic;
    private List<WordsModel> WordsByDic;

    public WordRepository(Application application,Integer lan_id) {
        DicDatabase db = DicDatabase.getInstance(application);
        wordDao=db.wordDao();
        allWordsByDic = wordDao.getAllWordsByDic(lan_id);
    }

    public WordRepository(Application application) {
        DicDatabase db = DicDatabase.getInstance(application);
        wordDao=db.wordDao();
        allWords = wordDao.getAllWords();
    }

    public void insert(WordsModel word){new InsertWordAsyncTask(wordDao).execute(word);}
    public void delete(WordsModel word){new DeleteWordAsyncTask(wordDao).execute(word);}
    public void update(WordsModel word){new UpdateWordAsyncTask(wordDao).execute(word);}
    public void deleteAllWordsByDic(Integer lan_id)
    {new DeleteWordByLanIdAsyncTask(wordDao).execute(lan_id);}

    public LiveData<List<WordsModel>> getAllWords(){return allWords;}
    public List<WordsModel> getAllRecentWords(){return wordDao.getAllRecentWords();}
    public LiveData<List<WordsModel>> getAllWordsByDic(Integer lan_id){return allWordsByDic;}
    public List<WordsModel> getWordsByDic(Integer lan_id){return wordDao.getWordsByDic(lan_id);}
    public List<WordsModel> getInterestedWords(){return wordDao.getAllInterestedWords();}




    private static class InsertWordAsyncTask extends AsyncTask<WordsModel,Void,Void>{
        private WordDao wordDao;

        public InsertWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(WordsModel... wordModels) {
            wordDao.insert(wordModels[0]);
            return null;
        }
    }

    public static class DeleteWordAsyncTask extends AsyncTask<WordsModel,Void,Void>{

        private WordDao wordDao;

        public DeleteWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(WordsModel... wordModels) {
            wordDao.delete(wordModels[0]);
            return null;
        }
    }

    public static class DeleteWordByLanIdAsyncTask extends AsyncTask<Integer,Void,Void>{

        private WordDao wordDao;

        public DeleteWordByLanIdAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            wordDao.deleteAllWordsByDic(integers[0]);
            return null;
        }
    }

    public static class UpdateWordAsyncTask extends AsyncTask<WordsModel,Void,Void>{
        private WordDao wordDao;

        public UpdateWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(WordsModel... wordModels) {
            wordDao.update(wordModels[0]);
            return null;
        }
    }
}
