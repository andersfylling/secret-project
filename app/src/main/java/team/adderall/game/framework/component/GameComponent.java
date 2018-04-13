package team.adderall.game.framework.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: implement lol
// in stead of using RegisterGameComponent in a method,
// class constructors can be auto wired when the class has this annotation
// every subpackage should be searched for this annotation if implemented.
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameComponent {
    String name() default "";
}
