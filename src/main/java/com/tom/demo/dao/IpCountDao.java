package com.tom.demo.dao;

import com.tom.demo.entity.IpCount;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by tom.tang on 2017/7/20.
 */
public interface IpCountDao extends JpaRepository<IpCount, Long> {
}
