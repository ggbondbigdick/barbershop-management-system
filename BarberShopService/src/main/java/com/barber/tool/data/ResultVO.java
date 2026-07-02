package com.barber.tool.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: ResultVO
 * Description: Unified Return Encapsulation Class
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultVO<T> implements Serializable {

    /**
     * 序列化版本号
     * Serialized version number
     */
    private static final long serialVersionUID = -3102383089700431850L;

    /**
     * 异常码
     * Exception Code
     */
    private String errorCode;

    /**
     * 异常描述
     * Exception Description
     */
    private String errorDesc;

    /**
     * 是否成功
     * Success Flag
     */
    private Boolean success;

    /**
     * 返回的结果
     * Return Result
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T result;

    /**
     * 返回的结果链表
     * Return Result List
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> rows;

    /**
     * 返回的总记录数
     * Total Record Count
     */
    private Long total;

    /**
     * 默认构造函数
     * Default Constructor
     */
    public ResultVO() {}

    /**
     * 构造函数
     * Constructor
     * @param succes 是否成功
     * @param result 返回结果
     */
    public ResultVO(boolean succes, T result) {
        this.success = succes;
        this.result = result;
    }

    /**
     * 构造函数
     * Constructor
     * @param succes 是否成功
     * @param desc   描述
     */
    public ResultVO(boolean succes, String desc) {
        this.success = succes;
        this.errorDesc = desc;
    }

    /**
     * 构造函数
     * Constructor
     * @param succes 是否成功
     * @param result 返回结果
     * @param desc   描述
     */
    public ResultVO(boolean succes, T result, String desc) {
        this.success = succes;
        this.result = result;
        this.errorDesc = desc;
    }

    /**
     * 构造函数
     * Constructor
     * @param rows  返回结果
     * @param total 总记录数
     */
    public ResultVO(List<T> rows, Long total) {
        this.success = true;
        this.rows = rows;
        this.total = total;
    }

    /**
     * 构造函数
     * Constructor
     * @param rows 返回结果列表
     */
    public ResultVO(List<T> rows) {
        this.success = true;
        this.rows = rows;
    }

    /**
     * 构造函数
     * Constructor
     * @param success 是否成功
     */
    public ResultVO(boolean success) {
        this.success = success;
    }

    /**
     * errorCode属性的get方法
     *
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * errorCode属性的set方法
     *
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * errorDesc属性的get方法
     *
     * @return the errorDesc
     */
    public String getErrorDesc() {
        return errorDesc;
    }

    /**
     * errorDesc属性的set方法
     *
     * @param errorDesc the errorDesc to set
     */
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    /**
     * success属性的get方法
     *
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * success属性的set方法
     *
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * total属性的get方法
     *
     * @return the total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * total属性的set方法
     *
     * @param total the total to set
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * result属性的get方法
     *
     * @return the result
     */
    public T getResult() {
        return result;
    }

    /**
     * result属性的set方法
     *
     * @param result the result to set
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * rows属性的get方法
     *
     * @return the rows
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * rows属性的set方法
     *
     * @param rows the rows to set
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
