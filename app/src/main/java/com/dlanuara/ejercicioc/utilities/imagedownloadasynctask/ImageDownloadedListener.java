package com.dlanuara.ejercicioc.utilities.imagedownloadasynctask;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

/**
 * Interfaz utilizada para poder definir la imagen una vez sea descargada
 */
public interface ImageDownloadedListener {
    /**
     * Setea la imagen recien descargada
     * @param image Imagen descargada. Es NULL en caso de no poder descargar la imagen.
     */
    void onImageDownloaded(@Nullable Bitmap image);
}
