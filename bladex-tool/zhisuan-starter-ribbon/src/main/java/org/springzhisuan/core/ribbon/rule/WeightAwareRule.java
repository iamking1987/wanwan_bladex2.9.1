/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package org.springzhisuan.core.ribbon.rule;

import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import com.netflix.loadbalancer.Server;
import org.springzhisuan.core.ribbon.predicate.MetadataAwarePredicate;
import org.springzhisuan.core.ribbon.predicate.MetadataServer;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ribbon 路由规则器
 *
 * @author dream.lu
 */
public class WeightAwareRule extends DiscoveryEnabledRule {
	public WeightAwareRule() {
		super(MetadataAwarePredicate.INSTANCE);
	}

	@Override
	public List<Server> filterServers(List<Server> serverList) {
		Chooser<String, Server> instanceChooser = new Chooser<>("zhisuan.ribbon.rule");

		List<Pair<Server>> hostsWithWeight = serverList.stream()
			.map(serviceInstance -> new Pair<>(serviceInstance, getWeight(serviceInstance)))
			.collect(Collectors.toList());

		instanceChooser.refresh(hostsWithWeight);
		Server server = instanceChooser.randomWithWeight();

		return Collections.singletonList(server);
	}

	/**
	 * Get {@link ServiceInstance} weight metadata.
	 *
	 * @param serviceInstance instance
	 * @return The weight of the instance.
	 */
	protected Double getWeight(Server serviceInstance) {
		MetadataServer metadataServer = new MetadataServer(serviceInstance);
		return Double.parseDouble(metadataServer.getMetadata().get("weight"));
	}

}
