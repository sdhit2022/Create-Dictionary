package sdh.it.mindic.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WordViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Integer mlan_id;


    public WordViewModelFactory(Application application, Integer lan_id) {
        mApplication = application;
        mlan_id = lan_id;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WordViewModel(mApplication, mlan_id);
    }
}
