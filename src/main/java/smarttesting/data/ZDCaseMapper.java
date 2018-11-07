package smarttesting.data;

import org.apache.ibatis.annotations.*;
import smarttesting.data.model.ZDCase;
import smarttesting.utils.Query;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface ZDCaseMapper {

    String table = "" +
            "zd_case";

    String column = "" +
            "NULL,#{projectId},#{interfaceId},#{name},#{method},#{url},#{urlSuffix},#{contentType},#{requestBody},#{resultScript}";

    String condition = "" +
            "<if test='id!=null'> and id=#{id}</if>" +
            "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
            "<if test='interfaceId!= null'> and interface_id=#{interfaceId}</if>" +
            "<if test='name!= null'> and name=#{name}</if>" +
            "<if test='method!= null'> and method=#{method}</if>" +
            "<if test='url!= null'> and url=#{url}</if>" +
            "<if test='urlSuffix!= null'> and url_suffix=#{urlSuffix}</if>" +
            "<if test='contentType!= null'> and content_type=#{contentType}</if>" +
            "<if test='requestBody!= null'> and request_body=#{requestBody}</if>" +
            "<if test='resultScript!= null'> and result_script=#{resultScript},</if>";

    String updateset = "" +
            "<if test='projectId!= null'> project_id=#{projectId},</if>" +
            "<if test='interfaceId!= null'>  interface_id=#{interfaceId},</if>" +
            "<if test='name!= null'>  name=#{name},</if>" +
            "<if test='method!= null'>  method=#{method},</if>" +
            "<if test='url!= null'>  url=#{url},</if>" +
            "<if test='urlSuffix!= null'> url_suffix=#{urlSuffix},</if>" +
            "<if test='contentType!= null'> content_type=#{contentType},</if>" +
            "<if test='requestBody!= null'>  request_body=#{requestBody},</if>" +
            "<if test='resultScript!= null'>  result_script=#{resultScript},</if>";

    String pager = "" +
            "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";

    String order = "" +
            "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";


    @Select({
            "<script>",
            "select * from " + table + " where 1=1 " + condition + order + pager,
            "</script>"})
    List<ZDCase> select(Query query);

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
    long insert(ZDCase zdCase);

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
    long update(ZDCase zdCase);
}
