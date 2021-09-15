package edu.escuelaing.arep.httpServer;

import edu.escuelaing.arep.nanoSpring.NanoSpring;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Clase HTTPServer
 *
 */
public class HttpServerReto {

    String rutaa = "/src/main/resources/Img/";
    private NanoSpring server;

    /**
     * Constructor de HttpServerReto
     */
    public HttpServerReto() {}

    /**
     * Constructor de HttpServerReto
     * @param server servidor usando anotaciones
     */
    public HttpServerReto(NanoSpring server) {
        this.server=server;
    }


    /**
     * Metodo StarServer que inicializa los sockets e inicia el servidor
     * @throws IOException excepcion de la clase ServerSocket
     * @throws IllegalAccessException acceso ilegal
     * @throws InvocationTargetException  exception en el target
     */
    public void startServer()  {
        try {

            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(getPort());
            } catch (IOException e) {
                System.err.println("Could not listen on port: 35000.");
                System.exit(1);
            }
            try {
                boolean running = true;
                while (running) {
                    Socket clientSocket = null;
                    try {
                        System.out.println("Listo para recibir ...");
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                        System.exit(1);
                    }
                    PrintWriter out = new PrintWriter(
                            clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                    String inputLine, outputLine;

                    Map<String, String> request = new HashMap<>();
                    boolean requestLineReady = false;
                    while ((inputLine = in.readLine()) != null) {
                        if (!requestLineReady) {
                            request.put("requestLine", inputLine);
                            requestLineReady = true;
                        } else {
                            String[] entry = inputLine.split(":");
                            if (entry.length > 1) {
                                request.put(entry[0], entry[1]);
                            }
                        }

                        //
                        if (!in.ready()) {break; }
                    }
                    if(request.get("requestLine")!=null){
                        RequestLine req = new RequestLine(request.get("requestLine"));
                        System.out.println("RequestLine: " + req);
                        if(req != null) {


                            outputLine = getDefaultokOutput();
                            URI theuri = req.getTheuri();
                            if (theuri.getPath().startsWith("/Apps")) {
                                String appuri= theuri.getPath().substring(5);
                                invokeApp(appuri,new PrintWriter(clientSocket.getOutputStream(), true));
                            } else {
                                getStaticResource(theuri.getPath(), out,clientSocket.getOutputStream());
                            }
                        }
                    }

                    in.close();
                    out.close();
                    clientSocket.close();
                }
            }catch (IOException ex) {
                Logger.getLogger(HttpServerReto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpServerReto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo que muestra el texto en la pagina
     * @param string la ruta donde encontraremos el archivo
     * @param outputStream metodo de salida
     * @param tipo tipo de archivo
     */
    private void mostrartexto(String string, OutputStream outputStream, String tipo) {
        try {
            System.out.println(System.getProperty("user.dir")+string);
            BufferedReader in = new BufferedReader(
                    new FileReader(System.getProperty("user.dir")+ string));
            String line;
            String out = "";
            while ((line = in.readLine()) != null) {
                out = out + line;
            }
            outputStream.write(("HTTP/1.1 201 OK\r\n"
                    + "Content-Type: text/"+tipo+ ";"
                    + "charset=\"UTF-8\" \r\n"
                    + "\r\n" + out).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Metodo MostrarImagen muestra la imagen en el servidor
     * @param string ruta de la imagen
     * @param outputStream metodo salida
     */
    private void mostrarImagen(String string, OutputStream outputStream) {
        try {
            BufferedImage imagen = ImageIO.read(new File(System.getProperty("user.dir") + string));
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            ImageIO.write(imagen, "JPG", ArrBytes);
            out.writeBytes("HTTP/1.1 200 OK \r\n"
                    + "Content-Type: image/png \r\n"
                    + "\r\n");
            out.write(ArrBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clase que mezcla la lectura de archivos, js, html, png
     * @param path ruta
     * @param out la estructura de la pagina
     * @param outputStream el ooutput
     */
    private void getStaticResource(String path, PrintWriter out,OutputStream outputStream) {
        Path file = Paths.get(System.getProperty("user.dir")+"/src/main/resources/Img" + path);
        if (path.contains("html") || path.contains("js")|| path.contains("ico")) {
            try (InputStream in = Files.newInputStream(file);
                 BufferedReader reader
                         = new BufferedReader(new InputStreamReader(in))) {

                String header = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n";
                out.println(header);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                    System.out.println(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(HttpServerReto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (path.contains("png")){
            System.out.println("/src/main/resources/Img" + path);
            try{
                BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir")+"/src/main/resources/Img/fondo2.png"));
                ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
                DataOutputStream out1 = new DataOutputStream(outputStream);
                ImageIO.write(image, "PNG", ArrBytes);
                out1.writeBytes("HTTP/1.1 200 OK \r\n"
                        + "Content-Type: image/png \r\n"
                        + "\r\n");
                out1.write(ArrBytes.toByteArray());
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pagina httml
     * @return html retorna html
     */
    private String getDefaultokOutput() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Title of the document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Mi propio mensaje</h1>\n" +
                "</body>\n" +
                "</html>\n";
    }

    /**
     * invokeapp el server
     * @param appuri url app
     * @param out la estructura
     * @throws InvocationTargetException exception de mala invocacion del target
     * @throws IllegalAccessException acceso ilegal a los recursos
     */
    private void invokeApp(String appuri, PrintWriter out) throws InvocationTargetException, IllegalAccessException {

        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
        String methodresponse = server.invoke(appuri);
        out.println(header + methodresponse);
    }

    /**
     * metodo getPort que retorna el puerto a usar
     * @return el puerto
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }





}