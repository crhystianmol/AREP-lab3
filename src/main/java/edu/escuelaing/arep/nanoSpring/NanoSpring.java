package edu.escuelaing.arep.nanoSpring;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.escuelaing.arep.httpServer.HttpServerReto;

/**
 * Clase que se encarga de cargar la pagina web usando anotaciones
 *
 */
public class NanoSpring {
    private static NanoSpring _instance= new NanoSpring();
    private boolean componentLoaded=false;
    private Map<String, Method> componentsRoute;


    /**
     * Constructor de la clase NanoSpring la cual permite almacenar en el hashMap
     */
    public NanoSpring() {
        componentsRoute= new HashMap<>();
    }


    /**
     * Inicia el servidor y carga los componentes
     * @param args los argumentos
     * @throws InvocationTargetException invocacion del target erronea
     * @throws IllegalAccessException sin acceso a recursos
     * @throws IOException execption IO
     */
    public void run(String[] args) throws Exception{

        try {
            _instance.loadComponents(args);
            _instance.componentLoaded=true;
            HttpServerReto server = new HttpServerReto(_instance);
            server.startServer();

        }catch(ClassNotFoundException ex) {
            Logger.getLogger(NanoSpring.class.getName()).log(Level.SEVERE, "aqui");
        }
    }


    /**
     * Carga los componentes
     * @param args listado de classpath
     * @throws ClassNotFoundException excepcion de no encontrar lo solicitado
     */
    private void loadComponents(String[] args) throws ClassNotFoundException {

        for (String classpath : args) {
            for (Method m : Class.forName(classpath).getMethods()) {
                System.out.println("revisando: " + m.getName());
                if (m.isAnnotationPresent(RequestMapping.class)) {
                    System.out.println("ENCONTRO UN REQUESTMAPPING");
                    componentsRoute.put(m.getAnnotation(RequestMapping.class).value(),m);
                }
            }
        }
    }

    /**
     * metodo invoke
     * @param path la url
     * @return componente de la ruta
     * @throws InvocationTargetException invocacion del target erronea
     * @throws IllegalAccessException sin acceso a recursos
     */
    public  String invoke(String path) throws InvocationTargetException, IllegalAccessException {
        return componentsRoute.get(path).invoke(null).toString();
    }
}