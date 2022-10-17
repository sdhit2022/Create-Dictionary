package sdh.it.mindic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.WordsModel;

public class DictionaryRecyclerViewAdapter extends RecyclerView.Adapter<DictionaryRecyclerViewAdapter.DicViewHolder> implements SectionIndexer {

    private List<String> mDataArray;
    private ArrayList<Integer> mSectionPositions;
    private List<WordsModel> wordsModelList=new ArrayList<>();
    private WordsListAdapter.OnItemClickListener listener;
    private WordsListAdapter.OnStarClickListener starListener;


    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = 25; i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @NonNull
    @Override
    public DicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_layout,parent,false);
        return new DicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DicViewHolder holder, int position) {
        WordsModel wordsModel = wordsModelList.get(position);
        holder.mainWords.setText(wordsModel.getWord());
        holder.translateWords.setText(wordsModel.getMeanings());
    }

    @Override
    public int getItemCount() {
        return wordsModelList.size();
    }

    public void setWordsModelList(List<WordsModel> wordsModelList) {
        this.wordsModelList = wordsModelList;
        notifyDataSetChanged();
    }

    public WordsModel getWordsModel(int position){
        return wordsModelList.get(position);
    }


    public class DicViewHolder extends RecyclerView.ViewHolder{

        public TextView mainWords,translateWords;

        public DicViewHolder(@NonNull View itemView) {
            super(itemView);
            mainWords = itemView.findViewById(R.id.word_txt);
            translateWords = itemView.findViewById(R.id.meaning_txt);

            itemView.setOnClickListener(view->{
                if (listener != null){
                    listener.onItemClick(wordsModelList.get(getAdapterPosition()));
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(WordsModel wordsModel);
    }
    public void setOnItemClickListener(WordsListAdapter.OnItemClickListener listener){ this.listener = listener;}

    public interface OnStarClickListener {
        void onStarClick(WordsModel wordsModel);
    }

    public void setOnStarClickListener(WordsListAdapter.OnStarClickListener listener) {
        this.starListener = listener;
    }
}
