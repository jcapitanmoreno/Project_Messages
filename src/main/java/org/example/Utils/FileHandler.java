package org.example.Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class FileHandler {
    /**
     * Método genérico para leer datos desde un archivo XML.
     *
     * @param <T> Tipo del objeto wrapper que encapsula los datos.
     * @param file Archivo XML desde el cual se leerán los datos.
     * @param wrapperClass Clase del objeto wrapper que encapsula los datos.
     * @return Objeto del tipo especificado con los datos leídos.
     * @throws Exception En caso de error de lectura del archivo.
     */
    public static <T> T leerDesdeArchivo(File file, Class<T> wrapperClass) throws Exception {
        JAXBContext context = JAXBContext.newInstance(wrapperClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return wrapperClass.cast(unmarshaller.unmarshal(file));
    }

    /**
     * Método genérico para escribir datos en un archivo XML.
     *
     * @param <T> Tipo del objeto wrapper que encapsula los datos.
     * @param filePath Ruta del archivo donde se escribirán los datos.
     * @param data Datos a escribir en el archivo.
     * @param wrapperClass Clase del objeto wrapper que encapsula los datos.
     * @throws Exception En caso de error al escribir en el archivo.
     */
    public static <T> void escribirEnArchivo(String filePath, T data, Class<T> wrapperClass) throws Exception {
        JAXBContext context = JAXBContext.newInstance(wrapperClass);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(data, new File(filePath));
    }
}
