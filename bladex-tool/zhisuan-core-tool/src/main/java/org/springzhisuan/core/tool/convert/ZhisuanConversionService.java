package org.springzhisuan.core.tool.convert;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * 类型 转换 服务，添加了 IEnum 转换
 *
 * @author L.cm
 */
public class ZhisuanConversionService extends ApplicationConversionService {
	@Nullable
	private static volatile ZhisuanConversionService SHARED_INSTANCE;

	public ZhisuanConversionService() {
		this(null);
	}

	public ZhisuanConversionService(@Nullable StringValueResolver embeddedValueResolver) {
		super(embeddedValueResolver);
		super.addConverter(new EnumToStringConverter());
		super.addConverter(new StringToEnumConverter());
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p>
	 * Note: This method actually returns an {@link ZhisuanConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 * @return the shared {@code ZhisuanConversionService} instance (never{@code null})
	 */
	public static GenericConversionService getInstance() {
		ZhisuanConversionService sharedInstance = ZhisuanConversionService.SHARED_INSTANCE;
		if (sharedInstance == null) {
			synchronized (ZhisuanConversionService.class) {
				sharedInstance = ZhisuanConversionService.SHARED_INSTANCE;
				if (sharedInstance == null) {
					sharedInstance = new ZhisuanConversionService();
					ZhisuanConversionService.SHARED_INSTANCE = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

}
