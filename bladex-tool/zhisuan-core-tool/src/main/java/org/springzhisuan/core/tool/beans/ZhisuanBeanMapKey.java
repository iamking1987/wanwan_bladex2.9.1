package org.springzhisuan.core.tool.beans;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * bean map key，提高性能
 *
 * @author L.cm
 */
@EqualsAndHashCode
@AllArgsConstructor
public class ZhisuanBeanMapKey {
	private final Class type;
	private final int require;
}
