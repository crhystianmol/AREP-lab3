package edu.escuelaing.arep.nanoSpring;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Anotaciones que se usan al ejecutar el HTTP
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

    /**
     * String que se encarga de llevar a cabo el String
     * @return la cadena que representa la pagina
     */
    String value();
}
