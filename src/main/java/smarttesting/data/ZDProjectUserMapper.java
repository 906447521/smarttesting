package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.ZDProjectUser;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface ZDProjectUserMapper {

    String table     =
            "zd_project_user";
    String column    =
            "NULL,#{userName},#{projectId},#{role}";
    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='userName!= null'> and user_name=#{userName}</if>" +
            "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
            "<if test='role!= null'> and project_id=#{role}</if>" +
            "";
    String updateset = "" +
            "<if test='userName!= null'>  user_name=#{userName},</if>" +
            "<if test='projectId!= null'>  project_id=#{projectId},</if>" +
            "<if test='role!= null'>  role=#{role},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";

    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<ZDProjectUser> select(Query query);

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
    long insert(ZDProjectUser zdUser);

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
    long update(ZDProjectUser zdUser);

}
