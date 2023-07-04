package com.bosonit.formacion.block14springsecurity.asignatura.controller;

import com.bosonit.formacion.block14springsecurity.asignatura.application.AsignaturaService;
import com.bosonit.formacion.block14springsecurity.asignatura.controller.dto.AsignaturaInputDto;
import com.bosonit.formacion.block14springsecurity.asignatura.controller.dto.AsignaturaOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {
    @Autowired
    AsignaturaService asignaturaService;


    @PostMapping
    public ResponseEntity<AsignaturaOutputDto> addAsignatura(@RequestBody AsignaturaInputDto asignaturaInputDto) {
        return new ResponseEntity<>(asignaturaService.addAsignatura(asignaturaInputDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaOutputDto> getAsignaturaById(@PathVariable Integer id) {
        return new ResponseEntity<>(asignaturaService.getAsignaturaById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllAsignaturas() {
        try {
            List<AsignaturaOutputDto> asignaturaOutputDtoList = asignaturaService.getAllAsignaturas();
            if (asignaturaOutputDtoList.isEmpty()) {
                throw new Exception("Lista vacía.");
            }
            return new ResponseEntity<>(asignaturaOutputDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaOutputDto> updateAsignatura(@PathVariable("id") Integer id, @RequestBody AsignaturaInputDto asignaturaInputDto) {
        return new ResponseEntity<>(asignaturaService.updateAsignatura(id, asignaturaInputDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsignaturaById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(asignaturaService.deleteAsignaturaById(id), HttpStatus.OK);
    }
}




