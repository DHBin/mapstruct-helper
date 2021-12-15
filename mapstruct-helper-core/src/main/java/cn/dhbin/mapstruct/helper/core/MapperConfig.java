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

import cn.dhbin.mapstruct.helper.core.scaner.DefaultMapperDefinitionScanner;
import cn.dhbin.mapstruct.helper.core.scaner.MapperDefinitionScanner;
import lombok.Builder;
import lombok.Data;

import java.util.function.BiFunction;

/**
 * Mapper配置
 *
 * @author dhbin
 */
@Data
@Builder
public class MapperConfig {

    private MapperDefinitionScanner<?> mapperDefinitionScanner;

    private boolean supportSubclass;

    private BiFunction<Object, Object, Object> convertFunction;


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static MapperConfig.MapperConfigBuilder defaultConfig(String... scanPackages) {
        return MapperConfig.builder()
                .mapperDefinitionScanner(new DefaultMapperDefinitionScanner(scanPackages))
                .supportSubclass(false)
                .convertFunction((mapper, source) -> {
                    return ((BeanConvertMapper) mapper).to(source);
                });
    }
}
