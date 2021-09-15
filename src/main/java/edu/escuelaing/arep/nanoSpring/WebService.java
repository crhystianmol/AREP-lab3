package edu.escuelaing.arep.nanoSpring;

/**
 * Clase de servicio web controller
 *
 */
public class WebService {

    /**
     * indice de busqueda
     * @return string de busqueda
     */
    @RequestMapping(value="/hellomundo")
    public String index() {
        return "hola mundo";
    }

}
