package com.bosonit.formacion.block14springsecurity.profesor.application;


import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import com.bosonit.formacion.block14springsecurity.exception.EntityNotFoundException;
import com.bosonit.formacion.block14springsecurity.exception.UnprocessableEntityException;
import com.bosonit.formacion.block14springsecurity.persona.repository.PersonaRepository;
import com.bosonit.formacion.block14springsecurity.profesor.controller.dto.ProfesorInputDto;
import com.bosonit.formacion.block14springsecurity.profesor.controller.dto.ProfesorOutputDto;
import com.bosonit.formacion.block14springsecurity.profesor.domain.Profesor;
import com.bosonit.formacion.block14springsecurity.profesor.repository.ProfesorRepository;
import com.bosonit.formacion.block14springsecurity.student.domain.Student;
import com.bosonit.formacion.block14springsecurity.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    ProfesorRepository profesorRepository;
    @Autowired
    StudentRepository studentRepository;


    @Override
    public ProfesorOutputDto addProfesor(ProfesorInputDto profesorInputDto) {
        //Primero compruebo que el idPersona no es nulo para que no salte error 500
        if (profesorInputDto.getIdPersona() == null) {
            throw new UnprocessableEntityException("IdPersona no puede ser nulo");
        }

        //Instancio Persona para comprobar si ya es estudiante.
        Persona persona = personaRepository.findById(profesorInputDto.getIdPersona()).orElseThrow(() -> new EntityNotFoundException(" IdPersona no puede ser nulo."));

        if (persona.getStudent() != null)
            throw new UnprocessableEntityException(" Esta persona es un estudiante, luego no puede ser profesor.");
        //Si fuera estudiante lanzaría la excepción.


        //Instancio profesor con constructor vacío.
        Profesor profesor = new Profesor();
        //Seteo los campos
        profesor.setComments(profesorInputDto.getComments());
        profesor.setBranch(profesorInputDto.getBranch());


        //Compruebo que branch no sea nulo
        if (profesorInputDto.getBranch() == null) {
            throw new UnprocessableEntityException(" Branch no puede ser nulo");
        }

        //Seteo la variable persona de la relación y la guardo en el repositorio
        profesor.setPersona(persona);

        for (Profesor p : profesorRepository.findAll()) {
            if (p.getPersona().getIdPersona().equals(profesor.getPersona().getIdPersona())) {
                throw new UnprocessableEntityException("Profesor ya registrado con este idPersona");
            }
        }

        //Ahora que el profesor está "completo" se guarda en repositorio
        profesorRepository.save(profesor);


        //Return del OutputDeto con el profesor por parámetro
        return new ProfesorOutputDto(profesor);
    }

    @Override
    public String assignStudentToProfesor(Integer idProfesor, Integer idStudent) {

        Profesor profesor = profesorRepository.findById(idProfesor).orElseThrow(() -> new EntityNotFoundException(" Profesor no encontrado."));
        Student student = studentRepository.findById(idStudent).orElseThrow(() -> new EntityNotFoundException(" Student no encontrado."));

        //Seteo el profesor del estudiante
        student.setProfesor(profesor);
        //Añado el estudiante a la lista de estudiantes del profesor
        profesor.getStudents().add(student);

        //Guardo en repositorio
        profesorRepository.save(profesor);
        studentRepository.save(student);


        return "El estudiante con id_Student " + idStudent + " " + student.getPersona().getUsuario()
                + ", ha sido añadid@ a los estudiantes del profesor con id_Profeesor " +
                idProfesor + " " + profesor.getPersona().getUsuario() + ".";

    }

    @Override
    public ProfesorOutputDto getProfesorById(Integer id) {
        return new ProfesorOutputDto(profesorRepository.findById(id).orElseThrow
                (() -> new EntityNotFoundException(" Profesor no encontrado.")));
    }


    @Override
    public List<ProfesorOutputDto> getAllProfesores() {
        List<ProfesorOutputDto> profesorOutputDtoList = new ArrayList<>();
        for (Profesor p : profesorRepository.findAll()) {
            profesorOutputDtoList.add(new ProfesorOutputDto(p));
        }
        return profesorOutputDtoList;
    }


    @Override
    public ProfesorOutputDto updateProfesor(Integer id, ProfesorInputDto profesorInputDto) {
        //Creo un profesor que será igual al que ya hay en el repositorio.
        Profesor profesorUpdate = profesorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(" Profesor no encontrado."));

        if (profesorInputDto.getComments() != null) profesorUpdate.setComments(profesorInputDto.getComments());
        if (profesorInputDto.getBranch() != null) profesorUpdate.setBranch(profesorInputDto.getBranch());

        //Guardo los cambios
        profesorRepository.save(profesorUpdate);

        return new ProfesorOutputDto(profesorUpdate);
    }


    @Override
    public String deleteProfesorById(Integer id) {
        Profesor profesor = profesorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado."));
        profesorRepository.delete(profesor);
        return "El profesor con id_Profesor " + id + " ha sido borrad@.";
    }
}









