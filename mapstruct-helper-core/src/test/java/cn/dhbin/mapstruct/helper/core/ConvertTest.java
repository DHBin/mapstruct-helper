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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 00113868
 */
class ConvertTest {

    @Test
    void convert() {
        BeanConvertMappers.config(MapperConfig.defaultConfig("cn.dhbin.mapstruct").supportSubclass(true).build());

        FooBean fooBean = new FooBean();
        fooBean.setName("xxx");
        assertEquals("xxx", fooBean.convert(BooBean.class).getName());

        FooSubBean fooSubBean = new FooSubBean();
        fooSubBean.setName("xxx");
        assertEquals("xxx", fooSubBean.convert(BooBean.class).getName());
    }
}