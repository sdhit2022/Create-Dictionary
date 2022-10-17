package sdh.it.mindic.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.DictionariesModel;
import sdh.it.mindic.viewModel.DictionaryViewModel;

public class DicListAdapter extends RecyclerView.Adapter<DicListAdapter.LanguageListHolder> {

    private List<DictionariesModel> dictionariesModelList = new ArrayList<>();
    private DictionaryViewModel dictionaryViewModel;
    private Activity activity;
    private DictionariesModel dictionariesModel;
    private Integer menu_id = -1;
    private OnItemClickListener listener;
    private OnMenuClickListener menuListener;
    public ImageView menu_img;


    public DicListAdapter(DictionaryViewModel dictionaryViewModel, Activity activity) {
        this.dictionaryViewModel = dictionaryViewModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LanguageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.languages_layout, parent, false);
        return new LanguageListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageListHolder holder, int position) {
        dictionariesModel = dictionariesModelList.get(position);
        holder.mainLanguage.setText(dictionariesModel.getMain_language_name());
        holder.translateLanguage.setText(dictionariesModel.getTranslate_language_name());
    }

    @Override
    public int getItemCount() {
        return dictionariesModelList.size();
    }

    public void setLanguageModelList(List<DictionariesModel> dictionariesModelList) {
        this.dictionariesModelList = dictionariesModelList;
        notifyDataSetChanged();
    }

    public DictionariesModel getLanguageModel(int position) {
        return dictionariesModelList.get(position);
    }

    public class LanguageListHolder extends RecyclerView.ViewHolder {

        public TextView mainLanguage, translateLanguage;
        PopupWindow myPopupWindow = new PopupWindow();
        public LinearLayout renameDic, deleteDic, pdfDic;


        public LanguageListHolder(@NonNull View itemView) {
            super(itemView);
            init();

        }

        private void init() {
            bindViews();

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(dictionariesModelList.get(getAdapterPosition()));
                }
            });


            // popup window
            setPopUpWindow(itemView);
            menu_img.setOnClickListener(v -> {
                myPopupWindow.showAsDropDown(v, 0, 20);
                renameDic.setOnClickListener(view -> {
                    menu_id = 1;
                    myPopupWindow.dismiss();
                    MenuListener();
                });
                deleteDic.setOnClickListener(view -> {
                    menu_id = 2;
                    myPopupWindow.dismiss();
                    MenuListener();
                    // showCustomDialog();
                });
                pdfDic.setOnClickListener(view -> {
                    menu_id = 3;
                    myPopupWindow.dismiss();
                    MenuListener();
                });

            });

        }

        private void MenuListener() {
            if (menuListener != null) {
                menuListener.onMenuClick(dictionariesModelList.get(getAdapterPosition())
                        , menu_id);
            }
        }


        private void bindViews() {
            mainLanguage = itemView.findViewById(R.id.main_lan);
            translateLanguage = itemView.findViewById(R.id.translate_lan);
            menu_img = itemView.findViewById(R.id.dictionaries_menu);
        }

        public void setPopUpWindow(View view) {
            LayoutInflater inflater = (LayoutInflater)
                    view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.popup_dictionary_menu, null);
//
            renameDic = view.findViewById(R.id.btn_rename);
            deleteDic = view.findViewById(R.id.btn_delete);
            pdfDic = view.findViewById(R.id.btn_pdf);

            myPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DictionariesModel dictionariesModel);
    }

    public interface OnMenuClickListener {
        void onMenuClick(DictionariesModel dictionariesModel1, Integer menuId);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.menuListener = listener;
    }


}
