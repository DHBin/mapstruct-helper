# mapstruct-helper

简化mapstruct使用

## 使用方法

```xml

<dependency>
    <groupId>cn.dhbin</groupId>
    <artifactId>mapstruct-helper-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 例子

```java
// 第一步：定义Mapper，继承cn.dhbin.mapstruct.helper.core.BeanConvertMapper
@Mapper
public interface FooToBooMapper extends BeanConvertMapper<FooBean, BooBean> {
}

// 第二步：扫描装载Mapper（只需要配置一次）
BeanConvertMappers.config(MapperConfig.defaultConfig("package").build());

        FooBean fooBean=new FooBean();
        fooBean.setName("xxx");

// 使用
        assertEquals("xxx",BeanConvertMappers.convert(fooBean,BooBean.class).getName());
        assertEquals("xxx",BeanConvertMappers.convert(fooBean,new BooBean()).getName());
```

## API

### Mapper配置

```java
BeanConvertMappers.config(
        MapperConfig.builder()
        // 是否支持待复制Bean的子类
        .supportSubclass(true)
        // mapper扫描器
        .mapperDefinitionScanner(new DefaultMapperDefinitionScanner("scanPackage"))
        // 转换方法
        .convertFunction((mapper,source)->{
        return((BeanConvertMapper)mapper).to(source);
        })
        .build()
```

#### 默认配置

```java
// 默认不支持待复制Bean的子类
MapperConfig.defaultConfig("scanPackage").build()
```

#### 扩展

默认是使用`cn.dhbin.mapstruct.helper.core.BeanConvertMapper`作为模板生成Mapper，但考虑到兼容性问题，支持自定义模板。

比如项目中原来的模板如下：

```java
public interface MyBeanConvertMapper<SOURCE, TARGET> {

    /**
     * source to target
     *
     * @param source source
     * @return target
     */
    TARGET convert(SOURCE source);

}

```

通过以下配置实现兼容：

```java
BeanConvertMappers.config(MapperConfig.builder()
        .supportSubclass(false)
        .mapperDefinitionScanner(new AbstractPackageMapperDefinitionScanner<MyBeanConvertMapper>("package"){

@Override
public Class<MyBeanConvertMapper> getMapperClass(){
        return MyBeanConvertMapper.class;
                   }
                           })
                           .convertFunction((mapper,source)->{
                           return((MyBeanConvertMapper)mapper).convert(source);
                           })
                           .build());
```

### 转换/复制属性

```java
public static<T> T convert(Object source,Class<T> targetClass);
public static<T> T convert(Object source,T target);
```

## LICENSE

```
Copyright 2021 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```





