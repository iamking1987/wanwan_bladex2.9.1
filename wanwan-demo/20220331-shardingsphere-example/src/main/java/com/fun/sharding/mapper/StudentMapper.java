package com.fun.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fun.sharding.pojo.Student;
import com.fun.sharding.pojo.StudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生表-测试(student)Mapper接口
 *
 * @author zz
 * @since 2022-03-23 16:46:15
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
	/**
	 * 自定义分页
	 */
	List<StudentVO> selectStudentPage(IPage<StudentVO> page, @Param("student") Student student);
}
