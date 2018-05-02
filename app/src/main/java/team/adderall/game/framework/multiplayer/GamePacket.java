package team.adderall.game.framework.multiplayer;

import java.nio.ByteBuffer;

public class GamePacket
{
    public final static int BUFFER_SIZE = 32;

    public final static int INDEX_SEQUENCE = 0;
    public final static int INDEX_TYPE = 8;
    public final static int INDEX_USER_ID = 9;
    public final static int INDEX_GAME_ID = 10;
    public final static int INDEX_EXTRA = 11;


    public final static byte TYPE_UNKNOWN_GAME_ID = (byte) 10;
    public final static byte TYPE_UNKNOWN_USER_ID = (byte) 11;
    public final static byte TYPE_MOVEMENT = (byte) 80;
    public final static byte TYPE_AUTHENTICATE = (byte) 55;
    public static final int TYPE_ATHENTICATION_FAILED = 8;

    private final byte[] buffer;

    public GamePacket(byte[] buffer) {
        this.buffer = buffer;
    }

    protected GamePacket() {
        this.buffer = null;
    }

    /**
     * Used to ship out details about player position.
     *
     * @param sequence
     * @param userID
     * @param gameID
     * @return
     */
    public static GamePacketMovementBuilder MovementBuilder(long sequence, int userID, int gameID) {
        return new GamePacketMovementBuilder(sequence, TYPE_MOVEMENT, userID, gameID);
    }

    /**
     * The UDP server needs to verify who you are, we send in the session token as identification.
     *
     * @param sequence
     * @param userID
     * @param gameID
     * @return
     */
    public static GamePacketAuthenticateBuilder AuthenticateBuilder(long sequence, int userID, int gameID) {
        return new GamePacketAuthenticateBuilder(sequence, TYPE_AUTHENTICATE, userID, gameID);
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
