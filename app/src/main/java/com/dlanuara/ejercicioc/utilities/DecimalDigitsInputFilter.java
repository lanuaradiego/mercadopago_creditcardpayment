package com.dlanuara.ejercicioc.utilities;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

public class DecimalDigitsInputFilter extends DigitsKeyListener {
    private int integer_count;
    private int decimal_count;

    public DecimalDigitsInputFilter(int integer_count, int decimal_count) {
        super(false, true);
        this.integer_count = integer_count;
        this.decimal_count = decimal_count;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);                    // Filtrar el caracter ingresado si no es un numero o el separador decimal
        int len, dlen = dest.length();
        if (out != null) {                                                                          // Si hubo filtro
            source = out;                                                                           // Remplezar el filtrado por el original
            start = 0;
            end = out.length();
        }
        // Borrar caracter
        len = end - start;
        if (len == 0) {                                                                             // Si se esta borrando un caracter
            if(dlen > 0 && dstart < dest.length()) {
                char c = dest.charAt(dstart);                                                       // Obtener que caracter se esta borrando
                if ((c == '.' || c == ',') && dest.length() - 1 > integer_count)                    // Si se esta borrando el separador decimal y la parte entera tiene mayor cantidad de digitos a la definida
                    return String.valueOf(c);                                                       // Evitar el borrado del separador decimal
            }
            return source;                                                                          // Continuar con el borrado
        }
        // Buscar el separador decimal
        for (int i = 0; i < dlen; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {                                                             // Si ya existia el separador decimal
                if(dstart > i) {                                                                    // Si se esta agregando uno/unos decimal
                    if(dlen-(i+1) + len > decimal_count)                                            // Si se supero la cantidad de decimales permitidos
                        return "";                                                                  // No agregar el/los decimales
                    else                                                                            // Si no se supero la cantidad de decimales permitidos
                        return new SpannableStringBuilder(source, start, end);                      // Agregar esos numeros decimales
                } else{                                                                             // Si se esta agregando uno/unos numeros enteros
                    if(len+i > integer_count)                                                      // Si supero la cantidad de numeros enteros
                        return "";                                                                  // No agregar el/los numeros a la parte entera
                    else                                                                            // Si no se supero la cantidad de numeros enteros
                        return new SpannableStringBuilder(source, start, end);                      // Agregar el/los numeros enteros
                }
            }
        }
        // Agregar separador
        for (int i = start; i < end; ++i) {
            char c = source.charAt(i);
            if (c == '.' || c == ',') {                                                             // Si se esta agregando un separador decimal
                if ((dlen-dend) + (end-(i + 1)) > decimal_count)                                    // Si se supero la cantidad de decimales permitos
                    return "";                                                                      // No permitir agregar el punto
                else                                                                                // Si no se supero la cantidad de decimales permitidos
                    return new SpannableStringBuilder(source, start, end);                          // Agregar numeros
            }
        }
        // Agregar parte entera
        if(dlen + len > integer_count)                                                              // Si se superaria la cantidad de cifras enteras
            return "";                                                                              // No agregar numeros
        return new SpannableStringBuilder(source, start, end);
    }
}