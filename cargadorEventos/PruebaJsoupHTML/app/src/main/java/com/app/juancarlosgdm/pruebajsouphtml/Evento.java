package com.app.juancarlosgdm.pruebajsouphtml;

import android.graphics.Bitmap;

import java.util.Date;

public class Evento {
    public String titulo;
    public String descripcion;
    public Date fechaInicio;
    public Date fechaFin;
    public Bitmap imagen;

    public Evento () {
        titulo = "";
        descripcion = "";
        fechaInicio = new Date();
        fechaFin = new Date();
    }

    @Override
    public String toString() {
        String res = ("Titulo: " + titulo);
//        res += ("\nDescripcion: " + descripcion);
        res += ("\nFechaInicio: " + fechaInicio);
        res += ("\nFechaFin: " + fechaFin);

        return res;
    }
}
