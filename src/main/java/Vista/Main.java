/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Vista;

import Controlador.ConexionBDD;
import Controlador.RequerimientoControlador;
import Controlador.SalaControlador;
import Controlador.UsuarioControlador;
import Controlador.VotoControlador;
import Modelo.Desarrollador;
import Modelo.ProductOwner;
import Modelo.Requerimiento;
import Modelo.Sala;
import Modelo.Usuario;
import Modelo.Voto;
import Vista.UsuarioVista;
import java.sql.Connection;

/**
 *
 * @author SUPERTRONICA
 */
public class Main {

    public static void main(String[] args) {

        Connection conexion = ConexionBDD.getConexion();

        if (conexion != null) {
            System.out.println("Prueba desde Main: conexion obtenida correctamente.");
        } else {
            System.out.println("Prueba desde Main: no se pudo conectar.");
        }

        new Main().inicializarLogin(); // <-- aqui se llama, creando una instancia de Main
    } // <-- esta llave le faltaba: cierra main()

    private void inicializarLogin() {
        InicioSesion vistaLogin = new InicioSesion();
        UsuarioControlador controlador = new UsuarioControlador();

        vistaLogin.getBtnAcceder().addActionListener(e -> {
            String email = vistaLogin.getTxtEmail();
            String contrasena = vistaLogin.getTxtContraseña();

            Usuario usuarioLogueado = controlador.iniciarSesion(email, contrasena);

            if (usuarioLogueado != null) {
                vistaLogin.mostrarMensaje("Bienvenido, " + usuarioLogueado.getNombre());
                vistaLogin.dispose(); // cierra el login
                abrirSala(usuarioLogueado);
                // Aqui abres la siguiente pantalla, ej: new SalaVista().setVisible(true);
            } else {
                vistaLogin.mostrarMensaje("Email o contrasena incorrectos.");
            }
        });

        vistaLogin.setVisible(true);
    }

    private void abrirSala(Usuario usuarioLogueado) {
        SalaVista vistaSala = new SalaVista();
        Sala salaModelo = new Sala();
        SalaControlador salaControlador = new SalaControlador(salaModelo, vistaSala);

        vistaSala.getBtnCrearSala().addActionListener(e -> {
            if (usuarioLogueado instanceof ProductOwner) {
                Sala salaCreada = salaControlador.crearSala();
                if (salaCreada != null) {
                    vistaSala.mostrarMensaje("Sala creada. Codigo: " + salaCreada.getCodigoAcceso());
                    vistaSala.dispose();
                    abrirRequerimientos(usuarioLogueado, salaCreada); // <-- ahora sigue el flujo
                }
            } else {
                vistaSala.mostrarMensaje("Solo un ProductOwner puede crear una sala.");
            }
        });

        vistaSala.getBtnUnirse().addActionListener(e -> {
            Sala sala = salaControlador.unirseASala(vistaSala.getTxtCodigo());
            if (sala != null) {
                vistaSala.mostrarMensaje("Te uniste a la sala: " + sala.getCodigoAcceso());
                vistaSala.dispose();
                abrirRequerimientos(usuarioLogueado, sala);
            }
        });

        vistaSala.getBtnCerrarSesion().addActionListener(e -> {
            vistaSala.dispose();
            inicializarLogin(); // vuelve a abrir la pantalla de login, sin reiniciar el programa
        });

        vistaSala.setVisible(true);
    }

    private void abrirRequerimientos(Usuario usuarioLogueado, Sala sala) {
        RequerimientoVista vistaReq = new RequerimientoVista();
        Requerimiento reqModelo = new Requerimiento();

        if (usuarioLogueado instanceof ProductOwner) {
            RequerimientoControlador reqControlador = new RequerimientoControlador(
                    reqModelo, vistaReq, sala, (ProductOwner) usuarioLogueado);

            vistaReq.getBtnVotar().setText("VER VOTOS");
            vistaReq.getBtnVotar().addActionListener(e -> {
                int fila = vistaReq.getTblRequerimientos().getSelectedRow();
                if (fila == -1) {
                    vistaReq.mostrarMensaje("Selecciona un requerimiento para ver sus votos.");
                    return;
                }
                int idReq = Integer.parseInt(vistaReq.getModelo().getValueAt(fila, 0).toString());
                Requerimiento reqSeleccionado = new Requerimiento();
                reqSeleccionado.setId(idReq);

                abrirVotosPO((ProductOwner) usuarioLogueado, reqSeleccionado, sala);
            });

            reqControlador.iniciar();

        } else {

            vistaReq.getBtnCrear().setVisible(false);
            vistaReq.getBtnActualizar().setVisible(false);
            vistaReq.getBtnInhabilitar().setVisible(false);

            RequerimientoControlador reqControlador = new RequerimientoControlador(
                    reqModelo, vistaReq, sala, null);
            reqControlador.cargarDatosTabla();

            vistaReq.getBtnVotar().addActionListener(e -> {
                int fila = vistaReq.getTblRequerimientos().getSelectedRow();
                if (fila == -1) {
                    vistaReq.mostrarMensaje("Selecciona un requerimiento para votar.");
                    return;
                }
                int idReq = Integer.parseInt(vistaReq.getModelo().getValueAt(fila, 0).toString());
                Requerimiento requerimientoSeleccionado = new Requerimiento();
                requerimientoSeleccionado.setId(idReq);

                abrirVotacion((Desarrollador) usuarioLogueado, requerimientoSeleccionado, sala);
            });

            vistaReq.setVisible(true);
        }

        vistaReq.getBtnVolver().addActionListener(e -> {
            vistaReq.dispose();
            abrirSala(usuarioLogueado); // regresa a la pantalla de Sala, sin reiniciar el programa
        });

    }

    private void abrirVotacion(Desarrollador desarrollador, Requerimiento requerimiento, Sala sala) {
        VotoVista vistaVoto = new VotoVista();
        Voto votoModelo = new Voto();

        VotoControlador votoControlador = new VotoControlador(
                votoModelo, vistaVoto, desarrollador, requerimiento);

        vistaVoto.getBtnRevelar().setVisible(false);

        vistaVoto.getBtnVotar().addActionListener(e -> votoControlador.registrarVoto());

        vistaVoto.getBtnVolver().addActionListener(e -> {
            vistaVoto.dispose();
            abrirRequerimientos(desarrollador, sala);
        });

        vistaVoto.setVisible(true);
    }

    private void abrirVotosPO(ProductOwner po, Requerimiento requerimiento, Sala sala) {
        VotoVista vistaVoto = new VotoVista();
        Voto votoModelo = new Voto();

        VotoControlador votoControlador = new VotoControlador(
                votoModelo, vistaVoto, null, requerimiento);

        vistaVoto.getCmbCarta().setVisible(false);
        vistaVoto.getBtnVotar().setVisible(false);

        vistaVoto.getBtnRevelar().addActionListener(e -> votoControlador.revelarVotos());
        vistaVoto.getBtnVolver().addActionListener(e -> {
            vistaVoto.dispose();
            abrirRequerimientos(po, sala);
        });

        vistaVoto.setVisible(true);
    }
}
