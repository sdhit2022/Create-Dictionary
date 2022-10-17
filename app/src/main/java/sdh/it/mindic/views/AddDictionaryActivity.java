package sdh.it.mindic.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.LanguagesModel;
import sdh.it.mindic.viewModel.LanguageViewModel;

public class AddDictionaryActivity extends AppCompatActivity {

    public static String EXTRA_DIC_ID = "sdh.it.mindic.dicId";
    public static String EXTRA_HOME_LAN = "sdh.it.mindic.homeLanTitle";
    public static String EXTRA_DESTINATION_LAN = "sdh.it.mindic.destinationLanTitle";

    private String homeLanguage,destinationLanguage;

    private AutoCompleteTextView homeLanEditText,destinationLanEditText;
    private Button addLanBtn;
    private FloatingActionButton backFAB;
    private Spinner fromSpinner, toSpinner;


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
        setContentView(R.layout.activity_add_dictionary);
        init();
    }

    private void init(){
        bindViews();
        Intent intent = getIntent();
        LanguageViewModel languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        List<LanguagesModel> lanList = languageViewModel.getAllLanguages();
        String[] fromLan = new String[35];
        String[] toLan = new String[35];
//        String[] code = new String[42];
        for (int i=0;i<35;i++){
            fromLan[i] = lanList.get(i).getLanguage();
            toLan[i] = lanList.get(i).getLanguage();
//            code[i] = lanList.get(i).getCode();
        }


        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item, fromLan);
        homeLanEditText.setAdapter(fromAdapter);

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item, toLan);
        destinationLanEditText.setAdapter(toAdapter);

        if(intent.hasExtra(EXTRA_DIC_ID)){
            setTitle(getResources().getString(R.string.edit_dic));
            homeLanguage=intent.getExtras().getString(EXTRA_HOME_LAN);
            destinationLanguage=intent.getExtras().getString(EXTRA_DESTINATION_LAN);

            homeLanEditText.setText(homeLanguage);
            destinationLanEditText.setText(destinationLanguage);
        }else {
            setTitle(R.string.add_language);
        }
        homeLanEditText.setOnItemClickListener((parent, view, position, id) -> {
             homeLanguage = parent.getItemAtPosition(position).toString();
        });
        destinationLanEditText.setOnItemClickListener((parent, view, position, id) -> {
            destinationLanguage = parent.getItemAtPosition(position).toString();
        });
        addLanBtn.setOnClickListener(v-> {
            if (checkInputDataValidation(homeLanguage, destinationLanguage)) return;
            Intent data = new Intent();
            data.putExtra(EXTRA_HOME_LAN,homeLanguage);
            data.putExtra(EXTRA_DESTINATION_LAN,destinationLanguage);

            if (intent.hasExtra(EXTRA_DIC_ID)) {
                int id = getIntent().getExtras().getInt(EXTRA_DIC_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_DIC_ID, id);
                }
            }
            setResult(RESULT_OK,data);
            finish();
        });

        backFAB.setOnClickListener(v-> onBackPressed());



//        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//             //   fromLanguageCode = getLanguageCode(fromLanguages[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        fromAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        fromSpinner.setAdapter(fromAdapter);
//
//        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            //    toLanguageCode = getLanguageCode(lanList.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        toAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        toSpinner.setAdapter(toAdapter);


    }

    private boolean checkInputDataValidation(String homeLanguage, String destinationLanguage) {
        if ((homeLanguage == null || homeLanguage.equals("")) || destinationLanguage == null || destinationLanguage.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
       //todo if (exist)
        return false;
    }

    private void bindViews() {
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        homeLanEditText = findViewById(R.id.HomeLanguageEditText);
        destinationLanEditText = findViewById(R.id.DestinationLanguageEditText);
        addLanBtn = findViewById(R.id.btnAddLanguage);
        backFAB = findViewById(R.id.back_fab);
    }
}