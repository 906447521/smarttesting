package smarttesting.utils;

import java.util.HashMap;

/**
 * @author
 */
public class Query extends HashMap {

    PageCriteria  pageCriteria;
    OrderCriteria orderCriteria;

    public Query with(Object key, Object value) {
        this.put(key, value);
        return this;
    }

    public PageCriteria getPageCriteria() {
        if (pageCriteria == null)
            withPager(1, 20);
        return pageCriteria;
    }

    public PageCriteria getPageCriteriaIfNullWithMaxInteger() {
        if (pageCriteria == null)
            withPager(1, Integer.MAX_VALUE);
        return pageCriteria;
    }

    public OrderCriteria getOrderCriteria() {
        if (orderCriteria == null)
            withOrder("id", "desc");
        return orderCriteria;
    }

    public Query withPager(Integer pageIndex, Integer pageSize) {
        this.pageCriteria = new PageCriteria(pageIndex, pageSize);
        put("pageCriteria", this.pageCriteria);
        return this;
    }

    public Query withOrder(String column, String order) {
        this.orderCriteria = new OrderCriteria(column, order);
        put("orderCriteria", this.orderCriteria);
        return this;
    }

    public static class OrderCriteria {

        private String order;
        private String column;

        public OrderCriteria(String column, String order) {
            this.column = column == null ? "id" : column;
            this.order = (order != null && order.equals("asc")) ? "asc" : "desc";
        }

        public String getOrder() {
            return order;
        }

        public String getColumn() {
            return column;
        }


    }

    public static class PageCriteria {

        private long start;
        private int  pageIndex;
        private int  pageSize;

        public PageCriteria(Integer pageIndex, Integer pageSize) {
            this.pageIndex = pageIndex == null ? 1 : pageIndex;
            this.pageSize = pageSize == null ? 20 : pageSize;
            this.start = (pageIndex - 1) * pageSize;
        }

        public long getStart() {
            return start;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }
    }

}
