package sdh.it.mindic.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import sdh.it.mindic.entities.LanguagesModel;
import sdh.it.mindic.repository.LanguageRepository;

public class LanguageViewModel extends AndroidViewModel {
    
    private LanguageRepository languageRepository;
    public LanguageViewModel(@NonNull Application application) {
        super(application);
        languageRepository = new LanguageRepository(application);
    }

    public void insert(LanguagesModel languageModel){ languageRepository.insert(languageModel);}

    public List<LanguagesModel> getAllLanguages(){return languageRepository.getAllLanguages();}
    public String getLanguageCode(String lan){return languageRepository.getLanCode(lan);}


}
