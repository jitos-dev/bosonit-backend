package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentSubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;
    private StudentRepository studentRepository;
    private StudentSubjectRepository studentSubjectRepository;

    @Override
    public List<PersonOutputDto> getAll() {
        return personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();
    }

    @Override
    public Optional<PersonOutputDto> getById(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return Optional.of(person.personToPersonOutputDto());
    }

    @Override
    public List<PersonOutputDto> getByUser(String user) {
        List<Person> personList = personRepository.findByUser(user);

        if (personList.isEmpty())
            throw new EntityNotFoundException();

        return personList.stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }

    @Override
    public Optional<PersonOutputDto> save(PersonInputDto personInputDto) {
        if (isAllFieldsCorrect(personInputDto)) {
            return Optional.of(personRepository.save(new Person(personInputDto))
                    .personToPersonOutputDto());
        }

        return Optional.empty();
    }

    @Override
    public Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Person personUpdated = getPersonUpdated(personInputDto, person);

        return Optional.of(personRepository.save(personUpdated).personToPersonOutputDto());
    }


    @Transactional
    @Override
    public void delete(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        /*Eliminamos todas los asociados a Person como son Teacher, Student y como eliminamos Student a su vez
        también eliminamos los StudentSubject*/
        Optional<Student> student = studentRepository.findByPersonId(person.getIdPerson());

        if (student.isPresent()) {
            studentSubjectRepository.deleteStudentSubjectByStudentId(student.get().getIdStudent());
            studentRepository.deleteStudentByPersonId(person.getIdPerson());
        }

        personRepository.delete(person);
    }

    private Boolean isAllFieldsCorrect(PersonInputDto personInputDto) {
        if (personInputDto.getUser() == null)
            throw new UnprocessableEntityException("The user field cannot be null");

        if (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10)
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters or greater than 12");

        if (personInputDto.getPassword() == null)
            throw new UnprocessableEntityException("The password field cannot be null");

        if (personInputDto.getName() == null)
            throw new UnprocessableEntityException("The name field cannot be null");

        if (personInputDto.getCompanyEmail() == null)
            throw new UnprocessableEntityException("The company email field cannot be null");

        if (personInputDto.getPersonalEmail() == null)
            throw new UnprocessableEntityException("The personal email field cannot be null");

        if (personInputDto.getCity() == null)
            throw new UnprocessableEntityException("The city field cannot be null");

        if (personInputDto.getActive() == null)
            throw new UnprocessableEntityException("The active field cannot be null");

        if (personInputDto.getCreatedDate() == null)
            throw new UnprocessableEntityException("The created date field cannot be null");

        return true;
    }

    private Person getPersonUpdated(PersonInputDto personInputDto, Person person) {

        if (personInputDto.getUser() != null &&
                (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10))
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters or greater than 12");


        person.setName(personInputDto.getName() == null ? person.getName() : personInputDto.getName());
        person.setUser(personInputDto.getUser() == null ? person.getUser() : personInputDto.getUser());
        person.setPassword(personInputDto.getPassword() == null ? person.getPassword() : personInputDto.getPassword());
        person.setSurname(personInputDto.getSurname() == null ? person.getSurname() : personInputDto.getSurname());
        person.setCompanyEmail(personInputDto.getCompanyEmail() == null ? person.getCompanyEmail() : personInputDto.getCompanyEmail());
        person.setPersonalEmail(personInputDto.getPersonalEmail() == null ? person.getPersonalEmail() : personInputDto.getPersonalEmail());
        person.setCity(personInputDto.getCity() == null ? person.getCity() : personInputDto.getCity());
        person.setActive(personInputDto.getActive() == null ? person.getActive() : personInputDto.getActive());
        person.setCreatedDate(personInputDto.getCreatedDate() == null ? person.getCreatedDate() : personInputDto.getCreatedDate());
        person.setImageUrl(personInputDto.getImageUrl() == null ? person.getImageUrl() : personInputDto.getImageUrl());
        person.setTerminationDate(personInputDto.getTerminationDate() == null ? person.getTerminationDate() : personInputDto.getTerminationDate());
        return person;
    }
}
