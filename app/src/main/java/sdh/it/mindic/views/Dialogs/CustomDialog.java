package sdh.it.mindic.views.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import sdh.it.mindic.R;
import sdh.it.mindic.adapters.DicListAdapter;
import sdh.it.mindic.adapters.WordsListAdapter;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.entities.Queue.RecentWordsQueue;
import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.DictionaryViewModel;
import sdh.it.mindic.viewModel.WordViewModel;

public class CustomDialog {
    private Context context;
    private WordsListAdapter adapter;
    private DicListAdapter lanAdapter;
    private RecyclerView.ViewHolder viewHolder;
    private WordViewModel wordViewModel;
    private DictionaryViewModel dictionaryViewModel;
    private String selectedChip = "";


    public CustomDialog(Context context, WordsListAdapter adapter, RecyclerView.ViewHolder viewHolder, WordViewModel wordViewModel) {
        this.context = context;
        this.adapter = adapter;
        this.viewHolder = viewHolder;
        this.wordViewModel = wordViewModel;
    }

    public CustomDialog(Context context, DicListAdapter adapter, RecyclerView.ViewHolder viewHolder, DictionaryViewModel dictionaryViewModel) {
        this.context = context;
        this.lanAdapter = adapter;
        this.viewHolder = viewHolder;
        this.dictionaryViewModel = dictionaryViewModel;
    }

    public CustomDialog(Context context, WordsListAdapter adapter, WordViewModel wordViewModel) {
        this.context = context;
        this.adapter = adapter;
        this.wordViewModel = wordViewModel;
    }

    public CustomDialog(Context context, DicListAdapter adapter, DictionaryViewModel dictionaryViewModel) {
        this.context = context;
        this.lanAdapter = adapter;
        this.dictionaryViewModel = dictionaryViewModel;
    }

    public CustomDialog(Context context, WordViewModel wordViewModel, DictionaryViewModel dictionaryViewModel) {
        this.context = context;
        this.wordViewModel = wordViewModel;
        this.dictionaryViewModel = dictionaryViewModel;
    }


    public void showCustomDeleteDialog(Integer id) {
        Button delete, cancel;
        TextView ask;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.delete_word_dialog, null);
        alert.setView(popupInputDialogView);
        ask = popupInputDialogView.findViewById(R.id.askTextView);
        delete = popupInputDialogView.findViewById(R.id.delete_word_btn);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        switch (id) {
            case 1:
                ask.setText(R.string.delete_word);
                final AlertDialog dialog = alert.create();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                delete.setOnClickListener(view -> {
                    wordViewModel.delete(adapter.getWordsModel(viewHolder.getAdapterPosition()));
                    Toast.makeText(context, R.string.word_deleted, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    adapter.notifyDataSetChanged();
                });
                adapter.notifyDataSetChanged();
                dialog.show();
                break;
            case 2:
                ask.setText(R.string.delete_dictionary);
                final AlertDialog dicDialog = alert.create();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dicDialog.cancel();
                    }
                });
                delete.setOnClickListener(view -> {
                    dictionaryViewModel.delete(lanAdapter.getLanguageModel(viewHolder.getAdapterPosition()));
                    Toast.makeText(context, R.string.dictionary_deleted, Toast.LENGTH_SHORT).show();
                    dicDialog.cancel();
                    lanAdapter.notifyDataSetChanged();
                });
                lanAdapter.notifyDataSetChanged();
                dicDialog.show();
                break;
        }

    }

    public void showCustomDeleteWordDialog(WordsModel wordsModel) {
        Button delete, cancel;
        TextView ask;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.delete_word_dialog, null);
        alert.setView(popupInputDialogView);
        ask = popupInputDialogView.findViewById(R.id.askTextView);
        delete = popupInputDialogView.findViewById(R.id.delete_word_btn);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        ask.setText(R.string.delete_word);
        final AlertDialog dialog = alert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        delete.setOnClickListener(view -> {
            wordViewModel.delete(wordsModel);
            Toast.makeText(context, R.string.word_deleted, Toast.LENGTH_SHORT).show();
            dialog.cancel();
            adapter.notifyDataSetChanged();
        });
        adapter.notifyDataSetChanged();
        dialog.show();
    }

    public void showCustomUpdateWordDialog(WordsModel wordsModel) {
        Button delete, cancel;
        TextView ask;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.delete_word_dialog, null);
        alert.setView(popupInputDialogView);
        ask = popupInputDialogView.findViewById(R.id.askTextView);
        delete = popupInputDialogView.findViewById(R.id.delete_word_btn);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        delete.setText(R.string.delete);
        ask.setText(R.string.delete_from_favorite);
        final AlertDialog dialog = alert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        delete.setOnClickListener(view -> {
            wordViewModel.update(wordsModel);
            Toast.makeText(context, R.string.done, Toast.LENGTH_SHORT).show();
            dialog.cancel();
            adapter.notifyDataSetChanged();
        });
        adapter.notifyDataSetChanged();
        dialog.show();
    }


    public void showCustomDeleteLanguageDialog(DictionariesModel dictionariesModel) {
        Button delete, cancel;
        TextView ask;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.delete_word_dialog, null);


        alert.setView(popupInputDialogView);
        ask = popupInputDialogView.findViewById(R.id.askTextView);
        delete = popupInputDialogView.findViewById(R.id.delete_word_btn);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        ask.setText(R.string.delete_dictionary);
        final AlertDialog dialog = alert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        delete.setOnClickListener(view -> {
            dictionaryViewModel.delete(dictionariesModel);
            wordViewModel.deleteAllWordsByDic(dictionariesModel.getId());
            Toast.makeText(context, R.string.dictionary_deleted, Toast.LENGTH_SHORT).show();
            dialog.cancel();

        });

        dialog.show();
    }


//    public void showCustomAddWordDialog(int lanId) {
//        ChipGroup chipGroup;
//        Chip verb, noun, adjective, adverb;
//        Button save;
//        ImageButton cancel;
//        ImageView micIV;
//        TextInputLayout mainWord, translatedWord, description;
//        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View popupInputDialogView = layoutInflater.inflate(R.layout.add_word_dialog, null);
//        alert.setView(popupInputDialogView);
//        mainWord = popupInputDialogView.findViewById(R.id.main_word);
//        translatedWord = popupInputDialogView.findViewById(R.id.translation_word);
//        description = popupInputDialogView.findViewById(R.id.description_word);
//        save = popupInputDialogView.findViewById(R.id.save_word_btn);
//        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
//        chipGroup = popupInputDialogView.findViewById(R.id.chipGroup);
//        verb = chipGroup.findViewById(R.id.verb_chip);
//        noun = chipGroup.findViewById(R.id.noun_chip);
//        adjective = chipGroup.findViewById(R.id.adjective_chip);
//        adverb = chipGroup.findViewById(R.id.adverb_chip);
//        micIV = popupInputDialogView.findViewById(R.id.idIVMic);
//
//        final AlertDialog dialog = alert.create();
//
//        micIV.setOnClickListener(v -> {
////            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
////            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
////            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
////            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak to convert into text");
////            try {
////               startActivityForResult(i, REQUEST_PERMISSION_CODE);
////            } catch (Exception e) {
////                e.printStackTrace();
////                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
////            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        save.setOnClickListener(view -> {
//            String word = mainWord.getEditText().getText().toString();
//            String meaning = translatedWord.getEditText().getText().toString();
//            String description_str = description.getEditText().getText().toString();
//            if (!checkInputDataValidation(word, meaning, chipGroup)) {
//                WordsModel wordsModel = new WordsModel(word, meaning, description_str, selectedChip, lanId);
//                wordsModel.setRecent(true);
//                wordViewModel.insert(wordsModel);
//
//                RecentWordsQueue recentWordsQueue = new RecentWordsQueue(wordViewModel);
//                recentWordsQueue.addWordToQueue(wordsModel);
//
//                Toast.makeText(context, "لغت جدید اضافه شد", Toast.LENGTH_SHORT).show();
//                dialog.cancel();
//            }
//        });
//        dialog.show();
//    }


    private boolean checkInputDataValidation(String homeLanguage, String destinationLanguage, ChipGroup chipGroup) {
        if ((homeLanguage == null || homeLanguage.equals("")) || destinationLanguage == null || destinationLanguage.equals("")) {
            Toast.makeText(context, R.string.fill_fields, Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (chipGroup.getCheckedChipId()) {
            case R.id.verb_chip:
                selectedChip = context.getResources().getString(R.string.verb);
                break;
            case R.id.noun_chip:
                selectedChip = context.getResources().getString(R.string.noun);
                break;
            case R.id.adjective_chip:
                selectedChip = context.getResources().getString(R.string.adjective);
                break;
            case R.id.adverb_chip:
                selectedChip = context.getResources().getString(R.string.adverb);
                break;
            default:
                Toast.makeText(context, R.string.select_word_type, Toast.LENGTH_SHORT).show();
                return true;
        }
        //todo if (exist)
        return false;
    }

    public void showCustomDetailWordDialog(WordsModel wordsModel) {
        ChipGroup chipGroup;
        Chip verb, noun, adjective, adverb;
        ImageButton cancel;
        Button save;
        TextInputLayout mainWord, translatedWord, description;
        TextView title;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.add_word_dialog, null);
        alert.setView(popupInputDialogView);

        mainWord = popupInputDialogView.findViewById(R.id.main_word);
        translatedWord = popupInputDialogView.findViewById(R.id.translation_word);
        description = popupInputDialogView.findViewById(R.id.description_word);
        title = popupInputDialogView.findViewById(R.id.add_word_title);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        save = popupInputDialogView.findViewById(R.id.save_word_btn);
        chipGroup = popupInputDialogView.findViewById(R.id.chipGroup);
        verb = chipGroup.findViewById(R.id.verb_chip);
        noun = chipGroup.findViewById(R.id.noun_chip);
        adjective = chipGroup.findViewById(R.id.adjective_chip);
        adverb = chipGroup.findViewById(R.id.adverb_chip);

        title.setText(R.string.word_d);
        mainWord.getEditText().setText(wordsModel.getWord());
        translatedWord.getEditText().setText(wordsModel.getMeanings());
        description.getEditText().setText(wordsModel.getDescription());
        switch (wordsModel.getType()) {
            case "اسم":
            case "Noun":
                noun.setSelected(true);
                break;
            case "فعل":
            case "Verb" :
                verb.setSelected(true);
                break;
            case "صفت":
            case "Adjective" :
                adjective.setSelected(true);
                break;
            case "قید":
            case "Adverb" :
                adverb.setSelected(true);
                break;

        }

        save.setVisibility(View.GONE);
        RecentWordsQueue recentWordsQueue = new RecentWordsQueue(wordViewModel);
        recentWordsQueue.addWordToQueue(wordsModel);
        wordsModel.setRecent(true);
        wordViewModel.update(wordsModel);


        final AlertDialog dialog = alert.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void showCustomEditWordDialog(WordsModel wordsModel) {
        ChipGroup chipGroup;
        Chip verb, noun, adjective, adverb;
        ImageButton cancel;
        Button save;
        TextInputLayout mainWord, translatedWord, description;
        TextView title;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupInputDialogView = layoutInflater.inflate(R.layout.add_word_dialog, null);
        alert.setView(popupInputDialogView);

        mainWord = popupInputDialogView.findViewById(R.id.main_word);
        translatedWord = popupInputDialogView.findViewById(R.id.translation_word);
        description = popupInputDialogView.findViewById(R.id.description_word);
        title = popupInputDialogView.findViewById(R.id.add_word_title);
        cancel = popupInputDialogView.findViewById(R.id.cancel_word_btn);
        save = popupInputDialogView.findViewById(R.id.save_word_btn);
        chipGroup = popupInputDialogView.findViewById(R.id.chipGroup);
        verb = chipGroup.findViewById(R.id.verb_chip);
        noun = chipGroup.findViewById(R.id.noun_chip);
        adjective = chipGroup.findViewById(R.id.adjective_chip);
        adverb = chipGroup.findViewById(R.id.adverb_chip);

        title.setText(R.string.word_d);
        mainWord.getEditText().setText(wordsModel.getWord());
        translatedWord.getEditText().setText(wordsModel.getMeanings());
        description.getEditText().setText(wordsModel.getDescription());
        Integer wordId = wordsModel.getWord_id();
        boolean interested = wordsModel.isInterested();
        wordsModel.setRecent(true);
        wordViewModel.update(wordsModel);
        switch (wordsModel.getType()) {
            case "اسم":
            case "Noun":
                chipGroup.check(R.id.noun_chip);
                break;
            case "فعل":
            case "Verb":
                chipGroup.check(R.id.verb_chip);
                break;
            case "صفت":
            case "Adjective":
                chipGroup.check(R.id.adjective_chip);
                break;
            case "قید":
            case "Adverb":
                chipGroup.check(R.id.adverb_chip);
                break;
        }

//
        RecentWordsQueue recentWordsQueue = new RecentWordsQueue(wordViewModel);
        recentWordsQueue.addWordToQueue(wordsModel);

        final AlertDialog dialog = alert.create();
        cancel.setOnClickListener(v -> dialog.cancel());

        save.setOnClickListener(view -> {
            String word = mainWord.getEditText().getText().toString();
            String meaning = translatedWord.getEditText().getText().toString();
            String description_str = description.getEditText().getText().toString();
            if (!checkInputDataValidation(word, meaning, chipGroup)) {
                WordsModel updateWord = new WordsModel();
                updateWord.setWord_id(wordId);
                updateWord.setWord(word);
                updateWord.setMeanings(meaning);
                updateWord.setType(selectedChip);
                updateWord.setInterested(interested);
                updateWord.setDescription(description_str);
                updateWord.setLanguage_id(wordsModel.getLanguage_id());
                updateWord.setRecent(true);
                wordViewModel.update(updateWord);
                Toast.makeText(context, R.string.word_updated, Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
