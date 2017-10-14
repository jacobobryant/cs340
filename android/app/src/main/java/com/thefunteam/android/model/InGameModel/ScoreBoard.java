package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class ScoreBoard {
    //list of the scores
    private List<Integer> scores;

    public ScoreBoard(List<Integer> scores) {
        this.scores = scores;
    }

    public List<Integer> getScores() {
        return scores;
    }

}
