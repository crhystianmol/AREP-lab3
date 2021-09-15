package edu.escuelaing.arep.urlReader;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * clase URlReader permite obtener los encabezados de la pagina
 *
 */
public class UrlReader {

    /**
     *Constructor de la clase URLReader
     */
    public UrlReader() {
        super();
    }

    /**
     * Clase main de la clase URLReader
     * @param args necesario para lanzar el main
     * @throws Exception exception de la clase urlConnection
     */
    public static void main(String[] args) throws Exception {
        String site= "http://localhost:35000/miotrositio/foto.png";

        URL siteURL = new URL(site);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();

        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if(headerName !=null){System.out.print(headerName + ":");}
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.print(value);
            }
            System.out.println("");
        }

        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine = null;
            while ((inputLine = reader1.readLine()) != null) {
                System.out.println(inputLine);
            }
        }catch (IOException x) {
            System.err.println(x);
        }
    }



    /**
     * Ejercicio 1 del laboratorio los comandos Get
     * @throws Exception de tipo IoException
     */
    public static void ejer1() throws Exception {
        URL ejer = new URL("http://localhost:36000/ejer1");
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(ejer.openStream()))) {
            System.out.println("-------------------");
            System.out.println(ejer.getHost());
            System.out.println(ejer.getProtocol());
            System.out.println(ejer.getAuthority());
            System.out.println(ejer.getPath());
            System.out.println(ejer.getPort());
            System.out.println(ejer.getQuery());
            System.out.println(ejer.getFile());
            System.out.println(ejer.getRef());
            System.out.println("-------------------");
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    /**
     * ejercicio 2 leyendo una url y guardando en html
     * @throws Exception del ejercicio 2
     */
    public static void ejer2() throws Exception {
        URL google = new URL("https://www.hostingatope.com/como-hacer-una-pagina-web-con-html/"); //ejemplo con pagina al azar.
        String ruta = "/archivo.html";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo, true));
        try (BufferedReader reader =new BufferedReader(new InputStreamReader(google.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                if(archivo.exists()) {
                    System.out.println(inputLine);
                    System.out.println(1);
                    bw.write(inputLine + "1");
                } else {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write("Acabo de crear el fichero de texto.");
                }
            }

        }catch (IOException x) {
            System.err.println(x);
        }
        bw.close();
    }




}