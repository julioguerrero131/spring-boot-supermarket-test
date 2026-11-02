package com.test.supermarket.repository;

import com.test.supermarket.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
