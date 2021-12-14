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

import cn.dhbin.mapstruct.helper.core.bean.BooBean;
import cn.dhbin.mapstruct.helper.core.bean.FooBean;
import cn.dhbin.mapstruct.helper.core.bean.FooSubBean;
import cn.dhbin.mapstruct.helper.core.exception.MapperDefinitionNotFoundException;
import cn.dhbin.mapstruct.helper.core.mapper.TestConvertMapper;
import cn.dhbin.mapstruct.helper.core.mapper.TestConvertMapperScan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author dhbin
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class BeanConvertMappersTest {

    @Test
    void getMapper() {
        BeanConvertMappers.config(MapperConfig.defaultConfig("cn.dhbin.mapstruct").supportSubclass(true).build());

        BeanConvertMapper<FooBean, BooBean> mapper = BeanConvertMappers.getMapper(FooBean.class, BooBean.class);
        FooBean fooBean = new FooBean();
        fooBean.setName("xxx");
        assertEquals("xxx", mapper.to(fooBean).getName());

        FooSubBean fooSubBean = new FooSubBean();
        fooSubBean.setName("xxx");
        assertEquals("xxx", mapper.to(fooSubBean).getName());

        BeanConvertMapper<FooSubBean, BooBean> mapper1 = BeanConvertMappers.getMapper(FooSubBean.class, BooBean.class);
        FooSubBean fooSubBean3 = new FooSubBean();
        fooSubBean3.setName("xxx");
        assertEquals("xxx", mapper1.to(fooSubBean3).getName());

        BeanConvertMappers.config(MapperConfig.defaultConfig("cn.dhbin.mapstruct").build());

        assertThrows(MapperDefinitionNotFoundException.class, () -> BeanConvertMappers.getMapper(FooSubBean.class, BooBean.class));

        BeanConvertMappers.config(MapperConfig.builder()
                .mapperDefinitionScanner(new TestConvertMapperScan("cn.dhbin.mapstruct"))
                .supportSubclass(true)
                .convertFunction((m, source) -> {
                    return ((TestConvertMapper) m).convert(source);
                })
                .build()
        );


        // 自定义Mapper
        TestConvertMapper<FooBean, BooBean> mapper2 = BeanConvertMappers.getMapper(FooBean.class, BooBean.class);
        assertEquals("xxx", mapper2.convert(fooBean).getName());
    }

    @Test
    void convert() {
        BeanConvertMappers.config(MapperConfig.defaultConfig("cn.dhbin.mapstruct").build());

        FooBean fooBean = new FooBean();
        fooBean.setName("xxx");
        assertEquals("xxx", BeanConvertMappers.convert(fooBean, BooBean.class).getName());
        assertEquals("xxx", BeanConvertMappers.convert(fooBean, new BooBean()).getName());

        FooSubBean fooSubBean = new FooSubBean();
        fooSubBean.setName("xxx");
        assertThrows(MapperDefinitionNotFoundException.class, () -> BeanConvertMappers.convert(fooSubBean, BooBean.class));

        BeanConvertMappers.config(MapperConfig.defaultConfig("cn.dhbin.mapstruct").supportSubclass(true).build());
        assertEquals("xxx", BeanConvertMappers.convert(fooSubBean, BooBean.class).getName());
        assertEquals("xxx", BeanConvertMappers.convert(fooSubBean, new BooBean()).getName());

    }
}