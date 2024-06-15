package com.example.semana07;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semana07.entity.Categoria;
import com.example.semana07.entity.Libro;
import com.example.semana07.entity.Pais;
import com.example.semana07.service.ServiceCategoriaLibro;
import com.example.semana07.service.ServiceLibro;
import com.example.semana07.service.ServicePais;
import com.example.semana07.util.ConnectionRest;
import com.example.semana07.util.FunctionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroFormularioCrudActivity extends AppCompatActivity {

    //Pais -- 1
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Categoria --2
    Spinner spnCategoria;
    ArrayAdapter<String> adaptadorCategoria;
    ArrayList<String> categorias = new ArrayList<>();

    //Servicio -- 3
    ServiceLibro serviceLibro;
    ServicePais servicePais;
    ServiceCategoriaLibro serviceCategoria;

    EditText txtTitulo, txtAnio, txtSerie;

    Button btnEnviar, btnRegresar;

    TextView idTitlePage;

    String metodo;

    Libro objActual;

    //estado 1
    RadioButton rbtActivo,rbtInactivo;
    TextView txtEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_libro_formulario_crud_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //estado2 -- el rbt solo bota boolean = true or false
        //se crea el objeto
        rbtActivo = findViewById(R.id.rbtActivo);
        rbtInactivo = findViewById(R.id.rbtInactivo);
        txtEstado = findViewById(R.id.txtEstado);

//4
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceCategoria = ConnectionRest.getConnection().create(ServiceCategoriaLibro.class);
        serviceLibro = ConnectionRest.getConnection().create(ServiceLibro.class);
//5
        adaptadorPais = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegLibPais);
        spnPais.setAdapter(adaptadorPais);

        adaptadorCategoria = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
        spnCategoria = findViewById(R.id.spnRegLibCategoria);
        spnCategoria.setAdapter(adaptadorCategoria);

        txtTitulo = findViewById(R.id.txtRegLibTitulo);
        txtAnio = findViewById(R.id.txtRegLibAnio);
        txtSerie = findViewById(R.id.txtRegLibSerie);

        metodo = (String) getIntent().getExtras().get("var_metodo");

        idTitlePage = findViewById(R.id.idTitlePage);
        btnEnviar = findViewById(R.id.btnRegLibEnviar);
        btnRegresar = findViewById(R.id.btnRegLibRegresar);

        if (metodo.equals("REGISTRAR")) {
            idTitlePage.setText("Registra Libro");
            btnEnviar.setText("Registrar");
            //ocultar estado de registrar
            rbtActivo.setVisibility(View.GONE);
            rbtInactivo.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);

        } else if (metodo.equals("ACTUALIZAR")) {
            idTitlePage.setText("Actualiza Libro");
            btnEnviar.setText("Actualizar");

            objActual = (Libro) getIntent().getExtras().get("var_objeto");
            txtTitulo.setText(objActual.getTitulo());
            txtAnio.setText(String.valueOf(objActual.getAnio()));
            txtSerie.setText(objActual.getSerie());

            //ESTADO4 - establecer el estado de los botones
            if (objActual.getEstado() == 1){
                rbtActivo.setChecked(true);
            }else{
                rbtInactivo.setChecked(true);
            }


        }


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibroFormularioCrudActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //registrar1 - ejecuta cuando el usuario hace clic en el botón
        // recoge los datos ingresados
        // llama a un método para registrar esos datos en el servidor.
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto ingresado en los campos de título, año y serie
                String titulo = txtTitulo.getText().toString();
                String anio = txtAnio.getText().toString();
                String serie = txtSerie.getText().toString();

                String idPais = spnPais.getSelectedItem().toString().split(":")[0];

                String idCategoria = spnCategoria.getSelectedItem().toString().split(":")[0];

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim())); // Elimina espacios en blanco y convierte a entero

                Categoria objCategoria = new Categoria();
                objCategoria.setIdCategoria(Integer.parseInt(idCategoria.trim())); // Elimina espacios en blanco y convierte a entero

                // Crea un objeto Libro y establece sus propiedades
                Libro objLibro = new Libro();
                objLibro.setTitulo(titulo);
                objLibro.setAnio(Integer.parseInt(anio));
                objLibro.setSerie(serie);
                objLibro.setPais(objPais);
                objLibro.setCategoria(objCategoria);
                objLibro.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());

                //ACTUALIZA2
                if (metodo.equals("REGISTRAR")) {
                    // Si el método es "REGISTRAR", se establece como 1
                    objLibro.setEstado(1);
                    // Llama al método registra para registrar el libro en el servidor
                    registra(objLibro);
                } else {
                    // estado3: Configura el estado del libro basado en la selección del radio button
                    objLibro.setEstado(rbtActivo.isChecked() ? 1 : 0);
                    //De lo contrario se establece el ID del libro actual en el objeto libro y llama al metodo actualiza
                    objLibro.setIdLibro(objActual.getIdLibro());
                    actualiza(objLibro);
                }

            }
        });


//6
        cargaPais();
        cargaCategoria();
    }

    //registrar3 - metodo registrar
    void registra(Libro obj) {
        // Realiza una llamada al servicio para registrar un libro
        Call<Libro> call = serviceLibro.registraLibro(obj);

        // Encola la llamada de manera asíncrona para manejar la respuesta
        call.enqueue(new Callback<Libro>() {
            // Se ejecuta cuando la respuesta del servidor es exitosa
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                // Verifica si la respuesta es exitosa
                if (response.isSuccessful()) {
                    // Obtiene el objeto Libro de la respuesta
                    Libro objSalida = response.body();

                    // Muestra un mensaje de alerta con los detalles del libro registrado
                    mensajeAlert(" Registro de Libro exitoso:  "
                            + " \n >>>> ID >> " + objSalida.getIdLibro()
                            + " \n >>> Título >>> " + objSalida.getTitulo());
                }
            }

            // Se ejecuta cuando hay un error en la llamada o en la respuesta del servidor
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                // Aquí puedes manejar el error, por ejemplo, mostrando un mensaje al usuario
            }
        });
    }

    //ACTUALIZA1
    void actualiza(Libro obj) {
        // Realiza una llamada al servicio para actualizar un libro
        Call<Libro> call = serviceLibro.actualizaLibro(obj);

        // Encola la llamada de manera asíncrona para manejar la respuesta
        call.enqueue(new Callback<Libro>() {
            // Se ejecuta cuando la respuesta del servidor es exitosa
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                // Verifica si la respuesta es exitosa
                if (response.isSuccessful()) {
                    // Obtiene el objeto Libro de la respuesta
                    Libro objSalida = response.body();

                    // Muestra un mensaje de alerta con los detalles del libro actualizado
                    mensajeAlert(" Actualización de Libro exitosa:  "
                            + " \n >>>> ID >> " + objSalida.getIdLibro()
                            + " \n >>> Título >>> " + objSalida.getTitulo());
                }
            }

            // Se ejecuta cuando hay un error en la llamada o en la respuesta del servidor
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                // Aquí puedes manejar el error, por ejemplo, mostrando un mensaje al usuario
            }
        });
    }


    //7
    void cargaPais() {
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                List<Pais> lstAux = response.body();
                paises.add(" [ Seleccione ] ");
                for (Pais aux : lstAux) {
                    paises.add(aux.getIdPais() + " : " + aux.getNombre());
                }
                adaptadorPais.notifyDataSetChanged();

                // Si el método es "ACTUALIZAR", selecciona la categoría correspondiente en el Spinner
                if (metodo.equals("ACTUALIZAR")) {
                    // Obtiene el ID y la descripción de la categoría del objeto actual
                    String id = String.valueOf(objActual.getPais().getIdPais());
                    String nombre = String.valueOf(objActual.getPais().getNombre());
                    String row = id + " : " + nombre;

                    // Itera a través de la lista de categorias para encontrar la categoría correspondiente
                    for (int i = 0; i <= paises.size(); i++) {
                        // Si encuentra la categoría, la selecciona en el Spinner automaticamente
                        if (paises.get(i).equals(row)) {
                            spnPais.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

            }
        });
    }

    void cargaCategoria() {
        Call<List<Categoria>> call = serviceCategoria.listaTodos();

        // Encola la llamada de manera asíncrona para obtener la respuesta
        call.enqueue(new Callback<List<Categoria>>() {
            // Se ejecuta cuando la respuesta del servidor es exitosa
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                List<Categoria> lstAux = response.body();
                categorias.add(" [ Seleccione ] ");
                // Itera a través de la lista de categorías obtenidas
                for (Categoria aux : lstAux) {
                    categorias.add(aux.getIdCategoria() + " : " + aux.getDescripcion());
                }

                // Notifica al adaptador de que los datos han cambiado para que se actualice la vista
                adaptadorCategoria.notifyDataSetChanged();

                // Si el método es "ACTUALIZAR", selecciona la categoría correspondiente en el Spinner
                if (metodo.equals("ACTUALIZAR")) {
                    // Obtiene el ID y la descripción de la categoría del objeto actual
                    String id = String.valueOf(objActual.getCategoria().getIdCategoria());
                    String nombre = String.valueOf(objActual.getCategoria().getDescripcion());
                    String row = id + " : " + nombre;

                    // Itera a través de la lista de categorías para encontrar la categoría correspondiente
                    for (int i = 0; i <= categorias.size(); i++) {
                        // Si encuentra la categoría, la selecciona en el Spinner automaticamente
                        if (categorias.get(i).equals(row)) {
                            spnCategoria.setSelection(i);
                            break;
                        }
                    }
                }
            }

            // Se ejecuta cuando hay un error en la llamada o en la respuesta del servidor
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                // Aquí puedes manejar el error
            }
        });
    }

    //registrar2
    //mensajes
    void mensajeToast(String mensaje) {
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }


    //alert
    public void mensajeAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }


}