package com.cobby.main.stat.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cobby.main.stat.db.entity.Stat;

@Repository
public interface StatRepository extends JpaRepository<Stat, String> {
}
