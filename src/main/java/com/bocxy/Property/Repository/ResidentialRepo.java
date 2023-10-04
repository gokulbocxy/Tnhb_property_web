package com.bocxy.Property.Repository;

import com.bocxy.Property.Entity.Residential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentialRepo extends JpaRepository<Residential, Long> {
}