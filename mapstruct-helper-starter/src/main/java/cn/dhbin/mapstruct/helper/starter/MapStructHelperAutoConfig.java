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
import cn.dhbin.mapstruct.helper.core.MapperConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dhbin
 */
@Configuration
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
@ImportAutoConfiguration(MapstructInject.class)
public class MapStructHelperAutoConfig {


    @Bean
    @ConditionalOnMissingBean(MapperConfig.class)
    public MapperConfig mapperConfig(ApplicationContext context) {
        return MapperConfig.builder()
                .mapperDefinitionScanner(new SpringMapperDefinitionScanner(context))
                .convertFunction((mapper, source) -> ((BeanConvertMapper) mapper).to(source))
                .build();
    }


}
