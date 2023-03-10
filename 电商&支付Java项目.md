

### 5.2 MyBatis部分



（1）编写pojo对象(注意驼峰编写，根据数据库所实现的字段进行)

Category.java

```java
package com.imooc.mall.pojo;

import java.util.Date;

/**
 * @author King
 * @create 2022-12-29 1:01 下午
 * @Description : po(persistent object) 持久层对象
 * pojo(plian ordinary java object)
 */
public class Category {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    public Category() {
    }

    public Category(Integer id, Integer parentId, String name, Integer status, Integer sortOrder, Date createTime, Date updateTime) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.status = status;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", sortOrder=" + sortOrder +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
```



（2）编写dao (注意在此处加上@Mapper，否则使用Autowire自动装配会报错无bean)（也可以不使用@Mapper，在主程序入口`@MapperScan(basePackages = "com.imooc.mall.dao"`)代替）

CategoryMapper.interface

```java
@Mapper
public interface CategoryMapper {

    @Select("select * from mall_category where id = #{id}")
    Category findById(@Param("id") Integer id);
}
```



（3）application.yml配置 （数据库连接和驼峰配置）

```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456789
    url: jdbc:mysql://127.0.0.1:3306/mall?characterEncoding=utf-8&useSSL=false
    
mybatis:
  configuration:
    map-underscore-to-camel-case: true
```





### 5.3 IDEA配置

（1）自动导包

<img src="/Users/liangjingfeng/Library/Application Support/typora-user-images/image-20221230001229798.png" alt="image-20221230001229798" style="zoom:200%;" />



(2)Lombok

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

@Data



### 5.4 Mybatis Xml使用

（1）先在application.yml中声明出mybatis的xml文件的路径

```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapper/*.xml
```



（2）**编写SQL时候尽量少用*，而是把每个特定字段给写出来**

同时使用<sql> <include>来封装

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.imooc.mall.dao.CategoryMapper">
    <sql id="Base_Column_List">
        id,parent_id,name,status,sort_order,create_time,update_time
    </sql>
    <!-- Category queryById(Integer id); -->
    <select id="queryById" resultType="com.imooc.mall.pojo.Category">
        select
        <include refid="Base_Column_List"></include>
        from mall_category where id = #{id}
    </select>

</mapper>
```



（3）为每一个Mapper接口创建一个测试类，

同时新建的测试类上加入

```
@RunWith(SpringRunner.class)
@SpringBootTest
```

或者新建的测试类继承主测试类 (主测试类可以内容只留下上述两个注解)

![image-20221230005635787](/Users/liangjingfeng/Library/Application Support/typora-user-images/image-20221230005635787.png)





### 5.5 MyBatis三剑客

(1)开发生成器：连接数据库 ——> 获取表结构 -> 生成文件

引入依赖(overwrite配置是否覆盖原先生成的)

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.7</version>
    <configuration>
        <overwrite>true</overwrite>
    </configuration>
</plugin>
```

Mybatis Generator 配置详解

```XML
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<!-- 配置生成器 -->
<generatorConfiguration>
<!-- 可以用于加载配置项或者配置文件，在整个配置文件中就可以使用${propertyKey}的方式来引用配置项
    resource：配置资源加载地址，使用resource，MBG从classpath开始找，比如com/myproject/generatorConfig.properties        
    url：配置资源加载地质，使用URL的方式，比如file:///C:/myfolder/generatorConfig.properties.
    注意，两个属性只能选址一个;

    另外，如果使用了mybatis-generator-maven-plugin，那么在pom.xml中定义的properties都可以直接在generatorConfig.xml中使用
<properties resource="" url="" />
 -->

 <!-- 在MBG工作的时候，需要额外加载的依赖包
     location属性指明加载jar/zip包的全路径
<classPathEntry location="/Program Files/IBM/SQLLIB/java/db2java.zip" />
  -->

<!-- 
    context:生成一组对象的环境 
    id:必选，上下文id，用于在生成错误时提示
    defaultModelType:指定生成对象的样式
        1，conditional：类似hierarchical；
        2，flat：所有内容（主键，blob）等全部生成在一个对象中；
        3，hierarchical：主键生成一个XXKey对象(key class)，Blob等单独生成一个对象，其他简单属性在一个对象中(record class)
    targetRuntime:
        1，MyBatis3：默认的值，生成基于MyBatis3.x以上版本的内容，包括XXXBySample；
        2，MyBatis3Simple：类似MyBatis3，只是不生成XXXBySample；
    introspectedColumnImpl：类全限定名，用于扩展MBG
-->
<context id="mysql" defaultModelType="hierarchical" targetRuntime="MyBatis3Simple" >

    <!-- 自动识别数据库关键字，默认false，如果设置为true，根据SqlReservedWords中定义的关键字列表；
        一般保留默认值，遇到数据库关键字（Java关键字），使用columnOverride覆盖
     -->
    <property name="autoDelimitKeywords" value="false"/>
    <!-- 生成的Java文件的编码 -->
    <property name="javaFileEncoding" value="UTF-8"/>
    <!-- 格式化java代码 -->
    <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
    <!-- 格式化XML代码 -->
    <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>

    <!-- beginningDelimiter和endingDelimiter：指明数据库的用于标记数据库对象名的符号，比如ORACLE就是双引号，MYSQL默认是`反引号； -->
    <property name="beginningDelimiter" value="`"/>
    <property name="endingDelimiter" value="`"/>

    <!-- 必须要有的，使用这个配置链接数据库
        @TODO:是否可以扩展
     -->
    <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql:///pss" userId="root" password="admin">
        <!-- 这里面可以设置property属性，每一个property属性都设置到配置的Driver上 -->
    </jdbcConnection>

    <!-- java类型处理器 
        用于处理DB中的类型到Java中的类型，默认使用JavaTypeResolverDefaultImpl；
        注意一点，默认会先尝试使用Integer，Long，Short等来对应DECIMAL和 NUMERIC数据类型； 
    -->
    <javaTypeResolver type="org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl">
        <!-- 
            true：使用BigDecimal对应DECIMAL和 NUMERIC数据类型
            false：默认,
                scale>0;length>18：使用BigDecimal;
                scale=0;length[10,18]：使用Long；
                scale=0;length[5,9]：使用Integer；
                scale=0;length<5：使用Short；
         -->
        <property name="forceBigDecimals" value="false"/>
    </javaTypeResolver>

    <!-- java模型创建器，是必须要的元素
        负责：1，key类（见context的defaultModelType）；2，java类；3，查询类
        targetPackage：生成的类要放的包，真实的包受enableSubPackages属性控制；
        targetProject：目标项目，指定一个存在的目录下，生成的内容会放到指定目录中，如果目录不存在，MBG不会自动建目录
     -->
    <javaModelGenerator targetPackage="com.imooc.mall.pojo" targetProject="src/main/java">
        <!--  for MyBatis3/MyBatis3Simple
            自动为每一个生成的类创建一个构造方法，构造方法包含了所有的field；而不是使用setter；
         -->
        <property name="constructorBased" value="false"/>

        <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
        <property name="enableSubPackages" value="true"/>

        <!-- for MyBatis3 / MyBatis3Simple
            是否创建一个不可变的类，如果为true，
            那么MBG会创建一个没有setter方法的类，取而代之的是类似constructorBased的类
         -->
        <property name="immutable" value="false"/>

        <!-- 设置一个根对象，
            如果设置了这个根对象，那么生成的keyClass或者recordClass会继承这个类；在Table的rootClass属性中可以覆盖该选项
            注意：如果在key class或者record class中有root class相同的属性，MBG就不会重新生成这些属性了，包括：
                1，属性名相同，类型相同，有相同的getter/setter方法；
         -->
        <property name="rootClass" value="com._520it.mybatis.domain.BaseDomain"/>

        <!-- 设置是否在getter方法中，对String类型字段调用trim()方法 -->
        <property name="trimStrings" value="true"/>
    </javaModelGenerator>

    <!-- 生成SQL map的XML文件生成器，
        注意，在Mybatis3之后，我们可以使用mapper.xml文件+Mapper接口（或者不用mapper接口），
            或者只使用Mapper接口+Annotation，所以，如果 javaClientGenerator配置中配置了需要生成XML的话，这个元素就必须配置
        targetPackage/targetProject:同javaModelGenerator
     -->
    <sqlMapGenerator targetPackage="com._520it.mybatis.mapper" targetProject="src/main/resources">
        <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
        <property name="enableSubPackages" value="true"/>
    </sqlMapGenerator>

    <!-- 对于mybatis来说，即生成Mapper接口，注意，如果没有配置该元素，那么默认不会生成Mapper接口 
        targetPackage/targetProject:同javaModelGenerator
        type：选择怎么生成mapper接口（在MyBatis3/MyBatis3Simple下）：
            1，ANNOTATEDMAPPER：会生成使用Mapper接口+Annotation的方式创建（SQL生成在annotation中），不会生成对应的XML；
            2，MIXEDMAPPER：使用混合配置，会生成Mapper接口，并适当添加合适的Annotation，但是XML会生成在XML中；
            3，XMLMAPPER：会生成Mapper接口，接口完全依赖XML；
        注意，如果context是MyBatis3Simple：只支持ANNOTATEDMAPPER和XMLMAPPER
    -->
    <javaClientGenerator targetPackage="com._520it.mybatis.mapper" type="ANNOTATEDMAPPER" targetProject="src/main/java">
        <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
        <property name="enableSubPackages" value="true"/>

        <!-- 可以为所有生成的接口添加一个父接口，但是MBG只负责生成，不负责检查
        <property name="rootInterface" value=""/>
         -->
    </javaClientGenerator>

    <!-- 选择一个table来生成相关文件，可以有一个或多个table，必须要有table元素
        选择的table会生成一下文件：
        1，SQL map文件
        2，生成一个主键类；
        3，除了BLOB和主键的其他字段的类；
        4，包含BLOB的类；
        5，一个用户生成动态查询的条件类（selectByExample, deleteByExample），可选；
        6，Mapper接口（可选）

        tableName（必要）：要生成对象的表名；
        注意：大小写敏感问题。正常情况下，MBG会自动的去识别数据库标识符的大小写敏感度，在一般情况下，MBG会
            根据设置的schema，catalog或tablename去查询数据表，按照下面的流程：
            1，如果schema，catalog或tablename中有空格，那么设置的是什么格式，就精确的使用指定的大小写格式去查询；
            2，否则，如果数据库的标识符使用大写的，那么MBG自动把表名变成大写再查找；
            3，否则，如果数据库的标识符使用小写的，那么MBG自动把表名变成小写再查找；
            4，否则，使用指定的大小写格式查询；
        另外的，如果在创建表的时候，使用的""把数据库对象规定大小写，就算数据库标识符是使用的大写，在这种情况下也会使用给定的大小写来创建表名；
        这个时候，请设置delimitIdentifiers="true"即可保留大小写格式；

        可选：
        1，schema：数据库的schema；
        2，catalog：数据库的catalog；
        3，alias：为数据表设置的别名，如果设置了alias，那么生成的所有的SELECT SQL语句中，列名会变成：alias_actualColumnName
        4，domainObjectName：生成的domain类的名字，如果不设置，直接使用表名作为domain类的名字；可以设置为somepck.domainName，那么会自动把domainName类再放到somepck包里面；
        5，enableInsert（默认true）：指定是否生成insert语句；
        6，enableSelectByPrimaryKey（默认true）：指定是否生成按照主键查询对象的语句（就是getById或get）；
        7，enableSelectByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询语句；
        8，enableUpdateByPrimaryKey（默认true）：指定是否生成按照主键修改对象的语句（即update)；
        9，enableDeleteByPrimaryKey（默认true）：指定是否生成按照主键删除对象的语句（即delete）；
        10，enableDeleteByExample（默认true）：MyBatis3Simple为false，指定是否生成动态删除语句；
        11，enableCountByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询总条数语句（用于分页的总条数查询）；
        12，enableUpdateByExample（默认true）：MyBatis3Simple为false，指定是否生成动态修改语句（只修改对象中不为空的属性）；
        13，modelType：参考context元素的defaultModelType，相当于覆盖；
        14，delimitIdentifiers：参考tableName的解释，注意，默认的delimitIdentifiers是双引号，如果类似MYSQL这样的数据库，使用的是`（反引号，那么还需要设置context的beginningDelimiter和endingDelimiter属性）
        15，delimitAllColumns：设置是否所有生成的SQL中的列名都使用标识符引起来。默认为false，delimitIdentifiers参考context的属性

        注意，table里面很多参数都是对javaModelGenerator，context等元素的默认属性的一个复写；
     -->
    <table tableName="userinfo" >

        <!-- 参考 javaModelGenerator 的 constructorBased属性-->
        <property name="constructorBased" value="false"/>

        <!-- 默认为false，如果设置为true，在生成的SQL中，table名字不会加上catalog或schema； -->
        <property name="ignoreQualifiersAtRuntime" value="false"/>

        <!-- 参考 javaModelGenerator 的 immutable 属性 -->
        <property name="immutable" value="false"/>

        <!-- 指定是否只生成domain类，如果设置为true，只生成domain类，如果还配置了sqlMapGenerator，那么在mapper XML文件中，只生成resultMap元素 -->
        <property name="modelOnly" value="false"/>

        <!-- 参考 javaModelGenerator 的 rootClass 属性 
        <property name="rootClass" value=""/>
         -->

        <!-- 参考javaClientGenerator 的  rootInterface 属性
        <property name="rootInterface" value=""/>
        -->

        <!-- 如果设置了runtimeCatalog，那么在生成的SQL中，使用该指定的catalog，而不是table元素上的catalog 
        <property name="runtimeCatalog" value=""/>
        -->

        <!-- 如果设置了runtimeSchema，那么在生成的SQL中，使用该指定的schema，而不是table元素上的schema 
        <property name="runtimeSchema" value=""/>
        -->

        <!-- 如果设置了runtimeTableName，那么在生成的SQL中，使用该指定的tablename，而不是table元素上的tablename 
        <property name="runtimeTableName" value=""/>
        -->

        <!-- 注意，该属性只针对MyBatis3Simple有用；
            如果选择的runtime是MyBatis3Simple，那么会生成一个SelectAll方法，如果指定了selectAllOrderByClause，那么会在该SQL中添加指定的这个order条件；
         -->
        <property name="selectAllOrderByClause" value="age desc,username asc"/>

        <!-- 如果设置为true，生成的model类会直接使用column本身的名字，而不会再使用驼峰命名方法，比如BORN_DATE，生成的属性名字就是BORN_DATE,而不会是bornDate -->
        <property name="useActualColumnNames" value="false"/>

        <!-- generatedKey用于生成生成主键的方法，
            如果设置了该元素，MBG会在生成的<insert>元素中生成一条正确的<selectKey>元素，该元素可选
            column:主键的列名；
            sqlStatement：要生成的selectKey语句，有以下可选项：
                Cloudscape:相当于selectKey的SQL为： VALUES IDENTITY_VAL_LOCAL()
                DB2       :相当于selectKey的SQL为： VALUES IDENTITY_VAL_LOCAL()
                DB2_MF    :相当于selectKey的SQL为：SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1
                Derby      :相当于selectKey的SQL为：VALUES IDENTITY_VAL_LOCAL()
                HSQLDB      :相当于selectKey的SQL为：CALL IDENTITY()
                Informix  :相当于selectKey的SQL为：select dbinfo('sqlca.sqlerrd1') from systables where tabid=1
                MySql      :相当于selectKey的SQL为：SELECT LAST_INSERT_ID()
                SqlServer :相当于selectKey的SQL为：SELECT SCOPE_IDENTITY()
                SYBASE      :相当于selectKey的SQL为：SELECT @@IDENTITY
                JDBC      :相当于在生成的insert元素上添加useGeneratedKeys="true"和keyProperty属性
        <generatedKey column="" sqlStatement=""/>
         -->

        <!-- 
            该元素会在根据表中列名计算对象属性名之前先重命名列名，非常适合用于表中的列都有公用的前缀字符串的时候，
            比如列名为：CUST_ID,CUST_NAME,CUST_EMAIL,CUST_ADDRESS等；
            那么就可以设置searchString为"^CUST_"，并使用空白替换，那么生成的Customer对象中的属性名称就不是
            custId,custName等，而是先被替换为ID,NAME,EMAIL,然后变成属性：id，name，email；

            注意，MBG是使用java.util.regex.Matcher.replaceAll来替换searchString和replaceString的，
            如果使用了columnOverride元素，该属性无效；

        <columnRenamingRule searchString="" replaceString=""/>
         -->

         <!-- 用来修改表中某个列的属性，MBG会使用修改后的列来生成domain的属性；
             column:要重新设置的列名；
             注意，一个table元素中可以有多个columnOverride元素哈~
          -->
         <columnOverride column="username">
             <!-- 使用property属性来指定列要生成的属性名称 -->
             <property name="property" value="userName"/>

             <!-- javaType用于指定生成的domain的属性类型，使用类型的全限定名
             <property name="javaType" value=""/>
              -->

             <!-- jdbcType用于指定该列的JDBC类型 
             <property name="jdbcType" value=""/>
              -->

             <!-- typeHandler 用于指定该列使用到的TypeHandler，如果要指定，配置类型处理器的全限定名
                 注意，mybatis中，不会生成到mybatis-config.xml中的typeHandler
                 只会生成类似：where id = #{id,jdbcType=BIGINT,typeHandler=com._520it.mybatis.MyTypeHandler}的参数描述
             <property name="jdbcType" value=""/>
             -->

             <!-- 参考table元素的delimitAllColumns配置，默认为false
             <property name="delimitedColumnName" value=""/>
              -->
         </columnOverride>

         <!-- ignoreColumn设置一个MGB忽略的列，如果设置了改列，那么在生成的domain中，生成的SQL中，都不会有该列出现 
             column:指定要忽略的列的名字；
             delimitedColumnName：参考table元素的delimitAllColumns配置，默认为false

             注意，一个table元素中可以有多个ignoreColumn元素
         <ignoreColumn column="deptId" delimitedColumnName=""/>
         -->
    </table>

</context>

</generatorConfiguration>
```





## 6.支付

### 6.1 支付名词

appid：

openid（微信）：



### 7.1 项目初始化 && 对接Native支付

SpringBoot:2.1.7.RELEASE

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.imooc</groupId>
    <artifactId>pay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>pay</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/cn.springboot/best-pay-sdk -->
        <dependency>
            <groupId>cn.springboot</groupId>
            <artifactId>best-pay-sdk</artifactId>
            <version>1.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <configuration>
                    <overwrite>true</overwrite>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



支付Github源码：https://github.com/Pay-Group/best-pay-sdk



### 7.2 Native支付业务逻辑实现

IPayService.java

```java
public interface IPayService {

    void create(String orderId, BigDecimal amount);
}
```



PayService.java

```java
@Slf4j
@Service
public class PayService implements IPayService {


    @Override
    public void create(String orderId, BigDecimal amount) {
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wxd898fcb01713c658");          //公众号Id
        wxPayConfig.setMchId("1483469312");      //商户Id
        wxPayConfig.setMchKey("7mdApPMfXddfWWbbP4DUaVYm2wjyh3v3");       //商户密钥
        wxPayConfig.setNotifyUrl("http://127.0.0.1");   //接送支付平台异步通知的地址

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);

        //发起支付
        PayRequest request = new PayRequest();
        request.setOrderName("8783955-最好的支付sdk1");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);


        PayResponse response = bestPayService.pay(request);
        log.info("response={}", response);
    }
}
```

单元测试类

```java
public class PayServiceTest extends PayApplicationTests {

    @Autowired
    private PayService payService;

    @Test
    public void create() {
        //BigDecimal.valueOf(0.01)
        payService.create("12345678912345643214", BigDecimal.valueOf(0.01));
    }
}
```



Postman请求url：https://api.mch.weixin.qq.com/pay/unifiedorder  方式：POST

<xml>

   <appid>wxd898fcb01713c658</appid>

   <mch_id>1483469312</mch_id>

   <nonce_str>I7O79rYuVi00n7HM</nonce_str>

   <sign>407D2B7E86527352493BC6FC76863927</sign>

   <body>8783955-最好的支付sdk1</body>

   <notify_url>http://127.0.0.1</notify_url>

   <out_trade_no>12345678912345643214</out_trade_no>

   <spbill_create_ip>8.8.8.8</spbill_create_ip>

   <total_fee>1</total_fee>

   <trade_type>NATIVE</trade_type>

</xml>

常见报错：1.商户id（mcn_id）、mcn_key错误， <return_msg><![CDATA[签名错误]]></return_msg>；

2.appid错误， <return_msg><![CDATA[AppID不存在，请检查后再试]]></return_msg>

3.order_id重复或者order_name，err_code = INVALID_REQUEST err_code_des=201 商户订单号重复



### 7.3 前端生成二维码

BootCDN:开源的前端项目库 ——>jquery  ——>jquery-qrcode



Create.ftl

```html
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付</title>
</head>
<body>
<div id="myQrcode"></div>

<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    //jQuery('#qrcode').qrcode("this plugin is great");

    jQuery('#myQrcode').qrcode({
        text   : "${codeUrl}"
    });
</script>
</body>
</html>
```



PayController.java

```java
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount){

        PayResponse response = payService.create(orderId,amount);
        Map map = new HashMap<>();

        map.put("codeUrl", response.getCodeUrl());
        return new ModelAndView("create",map);
    }

}
```





### 7.4 微信异步通知

问题1:notify_url要在微信后台设置吗？   问题2:notify_url一定要用域名吗？

notify_url:异步接受微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。

解决方案：NATAPP.cn —— 内网穿透应用



PayService.java

```java
/**
* 异步通知
* @param notifyData
*
*/
@Override
public String asyncNotify(String notifyData) {
    //1.签名验证
    PayResponse payResponse = bestPayService.asyncNotify(notifyData);
    log.info("payResponse={}",payResponse);

    //2.金额校验（数据库查看订单）


    //3.修改订单支付状态

    //4.告诉微信不要再通知
    return "<xml>\n" +
            "\n" +
            " <return_code><!CDATA[SUCCESS}}></return_code>\n" +
            " <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
}
```

请求数据

```xml
<xml>
   <appid>wxd898fcb01713c658</appid>
   <mch_id>1483469312</mch_id>
   <nonce_str>dgxHN97fHS4Z90CW</nonce_str>
   <sign>E75C3680607A3D0849D7DF9BD0A93C68</sign>
   <body>8783955-最好的支付sdk1</body>
   <notify_url>http://53a6165b577dc348.natapp.cc/pay/notify</notify_url>
   <out_trade_no>12345415532114312</out_trade_no>
   <spbill_create_ip>8.8.8.8</spbill_create_ip>
   <total_fee>1</total_fee>
   <trade_type>NATIVE</trade_type>
</xml>
```

返回的通知(里面含二维码的url：code_url)

```xml
<xml>
  <return_code><![CDATA[SUCCESS]]></return_code>
  <return_msg><![CDATA[OK]]></return_msg>
  <result_code><![CDATA[SUCCESS]]></result_code>
  <mch_id><![CDATA[1483469312]]></mch_id>
  <appid><![CDATA[wxd898fcb01713c658]]></appid>
  <nonce_str><![CDATA[xorEGmQcsyJuFp2I]]></nonce_str>
  <sign><![CDATA[CCFB14AE914E080D22C529375EFC27E3]]></sign>
  <prepay_id><![CDATA[wx06133725654844ea617fe67e6f8f4e0000]]></prepay_id>
  <trade_type><![CDATA[NATIVE]]></trade_type>
  <code_url><![CDATA[weixin://wxpay/bizpayurl?pr=hW9LbIyzz]]></code_url>
</xml>
```



### 7.5 支付宝PC支付业务实现

PayController.java

```java
@GetMapping("/create")
public ModelAndView create(@RequestParam("orderId") String orderId,
                           @RequestParam("amount") BigDecimal amount,
                           @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum){

    PayResponse response = payService.create(orderId, amount, bestPayTypeEnum);
    Map<String, String> map = new HashMap<>();

    //支付方式不同，渲染方式就不同，WXPAY_NATIVE使用code_url,ALIPAY_PC使用body
    if(bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE){
        map.put("codeUrl", response.getCodeUrl());
        return new ModelAndView("createForWxNative",map);
    }else if(bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC){
        map.put("body", response.getBody());
        return new ModelAndView("createForAlipayPc",map);
    }
    throw new RuntimeException("暂不支持的支付类型");
}
```

Alipay会提供一个可潜入html使用的body(直接跳转)

```html
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付</title>
</head>
<body>
${body}

</body>
</html>
```



### 7.6 数据库

建表

```sql
CREATE TABLE `mall_pay_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `order_no` bigint NOT NULL COMMENT '订单号',
  `pay_platform` int DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信',
  `platform_number` varchar(200) DEFAULT NULL COMMENT '支付流水号',
  `platform_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `pay_amount` decimal(20,2) NOT NULL COMMENT '支付金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uqe_order_no` (`order_no`),
  UNIQUE KEY `uqe_platform_number` (`platform_number`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
```

设置order_no、platform_number不可重复。

DEFAULT CURRENT_TIMESTAMP COMMENT为当前时间默认值；

DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT为每次更新数据后获取并更新当前的时间



实现异步通知(PayService.java)

```java
/**
* 异步通知
* @param notifyData
*
*/
@Override
public String asyncNotify(String notifyData) {
    //1.签名验证
    PayResponse payResponse = bestPayService.asyncNotify(notifyData);
    log.info("payResponse={}",payResponse);

    //2.金额校验（数据库查看订单）
    //比较严重（正常情况下不会发生） 发出告警：钉钉、短信
    PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
    if(payInfo == null){

        throw new RuntimeException("通过orderNo查询到到结果是null");
    }
    //如果订单状态不是已支付
    if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
        if(payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0){
            //告警
            throw new RuntimeException("异步通知中到金额和数据库里到不一致,orderNo=" + payResponse.getOrderId());
        }

        //3.修改订单支付状态
        payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
        payInfoMapper.updateByPrimaryKeySelective(payInfo);
    }
    
    //4.告诉微信、支付宝不要再通知
    if(payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX){
        return "<xml>\n" +
                "\n" +
                " <return_code><!CDATA[SUCCESS}}></return_code>\n" +
                " <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }else if(payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY){
        return "success";
    }
    throw new RuntimeException("异步通知中错误的支付类型");
}
```



### 7.7 支付完成页面跳转

ajax ——> api（通过订单号查询支付状态）

已支付 ——> 跳转

方便读取配置，可以单独把微信配置写出来后传入

BestPayConfig.java

```java
@Component
public class BestPayConfig {

    @Bean
    public WxPayConfig wxPayConfig(){
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wxd898fcb01713c658");          //公众号Id
        wxPayConfig.setMchId("1483469312");      //商户Id
        wxPayConfig.setMchKey("7mdApPMfXddfWWbbP4DUaVYm2wjyh3v3");       //商户密钥
        wxPayConfig.setNotifyUrl("http://king-mall.nat300.top/pay/notify");   //接送支付平台异步通知的地址
        wxPayConfig.setReturnUrl("http://127.0.0.1");

        return wxPayConfig;
    }

}
```

PayController.java中加入

```java
@Autowired
    private WxPayConfig wxPayConfig;
```

```java
map.put("returnUrl", wxPayConfig.getReturnUrl());
```

就可以获得配置文件中的配置



### 7.8 规范配置

在application.yml中配置微信支付配置

```yaml
wx:
  appid: wxd898fcb01713c658
  mchId: 1483469312
  mchKey: 7mdApPMfXddfWWbbP4DUaVYm2wjyh3v3
  notifyUrl: http://king-mall:nat300:top/pay/notify
  returnUrl: http://127:0:0:1
```

新建WxAccountConfig.java对象类

```java
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccountConfig {

    private String appId;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String returnUrl;
}
```

在主配置中实例配置对象

```java
@Autowired
    private WxAccountConfig wxAccountConfig;

@Bean
    public WxPayConfig wxPayConfig(){
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());          //公众号Id
        wxPayConfig.setMchId(wxAccountConfig.getMchId());      //商户Id
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());       //商户密钥
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());   //接送支付平台异步通知的地址
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());

        return wxPayConfig;
    }
```



## 8.商城Mall

### 8.1 用户模块开发

Content-Type:application/json

开发顺序：Dao->Service->Controller

单元测试：Service层   Mybatis打印SQL语句



### 8.2 Service-完成注册功能

Mybatis-generator生成User UserMapper.xml UserMapper.java 

新建service层 IUserService.java(Interface) UserServiceImpl.java

```xml
	<select id="countByUsername" parameterType="java.lang.String" resultType="java.lang.Integer">
    select
    count(1)
    from mall_user
    where username = #{username,jdbcType=VARCHAR}
  </select>
  <select id="countByEmail" parameterType="java.lang.String" resultType="java.lang.Integer">
    select
      count(1)
    from mall_user
    where email = #{email,jdbcType=VARCHAR}
  </select>
```

单元测试控制台输出的结果

第一行：SQL语句（可复制到mysql控制台验证查询结果）；第二行：查询到参数

第三行：列的字段；  第四行：行——表示每个字段对应的数据

第五行Total：代表有多少行

```
==>  Preparing: select id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time from mall_order where id = ? 
==> Parameters: 4(Integer)
<==    Columns: id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time
<==        Row: 4, 105, 113, 603, 491.04, 193, 623, 787, 2005-10-15 04:07:15, 2021-04-12 09:00:24, 2008-02-27 05:13:32, 2015-09-09 07:39:01, 2002-06-28 00:20:10, 2019-11-18 20:22:54
<==      Total: 1
```



### 8.3 Controller实现

接受前端传来的数据(使用**urlencoded**)

方法一：指定value 后面的参数名随意（如果不指定value后面的参数名要和实体对象的属性一样

```java
@PostMapping("/register")
public void register(@RequestParam(value = "username") String userName){
    log.info("username={}", userName);
}
```

方法二：通过实例对象来获取前端传来的参数

```java
@PostMapping("/register")
public void register(User user){
    log.info("username={}", user.getUsername());
}
```



若是通过**json**格式传来的(加上@RequestBody注解才有效)

```java
@PostMapping("/register")
    public void register(@RequestBody User user){
        log.info("username={}", user.getUsername());
    }
```



Vo统称为返回前端的数据(使用泛型可以多个类型复用)

```java
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    private T data;

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <T> ResponseVo<T> success(String msg){
        return new ResponseVo<>(0,msg);
    }
}
```

新版本用法（返回json中取出data=null）

```java
@JsonInclude(value = JsonInclude.Include.NON_NULL)
```



### 8.4 错误状态码使用枚举

枚举类

```java
@Getter
public enum ResponseEnum {

    ERROR(-1, "服务端错误"),

    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密码错误"),

    USER_EXIST(2, "用户已存在"),

    NEED_LOGIN(10, "用户未登录,请先登录"),

    ;


    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
```



Vo重建构造方法封装枚举对象()

```java
		public static <T> ResponseVo<T> success(){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum){
        return new ResponseVo<>(responseEnum.getCode(), responseEnum.getDesc());
    }
```



### 8.5 表单验证

步骤：新建form包下再新建UserForm.java类

```java
@Data
public class UserForm {

    //@NotBlank //用于String 判断空格
    //@NotEmpty 用于集合
    //@NotNull
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}

```



把验证状态码和结果信息传给前端

封装把错误信息传给msg

```java
public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg){
        return new ResponseVo<>(responseEnum.getCode(), msg);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult){
        return new ResponseVo<>(responseEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError().getField()) + " " + bindingResult.getFieldError().getDefaultMessage());
    }
```



把bindingResult记录的错误信息给返回给msg再传给前端

```java
@PostMapping("/register")
    public ResponseVo register(@Valid @RequestBody UserForm userForm,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("注册提交的参数有误，{} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(ResponseEnum.PARAM_ERROR,
                    bindingResult);
        }

        log.info("username={}", userForm.getUsername());
        //return ResponseVo.success();
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }
```



### 8.6 接入service完成注册功能

调整返回的json格式

调整前：

```json
{
    "timestamp": "2023-02-07T08:41:01.812+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "意外错误",
    "path": "/user/register"
}
```

新建exception包和捕获异常类

```java
@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseVo handle(RuntimeException e){
        return ResponseVo.error(ResponseEnum.ERROR, e.getMessage());
    }
}
```

调整后：

```json
{
    "status": -1,
    "msg": "意外错误"
}
```



### 8.7 用户模块开发——登陆

service层的实现类（为了更安全，一概提示用户名或密码错误）

```java
@Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if(user == null){
            //用户不存在(返回用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        if(!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)))){
            //密码错误(返回用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        return ResponseVo.success();
    }
```



Session和Cookie的使用

新建一个存在常量的包，用类存储常量 consts.MallConst

```java
public class MallConst {
    public static final String CURRENT_USER = "currentUser";
}
```



#### 遇到的BUG一：

DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8))是把原字符串加密后生成的字符串，因此不能直接从数据库中复制相同的密码作为输入，否则报错，应该输入原本注册时候的字符串，会自动解码为与数据库相同的加密字符串



根据自定义来设置返回的data  重写ResponseVo方法和success方法 直接把范型data传入并处理

```java
private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ResponseVo<T> successByMsg(String msg){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }

    public static <T> ResponseVo<T> success(T data){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), data);
    }
```

返回了对象的密码（需要避免）通常在获取Get对象后 加上 user.setPassword("");



### 8.8 Session和Cookie

session保存在内容里，容易丢失  改进版本：token+redis

```java
//设置Session
        session.setAttribute(MallConst.CURRENT_USER , userResponseVo.getData());
        log.info("/login sessionId={}", session.getId());
       
```

```java
User user = (User) session.getAttribute(MallConst.CURRENT_USER);
```

localhost是域名 127.0.0.1是ip地址 两者也算跨域

cookie跨域



退出登陆：

```java
@PostMapping("/user/logout")
    public ResponseVo<User> logout(HttpSession session){
        log.info("/user/logout sessionId={}", session.getId());
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }
```

```
/**
* {@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}
 * 
*/
```

SessionTimeOut最少设置1分钟，源码中有对1分钟与输入值取最大值的操作



### 8.9 拦截器

用于判断登陆状态

Interceptor - Url

AOP - 包名



新建用户登陆拦截器 UserLoginInterceptor.java

重写preHandle方法（执行前判断拦截）

```java
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
    * true表示继续流程，false表示中断
    * @param request
     * @param response
     * @param handler
    * @return
     * @throws Exception
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHanler...");
        User user =(User) request.getSession().getAttribute(MallConst.CURRENT_USER);
        if(user == null){
            log.info("user==null");
            return false;
//            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return true;
    }
}
```



配置拦截器 InterceptorConfig.java

重写WebMvcConfigurer 注册UserLoginInterceptor并交给容器管理

```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())  //注册UserLoginInterceptor
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register");
    }
}
```

当未登陆时返回的信息**：不推荐：**response.getWriter().print("error");

**推荐：**抛异常 throw new UserLoginException();

```java
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle(){
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }
```





## 9. 类目功能开发

### 9.1 多级目录

（1）先查出1级目录 -> 查其子目录，一直查到null

（2）查出目录 -> 查父目录，一直查到parent_id=0



耗时：http（请求微信api） > 磁盘 > 内存

mysql（内网+磁盘）

Service层实现 (查询多级目录的全部数据输出到前端)

```java
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categoryList = categoryMapper.selectAll();

        //查询partent_id = 0的数据 （for循环方式）
//        for(Category category : categoryList){
//            if(category.getParentId().equals(MallConst.ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category, categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

        //Lambda表达式 + stream
        categoryVoList = categoryList.stream()
                .filter(e -> e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                .map(e -> category2CategoryVo(e))
                .collect(Collectors.toList());

        //查询子目录
        findSubCategory(categoryVoList, categoryList);

        return ResponseVo.success(categoryVoList);
    }

    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categoryList){
        for(CategoryVo categoryVo : categoryVoList){
            List<CategoryVo> subCategoryVoList = new ArrayList<>();

            for(Category category : categoryList){
                //如果查到内容，设置subCategory，继续往下查
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                //降序
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                //递归：把新找到的子目录再调用该方法继续找下一级子目录
                findSubCategory(subCategoryVoList, categoryList);
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return  categoryVo;
    }
}
```





## 10.商品模块

### 10.1 获取商品列表

查询 这种方法会导致一直查数据库

```java
@Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        for(Category category : categories){
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet);
            }
        }
    }
```



这样修改可以防止重复查数据库

```java
@Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories){
        for(Category category : categories){
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }
```



```java
List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);
```

这里入参是Set，需要添加@Param("categoryIdSet")并映射到Mapper中

```xml
<select id="selectByCategoryIdSet" resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List" />
  from mall_product
  where status = 1
  <if test="categoryIdSet != null">
    and category_id in
    <foreach collection="categoryIdSet" item="item" index="index" open="(" separator="," close=")">
      #{item}
    </foreach>

  </if>
</select>
```



此时还要注意一点：当传入的categotyIdSet为null时，需要查询所有商品（需求）

方式一：判断categoId状态后再执行语句；注意还要再传入SQL语句参数时候若categotyIdSet没参数则传入null，否则正常传参（因为categotyId为null时候，并不能把null字符串add到Set中，此时Set中的size为0，即无任何参数。

```java
@Override
    public ResponseVo<List<ProductVo>> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        List<Product> products = productMapper.selectByCategoryIdSet(categoryIdSet.size() == 0 ? null : categoryIdSet);
        log.info("products = {}" , products);
        return null;
    }
```

方式二（推荐）：

```java
<select id="selectByCategoryIdSet" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List" />
    from mall_product
    where status = 1
    <if test="categoryIdSet.size() > 0">
      and category_id in
      <foreach collection="categoryIdSet" item="item" index="index" open="(" separator="," close=")">
        #{item}
      </foreach>
    </if>
  </select>
```



遇到的BUG：加入断言后报错，Debug发现responseVo一直为null，再进一步发现是ServiceImpl忘记了return responseVo.success(T data)

```java
Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
```



加入分页功能， 使用PageHelper插件，加入依赖

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper-spring-boot-starter -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.13</version>
        </dependency>
```

Service实现功能

```java
@Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = productList.stream()
                .map(e->{
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);

//        List<Product> products = productMapper.selectByCategoryIdSet(categoryIdSet.size() == 0 ? null : categoryIdSet);
//        log.info("products = {}" , products);
        return ResponseVo.success(pageInfo);
    }
```

单元测试通过后，加入Controller层中实现业务（参数非必填，且有默认值）

```java
@GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        return productService.list(categoryId, pageNum, pageSize);
    }
```



### 10.2 获取商品详情

Service层

```java
@Override
public ResponseVo<ProductDetailVo> detail(Integer productId) {
    Product product = productMapper.selectByPrimaryKey(productId);

    //只对确定性条件判断
    if(product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode()) ||
            product.getStatus().equals(ProductStatusEnum.DELETE.getCode())){
        return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
    }

    ProductDetailVo productDetailVo = new ProductDetailVo();
    BeanUtils.copyProperties(product, productDetailVo);
    productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock()); //对敏感数据处理，大于100都只显示100
    return ResponseVo.success(productDetailVo);
}
```

Controller层 （注意这里使用RequestMapping URI映射 即（products/{productID})

```java
@GetMapping("/products/{productId}")
public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId){
    return productService.detail(productId);
}
```





## 11.购物车模块

Redis（高性能）

增加redis依赖

```xml
				<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```

```yml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
```

序列化插件gson

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```



MongoDB（海量数据）

### 11.1 业务逻辑实现(购物车中添加商品)

要判断哪些应该存进Redis，哪些不应该！ 具有时效性的数据不应该存在redis中

Service层的实现 CartServiceImpl

```java
@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLATE = "cary_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        //判断商品是否存在
        if(product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //商品是否正常在售
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //商品库存是否充足
        if(product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //写入到Redis
        //key：caty_1 使用Hash结构实现Redis高性能
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //从redis读出数据
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if(StringUtils.isEmpty(value)){
            //没有该商品
            cart = new Cart(product.getId(), quantity, form.getSelected());
        }else{
            //已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(String.format(CART_REDIS_KEY_TEMPLATE, uid),
                String.valueOf(product.getId()),
                gson.toJson(cart));

        return null;
    }
}
```



单元测试：

```java
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    @Test
    public void add(){
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        cartService.add(1, form);

    }
}
```

控制台报错信息：     

```
org.springframework.data.redis.RedisSystemException: Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: WRONGTYPE Operation against a key holding the wrong kind of value
```

原因：Redis由之前的key、value的Set改成了HashMap来实现，因此redis中的Set数据会冲突（解决方法：先把过去的数据清除掉）



### 11.2 购物车-列表

返回购物车列表的接口业务Service层代码：

```java
@Override
public ResponseVo<CartVo> list(Integer uid) {

    HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
    String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
    Map<String, String> entries =  opsForHash.entries(redisKey);

    boolean selectAll = true;
    Integer cartTotalQuantity = 0;
    BigDecimal cartTotalPrice = BigDecimal.ZERO;

    CartVo cartVo = new CartVo();
    List<CartProductVo> cartProductVoList = new ArrayList<>();

    for (Map.Entry<String, String> entry : entries.entrySet()) {
        Integer productId = Integer.valueOf(entry.getKey());
        Cart cart = gson.fromJson(entry.getValue(), Cart.class);

        //TODO 需要优化，使用mysql的in（避免for循环查数据）
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product != null){
            CartProductVo cartProductVo = new CartProductVo(productId,
                    cart.getQuantity(),
                    product.getName(),
                    product.getSubtitle(),
                    product.getMainImage(),
                    product.getPrice(),
                    product.getStatus(),
                    product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                    product.getStock(),
                    cart.getProductSelected()
                    );
            cartProductVoList.add(cartProductVo);

            if(!cart.getProductSelected()){
                selectAll = false;
            }

            //业务：计算总价（只计算选中的）
            if(cart.getProductSelected()){
                cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice()); //add方法需要重新返回累加后的值
            }

        }

        cartTotalQuantity += cart.getQuantity();
    }

    //有一个未选中就不是全选
    cartVo.setSelectAll(selectAll);
    cartVo.setCartTotalQuantity(cartTotalQuantity);
    cartVo.setCartTotalPrice(cartTotalPrice);

    cartVo.setCartProductVoList(cartProductVoList);
    return ResponseVo.success(cartVo);
}
```



### 11.3 购物车—更新&删除





### 11.5 全单元测试

```java
@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer uid = 1;

    private Integer productId = 26;

    @Before
    public void add(){
        log.info("【新增购物车...】");
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list(){
        ResponseVo<CartVo> responseVo = cartService.list(uid);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update(){
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(10);
//        form.setSelected();
        ResponseVo<CartVo> responseVo = cartService.update(uid, productId, form);
        log.info("update={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete(){
        log.info("【删除购物车...】");
        ResponseVo<CartVo> responseVo = cartService.delete(uid, productId);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void selectAll(){
        ResponseVo<CartVo> responseVo = cartService.selectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void sum(){
        ResponseVo<Integer> responseVo = cartService.sum(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}
```

BUG：加入断言后一直报错（已解决）

原因是：Service实现层中对于方法的返回值写成了null，这样就导致断言执行到responseVo.getStatus()时未找到responseVo；在我们的其他方法返回值是list(uid)，list的类型是ResponseVo，把列表返回，再从列表中找到Status状态来判断时候执行成功



### 11.6 联通Controller

```java
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                    @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(), productId, form);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }

}
```





## 12.收获地址开发

### 12.1 新增收获地址

```java
@Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        int row = shippingMapper.insertSelective(shipping);
        if (row == 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());

        return ResponseVo.success(map);
    }
```

time时间相关使用数据库自带的时间数据去更新

id使用自增主键

```
<insert id="insertSelective" parameterType="com.imooc.mall.pojo.Shipping" useGeneratedKeys="true" keyProperty="id">
```

### 12.2 删除&更新收获地址

使用软删除的方式

```java
@Override
public ResponseVo delete(Integer uid, Integer shippingId) {
    int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
    if (row == 0){
        return ResponseVo.error(ResponseEnum.DELETE_SHIPPING_FAIL);
    }
    return ResponseVo.success();
}
```



更新：

```java
@Override
public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
    Shipping shipping = new Shipping();
    BeanUtils.copyProperties(form, shipping);
    shipping.setUserId(uid);
    shipping.setId(shippingId);
    int row = shippingMapper.updateByPrimaryKeySelective(shipping);
    if (row == 0){
        return ResponseVo.error(ResponseEnum.ERROR);
    }
    return ResponseVo.success();
}
```





## 13.订单模块

### 13.1 流程分析

  `product_id` int DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片地址',

商品的这些字段在订单里再存一次，原因是这些数据是变化的。订单一旦支付好，很多数据是不可变的！

**不可变：商品图片、收获地址**。   可变：状态



### 13.2 创建订单-购物车

整合后的Service层代码（含13.3、13.4等功能） 

总流程：

```java
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收获地址校验（总要查出来的）
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //获取购物车，校验（是否有商品、库存）
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //获取cartList里的productLists
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());

        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList){
            //根据productId查询数据库
            Product product = map.get(cart.getProductId());
            //是否有商品
            if (product == null){
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在. productId = " + cart.getProductId());
            }
            //商品上下架状态(业务场景：当商品加入购物车后，后续管理员如果做了下架操作，则取消）
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                        "商品不是在售状态. " + product.getName());
            }

            //库存是否充足
            if (product.getStock() < cart.getQuantity()){
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,
                        "库存不正确. " + product.getName());
            }

            OrderItem orderItem = bulidOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存（库存充足的情况下执行）
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }
        //计算总价
        //生成订单，入库：order和order_item，使用事务
        Order order = bulidOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

//        for (OrderItem item : orderItemList){ 这是错误示范，避免for循环中使用sql语句
//            orderItemMapper.insertSelective();
//        }
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车（选中的商品）
        //redis有事务（redis是单线程的，打包命令），不能回滚,通过写补偿（删除后重新添加）的方法来模拟回滚
        //场景：假如购物车有两件商品，第一件商品成功购买，此时如果更新了redis的内容，则当第二件商品出现错误mysql要回滚时，redis无法回滚，这样会导致数据的丢失和出错。为避免错误，更新购物车应该重新写一个循环
        for (Cart cart : cartList){
            cartService.delete(uid, cart.getProductId());
        }

        //构造orderVo对象返回前端
        OrderVo orderVo = bulidOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    private OrderVo bulidOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {

        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVoList);
        orderVo.setShippingId(shipping.getId());
        orderVo.setShippingVo(shipping);

        return orderVo;
    }

    private Order bulidOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {
        //价格去做累加计算，使用BigDecimal防止精度损失
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);//累加后的结果set进order里面
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0); //暂时无涉及运费，先入0
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
    * 企业级用：分布式唯一id/主键（后续学习）
    * @param
    * @return
    */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    //  创建订单的方法（提高代码阅读性）
    private OrderItem bulidOrderItem(Integer uid, Long orderNo,  Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        return item;

    }
}
```



### 13.3 构造OrderItem



```java
//  创建订单的方法（提高代码阅读性）
private OrderItem bulidOrderItem(Integer uid, Long orderNo,  Integer quantity, Product product) {
    OrderItem item = new OrderItem();
    item.setUserId(uid);
    item.setOrderNo(orderNo);
    item.setProductId(product.getId());
    item.setProductName(product.getName());
    item.setProductImage(product.getMainImage());
    item.setCurrentUnitPrice(product.getPrice());
    item.setQuantity(quantity);
    item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

    return item;
  
  private Order bulidOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {
        //价格去做累加计算，使用BigDecimal防止精度损失
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);//累加后的结果set进order里面
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0); //暂时无涉及运费，先入0
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
    * 企业级用：分布式唯一id/主键（后续学习）
    * @param
    * @return
    */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }
```



批量插入的sql语句

```xml
<insert id="batchInsert" parameterType="list">
    insert into mall_order_item (user_id, order_no,
                                 product_id, product_name, product_image,
                                 current_unit_price, quantity, total_price
                                )
    values
    <foreach collection="orderItemList" index="index" item="item" separator=",">
      (
       #{item.userId},
       #{item.orderNo},
       #{item.productId},
       #{item.productName},
       #{item.productImage},
       #{item.currentUnitPrice},
       #{item.quantity},
       #{item.totalPrice}
       )
    </foreach>
  </insert>
```





### 13.4 扣库存&更新购物车

注意更新购物车不能放在与mysql执行的同一个循环内，因为redis有事务（redis是单线程的，打包命令），不能回滚；

场景：假如购物车有两件商品，第一件商品成功购买，此时如果更新了redis的内容，则当第二件商品出现错误mysql要回滚时，redis无法回滚，这样会导致数据的丢失和出错。为避免错误，更新购物车应该重新写一个循环

```java
//减库存（库存充足的情况下执行）
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }
```

```java
//更新购物车（选中的商品）
        //redis有事务（redis是单线程的，打包命令），不能回滚,通过写补偿（删除后重新添加）的方法来模拟回滚
        //场景：假如购物车有两件商品，第一件商品成功购买，此时如果更新了redis的内容，则当第二件商品出现错误mysql要回滚时，redis无法回滚，这样会导致数据的丢失和出错。为避免错误，更新购物车应该重新写一个循环
        for (Cart cart : cartList){
            cartService.delete(uid, cart.getProductId());
        }
```



