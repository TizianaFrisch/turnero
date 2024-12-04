package com.ar.turnos.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ar.turnos.model.Turno;
import com.ar.turnos.service.TurnoService;

import jakarta.validation.Valid;

@Controller
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    // Mostrar el formulario para crear un nuevo turno
    @GetMapping("/turno")
    public String mostrarFormulario(Model model) {
        model.addAttribute("turno", new Turno());
        return "formularioTurno";  // Muestra el formulario vacío
    }

    // Mostrar el formulario para actualizar un turno existente
    @GetMapping("/cambiar/{id}")
    public String mostrarFormularioActualizacion(@PathVariable("id") Long id, Model model) {
        Turno turno = turnoService.obtenerTurnoPorId(id);
        if (turno == null) {
            return "redirect:/verTurnos"; // Si no existe, redirige al listado
        }
        model.addAttribute("turno", turno); // Carga el turno existente para editar
        return "formularioTurno";  // Muestra el formulario con los datos actuales
    }

    // Ruta alternativa para mostrar el formulario desde la página de inicio
    @GetMapping("/home")
    public String home() {
        return "home";  // Asegúrate de tener la vista 'home.html'
    }

    @GetMapping("/home/formulario")
    public String mostrarFormularioDesdeHome(Model model) {
        model.addAttribute("turno", new Turno()); // Nuevo turno (sin datos)
        return "home";  // Redirige a la página principal con el formulario
    }

    @PostMapping("/turno")
    public String solicitarTurno(@Valid @ModelAttribute Turno turno, 
                                 BindingResult result, 
                                 Model model) {
        if (result.hasErrors()) {
            return "formularioTurno";  // Si hay errores, vuelve al formulario
        }

        // Verificar si el turno está disponible
        boolean disponible = turnoService.verificarDisponibilidad(turno.getFecha(), turno.getHora());

        if (!disponible) {
            model.addAttribute("error", "El turno no está disponible.");
            return "formularioTurno";  // Vuelve al formulario con error
        }

        turnoService.guardarTurno(turno);
        model.addAttribute("mensaje", "Turno reservado con éxito.");
        return "redirect:/home";  // Redirige al inicio después de guardar el turno
    }


    // Actualizar un turno existente
    @PostMapping("/actualizar/{id}")
    public String actualizarTurno(@PathVariable Long id,
                                   @Valid @ModelAttribute Turno turno,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, corrige los errores del formulario.");
            return "formularioTurno";  // Retorna al formulario si hay errores
        }

        Turno turnoExistente = turnoService.obtenerTurnoPorId(id);

        if (turnoExistente == null) {
            model.addAttribute("error", "Turno no encontrado.");
            return "redirect:/verTurnos"; // Si no encuentra el turno, redirige
        }

        // Actualiza el turno con los nuevos datos
        turnoExistente.setNombre(turno.getNombre());
        turnoExistente.setApellido(turno.getApellido());
        turnoExistente.setEmail(turno.getEmail());
        turnoExistente.setFecha(turno.getFecha());
        turnoExistente.setHora(turno.getHora());

        turnoService.guardarTurno(turnoExistente);

        model.addAttribute("mensaje", "Turno actualizado correctamente.");
        return "redirect:/home"; // Redirige a la lista de turnos
    }

    // Ver turnos por apellido
    @GetMapping("/verTurnos")
    public String mostrarFormularioBusqueda(Model model) {
        model.addAttribute("turnos", null);  // Inicia con la lista vacía
        return "verTurnos";  // Vista para ver turnos
    }

    @PostMapping("/verTurnos")
    public String buscarTurnosPorApellido(@RequestParam("apellido") String apellido, Model model) {
        List<Turno> turnos = turnoService.obtenerTurnosPorApellido(apellido);
        model.addAttribute("turnos", turnos);
        if (turnos.isEmpty()) {
            model.addAttribute("mensaje", "No se encontraron turnos para el apellido ingresado.");
        }
        return "verTurnos";  // Muestra los turnos encontrados
    }

    // Obtener horarios disponibles para una fecha específica
    @GetMapping("/turno/horariosDisponibles")
    @ResponseBody
    public List<String> obtenerHorariosDisponibles(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha) {
        return turnoService.obtenerHorariosDisponibles(fecha)
                .stream()
                .map(hora -> hora.toString())
                .collect(Collectors.toList());  // Devuelve los horarios disponibles como lista
    }

    // Listar turnos activos desde la fecha actual
    @GetMapping("/listarTurnosActivos")
    public String listarTurnosActivos(Model model) {
        LocalDate hoy = LocalDate.now();
        List<Turno> turnosActivos = turnoService.obtenerTurnosDesde(hoy);
        model.addAttribute("turnos", turnosActivos);
        return "medica";  // Vista para listar los turnos activos
    }

    // Borrar un turno activo
    @PostMapping("/borrarTurnoActual/{id}")
    public String borrarTurnoActual(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return "redirect:/home";  // Redirige al inicio después de eliminar el turno
    }

    // Eliminar un turno
    @PostMapping("/turno/eliminar/{id}")
    public String eliminarTurno(@PathVariable Long id, Model model) {
        turnoService.eliminarTurno(id);
        model.addAttribute("mensaje", "Turno eliminado correctamente.");
        return "redirect:/verTurnos";  // Redirige a la lista de turnos después de eliminar
    }
}
