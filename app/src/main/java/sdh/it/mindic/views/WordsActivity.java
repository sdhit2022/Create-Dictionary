package sdh.it.mindic.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import sdh.it.mindic.entities.Queue.RecentWordsQueue;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.WordViewModel;
import sdh.it.mindic.viewModel.WordViewModelFactory;
import sdh.it.mindic.views.Dialogs.CustomDialog;

public class WordsActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EXTRA_LAN_ID = "language_id";
    public static String EXTRA_Home_LAN = "home_language";
    public static String EXTRA_DESTINATION_LAN = "translation_language";

    public static String EXTRA_MAIN_WORD = "translation_language";
    public static String EXTRA_TRANSLATE = "translation_language";

    private MaterialToolbar topAppBar;
    private FloatingActionButton addFab;
    private RecyclerView wordsListRecyclerView;
    private WordViewModel wordViewModel;
    private List<WordsModel> wordsList;
    private Integer lanId;
    private TextView[] alphabet = new TextView[26];
    private LinearLayout alphabetList;
    private TextInputLayout searchEditText;
    private ImageView searchImage;
    private WordsListAdapter adapter;
    private TextView info;
    private TextToSpeech textToSpeech;
    private int count;
    private String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";



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
        setContentView(R.layout.activity_words);
        init();
    }

    private void init() {
        bindViews();
        Bundle b = getIntent().getExtras();
        lanId = b.getInt(EXTRA_LAN_ID);
        String title1 = b.getString(EXTRA_Home_LAN);
        String title2 = b.getString(EXTRA_DESTINATION_LAN);

        wordsListRecyclerView.setLayoutManager(new LinearLayoutManager(WordsActivity.this));
        wordsListRecyclerView.setHasFixedSize(true);
        adapter = new WordsListAdapter(this);
        wordsListRecyclerView.setAdapter(adapter);

        wordViewModel = new ViewModelProvider(WordsActivity.this, new WordViewModelFactory(
                getApplication(), lanId)).get(WordViewModel.class);
        wordViewModel.getAllWordsListByDic(lanId).observe(WordsActivity.this, wordModels -> {
            adapter.setWordsModelList(wordModels);
            if(wordModels.isEmpty()){
                info.setText(getResources().getString(R.string.words_list_empty));
                String title = title1 + "-" + title2  ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    topAppBar.setTitle(title);
                    topAppBar.setSubtitle(String.valueOf(count));
                    topAppBar.setSubtitleCentered(true);
                    topAppBar.setSubtitleTextColor(getResources().getColor(R.color.red));
                }
                info.setVisibility(View.VISIBLE);
            }else {
                count = wordModels.size();
                String title = title1 + "-" + title2  ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    topAppBar.setTitle(title);
                    topAppBar.setSubtitle(String.valueOf(count));
                    topAppBar.setSubtitleCentered(true);
                    topAppBar.setSubtitleTextColor(getResources().getColor(R.color.red));
                }
            }
        });




        RecentWordsQueue recentWordsQueue = new RecentWordsQueue(wordViewModel);
        recentWordsQueue.updateQueue(wordViewModel.getAllRecentWords());


        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(WordsActivity.this,AddWordActivity.class);
            intent.putExtra("language_id",lanId);
            startActivity(intent);

        });

        topAppBar.setNavigationOnClickListener(v -> onBackPressed());

        for (int j = 0; j < alphabet.length; j++) {
            alphabet[j].setOnClickListener(this);
        }
        /**
         * we use starClickListener in wordsListAdapter
         * for mark word as interested word
         * when user click on star element
         */
        adapter.setOnStarClickListener(wordsModel1 -> {
            wordViewModel.update(wordsModel1);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnItemClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    WordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomDetailWordDialog(wordsModel);
        });

        adapter.setOnDeleteClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    WordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomDeleteWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnEditClickListener(wordsModel -> {
            CustomDialog customDialog = new CustomDialog(
                    WordsActivity.this,adapter,wordViewModel);
            customDialog.showCustomEditWordDialog(wordsModel);
            adapter.notifyDataSetChanged();
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
//                    if(!isPackageInstalled(getPackageManager(), googleTtsPackage))
//                        confirmDialog();
//                    else {
//                        textToSpeech.setEngineByPackageName(googleTtsPackage);
//                    }
                        // To Choose language of speech
                        switch (title2.toLowerCase(Locale.ROOT)) {
                            case "german":
                                textToSpeech.setLanguage(Locale.GERMANY);
                                break;
                            case "french":
                                textToSpeech.setLanguage(Locale.FRANCE);
                                break;
                            case "chinese":
                                textToSpeech.setLanguage(Locale.CHINA);
                                break;
                            case "japanese":
                                textToSpeech.setLanguage(Locale.JAPAN);
                                break;
                            default:
                                textToSpeech.setLanguage(Locale.UK);

                        }
                    }
                }

        });
        adapter.setOnSpeakerClickListener(wordsModel -> {
            textToSpeech.speak(wordsModel.getMeanings(),TextToSpeech.QUEUE_FLUSH,null);
        });

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

    private void confirmDialog(){
        AlertDialog.Builder d = new AlertDialog.Builder(WordsActivity.this);
        d.setTitle("Install recommeded speech engine?");
        d.setMessage("Your device isn't using the recommended speech engine. Do you wish to install it?");
        d.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int arg1){
                Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installVoice);
            }});
        d.setNegativeButton("No, later", new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int arg1){
                if(isPackageInstalled(getApplicationContext().getPackageManager(), picoPackage))
                    textToSpeech.setEngineByPackageName(picoPackage);

            }
        });
        d.show();
    }

    public static boolean isPackageInstalled(PackageManager pm, String packageName) {
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void filter(String text) {
        List<WordsModel> filteredList = new ArrayList<>();
        List<WordsModel> List = new ArrayList<>();
        List.addAll(wordViewModel.getWordsListByDic(lanId));

        for (WordsModel item : List) {
            if (item.getWord().toLowerCase().startsWith(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if(wordViewModel.getWordsListByDic(lanId).isEmpty()){
            info.setText(getResources().getString(R.string.words_list_empty));
            info.setVisibility(View.VISIBLE);
        }else
            info.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        //region switch alphabet id
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
        //endregion
    }

    private void go(String str) {
        int position;
        position = getTargetPosition(wordsListRecyclerView, str);
        if (position != -1)
            wordsListRecyclerView.scrollToPosition(position);
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
        addFab = findViewById(R.id.add_word_fab);
        wordsListRecyclerView = findViewById(R.id.words_list_recycler_view);
        topAppBar = findViewById(R.id.word_top_app_bar);
        alphabetList = findViewById(R.id.alphabet_linear_layout);
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