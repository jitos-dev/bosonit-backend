package com.bosonit.garciajuanjo.block7crudvalidation.client;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8081/teacher/", name = "teacherFeignClient")
public interface TeacherFeignClient {

    @GetMapping("{id}")
    TeacherOutputDto getById(@PathVariable String id);
}
