package com.test.supermarket.repository;

import com.test.supermarket.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale,Long> {

}
