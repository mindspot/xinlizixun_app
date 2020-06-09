package com.kuangji.paopao.base;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 特殊情况无法使用分页 插件时候使用
 * <b> 分页工具类 </b>
 * @author 金威正
 * @param <E>
 * 
 */
@Data
public class Pager<E> implements Serializable {
    private static final long serialVersionUID = 4542617637761955078L;
    
    //结果集
    private E list;
    /**
     * currentPage 当前页
     */
    private int currentPage = 1;
    /**
     * pageSize 每页大小
     */
    private int pageSize = 13;
    /**
     * pageTotal 总页数
     */
    private int pageTotal;
    /**
     * recordTotal 总条数
     */
    private int recordTotal = 0;
    /**
     * previousPage 前一页
     */
    private int previousPage;
    /**
     * nextPage 下一页
     */
    private int nextPage;
    /**
     * firstPage 第一页
     */
    private int firstPage = 1;
    /**
     * lastPage 最后一页
     */
    private int lastPage;
    /**
     * 页号式导航, 起始页号
     */
    private int startPage;
    /**
     * 页号式导航, 结束页号
     */
    private int endPage;
    /**
     * 页号式导航, 最多显示页号数量为numCount+1;这里显示11页。
     */
    private int numCount = 10;
    /**
     * 要显示的页号
     */
    public List<Integer> showPagesNum = new ArrayList<Integer>();
    
    // 以下set方式是需要赋值的 不需要全部生成set 
    /**
     * 设置当前页 <br>
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 设置每页大小,也可以不用赋值,默认大小为10条 <br>
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置总条数,默认为0 <br>
     * @param recordTotal
     */
    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
        otherAttr();
    }

    /**
     * 设置其他参数
     */
    public void otherAttr() {
        // 总页数
        this.pageTotal = this.recordTotal % this.pageSize > 0 ? this.recordTotal / this.pageSize + 1 : this.recordTotal / this.pageSize;
        // 第一页
        this.firstPage = 1;
        // 最后一页
        this.lastPage = this.pageTotal;
        // 前一页
        if (this.currentPage > 1) {
            this.previousPage = this.currentPage - 1;
        } else {
            this.previousPage = this.firstPage;
        }
        // 下一页
        if (this.currentPage < this.lastPage) {
            this.nextPage = this.currentPage + 1;
        } else {
            this.nextPage = this.lastPage;
        }
        
        // 计算page 控制
        /**
             计算数字翻页起始数字从哪一个开始 ,因为我们默认的是需要显示10个翻页数字（加上本页等于11页）
             startPage是起始翻页数字，所以需要个根据本页向前显示5个翻页数字   例如本页是 6   那么就是  1  2  3  4  5  6(本页)
             同时需要判断是不是第一页，第一页不需要向前显示翻页数字。
             this.currentPage - numCount / 2  只要本页不大于6  它的数字翻页始终从1开始，7就从2开始  8就从3开始  以此类推
         */
        startPage = Math.max(this.currentPage - numCount / 2, firstPage);
        endPage = Math.min(startPage + numCount, lastPage);
        /**
            查看所有的数字翻页是否大于设置的默认数字翻页个数
            如果不大于 那就说明总的页数小于默认设置要显示的数字翻页的个数或者等于
             那么就从第1页的数字翻页开始显示 一直到总的页数
         */
        if (endPage - startPage < numCount) {
            startPage = Math.max(endPage - numCount, 1);
        }
        /**
             编写数字翻页集合利于页面迭代取出相应的数字翻页。
         */
        for(int i = startPage; i <= endPage; i++){
            showPagesNum.add(i);
        }
    }

   
   
}