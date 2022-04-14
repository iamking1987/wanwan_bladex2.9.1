package com.fun.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import com.fun.test.entity.Student;
import com.fun.test.mapper.StudentMapper;
import com.fun.test.service.StudentService;
import com.fun.test.vo.StudentVO;
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
