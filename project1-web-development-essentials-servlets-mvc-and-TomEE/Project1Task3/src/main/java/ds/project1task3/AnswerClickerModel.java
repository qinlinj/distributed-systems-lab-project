package ds;

import java.util.HashMap;

/**
 * The AnswerClickerModel class is used to store and update the answers to the question.
 */
public class AnswerClickerModel {
    // A HashMap to store the answers and the number of times each answer has been selected
    HashMap<String, Integer> answer = new HashMap<>();

    // A constructor to initialize the HashMap with 0 values for each answer option
    AnswerClickerModel(){
        answer.put("A",0);
        answer.put("B",0);
        answer.put("C",0);
        answer.put("D",0);
    }

    /**
     * The addNAnswer method is used to update the number of times a particular answer has been selected.
     * @param ans The answer that is to be updated.
     */
    public void addAnswer(String ans) {
        int count = answer.get(ans);
        answer.put(ans, count + 1);
    }
    public int getAnswerCount(String ans) {
        return answer.get(ans);
    }
}
