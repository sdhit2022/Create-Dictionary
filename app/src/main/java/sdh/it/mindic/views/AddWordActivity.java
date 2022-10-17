package sdh.it.mindic.views;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.Queue.RecentWordsQueue;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.utils.InternetConnection;
import sdh.it.mindic.viewModel.DictionaryViewModel;
import sdh.it.mindic.viewModel.WordViewModel;
import sdh.it.mindic.viewModel.WordViewModelFactory;

public class AddWordActivity extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Chip verb, noun, adjective, adverb;
    private Button save, translateBtn;
    private ImageButton cancel;
    private ImageView micIV;
    private TextView info;
    private TextInputLayout mainWord, translatedWord, description;
    private static final int REQUEST_PERMISSION_CODE = 1;
    String  fromLanguageCode, toLanguageCode = "";
    private String selectedChip = "";
    private WordViewModel wordViewModel;
    private DictionaryViewModel dictionaryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.MinDic);
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_add_word);
        init();
    }


    private void init() {
        bindViews();

        Intent intent = getIntent();
        int lanId = intent.getExtras().getInt("language_id");

        wordViewModel = new ViewModelProvider(AddWordActivity.this, new WordViewModelFactory(
                getApplication(), lanId)).get(WordViewModel.class);
        dictionaryViewModel = new ViewModelProvider(AddWordActivity.this).get(DictionaryViewModel.class);
        DictionariesModel dictionariesModel = dictionaryViewModel.getLanguage(lanId);
        toLanguageCode = getLanguageCode(dictionariesModel.getTranslate_language_name());
        fromLanguageCode = getLanguageCode(dictionariesModel.getMain_language_name());

        //preparing firebase translation for this Dictionary

        micIV.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak to convert into text");
            try {
                startActivityForResult(i, REQUEST_PERMISSION_CODE);
                //todo deprecated
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        translateBtn.setOnClickListener(v -> {
            boolean isConnected = InternetConnection.checkConnection(AddWordActivity.this);
            if (!isConnected){
                info.setTextColor(getResources().getColor(R.color.red));
                info.setText(getResources().getString(R.string.check_network));
                info.setVisibility(View.VISIBLE);
                return;
            }
            translatedWord.getEditText().setText("");
            if (mainWord.getEditText().getText().toString().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.fill_word_entry), Toast.LENGTH_SHORT).show();
            } else if (fromLanguageCode == "und") {
                Toast.makeText(this, getResources().getString(R.string.enter_source), Toast.LENGTH_SHORT).show();
            } else if (toLanguageCode == "und") {
                Toast.makeText(this, getResources().getString(R.string.enter_destination), Toast.LENGTH_SHORT).show();
            } else {
                translateText(mainWord.getEditText().getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(view -> {
            String word = mainWord.getEditText().getText().toString();
            String meaning = translatedWord.getEditText().getText().toString();
            String description_str = description.getEditText().getText().toString();
            if (!checkInputDataValidation(word, meaning, chipGroup)) {
                WordsModel wordsModel = new WordsModel(word, meaning, description_str, selectedChip, lanId);
                wordsModel.setRecent(true);
                wordViewModel.insert(wordsModel);

                RecentWordsQueue recentWordsQueue = new RecentWordsQueue(wordViewModel);
                recentWordsQueue.addWordToQueue(wordsModel);

                Toast.makeText(this, getResources().getString(R.string.new_word_added),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mainWord.getEditText().setText(result.get(0));
            }
        }
    }

    private void translateText(String source) {
        info.setText(getResources().getString(R.string.downloading_translator));
        info.setVisibility(View.VISIBLE);
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(fromLanguageCode)
                        .setTargetLanguage(toLanguageCode)
                        .build();

        final com.google.mlkit.nl.translate.Translator translator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(s -> {
            info.setText(getResources().getString(R.string.translating));
            translator.translate(source).addOnSuccessListener(
                    s1 -> {
                        translatedWord.getEditText().setText(s1);
                        info.setVisibility(View.GONE);
                    }
            ).addOnFailureListener(e -> {
                Toast.makeText(this, "fail to translate : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                info.setVisibility(View.GONE);
            });
        }).addOnFailureListener(f -> {
            Toast.makeText(this, "fail to download language model : " + f.getMessage(), Toast.LENGTH_SHORT).show();
            info.setVisibility(View.GONE);
        });
    }



    private String getLanguageCode(String language) {
        String LanguageCode = "";
        switch (language) {
            case "Arabic":
                LanguageCode = TranslateLanguage.ARABIC;
                break;
            case "Bulgarian":
                LanguageCode = TranslateLanguage.BULGARIAN;
                break;
            case "Catalan":
                LanguageCode = TranslateLanguage.CATALAN;
                break;
            case "Chinese":
                LanguageCode = TranslateLanguage.CHINESE;
                break;
            case "Croatian":
                LanguageCode = TranslateLanguage.CROATIAN;
                break;
            case "Czech":
                LanguageCode = TranslateLanguage.CZECH;
                break;
            case "Danish":
                LanguageCode = TranslateLanguage.DANISH;
                break;
            case "Dutch":
                LanguageCode = TranslateLanguage.DUTCH;
                break;
            case "English":
                LanguageCode = TranslateLanguage.ENGLISH;
                break;
            case "Farsi":
                LanguageCode = TranslateLanguage.PERSIAN;
                break;
            case "Finnish":
                LanguageCode = TranslateLanguage.FINNISH;
                break;
            case "French":
                LanguageCode =TranslateLanguage.FRENCH;
                break;
            case "German":
                LanguageCode = TranslateLanguage.GERMAN;
                break;
            case "Greek":
                LanguageCode = TranslateLanguage.GREEK;
                break;
            case "Hindi":
                LanguageCode = TranslateLanguage.HINDI;
                break;
            case "Hungarian":
                LanguageCode = TranslateLanguage.HUNGARIAN;
                break;
            case "Indonesian":
                LanguageCode = TranslateLanguage.INDONESIAN;
                break;
            case "Italian":
                LanguageCode = TranslateLanguage.ITALIAN;
                break;
            case "Japanese":
                LanguageCode = TranslateLanguage.JAPANESE;
                break;
            case "Korean":
                LanguageCode = TranslateLanguage.KOREAN;
                break;
            case "Latvian":
                LanguageCode = TranslateLanguage.LATVIAN;
                break;
            case "Lithuanian":
                LanguageCode = TranslateLanguage.LITHUANIAN;
                break;
            case "Norwegian":
                LanguageCode =TranslateLanguage.NORWEGIAN;
                break;
            case "Polish":
                LanguageCode = TranslateLanguage.POLISH;
                break;
            case "Portuguese":
                LanguageCode = TranslateLanguage.PORTUGUESE;
                break;
            case "Romanian":
                LanguageCode = TranslateLanguage.ROMANIAN;
                break;
            case "Russian":
                LanguageCode = TranslateLanguage.RUSSIAN;
                break;
            case "Slovak":
                LanguageCode =TranslateLanguage.SLOVAK;
                break;
            case "Slovenian":
                LanguageCode = TranslateLanguage.SLOVENIAN;
                break;
            case "Spanish":
                LanguageCode = TranslateLanguage.SPANISH;
                break;
            case "Swedish":
                LanguageCode = TranslateLanguage.SWEDISH;
                break;
            case "Thai":
                LanguageCode = TranslateLanguage.THAI;
                break;
            case "Turkish":
                LanguageCode = TranslateLanguage.TURKISH;
                break;
            case "Ukrainian":
                LanguageCode =TranslateLanguage.UKRAINIAN;
                break;
            case "Vietnamese":
                LanguageCode = TranslateLanguage.VIETNAMESE;
                break;

            default:
                LanguageCode = "und";
        }
        return LanguageCode;
    }


    private boolean checkInputDataValidation(String homeLanguage, String destinationLanguage, ChipGroup chipGroup) {
        if ((homeLanguage == null || homeLanguage.equals("")) || destinationLanguage == null || destinationLanguage.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (chipGroup.getCheckedChipId()) {
            case R.id.verb_chip:
                selectedChip = getResources().getString(R.string.verb);
                break;
            case R.id.noun_chip:
                selectedChip = getResources().getString(R.string.noun);
                break;
            case R.id.adjective_chip:
                selectedChip = getResources().getString(R.string.adjective);
                break;
            case R.id.adverb_chip:
                selectedChip = getResources().getString(R.string.adverb);
                break;
            default:
                Toast.makeText(this, getResources().getString(R.string.select_word_type), Toast.LENGTH_SHORT).show();
                return true;
        }
        //todo if (exist)
        return false;
    }

    private void bindViews() {
        mainWord = findViewById(R.id.main_word);
        translatedWord = findViewById(R.id.translation_word);
        description = findViewById(R.id.description_word);
        save = findViewById(R.id.save_word_btn);
        cancel = findViewById(R.id.cancel_word_btn);
        chipGroup = findViewById(R.id.chipGroup);
        verb = chipGroup.findViewById(R.id.verb_chip);
        noun = chipGroup.findViewById(R.id.noun_chip);
        adjective = chipGroup.findViewById(R.id.adjective_chip);
        adverb = chipGroup.findViewById(R.id.adverb_chip);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idTranslateBtn);
        info = findViewById(R.id.download_info_txt);
    }
}