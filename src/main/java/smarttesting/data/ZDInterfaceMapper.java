package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.ZDInterface;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface ZDInterfaceMapper {

    String table = "" +
            "zd_interface";

    String column = "" +
            "NULL,#{projectId},#{name},#{type},#{url},#{method},#{requestHeader},#{responseCharset}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
            "<if test='name!= null'> and name=#{name}</if>" +
            "<if test='type!= null'> and type=#{type}</if>" +
            "<if test='url!= null'> and url=#{url}</if>" +
            "<if test='method!= null'> and method=#{method}</if>" +
            "<if test='requestHeader!= null'> and request_header=#{requestHeader}</if>" +
            "<if test='responseCharset!= null'> and response_charset=#{responseCharset}</if>";

    String updateset = "" +
            "<if test='projectId!= null'>  project_id=#{projectId},</if>" +
            "<if test='name!= null'>  name=#{name},</if>" +
            "<if test='type!= null'>  type=#{type},</if>" +
            "<if test='url!= null'>  url=#{url},</if>" +
            "<if test='method!= null'>  method=#{method},</if>" +
            "<if test='requestHeader!= null'>  request_header=#{requestHeader},</if>" +
            "<if test='responseCharset!= null'>  response_charset=#{responseCharset},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";


    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<ZDInterface> select(Query query);

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
    long insert(ZDInterface zdInterface);

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
    long update(ZDInterface zdInterface);

}
