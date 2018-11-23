package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.STScene;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface STSceneMapper {
    String table = "" +
            "ST_SCENE";

    String column = "" +
            "NULL,#{projectId},#{name},#{cases}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
            "<if test='name!= null'> and name=#{name}</if>" +
            "<if test='cases!= null'> and cases=#{cases}</if>";

    String updateset = "" +
            "<if test='projectId!= null'> project_id=#{projectId},</if>" +
            "<if test='name!= null'>  name=#{name},</if>" +
            "<if test='cases!= null'>  cases=#{cases},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";

    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<STScene> select(Query query);

    @Select({
            "<script>",
            "select count(1) from " + table + " where 1=1 " + condition,
            "</script>"})
    int count(Query query);

    @Insert({
            "<script>",
            "insert into " + table + " values (" + column + ")",
            "</script>"})
    @SelectKey(statement = "select LAST_INSERT_ID() as ID", keyProperty = "id", before = false, resultType = long.class)
    long insert(STScene e);

    @Delete({
            "<script>",
            "delete from " + table + " where 1=1 " + condition,
            "</script>"})
    long delete(Query query);

    @Update({
            "<script>",
            "update " + table,
            "<set>" + updateset + " </set>",
            "where id=#{id} ",
            "</script>"})
    long update(STScene e);
}
