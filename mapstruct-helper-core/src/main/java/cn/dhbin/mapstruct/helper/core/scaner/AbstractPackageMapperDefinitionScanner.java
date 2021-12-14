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

package cn.dhbin.mapstruct.helper.core.scaner;

import cn.dhbin.mapstruct.helper.core.ClassKey;
import cn.dhbin.mapstruct.helper.core.MapperDefinition;
import cn.hutool.core.util.ClassUtil;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author dhbin
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractPackageMapperDefinitionScanner<M> implements MapperDefinitionScanner<M> {

    private final String[] scanPackages;

    /**
     * @param scanPackages 扫描包路径
     */
    public AbstractPackageMapperDefinitionScanner(String... scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public Map<ClassKey, MapperDefinition> scan() {
        Class mapperClass = getMapperClass();
        Reflections reflections = new Reflections((Object) scanPackages);
        Set<Class> subTypesOf = (Set<Class>) reflections.getSubTypesOf(mapperClass);
        Map<ClassKey, MapperDefinition> mapperDefinitionMap = new HashMap<>(subTypesOf.size());

        for (Class mapper : subTypesOf) {
            if (mapper.isInterface()) {
                continue;
            }
            try {
                M instance = (M) mapper.newInstance();
                Class<?> sourceType = ClassUtil.getTypeArgument(mapper, 0);
                Class<?> targetType = ClassUtil.getTypeArgument(mapper, 1);

                ClassKey classKey = new ClassKey(sourceType, targetType);
                MapperDefinition mapperDefinition = new MapperDefinition<>(sourceType, targetType, instance);
                mapperDefinitionMap.put(classKey, mapperDefinition);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("can not new instance for " + mapper.getName() + ": " + e.getMessage());
            }
        }

        return mapperDefinitionMap;
    }

}
