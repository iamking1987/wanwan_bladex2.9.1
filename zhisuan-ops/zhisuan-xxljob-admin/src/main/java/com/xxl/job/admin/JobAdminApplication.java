package com.xxl.job.admin;

import org.springzhisuan.common.constant.LauncherConstant;
import org.springzhisuan.core.launch.ZhisuanApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
public class JobAdminApplication {

	public static void main(String[] args) {
		ZhisuanApplication.run(LauncherConstant.APPLICATION_XXLJOB_ADMIN_NAME, JobAdminApplication.class, args);
	}

}
