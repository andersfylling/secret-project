package team.adderall.game.highscore;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class HighScoreObject implements Serializable,Comparable {
    private final long score;
    private final boolean synced;
    private final long time;

    public HighScoreObject(long score, boolean synced){
        this.score = score;
        this.synced = synced;
        this.time = System.nanoTime();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        HighScoreObject highscore = (HighScoreObject)o;
        long compareScore = this.score - highscore.getScore();
        return (int) compareScore;
    }

    public long getScore() {
        return score;
    }

}
