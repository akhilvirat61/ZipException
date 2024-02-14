package com.dailycodebuffer.Springboot.tutorial.repository;

import com.dailycodebuffer.Springboot.tutorial.entity.ZipFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZipFileRepository extends JpaRepository<ZipFile, Long> {
}