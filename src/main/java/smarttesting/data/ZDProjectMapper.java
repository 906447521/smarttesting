package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.ZDProject;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface ZDProjectMapper {

    String table = "" +
            "zd_project";

    String column = "" +
            "NULL,#{name},#{creator}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='name!= null'> and name=#{name}</if>" +
            "<if test='creator!= null'> and creator=#{creator}</if>" +
            "<if test='userIn!=null'> and id in (select project_id from zd_project_user where user_name=#{userIn})</if>";

    String updateset = "" +
            "<if test='name!= null'> name=#{name},</if>" +
            "<if test='creator!= null'> creator=#{creator},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";


    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<ZDProject> select(Query query);

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
    long insert(ZDProject zdProject);

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
    long update(ZDProject zdProject);

}
