package me.youzheng.common.typeutil;

import java.util.List;
import java.util.Map;

public interface ConvertType<T> {

    ConvertType<Map<String, Object>> DEFAULT_MAP_TYPE = new ConvertType<Map<String, Object>>() {};
    ConvertType<List<Map<String, Object>>> DEFAULT_MAP_LIST_TYPE = new ConvertType<List<Map<String, Object>>>() {};

}