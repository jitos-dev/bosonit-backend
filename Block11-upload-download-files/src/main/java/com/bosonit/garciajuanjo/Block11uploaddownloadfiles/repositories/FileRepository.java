package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.repositories;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
