package org.orh.spring.core;

import org.junit.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * PropertySourceTest
 *
 * @author ouronghui
 * @since 2022/8/26 17:52
 */
public class PropertySourceTest {

    @Test
    public void test() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "gbk");
        PropertySource propertySource1 = new MapPropertySource("map", map);
        System.out.println(propertySource1.getProperty("encoding"));

        //name, location
        ResourcePropertySource propertySource2 = new ResourcePropertySource("resource", "classpath:resources.properties");
        System.out.println(propertySource2.getProperty("encoding"));

        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(propertySource1);
        propertySources.addLast(propertySource2);
    }
}
