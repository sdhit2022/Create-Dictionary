package sdh.it.mindic.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.repository.DictionaryRepository;

public class DictionaryViewModel extends AndroidViewModel {
    private DictionaryRepository dictionaryRepository;
    private LiveData<List<DictionariesModel>> allLanguagesList;

    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        dictionaryRepository = new DictionaryRepository(application);
        allLanguagesList = dictionaryRepository.getAllLanguages();
    }

    public void update(DictionariesModel dictionariesModel){
        dictionaryRepository.update(dictionariesModel);}
    public void insert(DictionariesModel dictionariesModel){
        dictionaryRepository.insert(dictionariesModel);}
    public void delete(DictionariesModel dictionariesModel){
        if (dictionariesModel == null || dictionariesModel.getId()<0)
            return;
        dictionaryRepository.delete(dictionariesModel);
    }

    public LiveData<List<DictionariesModel>> getAllLanguagesList(){return allLanguagesList;}
    public DictionariesModel getLanguage(int id){return dictionaryRepository.getLanguage(id);}
}
