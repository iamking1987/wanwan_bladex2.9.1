package com.fun.sharding.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fun.sharding.pojo.Student;
import com.fun.sharding.pojo.StudentVO;

/**
 * 学生表-测试(student)表服务接口
 *
 * @author zz
 * @since 2022-03-23 16:46:12
 */
public interface StudentService extends IService<Student> {

	/**
	 * 自定义分页
	 */
	IPage<StudentVO> selectStudentPage(IPage<StudentVO> page, StudentVO student);
}
