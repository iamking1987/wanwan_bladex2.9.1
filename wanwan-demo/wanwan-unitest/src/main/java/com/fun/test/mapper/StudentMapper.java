package com.fun.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fun.test.entity.Student;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.fun.test.entity.Student;
import com.fun.test.vo.StudentVO;

import java.util.HashMap;
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
