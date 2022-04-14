package com.fun.sharding.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fun.sharding.mapper.StudentMapper;
import com.fun.sharding.pojo.Student;
import com.fun.sharding.pojo.StudentVO;
import com.fun.sharding.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 学生表-测试(student)表服务实现类
 *
 * @author zz
 * @since 2022-03-23 16:46:14
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

	/**
	 * 自定义分页
	 */
	@Override
	public IPage<StudentVO> selectStudentPage(IPage<StudentVO> page, StudentVO student) {
		return page.setRecords(baseMapper.selectStudentPage(page, student));
	}
}
