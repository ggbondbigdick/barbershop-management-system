package com.barber.tool;

import com.barber.tool.data.ResultVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * ClassName: PageTool
 * Description: PageTool
 */
public class PageTool {

    public static <T> PageInfo<T> getPageInfo(Integer page, Integer size, List<T> list) {

        PageInfo<T> objectPageInfo = new PageInfo<>();

        // 总条数
        int total = list.size();

        objectPageInfo.setTotal(total);

        if (page > 0 && size > 0) {

            // 总页数
            objectPageInfo.setPages(total == 0 ? 0 : total % size == 0 ? total / size : total / size + 1);

            // 截取的范围
            int index = (page - 1) * size;
            list = list.subList(index, (total - index > size ? index + size : total));

            // 当前页
            objectPageInfo.setPageNum(page);
            //  每页数量
            objectPageInfo.setPageSize(size);
        }

        // 当前页显示的数据
        objectPageInfo.setList(list);

        return objectPageInfo;
    }



    public static <T> ResultVO<T> convertResult(T data) {

        ResultVO<T> resultVO = new ResultVO<>();

        if (data instanceof PageInfo) {
            PageInfo<T> pageInfo = (PageInfo<T>) data;
            resultVO.setSuccess(true);
            resultVO.setRows(pageInfo.getList());
            resultVO.setTotal(pageInfo.getTotal());
        } else {
            resultVO.setSuccess(true);
            resultVO.setResult(data);
        }

        return resultVO;
    }

}
