package sdh.it.mindic.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.repository.WordRepository;

public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    private LiveData<List<WordsModel>> allWordsList;
    private LiveData<List<WordsModel>> allWordsListByDic;

    public WordViewModel(@NonNull Application application,Integer lan_id) {
        super(application);
        wordRepository = new WordRepository(application,lan_id);
        allWordsList = wordRepository.getAllWords();
        allWordsListByDic = wordRepository.getAllWordsByDic(lan_id);
    }

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        allWordsList = wordRepository.getAllWords();
    }

    public void insert(WordsModel wordModel){ wordRepository.insert(wordModel);}
    public void update(WordsModel wordModel){ wordRepository.update(wordModel);}
    public void delete(WordsModel wordModel){
        if (wordModel == null || wordModel.getWord_id()<0)
            return;
        wordRepository.delete(wordModel);
    }

    public LiveData<List<WordsModel>> getAllWords(){return allWordsList;}
    public List<WordsModel> getAllRecentWords(){return wordRepository.getAllRecentWords();}
    public LiveData<List<WordsModel>> getAllWordsListByDic(Integer lan_id){return allWordsListByDic;}
    public List<WordsModel> getWordsListByDic(Integer lan_id){return wordRepository.getWordsByDic(lan_id);}
    public List<WordsModel> getInterestedWordsList(){return wordRepository.getInterestedWords();}
    public void deleteAllWordsByDic(Integer lan_id){wordRepository.deleteAllWordsByDic(lan_id);}

}
