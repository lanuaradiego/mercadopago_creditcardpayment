package com.dlanuara.ejercicioc.utilities;

import android.os.Binder;

/**
 * Clase utilizada para enviar objetos entre actividades
 * NOTA: Solo funciona si las actividades son ejecutadas en el mismo proceso, caso contrario se lanzara la excepcion ClassCastException al binder
 * @param <T> Tipo de dato de la informacion
 */
public class WrapperBinder<T> extends Binder {
    private final T data;

    public WrapperBinder(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
