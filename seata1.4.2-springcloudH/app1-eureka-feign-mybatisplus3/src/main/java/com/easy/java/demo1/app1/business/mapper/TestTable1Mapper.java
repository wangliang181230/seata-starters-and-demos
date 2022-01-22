package com.easy.java.demo1.app1.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import org.apache.ibatis.annotations.Select;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:53
 */
public interface TestTable1Mapper extends BaseMapper<TestTable1> {

	@Select("select * from test_table1 where id = #{id} for update")
	TestTable1 selectByIdForUpdate(String id);

}
