package com.example.semana07.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.semana07.R;
import com.example.semana07.entity.Libro;
import com.example.semana07.entity.Libro;

import java.util.List;

public class LibroAdapter extends ArrayAdapter<Libro>  {

    private Context context;
    private List<Libro> lista;

    public LibroAdapter(@NonNull Context context, int resource, @NonNull List<Libro> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_libro_item_crud, parent, false);

        Libro obj = lista.get(position);

        TextView txtId = row.findViewById(R.id.itemIdLibro);
        txtId.setText("ID :"+String.valueOf(obj.getIdLibro())); //int

        TextView txtTitulo = row.findViewById(R.id.itemTituloLibro);
        txtTitulo.setText("Título :"+obj.getTitulo()); //string

        TextView txtAnio = row.findViewById(R.id.itemAnioLibro);
        txtAnio.setText("Año :"+obj.getAnio());

        TextView txtSerie = row.findViewById(R.id.itemSerieLibro);
        txtSerie.setText("Serie :"+obj.getSerie());

        TextView txtCategoria = row.findViewById(R.id.itemCategoriaLibro);
        txtCategoria.setText("Categoría :"+obj.getCategoria().getDescripcion());

        TextView txtPais = row.findViewById(R.id.itemPaisLibro);
        txtPais.setText("País :"+obj.getPais().getNombre());
        return row;
    }
}
