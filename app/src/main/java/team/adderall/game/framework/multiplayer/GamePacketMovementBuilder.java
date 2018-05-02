package team.adderall.game.framework.multiplayer;

import java.nio.ByteBuffer;

public class GamePacketMovementBuilder
{
    public final static int INDEX_X = 12;
    public final static int INDEX_Y = 21;
    public final static int INDEX_IN_AIR = 31;
    public final static byte IS_IN_AIR = (byte) 1;
    public final static byte IS_NOT_IN_AIR = (byte) 0;

    private ByteBuffer buffer;

    public GamePacketMovementBuilder(long sequence, byte type, int userID, int gameID) {
        buffer = ByteBuffer.allocate(GamePacket.BUFFER_SIZE);

        buffer.putLong(GamePacket.INDEX_SEQUENCE, sequence);
        buffer.put(GamePacket.INDEX_TYPE, type);
        buffer.put(GamePacket.INDEX_USER_ID, (byte) userID);
        buffer.put(GamePacket.INDEX_GAME_ID, (byte) gameID);
    }

    public GamePacketMovementBuilder extra(byte extra) {
        buffer.put(GamePacket.INDEX_EXTRA, extra);
        return this;
    }

    public GamePacketMovementBuilder x(long v) {
        buffer.putLong(INDEX_X, v);
        return this;
    }
    public GamePacketMovementBuilder x(double v) {
        return x((long) v);
    }



    public GamePacketMovementBuilder y(long v) {
        buffer.putLong(INDEX_Y, v);
        return this;
    }
    public GamePacketMovementBuilder y(double v) {
        return y((long) v);
    }

    public GamePacketMovementBuilder inAir(boolean inAir) {
        buffer.put(INDEX_IN_AIR, inAir ? IS_IN_AIR : IS_NOT_IN_AIR);
        return this;
    }

    public GamePacket build() {
        return new GamePacket(buffer.array());
    }
}
