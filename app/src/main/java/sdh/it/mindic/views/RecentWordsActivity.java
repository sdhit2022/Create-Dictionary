package sdh.it.mindic.views;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import sdh.it.mindic.R;
import sdh.it.mindic.adapters.WordsListAdapter;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.Queue.RecentWordsQueue;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.DictionaryViewModel;
import sdh.it.mindic.viewModel.WordViewModel;
import sdh.it.mindic.views.Dialogs.CustomDialog;

public class RecentWordsActivity extends AppCompatActivity {

    private RecyclerView recentWordsRecyclerView;
    private WordViewModel recentWordsViewModel;
    private WordsListAdapter adapter;
    private MaterialToolbar topAppBar;
    private FloatingActionButton backFab;
    private TextView info;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.MinDic);
        }
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recent_words);
        init();
    }
    private void init(){
        bindViews();
        recentWordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WordsListAdapter(this);
        recentWordsRecyclerView.setAdapter(adapter);

        recentWordsViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        RecentWordsQueue recentWordsQueue = new RecentWordsQueue(recentWordsViewModel);
        adapter.setWordsModelList(recentWordsQueue.getRecentWords());
        if(recentWordsQueue.getRecentWords().isEmpty()){
            info.setText(getResources().getString(R.string.recent_list_empty));
            info.setVisibility(View.VISIBLE);
        }

        adapter.setOnItemClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    RecentWordsActivity.this,adapter,recentWordsViewModel);
            customDialog.showCustomDetailWordDialog(wordsModel);
        });

        adapter.setOnDeleteClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    RecentWordsActivity.this,adapter,recentWordsViewModel);
            customDialog.showCustomDeleteWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnEditClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    RecentWordsActivity.this,adapter,recentWordsViewModel);
            customDialog.showCustomEditWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnStarClickListener(wordsModel1 -> {
            recentWordsViewModel.update(wordsModel1);
            adapter.notifyDataSetChanged();
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        adapter.setOnSpeakerClickListener(wordsModel -> {
            DictionaryViewModel dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
            DictionariesModel model = dictionaryViewModel.getLanguage(wordsModel.getLanguage_id());
            if (model != null)
                setTextToSpeechLanguage(model.getTranslate_language_name(),wordsModel);
        });
        backFab.setOnClickListener(v-> onBackPressed());
    }

    private void setTextToSpeechLanguage(String language, WordsModel wordsModel) {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    switch (language.toLowerCase(Locale.ROOT)) {
                        case "german":
                            textToSpeech.setLanguage(Locale.GERMANY);
                            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);

                            break;
                        case "french":
                            textToSpeech.setLanguage(Locale.FRANCE);
                            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);

                            break;
                        case "chinese":
                            textToSpeech.setLanguage(Locale.CHINA);
                            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);

                            break;
                        case "japanese":
                            textToSpeech.setLanguage(Locale.JAPAN);
                            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);

                            break;
                        default:
                            textToSpeech.setLanguage(Locale.UK);
                            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);

                    }                }
            }
        });

    }

    private void bindViews() {
        recentWordsRecyclerView = findViewById(R.id.recent_words_list);
        topAppBar = findViewById(R.id.recent_top_app_bar);
        backFab = findViewById(R.id.back_fab);
        info = findViewById(R.id.info_txt);
    }
}