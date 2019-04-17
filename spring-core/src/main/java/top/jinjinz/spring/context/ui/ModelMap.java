package top.jinjinz.spring.context.ui;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * mvc的通用Model持有者
 * @author jinjin
 * @date 2019-04-17
 */
public class ModelMap extends LinkedHashMap<String, Object> {

        public ModelMap() {
        }

        /**
         * @see #addAttribute(String, Object)
         */
        public ModelMap(String attributeName, Object attributeValue) {
            addAttribute(attributeName, attributeValue);
        }


        /**
         * 根据名称添加属性
         * @param attributeName 名称
         * @param attributeValue 属性
         */
        public ModelMap addAttribute(String attributeName,Object attributeValue) {
            put(attributeName, attributeValue);
            return this;
        }



        /**
         * 增加提供的map
         * @see #addAttribute(String, Object)
         */
        public ModelMap addAllAttributes(Map<String, ?> attributes) {
            if (attributes != null) {
                putAll(attributes);
            }
            return this;
        }

        /**
         * 根据key值合并提供的map
         */
        public ModelMap mergeAttributes(Map<String, ?> attributes) {
            if (attributes != null) {
                attributes.forEach((key, value) -> {
                    if (!containsKey(key)) {
                        put(key, value);
                    }
                });
            }
            return this;
        }

        /**
         * 判断此名称是否存在于map中
         */
        public boolean containsAttribute(String attributeName) {
            return containsKey(attributeName);
        }

    }
