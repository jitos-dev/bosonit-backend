package com.bosonit.formacion.block14springsecurity.asignatura.repository;

import com.bosonit.formacion.block14springsecurity.asignatura.domain.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {
}
