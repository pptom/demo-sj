package com.tom.demo.dao;

import com.tom.demo.entity.Asin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tom.tang on 2017/6/19.
 */
public interface AsinDao extends JpaRepository<Asin, String> {
}
