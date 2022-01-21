package com.easy.java.starter.seata.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * State lang resource util.
 *
 * @author wang.liang
 * @date 2020/4/15
 */
public class ResourceUtil {

	private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

	public static Resource[] getResources(String location) {
		try {
			return RESOURCE_RESOLVER.getResources(location);
		} catch (IOException var3) {
			return new Resource[0];
		}
	}

	public static Resource[] getResources(String[] locationArr) {
		return Stream
				.of(Optional.ofNullable(locationArr).orElse(new String[0]))
				.flatMap((location) -> Stream.of(getResources(location)))
				.toArray(Resource[]::new);
	}
}