package sdh.it.mindic.views;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sdh.it.mindic.R;
import sdh.it.mindic.adapters.DicListAdapter;
import sdh.it.mindic.adapters.WordsListAdapter;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.LanguagesModel;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.DictionaryViewModel;
import sdh.it.mindic.viewModel.LanguageViewModel;
import sdh.it.mindic.viewModel.WordViewModel;
import sdh.it.mindic.viewModel.WordViewModelFactory;
import sdh.it.mindic.views.Dialogs.CustomDialog;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddLanguageFab, mindDicFab, recentWordsFab;

    // Use the ExtendedFloatingActionButton to handle the
    // parent FAB
    private ExtendedFloatingActionButton mAddFab;

    // These TextViews are taken to make visible and
    // invisible along with FABs except parent FAB's action
    // name
    private TextView addLanguageActionText, mindDicActionText, recentWordsActionText;

    // to check whether sub FABs are visible or not
    private Boolean isAllFabsVisible;

    /////////////
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;


    private SwipeRefreshLayout swipeRefreshLayout;
    private DictionaryViewModel dictionaryViewModel;
    private MaterialToolbar top_app_bar;
    private DrawerLayout mainDrawer;
    private NavigationView navView;
    private ConstraintLayout homeView;
    private RecyclerView dicListRecyclerView;
    private boolean doubleBackToExit = false;
    private WordViewModel wordViewModel;
    private WordsListAdapter wordsListAdapter;
    private SwitchCompat switchToDarkStyle;
    private DicListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.MinDic);
        }
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        init();
    }


    private void init() {
        bindViews();
        drawerSettings();
        appStyleSettings();

        //for open drawer menu
        top_app_bar.setNavigationOnClickListener(view -> {
            mainDrawer.open();
        });

        //set navigation menu items job
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(view -> {
            switch (view.getItemId()) {
                case R.id.share:
                    try {
                        //share app link in app store with other installed applications in device
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage = "\nسلام دوست عزیز \nاگر برای یادگیری لغت تو یه زبان جدید مشکل داری ...\n" +
                                "اگر برای یادآوری هر لغتی که معنیش رو فراموش کردی مجبوری بری سراغ دیکشنری و یا google translate  هی بگردی و بگردی..." +
                                "\nاگر تلفظ صحیح لغت هایی که یاد گرفتی فراموش میکنی و نیاز به مرور سریع و راحت داری ..." +
                                "\nبهت پیشنهاد می کنم که حتما این اپلیکیشن رو دانلود کنی :)\n\n";
                        shareMessage = shareMessage + "http://cafebazaar.ir/app/?id=sdh.it.mindic&ref=share"  + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                    break;
                case R.id.about_us:
                    //start activity with some info about application
                    startActivity(new Intent(MainActivity.this, AboutAppActivity.class));
                    break;
                case R.id.text_translator:
                    //start activity for translate long text input
                    startActivity(new Intent(MainActivity.this, TextTranslateActivity.class));
                    break;

            }
            return true;
        });

        //when switch is on dark theme will be apply
        switchToDarkStyle.setOnClickListener(v -> {
            if (switchToDarkStyle.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                writeDarkModeIsOn(true);
                setTheme(R.style.darkTheme);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                writeDarkModeIsOn(false);
                setTheme(R.style.MinDic);


            }
        });





                //region fab settings
                // Now set all the FABs and all the action name
                // texts as GONE
                mAddLanguageFab.setVisibility(View.GONE);
        mindDicFab.setVisibility(View.GONE);
        recentWordsFab.setVisibility(View.GONE);
        addLanguageActionText.setVisibility(View.GONE);
        mindDicActionText.setVisibility(View.GONE);
        recentWordsActionText.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        mAddFab.shrink();
        // We will make all the FABs and action name texts
        // visible only when Parent FAB button is clicked So
        // we have to handle the Parent FAB button first, by
        // using setOnClickListener you can see below
        mAddFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {

                // when isAllFabsVisible becomes
                // true make all the action name
                // texts and FABs VISIBLE.
                mAddLanguageFab.show();
                mindDicFab.show();
                recentWordsFab.show();
                addLanguageActionText.setVisibility(View.VISIBLE);
                mindDicActionText.setVisibility(View.VISIBLE);
                recentWordsActionText.setVisibility(View.VISIBLE);
                // Now extend the parent FAB, as
                // user clicks on the shrinked
                // parent FAB
                mAddFab.extend();

                // make the boolean variable true as
                // we have set the sub FABs
                // visibility to GONE
                isAllFabsVisible = true;
            } else {

                // when isAllFabsVisible becomes
                // true make all the action name
                // texts and FABs GONE.
                mAddLanguageFab.hide();
                mindDicFab.hide();
                recentWordsFab.hide();
                addLanguageActionText.setVisibility(View.GONE);
                mindDicActionText.setVisibility(View.GONE);
                recentWordsActionText.setVisibility(View.GONE);
                // Set the FAB to shrink after user
                // closes all the sub FABs
                mAddFab.shrink();

                // make the boolean variable false
                // as we have set the sub FABs
                // visibility to GONE
                isAllFabsVisible = false;
            }
        });
        // below is the sample action to handle add person
        // FAB. Here it shows simple Toast msg. The Toast
        // will be shown only when they are visible and only
        // when user clicks on them

        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them

        ActivityResultLauncher<Intent> addLanResultLauncher = getAddLanIntentActivityResultLauncher();
        mAddLanguageFab.setOnClickListener(view -> {
            Intent addLanIntent = new Intent(MainActivity.this, AddDictionaryActivity.class);
            addLanResultLauncher.launch(addLanIntent);
        });
        addLanguageActionText.setOnClickListener(view -> {
            Intent addLanIntent = new Intent(MainActivity.this, AddDictionaryActivity.class);
            addLanResultLauncher.launch(addLanIntent);
        });

        mindDicFab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InterestedWordsActivity.class));
        });
        mindDicActionText.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, InterestedWordsActivity.class));
        });

        recentWordsFab.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RecentWordsActivity.class)));
        recentWordsActionText.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RecentWordsActivity.class)));
//endregion fab

        //set dictionary recycler view adapter and view model
        setDictionaryViewModel();
        //insert languages list to db
        insertLanguages();


        //refreshing view with swipe down page
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        //show specific Words list by click on every dictionary in list
        adapter.setOnItemClickListener(languageModel -> {
            Intent wordsListIntent = new Intent(MainActivity.this, WordsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(WordsActivity.EXTRA_LAN_ID, languageModel.getId());
            bundle.putString(WordsActivity.EXTRA_Home_LAN, languageModel.getMain_language_name());
            bundle.putString(WordsActivity.EXTRA_DESTINATION_LAN, languageModel.getTranslate_language_name());
            wordsListIntent.putExtras(bundle);
            startActivity(wordsListIntent);
        });

        ActivityResultLauncher<Intent> EditDicResultLauncher = getEditDicActivityResultLauncher();
        /** show menu when user click on menu image for each
         * dictionary item in list , menu contains 3 option
         * for rename ,delete and create pdf
         *
         *
         */
        adapter.setOnMenuClickListener((dictionariesModel, menuId) -> {
            wordViewModel = new ViewModelProvider(MainActivity.this, new WordViewModelFactory(getApplication(), dictionariesModel.getId())).get(WordViewModel.class);
            wordsListAdapter = new WordsListAdapter(this);
            wordViewModel.getAllWordsListByDic(dictionariesModel.getId()).observe(MainActivity.this, wordModels -> {
                wordsListAdapter.setWordsModelList(wordModels);

            });

            switch (menuId) {
                case 1:
                    Intent editIntent = new Intent(MainActivity.this, AddDictionaryActivity.class);
                    editIntent.putExtra(AddDictionaryActivity.EXTRA_DIC_ID, dictionariesModel.getId());
                    editIntent.putExtra(AddDictionaryActivity.EXTRA_HOME_LAN, dictionariesModel.getMain_language_name());
                    editIntent.putExtra(AddDictionaryActivity.EXTRA_DESTINATION_LAN, dictionariesModel.getTranslate_language_name());
                    EditDicResultLauncher.launch(editIntent);
                    break;
                case 2:
                    CustomDialog customDialog = new CustomDialog(MainActivity.this, wordViewModel, dictionaryViewModel);
                    customDialog.showCustomDeleteLanguageDialog(dictionariesModel);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    // below code is used for
                    // checking our permissions.
                    if (checkPermission()) {
                        //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermission();
                    }

                    generatePDF(dictionariesModel);
                    break;
            }
        });


    }

    private void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getApplicationContext().getResources().updateConfiguration(configuration,getApplicationContext().getResources().getDisplayMetrics());
//        Intent refresh = new Intent(this, WordsActivity.class);
//        startActivity(refresh);
//        finish();
 //       mAddFab.setText(getString(R.string.Actions));

    }

    private void setDictionaryViewModel() {
        dicListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DicListAdapter(dictionaryViewModel, this);
        dicListRecyclerView.setAdapter(adapter);

        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
        dictionaryViewModel.getAllLanguagesList().observe(this, languageModels -> {
            adapter.setLanguageModelList(languageModels);
            if (!(languageModels.size() > 0)) {
                DictionariesModel dictionariesModel = new DictionariesModel("English", "Farsi");
                dictionaryViewModel.insert(dictionariesModel);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void appStyleSettings() {
        if (readDarkModeIsOn()) {
            switchToDarkStyle.setChecked(true);
        } else {
            switchToDarkStyle.setChecked(false);
        }
    }

    private void drawerSettings() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, top_app_bar, R.string.open, R.string.close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.red));
        mainDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void insertLanguages() {
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

        LanguageViewModel languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        List<LanguagesModel> lanList = languageViewModel.getAllLanguages();
        if (lanList.size() == 0) {
            for (int i = 0; i < 35; i++) {
                LanguagesModel l = new LanguagesModel(languages[i], cods[i]);
                languageViewModel.insert(l);
            }
        }
    }

    private void writeDarkModeIsOn(boolean b) {
        SharedPreferences sharedPreferences = getSharedPreferences("DarkMode", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("DarkMode", b);
        myEdit.commit();
    }

    private boolean readDarkModeIsOn() {
        boolean darkMode = false;
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("DarkMode", MODE_APPEND);
        return sh.getBoolean("DarkMode", darkMode);
    }

    private void generatePDF(DictionariesModel dictionariesModel) {

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.dictionary1);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        List<WordsModel> wordsList = new ArrayList<>();
        wordsList.addAll(wordViewModel.getWordsListByDic(dictionariesModel.getId()));
        if (wordsList.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.there_is_not_word), Toast.LENGTH_SHORT).show();
            return;
        }
        int pageNumber = wordsList.size() / 14;
        if (wordsList.size() % 14 > 0) pageNumber++;

        PdfDocument.PageInfo[] mypageInfo = new PdfDocument.PageInfo[pageNumber];
        mypageInfo[0] = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page[] myPage = new PdfDocument.Page[pageNumber];
        myPage[0] = pdfDocument.startPage(mypageInfo[0]);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage[0].getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(25);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.red));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        // canvas.drawText("A portal for IT professionals.", 209, 100, title);
        String homeLan = dictionariesModel.getMain_language_name();
        String destinationLan = dictionariesModel.getTranslate_language_name();
        String strTitle = homeLan + "  -  " + destinationLan;
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(strTitle, pagewidth / 2, 80, title);


        int y = 250;
        int j = 0;
        int pageNum = 1;
        for (int i = 1; i <= wordsList.size(); i += 2) {
            if ((i % 13) == 0) {
                pdfDocument.finishPage(myPage[j]);
                j++;
                pageNum++;
                y = 200;
                // below line is used for setting
                // start page for our PDF file.
                mypageInfo[j] = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, pageNum).create();
                myPage[j] = pdfDocument.startPage(mypageInfo[j]);
                canvas = myPage[j].getCanvas();

            }
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(15);
            // below line is used for setting
            // our text to center of PDF.
            title.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("کلمه : " + wordsList.get(i - 1).getWord(), 160, y, title);
            canvas.drawText("معنی : " + wordsList.get(i - 1).getMeanings(), 160, y + 20, title);
            canvas.drawText("نوع : " + wordsList.get(i - 1).getType(), 160, y + 40, title);
//            if ( wordsList.get(i).getDescription() == null ||  wordsList.get(i).getDescription().equals("")) {
//                canvas.drawText("توضیحات : ندارد", pagewidth/2, y + 60, title);
//            }else {
//                canvas.drawText("توضیحات : " + wordsList.get(i).getDescription(), pagewidth/2, y + 60, title);
//            }
            canvas.drawText("------------------------", 160, y + 90, title);

            if (!((i) >= wordsList.size())) {
                // next column
                canvas.drawText("کلمه : " + wordsList.get(i).getWord(), 660, y, title);
                canvas.drawText("معنی : " + wordsList.get(i).getMeanings(), 660, y + 20, title);
                canvas.drawText("نوع : " + wordsList.get(i).getType(), 660, y + 40, title);
//                if ( wordsList.get(i+1).getDescription() == null ||  wordsList.get(i+1).getDescription().equals("")) {
//                    canvas.drawText("توضیحات : ندارد", 660, y + 60, title);
//                }else {
//                    canvas.drawText("توضیحات : " + wordsList.get(i+1).getDescription(), 660, y + 60, title);
//                }
                canvas.drawText("------------------------", 660, y + 90, title);

            }
            y += 120;


        }

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage[j]);

        // below line is used to set the name of
        // our PDF file and its path.
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dictionariesModel.getMain_language_name() + "-" + dictionariesModel.getTranslate_language_name() + ".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));
            pdfDocument.close();
            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, getResources().getString(R.string.dictionary_saved), Toast.LENGTH_SHORT).show();

            if (checkPermission()) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                Uri uriForFile = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                target.setDataAndType(uriForFile,"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                requestPermission();

            }
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private ActivityResultLauncher<Intent> getEditDicActivityResultLauncher() {
        ActivityResultLauncher<Intent> editEditResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null) return;
                Integer id = data.getIntExtra(AddDictionaryActivity.EXTRA_DIC_ID, -1);
                if (id == -1) {
                    Toast.makeText(this, getResources().getString(R.string.incorrect_info), Toast.LENGTH_SHORT).show();
                }
                String home_lan = data.getStringExtra(AddDictionaryActivity.EXTRA_HOME_LAN);
                String destination_lan = data.getStringExtra(AddDictionaryActivity.EXTRA_DESTINATION_LAN);
                DictionariesModel dictionariesModel = new DictionariesModel(home_lan, destination_lan);
                dictionariesModel.setId(id);
                dictionariesModel.setMain_language_name(home_lan);
                dictionariesModel.setTranslate_language_name(destination_lan);
                dictionaryViewModel.update(dictionariesModel);
                Toast.makeText(this, getResources().getString(R.string.dictionary_updated), Toast.LENGTH_SHORT).show();
            }
        });
        return editEditResultLauncher;
    }

    @NonNull
    private ActivityResultLauncher<Intent> getAddLanIntentActivityResultLauncher() {
        ActivityResultLauncher<Intent> addLanResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null) return;
                String home = data.getStringExtra(AddDictionaryActivity.EXTRA_HOME_LAN);
                String translate = data.getStringExtra(AddDictionaryActivity.EXTRA_DESTINATION_LAN);
                DictionariesModel dictionariesModel = new DictionariesModel(home, translate);
                dictionaryViewModel.insert(dictionariesModel);
                Toast.makeText(this, getResources().getString(R.string.dictionary_created), Toast.LENGTH_SHORT).show();
            }
        });
        return addLanResultLauncher;
    }


    @Override
    public void onBackPressed() {
// if user press BACK twice app will be close
        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExit = true;
        showSnackbar(String.valueOf(R.string.double_back));
        new Handler().postDelayed(() -> doubleBackToExit = false, 2000);
    }

    @Override
    protected void onResume() {
        mAddLanguageFab.setVisibility(View.GONE);
        mindDicFab.setVisibility(View.GONE);
        recentWordsFab.setVisibility(View.GONE);
        addLanguageActionText.setVisibility(View.GONE);
        mindDicActionText.setVisibility(View.GONE);
        recentWordsActionText.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        mAddFab.shrink();
        super.onResume();

    }

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(homeView, getResources().getString(R.string.back_pressed), Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        snackbar.setAnchorView(mAddFab);
        snackbar.setTextColor(getResources().getColor(R.color.red));
        sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
     //   sbView.setBackgroundColor(getResources().getColor());
        snackbar.show();

    }


    private void bindViews() {
        mAddFab = findViewById(R.id.add_fab);
        // FAB button
        mAddLanguageFab = findViewById(R.id.add_language_fab);
        mindDicFab = findViewById(R.id.mind_dic_fab);
        recentWordsFab = findViewById(R.id.recent_words_fab);

        // Also register the action name text, of all the
        // FABs. except parent FAB action name text
        addLanguageActionText = findViewById(R.id.add_language_action_text);
        mindDicActionText = findViewById(R.id.mind_dic_action_text);
        recentWordsActionText = findViewById(R.id.recent_words_text);


        mainDrawer = findViewById(R.id.main_drawer);
        top_app_bar = findViewById(R.id.top_app_bar);
        navView = findViewById(R.id.navigation);
        homeView = findViewById(R.id.home_view);
        dicListRecyclerView = findViewById(R.id.lan_list_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        View headerView = navView.getHeaderView(0);
        switchToDarkStyle = headerView.findViewById(R.id.dark_mode);
    //    appLanguageFa = headerView.findViewById(R.id.radio_button_fa);
   //     appLanguageEn = headerView.findViewById(R.id.radio_button_en);
    }
}