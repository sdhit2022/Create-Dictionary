package sdh.it.mindic.entities.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

import sdh.it.mindic.entities.WordsModel;
import sdh.it.mindic.viewModel.WordViewModel;

public class RecentWordsQueue {
    public static CircularFifoQueue<WordsModel> recentWordsQueue;
    private WordViewModel wordViewModel;

    public RecentWordsQueue(WordViewModel wordViewModel) {
        this.wordViewModel = wordViewModel;
//        if (recentWordsQueue == null) {
            recentWordsQueue = new CircularFifoQueue<>(10);
            List<WordsModel> list = new ArrayList<>();
            list.addAll(wordViewModel.getAllRecentWords());
            updateQueue(list);
//        }

    }

    public void addWordToQueue(WordsModel wordsModel){
        if (recentWordsQueue.isAtFullCapacity()){
            WordsModel lastWord = recentWordsQueue.poll();
            lastWord.setRecent(false);
            wordViewModel.update(lastWord);
        }
        wordsModel.setRecent(true);
        recentWordsQueue.add(wordsModel);
        wordViewModel.update(wordsModel);
    }

    public List<WordsModel> getRecentWords(){
        List<WordsModel> list = new ArrayList<>();
        list.addAll(wordViewModel.getAllRecentWords());
        updateQueue(list);
        return list;
    }
    public void updateQueue(List<WordsModel> list){
        if (recentWordsQueue.isEmpty())
              recentWordsQueue.addAll(list);
    }
}
