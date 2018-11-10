package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.STTaskResult;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface STTaskResultMapper {
    String table = "" +
            "ST_TASK_RESULT";

    String column = "" +
            "NULL,#{tid},#{rid},#{result},#{time},#{start},#{end}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='tid!= null'> and tid=#{tid}</if>" +
            "<if test='rid!= null'> and rid=#{rid}</if>" +
            "<if test='result!= null'> and result=#{result}</if>" +
            "<if test='time!= null'> and time=#{time}</if>" +
            "<if test='start!= null'> and start=#{start}</if>" +
            "<if test='end!= null'> and end=#{end}</if>";

    String updateset = "" +
            "<if test='tid!= null'> tid=#{tid},</if>" +
            "<if test='rid!= null'>  rid=#{rid},</if>" +
            "<if test='result!= null'>  result=#{result},</if>" +
            "<if test='time!= null'>  time=#{time},</if>" +
            "<if test='start!= null'>  start=#{start},</if>" +
            "<if test='end!= null'>  end=#{end},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";


    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<STTaskResult> select(Query query);

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
    long insert(STTaskResult e);

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
    long update(STTaskResult e);
}
