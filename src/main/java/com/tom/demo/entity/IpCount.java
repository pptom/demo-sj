package com.tom.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by tom.tang on 2017/7/20.
 */
@Entity
public class IpCount {
	@Id
	@GeneratedValue
	private Long id;
	//ip地址
	private String ip;
	//创建时间，即首次访问时间
	private Date createTime;
	//上次访问时间
	private Date lastTime;
	//访问次数
	private Integer count;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "IpCount{" +
				"id=" + id +
				", ip='" + ip + '\'' +
				", createTime=" + createTime +
				", lastTime=" + lastTime +
				", count=" + count +
				'}';
	}
}
