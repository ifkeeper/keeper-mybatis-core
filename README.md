# QueryPageUtil

该工具类主要用于多表分页使用。

在 `MyBatis` 中定义：

```xml
<sql id="sql_pagination_start">
  <![CDATA[
  
  ]]>
</sql>
<sql id="sql_pagination_end">
  <![CDATA[
    limit ${startRow-1},${endRow-startRow+1}
  ]]>
</sql>
```

其中，`sql_pagination_start` 主要用于对数据符号转义，在 `Mybatis` 中有些符号转义后才能使用。`sql_pagination_end` 则是定义分页条件。

具体使用实例如下：

```xml
<select id="id" parameterType="java.util.Map" resultType="">
    <!-- 该语句可以不引用 -->
    <if test="offset != null"><include refid="sql_pagination_start"/></if>
    SELECT ... WHERE 1 = 1
    <!-- 引入该语句 -->
    <if test="offset != null"> <include refid="sql_pagination_end"/></if>
</select>
```
