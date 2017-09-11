package com.tom.demo;

import com.tom.demo.dao.IpCountDao;
import com.tom.demo.entity.IpCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@SpringBootApplication
@Controller
public class DemoSjApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSjApplication.class, args);
	}

	@Autowired
	private IpCountDao ipCountDao;


	@RequestMapping("")
	@ResponseBody
	public Object socket(){
		IpCount ipCount = new IpCount();
		ipCount.setCount(1);
		ipCount.setCreateTime(new Date());
		ipCount.setIp("127.0.0.1");
		ipCount.setLastTime(new Date());
		IpCount save = ipCountDao.save(ipCount);
		return save;
	}
}
