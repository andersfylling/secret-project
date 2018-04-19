package team.adderall.game.framework.multiplayer;

import java.util.Locale;

public class Packet {
    public final static int VERSION = 1;
    public final static int TYPE_PLAYER_MOVED = 2;

    private final long typeRange    = 0b0000000000000000000000000000000000000000000000000000000000000011L;
    private final long xRange       = 0b0000000000000000000000000000000000000000000000011111111111111100L;
    private final long yRange       = 0b0000000000000000000000000000000011111111111111100000000000000000L;
    private final long jumpingRange = 0b0000000000000000000000000000000100000000000000000000000000000000L;
    private final long userIDRange  = 0b0000000000000000000000000000111000000000000000000000000000000000L;
    private final long gameIDRange  = 0b0000000000000000111111111111000000000000000000000000000000000000L;
    private final long extraRange   = 0b1111111111111111000000000000000000000000000000000000000000000000L;

    private final long typeOffset = 0;
    private final long xOffset = 2;
    private final long yOffset = 17;
    private final long jumpingOffset = 32;
    private final long userIDOffset = 33;
    private final long gameIDOffset = 37;
    private final long extraOffset = 50;

    private final int type;
    private final int x;
    private final int y;
    private final boolean jumping;
    private final long userID;
    private final long gameID;

    /**
     * Create an outgoing game packet
     * @param type
     * @param x
     * @param y
     * @param jumping
     * @param userID
     * @param gameID
     */
    public Packet(int type, int x, int y, boolean jumping, long userID, long gameID) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.jumping = jumping;
        this.userID = userID;
        this.gameID = gameID;
    }

    /**
     * Parse an incoming game packet
     * @param event
     */
    public Packet(final long event) {
        type = (int) (event & typeRange);
        x = (int) ((event & xRange) >> xOffset);
        y = (int) ((event & yRange) >> yOffset);
        jumping = ((event & jumpingRange) >> jumpingOffset) == 1;
        userID = (event & userIDRange) >> userIDOffset;
        gameID = (event & gameIDRange) >> gameIDOffset;
    }


    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isJumping() {
        return jumping;
    }

    public long getUserID() {
        return userID;
    }

    public long getGameID() {
        return gameID;
    }

    public long getAsLong() {
        long packetType = getType();
        long x = getX();
        long y = getY();
        long jumping = isJumping() ? 1L : 0L;
        long userID = getUserID();
        long gameID = getGameID();

        return packetType | x << 2 | y << 17 | jumping << 32 | userID << 33 | gameID << 37;
    }

    @Override
    public String toString() {
        String jumping = "`in air`";
        if (!isJumping()) {
            jumping = "`ground`";
        }

        return String.format(Locale.ENGLISH, "packet(%d){x:%d, y:%d, %s, user: %d, game: %d}", getType(), getX(), getY(), jumping, getUserID(), getGameID());
    }
}
