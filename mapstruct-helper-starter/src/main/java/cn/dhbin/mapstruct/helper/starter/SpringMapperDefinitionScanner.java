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

package cn.dhbin.mapstruct.helper.starter;

import cn.dhbin.mapstruct.helper.core.BeanConvertMapper;
import cn.dhbin.mapstruct.helper.core.ClassKey;
import cn.dhbin.mapstruct.helper.core.MapperDefinition;
import cn.dhbin.mapstruct.helper.core.scaner.MapperDefinitionScanner;
import cn.hutool.core.util.ClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dhbin
 */
@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
public class SpringMapperDefinitionScanner implements MapperDefinitionScanner<BeanConvertMapper> {

    private final ApplicationContext context;

    @Override
    public Class<BeanConvertMapper> getMapperClass() {
        return BeanConvertMapper.class;
    }

    @Override
    public Map<ClassKey, MapperDefinition> scan() {
        Map<String, BeanConvertMapper> beansOfType = context.getBeansOfType(BeanConvertMapper.class);
        Collection<BeanConvertMapper> beanConvertMappers = beansOfType.values();

        Map<ClassKey, MapperDefinition> mapperDefinitionMap = new HashMap<>(beanConvertMappers.size());

        for (BeanConvertMapper beanConvertMapper : beanConvertMappers) {
            Class<?> sourceType = ClassUtil.getTypeArgument(beanConvertMapper.getClass(), 0);
            Class<?> targetType = ClassUtil.getTypeArgument(beanConvertMapper.getClass(), 1);
            ClassKey classKey = new ClassKey(sourceType, targetType);
            MapperDefinition mapperDefinition = new MapperDefinition<>(sourceType, targetType, beanConvertMapper);
            mapperDefinitionMap.put(classKey, mapperDefinition);
        }

        return mapperDefinitionMap;
    }
}
