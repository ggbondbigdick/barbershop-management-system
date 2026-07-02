package com.barber.tool.data;

import lombok.Data;

/**
 * ClassName: PageDTO
 * Description: Paging-in parameters
 */
@Data
public class PageDTO {


    /**
     * 当前页码
     * Current page number
     */
    private Integer page = 1;


    /**
     * 每页显示条数
     * Number of records per page
     */
    private Integer size = 10;

}
