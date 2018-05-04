package team.adderall.game;

/**
 * Do we even have a word for objects affected by gravity?
 */
public interface GravityAffected {
    double y();
    void y(double y);

    double velocity();
    void velocity(double velocity);
}
