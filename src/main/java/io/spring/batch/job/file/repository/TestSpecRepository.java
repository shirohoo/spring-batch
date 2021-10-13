package io.spring.batch.job.file.repository;

import io.spring.batch.job.file.model.TestSpec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSpecRepository extends JpaRepository<TestSpec, Long> {

}
