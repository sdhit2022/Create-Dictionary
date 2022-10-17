package sdh.it.mindic.views;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.LanguagesModel;
import sdh.it.mindic.utils.InternetConnection;
import sdh.it.mindic.viewModel.LanguageViewModel;

public class TextTranslateActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private ImageView micIV;
    private TextInputEditText sourceEdt,translatedTV;
    private Button translateBtn;
    private FloatingActionButton backFAB;
    private TextView info;


    private static final int REQUEST_PERMISSION_CODE = 1;
    String fromLanguageCode, toLanguageCode = "";

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
        setContentView(R.layout.activity_google_translate);
        init();
    }

    private void init() {
        bindViews();

        LanguageViewModel languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        List<LanguagesModel> lanList = languageViewModel.getAllLanguages();
        String[] fromLanguages = new String[35];
        String[] toLanguages = new String[35];
//        String[] code = new String[42];
        for (int i=0;i<35;i++){
            fromLanguages[i] = lanList.get(i).getLanguage();
            toLanguages[i] = lanList.get(i).getLanguage();
//            code[i] = lanList.get(i).getCode();
        }

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        toSpinner.setAdapter(toAdapter);


        translateBtn.setOnClickListener(v -> {
            translatedTV.setText("");
            if (sourceEdt.getText().toString().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.fill_text_entry), Toast.LENGTH_SHORT).show();
            } else if (fromLanguageCode == "und") {
                Toast.makeText(this, getResources().getString(R.string.enter_source), Toast.LENGTH_SHORT).show();
            } else if (toLanguageCode == "und") {
                Toast.makeText(this, getResources().getString(R.string.enter_destination), Toast.LENGTH_SHORT).show();
            } else {
               translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
            }
        });

        micIV.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak to convert into text");
            try {
                startActivityForResult(i, REQUEST_PERMISSION_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        backFAB.setOnClickListener(v-> onBackPressed());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));
            }
        }
    }

    private void translateText(String fromLanguageCode, String toLanguageCode, String source) {
        boolean isConnected = InternetConnection.checkConnection(TextTranslateActivity.this);
        if (!isConnected){
            info.setTextColor(getResources().getColor(R.color.red));
            info.setText(getResources().getString(R.string.check_network));
            info.setVisibility(View.VISIBLE);
            return;
        }
        info.setText(getResources().getString(R.string.downloading_translator));
        info.setVisibility(View.VISIBLE);
        info.setTextColor(getResources().getColor(R.color.AF));

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
                        translatedTV.setText(s1);
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
        String[] languages = {"Arabic", "Bulgarian", "Catalan", "Chinese"
                , "Croatian", "Czech", "Danish", "Dutch",
                "English", "Farsi", "Finnish", "French", "German", "Greek",
                "Hindi", "Hungarian", "Indonesian", "Italian", "Japanese", "Korean",
                "Latvian", "Lithuanian", "Norwegian (Bokmal)", "Polish", "Portuguese (Portugal)",
                "Romanian", "Russian", "Slovak", "Slovenian", "Spanish",
                "Swedish", "Thai", "Turkish", "Ukrainian", "Vietnamese"};
        String[] cods = {"AR", "BG", "CA", "ZH", "HR", "CS", "DA",
                "NL", "EN", "FA", "FI", "FR", "DE", "EL",
                "HI", "HU", "ID", "IT", "JA", "KO", "LV", "LT", "NO", "PL",
                "PT", "RO", "RU", "SK", "SL", "ES", "SV", "TH",
                "TR", "UK", "VI"};
        return LanguageCode;
    }

    private void bindViews() {
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idTranslateBtn);
        sourceEdt = findViewById(R.id.idEdtSource);
        translatedTV = findViewById(R.id.idTVTranslated);
        backFAB = findViewById(R.id.back_fab);
        info = findViewById(R.id.download_info_txt);
    }
}