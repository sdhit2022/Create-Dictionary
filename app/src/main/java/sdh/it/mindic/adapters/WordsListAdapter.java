package sdh.it.mindic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sdh.it.mindic.R;
import sdh.it.mindic.entities.WordsModel;

public class WordsListAdapter extends RecyclerView.Adapter<WordsListAdapter.WordsListHolder> {

    private List<WordsModel> wordsModelList=new ArrayList<>();
    private OnItemClickListener listener;
    private OnStarClickListener starListener;
    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;
    private OnSpeakerClickListener speakListener;
    private Context context;

    public WordsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WordsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_layout,parent,false);
        return new WordsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsListHolder holder, int position) {
        WordsModel wordsModel = wordsModelList.get(position);
        holder.mainWords.setText(wordsModel.getWord());
        holder.translateWords.setText(wordsModel.getMeanings());
        holder.typeWords.setText(wordsModel.getType());
        if (wordsModel.isInterested()){
            holder.interestWordImage.setImageDrawable(context.getResources().getDrawable(R.drawable.interested));
        }else {
            holder.interestWordImage.setImageDrawable(context.getResources().getDrawable(R.drawable.not_interested));
        }

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



    public void filterList(List<WordsModel> filteredList) {
        wordsModelList = filteredList;
        notifyDataSetChanged();
    }

    public class WordsListHolder extends RecyclerView.ViewHolder{

        public TextView mainWords,translateWords,typeWords;
        public ImageView interestWordImage,deleteImage,editImage,speakerImage;

        public WordsListHolder(@NonNull View itemView) {
            super(itemView);
            mainWords = itemView.findViewById(R.id.word_txt);
            translateWords = itemView.findViewById(R.id.meaning_txt);
            typeWords = itemView.findViewById(R.id.type_txt);
            interestWordImage = itemView.findViewById(R.id.interest_word_img);
            deleteImage = itemView.findViewById(R.id.delete_word_img);
            editImage = itemView.findViewById(R.id.edite_word_img);
            speakerImage = itemView.findViewById(R.id.speaker_img);


            itemView.setOnClickListener(view->{
                if (listener != null){
                    listener.onItemClick(wordsModelList.get(getAdapterPosition()));
                }
            });

            deleteImage.setOnClickListener(view->{
                if (deleteListener != null){
                    deleteListener.onDeleteClick(wordsModelList.get(getAdapterPosition()));
                }
            });

            editImage.setOnClickListener(view->{
                if (editListener != null){
                    editListener.onEditClick(wordsModelList.get(getAdapterPosition()));
                }
            });

            speakerImage.setOnClickListener(view->{
                if (speakListener != null){
                    speakListener.onSpeakerClick(wordsModelList.get(getAdapterPosition()));
                }
            });


            interestWordImage.setOnClickListener(v->{
                if (starListener != null){
                    WordsModel word;
                    word = wordsModelList.get(getAdapterPosition());
                    word.setInterested(!word.isInterested());
                    if (word.isInterested()) {
                        interestWordImage.setImageDrawable(context.getResources().getDrawable(R.drawable.interested));

                    }else {
                        interestWordImage.setImageDrawable(context.getResources().getDrawable(R.drawable.not_interested));
                    }

                    starListener.onStarClick(word);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WordsModel wordsModel);
    }
    public void setOnItemClickListener(OnItemClickListener listener){ this.listener = listener;}

    public interface OnStarClickListener {
        void onStarClick(WordsModel wordsModel);
    }

    public void setOnStarClickListener(OnStarClickListener listener) {
        this.starListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(WordsModel wordsModel);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public interface OnEditClickListener {
        void onEditClick(WordsModel wordsModel);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editListener = listener;
    }


    public interface OnSpeakerClickListener {
        void onSpeakerClick(WordsModel wordsModel);
    }

    public void setOnSpeakerClickListener(OnSpeakerClickListener listener) {
        this.speakListener = listener;
    }

}
