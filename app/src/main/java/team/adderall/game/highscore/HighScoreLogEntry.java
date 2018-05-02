package team.adderall.game.highscore;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class HighScoreLogEntry implements Serializable,Comparable {
    private final long score;
    private boolean synced;
    private final long time;

    public HighScoreLogEntry(long score, boolean synced){
        this.score = score;
        this.synced = synced;
        this.time = System.nanoTime();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        HighScoreLogEntry highscore = (HighScoreLogEntry)o;
        long compareScore = this.score - highscore.getScore();
        return (int) compareScore;
    }

    public long getScore() {
        return score;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean getSynced() {
        return this.synced;
    }
}
