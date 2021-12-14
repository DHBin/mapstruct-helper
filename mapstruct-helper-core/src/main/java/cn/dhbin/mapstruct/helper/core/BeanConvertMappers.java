/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.dhbin.mapstruct.helper.core;

import cn.dhbin.mapstruct.helper.core.exception.MapperDefinitionNotFoundException;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author dhbin
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanConvertMappers {

    private static Map<ClassKey, MapperDefinition> mapperDefinitionMap;

    private static MapperConfig mapperConfig;

    private static BiFunction<Object, Object, Object> convertFunction;

    public static synchronized void config(MapperConfig mapperConfig) {
        mapperDefinitionMap = mapperConfig.getMapperDefinitionScanner().scan();
        BeanConvertMappers.mapperConfig = mapperConfig;
        convertFunction = mapperConfig.getConvertFunction();
    }

    public static <S, T, M> M getMapper(Class<S> sourceClass, Class<T> targetClass) {
        if (mapperDefinitionMap == null) {
            throw new NullPointerException("mapperDefinitionMap == null, may not execute init() method");
        }
        if (mapperConfig.isSupportSubclass()) {
            for (Map.Entry<ClassKey, MapperDefinition> definitionEntry : mapperDefinitionMap.entrySet()) {
                ClassKey classKey = definitionEntry.getKey();
                // 支持sourceClass为子类
                if (classKey.getSourceClass().isAssignableFrom(sourceClass) && classKey.getTargetClass().equals(targetClass)) {
                    return (M) definitionEntry.getValue().getConvert();
                }
            }
            throw new MapperDefinitionNotFoundException(sourceClass, targetClass);
        } else {
            ClassKey classKey = new ClassKey(sourceClass, targetClass);
            MapperDefinition mapperDefinition = mapperDefinitionMap.get(classKey);
            if (mapperDefinition == null) {
                throw new MapperDefinitionNotFoundException(sourceClass, targetClass);
            }
            return (M) mapperDefinition.getConvert();
        }
    }

    public static <T> T convert(Object source, Class<T> targetClass) {
        Object mapper = getMapper(source.getClass(), targetClass);
        return (T) convertFunction.apply(mapper, source);
    }

    public static <T> T convert(Object source, T target) {
        return (T) convert(source, target.getClass());
    }

}
