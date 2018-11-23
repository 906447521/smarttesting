package smarttesting.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author
 */
public class Pagination<E> implements Serializable {

    private int     pageIndex   = 1;
    private int     pageSize    = 20;
    private int     totalRecord = 0;
    private List<E> data        = Collections.emptyList();

    public Pagination() {

    }

    public Pagination(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Pagination(int pageIndex, int pageSize, int totalRecord) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
    }

    public Pagination<E> withResult(List<E> pageData, int totalRecord) {
        this.setData(pageData);
        this.setTotalRecord(totalRecord);
        return this;
    }

    public int getTotalPage() {
        return (this.totalRecord + this.pageSize - 1) / this.pageSize;
    }

    public Integer getPrevPage() {
        return pageIndex <= 1 ? 1 : pageIndex - 1;
    }

    public Integer getNextPage() {
        return pageIndex * pageSize >= totalRecord ? getTotalPage() : pageIndex + 1;
    }

    public boolean isEmpty() {
        return this.data == null || this.data.isEmpty();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public Pagination<E> setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pagination<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public Pagination<E> setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public List<E> getData() {
        return data;
    }

    public Pagination<E> setData(List<E> pageData) {
        if (pageData != null || !pageData.isEmpty()) {
            this.data = new ArrayList<E>(pageData);
        }
        return this;
    }

    public E singleResult() {
        if (data.size() > 0) {
            return data.get(0);
        }
        return null;
    }

}
