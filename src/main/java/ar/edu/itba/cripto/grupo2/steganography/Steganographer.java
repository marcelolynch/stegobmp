package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

public interface Steganographer {

    /* Indica si puede esteganografiarse el payload en ese bitmap */
    boolean canWrite(final Bitmap bitmap, Message message);

    /*
    *  Esteganografia el payload en el bitmap, que se ve modificado por esta llamada
    *  Debería lanzar IllegalArgumentException si no puede esteganografiarse este
    *  payload en este bitmap
    *
    *  */
    void write(Bitmap bitmap, Message message) throws IllegalArgumentException;

    /*
    *  Recupera datos de un bitmap asumiendo que fueron esteganografiados
    *  por el metodo write de la misma clase.
    *
    *  Lanza IllegalArgumentException si es imposible interpretar los datos
    *  del bitmap como esteganografiados
    * */
    byte[] read(final Bitmap bitmap) throws IllegalArgumentException;
}
