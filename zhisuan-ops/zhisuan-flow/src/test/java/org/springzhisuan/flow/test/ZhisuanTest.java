package org.springzhisuan.flow.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springzhisuan.core.test.ZhisuanBootTest;
import org.springzhisuan.core.test.ZhisuanSpringRunner;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.flow.engine.entity.FlowModel;
import org.springzhisuan.flow.engine.service.FlowEngineService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Zhisuan单元测试
 *
 * @author Chill
 */
@RunWith(ZhisuanSpringRunner.class)
@ZhisuanBootTest(appName = "zhisuan-flow", enableLoader = true)
public class ZhisuanTest {

	@Autowired
	private FlowEngineService service;

	@Test
	public void contextLoads() {
		System.out.println("=====数据迁移启动=====");

		// 获取 ACT_DE_MODEL 表需要转换的数据
		List<FlowModel> list = service.list();
		// 循环转换
		list.forEach(flowModel -> {
			if (StringUtil.isBlank(flowModel.getModelEditorXml())) {
				service.update(Wrappers.<FlowModel>lambdaUpdate()
					.set(FlowModel::getModelEditorXml, new String(service.getModelEditorXML(flowModel)))
					.ge(FlowModel::getId, flowModel.getId())
				);
			}
		});

		System.out.println("=====数据迁移完毕=====");
	}

}
