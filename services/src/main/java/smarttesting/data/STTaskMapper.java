package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.STTask;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface STTaskMapper {
    String table = "" +
            "ST_TASK";

    String column = "" +
            "NULL,#{projectId},#{name},#{con},#{scenes},#{run},#{lastrun},#{lastruntime},#{lastrunstatus}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
            "<if test='name!= null'> and name=#{name}</if>" +
            "<if test='con!= null'> and con=#{con}</if>" +
            "<if test='scenes!= null'> and scenes=#{scenes}</if>" +
            "<if test='run!= null'> and run=#{run}</if>" +
            "<if test='lastrun!= null'> and lastrun=#{lastrun}</if>" +
            "<if test='lastruntime!= null'> and lastruntime=#{lastruntime}</if>" +
            "<if test='lastrunstatus!= null'> and lastrunstatus=#{lastrunstatus}</if>";

    String updateset = "" +
            "<if test='projectId!= null'> project_id=#{projectId},</if>" +
            "<if test='name!= null'>  name=#{name},</if>" +
            "<if test='con!= null'>  con=#{con},</if>" +
            "<if test='scenes!= null'>  scenes=#{scenes},</if>" +
            "<if test='run!= null'>  run=#{run},</if>" +
            "<if test='lastrun!= null'>  lastrun=#{lastrun},</if>" +
            "<if test='lastruntime!= null'>  lastruntime=#{lastruntime},</if>" +
            "<if test='lastrunstatus!= null'>  lastrunstatus=#{lastrunstatus},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";

    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<STTask> select(Query query);

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
    long insert(STTask e);

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
    long update(STTask e);
}
