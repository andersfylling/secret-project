package team.adderall.game.framework.multiplayer;

import java.util.Locale;

public class Packet {
    public final static int VERSION = 1;
    public final static int TYPE_REGISTER = 0;
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

    private long sequence;
    private final int type;
    private final double x;
    private final double y;
    private final boolean jumping;
    private final long userID;
    private final long gameID;

    private final long data;

    /**
     * Create an outgoing game packet
     * @param type
     * @param x
     * @param y
     * @param jumping
     * @param userID
     * @param gameID
     */
    public Packet(int type, double x, double y, boolean jumping, long userID, long gameID) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.jumping = jumping;
        this.userID = userID;
        this.gameID = gameID;
        this.data = 0;
    }

    public Packet(int type, Long data) {
        this.type = type;
        this.data = data;

        this.x = 0;
        this.y = 0;
        this.jumping = false;
        this.userID = 0;
        this.gameID = 0;
    }

    /**
     * Parse an incoming game packet
     * @param event
     */
    public Packet(final long event) {
        type = (int) (event & typeRange);

        if ((type & (TYPE_PLAYER_MOVED | TYPE_REGISTER)) > 0) {
            x = (int) ((event & xRange) >> xOffset);
            y = (int) ((event & yRange) >> yOffset);
            jumping = ((event & jumpingRange) >> jumpingOffset) == 1;
            userID = (event & userIDRange) >> userIDOffset;
            gameID = (event & gameIDRange) >> gameIDOffset;
            data = 0;
        } else {
            x = 0;
            y = 0;
            jumping = false;
            userID = 0;
            gameID = 0;
            data = 0;
        }
    }


    public int getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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
        if (type == TYPE_PLAYER_MOVED) {
            long packetType = getType();
            long x = (long) getX();
            long y = (long) getY();
            long jumping = isJumping() ? 1L : 0L;
            long userID = getUserID();
            long gameID = getGameID();

            return packetType | x << 2 | y << 17 | jumping << 32 | userID << 33 | gameID << 37;
        }
        else if (type == TYPE_REGISTER) {
            return getType() | data << 2;
        }


        return 0;
    }

    @Override
    public String toString() {
        String jumping = "`in air`";
        if (!isJumping()) {
            jumping = "`ground`";
        }

        return String.format(Locale.ENGLISH, "packet(%d){x:%d, y:%d, %s, user: %d, game: %d}", getType(), (long) getX(), (long) getY(), jumping, getUserID(), getGameID());
    }
}
