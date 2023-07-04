package com.bosonit.formacion.block14springsecurity.student.application;

import com.bosonit.formacion.block14springsecurity.asignatura.domain.Asignatura;
import com.bosonit.formacion.block14springsecurity.asignatura.repository.AsignaturaRepository;
import com.bosonit.formacion.block14springsecurity.exception.EntityNotFoundException;
import com.bosonit.formacion.block14springsecurity.exception.UnprocessableEntityException;
import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import com.bosonit.formacion.block14springsecurity.persona.repository.PersonaRepository;
import com.bosonit.formacion.block14springsecurity.profesor.repository.ProfesorRepository;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentInputDto.StudentInputDto;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentOutputDto.StudentOutputDtoFull;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentOutputDto.StudentOutputDtoSimple;
import com.bosonit.formacion.block14springsecurity.student.domain.Student;
import com.bosonit.formacion.block14springsecurity.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ProfesorRepository profesorRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Override
    public StudentOutputDtoSimple addStudent(StudentInputDto studentInputDto) {
        //Primero compruebo que el idPersona no es nulo para que no salte error 500
        if (studentInputDto.getIdPersona() == null) {
            throw new UnprocessableEntityException("IdPersona no puede ser nulo");
        }

        //Instancio Persona para comprobar si ya es profesor.
        Persona persona = personaRepository.findById(studentInputDto.getIdPersona()).orElseThrow
                (() -> new EntityNotFoundException(" IdPersona no puede ser nulo."));
        if (persona.getProfesor() != null)
            throw new UnprocessableEntityException(" Esta persona ya es un profesor, luego no puede ser estudiante.");
        //Si fuera estudiante lanzaría la excepción.

        //Instancio student con el constructor vacío.
        Student student = new Student();
        //Seteo los campos
        student.setNumHourWeek(studentInputDto.getNumHourWeek());
        student.setComments(studentInputDto.getComments());
        student.setBranch(studentInputDto.getBranch());

        //Compruebo que branch y numHourWeek no sean nulos
        if (studentInputDto.getNumHourWeek() == null)
            throw new UnprocessableEntityException("numHourWeek no puede ser nulo");
        if (studentInputDto.getBranch() == null) throw new UnprocessableEntityException("numHourWeek no puede ser nulo");


        //Seteo la variable persona de la relación y la guardo en el repositorio
        student.setPersona(personaRepository.findById(studentInputDto.getIdPersona()).orElseThrow());

        //Creo una lista de personas con todas las que hay en el repository
        //Con el bucle for valido si ya esxiste un estudiante para esta persona.
        for (
                Student s : studentRepository.findAll()) {
            if (s.getPersona().getIdPersona().equals(student.getPersona().getIdPersona())) {
                throw new UnprocessableEntityException("Student ya registrado con este idPersona");
            }
        }

        //Ahora que el student está completo se guarda en repositorio
        studentRepository.save(student);

        return new StudentOutputDtoSimple(student);

    }

    @Override
    public String assignAsignaturaToStudent(Integer idStudent, List<Integer> idsAsignaturas) {
        //Creo student con el id obtenido.
        Student student = studentRepository.findById(idStudent).orElseThrow
                (() -> new EntityNotFoundException("Student no encontrado."));

        //Bucle for each donde creo una lista de asignaturas con los ids obtenidos
        for (Asignatura a : asignaturaRepository.findAllById(idsAsignaturas)) {
            if (!student.getAsignaturas().stream().anyMatch(asignatura -> asignatura.getIdAsignatura().equals(a.getIdAsignatura()))) {
                student.getAsignaturas().add(a);
            }
        }
        //Con el if filtro a traves de .stream()anyMatch si alguna asignatura ya aparece en la lista de ese student no la vuelve a añadir.

        //Se guarda el student en repositorio con su lista actualizada
        studentRepository.save(student);


        return "El estudiante con id_Student " + idStudent + " " + student.getPersona().getUsuario()
                + ", ha sido matriculado en " +
                student.getAsignaturas().stream().map(Asignatura::getNombreAsignatura).collect(Collectors.toList());
    }

    @Override
    public String removeAsignaturasFromStudent(Integer idStudent, List<Integer> idsAsignaturas) {
        //Creo student con el id obtenido.
        Student student = studentRepository.findById(idStudent).orElseThrow(() -> new EntityNotFoundException("Student no encontrado."));

        //Bucle for each donde creo una lista de asignaturas con los ids obtenidos
        for (Asignatura a : asignaturaRepository.findAllById(idsAsignaturas)) {
            {
                student.getAsignaturas().remove(a);
            }
        }

        //Se guarda el student en repositorio con su lista actualizada
        studentRepository.save(student);

        return "El estudiante con id_Student " + idStudent + " " + student.getPersona().getUsuario()
                + ", ha sido desmatriculado de " +
                asignaturaRepository.findAllById(idsAsignaturas).stream().map(Asignatura::getNombreAsignatura).collect(Collectors.toList());
    }


    @Override
    public StudentOutputDtoSimple getStudentById(Integer id, String outputType) {

        Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(" Student no encontrado."));

        if (outputType.equals("simple")) {
            return new StudentOutputDtoSimple(student);
        }
        if (outputType.equals("full")) {
            return new StudentOutputDtoFull(student);
        } else {
            return new StudentOutputDtoSimple(student);
        }
    }


    @Override
    public List<StudentOutputDtoSimple> getAllStudents() {
        List<StudentOutputDtoSimple> listStudentOutputDtoSimple = new ArrayList<>();
        for (Student s : studentRepository.findAll()) {
            listStudentOutputDtoSimple.add(new StudentOutputDtoSimple(s));
        }
        return listStudentOutputDtoSimple;
    }

    @Override
    public StudentOutputDtoSimple updateStudent(Integer id, StudentInputDto studentInputDto) {
        //Creo un student que será igual al que ya hay en el repositorio.
        Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(" Student no encontrado."));
        if (studentInputDto.getComments() != null) student.setComments(studentInputDto.getComments());
        if (studentInputDto.getComments() != null) student.setComments(studentInputDto.getComments());
        if (studentInputDto.getBranch() != null) student.setBranch(studentInputDto.getBranch());

        //Guardo los cambios
        studentRepository.save(student);

        return new StudentOutputDtoSimple(student);
    }

    @Override
    public String deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow
                (() -> new EntityNotFoundException("Student no encontrado."));
        studentRepository.delete(student);
        return "El estudiante con id_Student " + id + " ha sido borrad@.";
    }
}


