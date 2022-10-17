package sdh.it.mindic.views;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sdh.it.mindic.R;
import sdh.it.mindic.adapters.WordsListAdapter;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.DictionaryViewModel;
import sdh.it.mindic.viewModel.WordViewModel;
import sdh.it.mindic.views.Dialogs.CustomDialog;

public class InterestedWordsActivity extends AppCompatActivity{

   // private TabLayout tabLayout;
    private WordViewModel wordViewModel;
    private MaterialToolbar topAppBar;
    private RecyclerView list;
    private TextView[] alphabet = new TextView[26];
    private FloatingActionButton backFab;
    private TextInputLayout searchEditText;
    private WordsListAdapter adapter;
    private TextView info;
    private TextToSpeech textToSpeech;
    private DictionariesModel model;



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
        setContentView(R.layout.activity_interested_words);
        init();
    }

    public void init(){
        bindViews();

        backFab.setOnClickListener(v->{
            onBackPressed();
        });

        list.setLayoutManager(new LinearLayoutManager(InterestedWordsActivity.this));
        list.setHasFixedSize(true);
        adapter = new WordsListAdapter(this);
        list.setAdapter(adapter);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getAllWords().observe(InterestedWordsActivity.this, wordModels -> {
            adapter.setWordsModelList(wordModels);
            if(wordModels.isEmpty()){
                info.setText(getResources().getString(R.string.favorite_list_empty));
                info.setVisibility(View.VISIBLE);
            }
        });
//        DictionaryViewModel dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
//        DictionariesModel dictionariesModel = dictionaryViewModel.getLanguage()

        adapter.setOnStarClickListener(wordsModel1 -> {
            CustomDialog customDialog = new CustomDialog(InterestedWordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomUpdateWordDialog(wordsModel1);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnItemClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    InterestedWordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomDetailWordDialog(wordsModel);
        });

        adapter.setOnDeleteClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    InterestedWordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomDeleteWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnEditClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    InterestedWordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomEditWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });


        adapter.setOnSpeakerClickListener(wordsModel -> {
            DictionaryViewModel dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
            model = dictionaryViewModel.getLanguage(wordsModel.getLanguage_id());
            if (model != null)
              setTextToSpeechLanguage(model.getTranslate_language_name(),wordsModel);
        });

        for (int j = 0; j < alphabet.length; j++) {
            alphabet[j].setOnClickListener(view -> {
                switch (view.getId()) {
                    case R.id.A:
                        go("A");
                        break;
                    case R.id.B:
                        go("B");
                        break;
                    case R.id.C:
                        go("C");
                        break;
                    case R.id.D:
                        go("D");
                        break;
                    case R.id.E:
                        go("E");
                        break;
                    case R.id.F:
                        go("F");
                        break;
                    case R.id.G:
                        go("G");
                        break;
                    case R.id.H:
                        go("H");
                        break;
                    case R.id.I:
                        go("I");
                        break;
                    case R.id.J:
                        go("J");
                        break;
                    case R.id.K:
                        go("K");
                        break;
                    case R.id.L:
                        go("L");
                        break;
                    case R.id.M:
                        go("M");
                        break;
                    case R.id.N:
                        go("N");
                        break;
                    case R.id.O:
                        go("O");
                        break;
                    case R.id.P:
                        go("P");
                        break;
                    case R.id.Q:
                        go("Q");
                        break;
                    case R.id.R:
                        go("R");
                        break;
                    case R.id.S:
                        go("S");
                        break;
                    case R.id.T:
                        go("T");
                        break;
                    case R.id.U:
                        go("U");
                        break;
                    case R.id.V:
                        go("V");
                        break;
                    case R.id.X:
                        go("X");
                        break;
                    case R.id.Y:
                        go("Y");
                        break;
                    case R.id.Z:
                        go("Z");
                        break;
                }
            });
        }

        searchEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void setTextToSpeechLanguage(String language,WordsModel wordsModel) {
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

    private void filter(String text) {
        List<WordsModel> filteredList = new ArrayList<>();
        List<WordsModel> List = new ArrayList<>();
        List.addAll(wordViewModel.getInterestedWordsList());

        for (WordsModel item : List) {
            if (item.getWord().toLowerCase().startsWith(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }
    private void go(String str) {
        int position;
        position = getTargetPosition(list, str);
        if (position != -1)
            list.scrollToPosition(position);
    }

    private int getTargetPosition(RecyclerView recycler, String str) {
        WordsListAdapter adapter = (WordsListAdapter) recycler.getAdapter();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getWordsModel(i).getWord().toLowerCase(Locale.ROOT).
                    startsWith(str.toLowerCase(Locale.ROOT))) {
                return i;
            }
        }
        return -1;
    }
    private void bindViews() {
        list = findViewById(R.id.list);
        topAppBar = findViewById(R.id.interested_top_app_bar);
        backFab = findViewById(R.id.back_fab);
        searchEditText = findViewById(R.id.search_word_edit_text);
        info = findViewById(R.id.info_txt);
        //region alphabet TextView definition
        alphabet[0] = findViewById(R.id.A);
        alphabet[1] = findViewById(R.id.B);
        alphabet[2] = findViewById(R.id.C);
        alphabet[3] = findViewById(R.id.D);
        alphabet[4] = findViewById(R.id.E);
        alphabet[5] = findViewById(R.id.F);
        alphabet[6] = findViewById(R.id.G);
        alphabet[7] = findViewById(R.id.H);
        alphabet[8] = findViewById(R.id.I);
        alphabet[9] = findViewById(R.id.J);
        alphabet[10] = findViewById(R.id.K);
        alphabet[11] = findViewById(R.id.L);
        alphabet[12] = findViewById(R.id.M);
        alphabet[13] = findViewById(R.id.N);
        alphabet[14] = findViewById(R.id.O);
        alphabet[15] = findViewById(R.id.P);
        alphabet[16] = findViewById(R.id.Q);
        alphabet[17] = findViewById(R.id.R);
        alphabet[18] = findViewById(R.id.S);
        alphabet[19] = findViewById(R.id.T);
        alphabet[20] = findViewById(R.id.U);
        alphabet[21] = findViewById(R.id.V);
        alphabet[22] = findViewById(R.id.W);
        alphabet[23] = findViewById(R.id.X);
        alphabet[24] = findViewById(R.id.Y);
        alphabet[25] = findViewById(R.id.Z);
        //endregion
    }
}