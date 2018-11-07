package smarttesting.data;

import org.junit.Test;

public class ZDInterfaceMapperTest {
    String table     = "zd_interface";
    String column    = "NULL,#{projectId},#{name},#{type},#{url},#{method},#{requestHeader},#{responseCharset}";
    String condition =
            "" +
                    "<if test='id!=null'> and id=#{id}</if>" +
                    "<if test='projectId!= null'> and project_id=#{projectId}</if>" +
                    "<if test='name!= null'> and name=#{name}</if>" +
                    "<if test='type!= null'> and type=#{type}</if>" +
                    "<if test='url!= null'> and url=#{url}</if>" +
                    "<if test='method!= null'> and method=#{method}</if>" +
                    "<if test='requestHeader!= null'> and request_header=#{requestHeader}</if>" +
                    "<if test='responseCharset!= null'> and response_charset=#{responseCharset}</if>" +
                    "";
    String update    =
            "" +
                    "<if test='projectId!= null'>  project_id=#{projectId}</if>" +
                    "<if test='name!= null'>  name=#{name}</if>" +
                    "<if test='type!= null'>  type=#{type}</if>" +
                    "<if test='url!= null'>  url=#{url}</if>" +
                    "<if test='method!= null'>  method=#{method}</if>" +
                    "<if test='requestHeader!= null'>  request_header=#{requestHeader}</if>" +
                    "<if test='responseCharset!= null'>  response_charset=#{responseCharset}</if>" +
                    "";
    String pager     = "<if test='pageCriteria != null'> limit #{pageCriteria.start},#{pageCriteria.pageSize}</if>";
    String order     = "<if test='orderCriteria != null'> order by ${orderCriteria.column} ${orderCriteria.order}</if>";

    @Test
    public void main() {
        System.out.println("<script>update " + table + " <set > " + update + " </set> where id=#{id} </script>");
    }
}