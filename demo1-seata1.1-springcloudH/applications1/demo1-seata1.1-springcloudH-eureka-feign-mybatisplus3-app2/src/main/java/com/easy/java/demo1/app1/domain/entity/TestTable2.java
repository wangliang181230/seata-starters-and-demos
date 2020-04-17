package com.easy.java.demo1.app1.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:58
 */
@TableName("test_table2")
public class TestTable2 {

	@TableId
	private Long id;

	private String name;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}