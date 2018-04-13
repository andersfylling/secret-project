package team.adderall.game.framework.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: implement lol
// in stead of defining waves in the Configuration, this can be used
// to auto configure components in stead.
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameLogic {
    int wave() default 1;
}
