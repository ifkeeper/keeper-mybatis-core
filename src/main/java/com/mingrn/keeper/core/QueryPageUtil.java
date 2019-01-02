package com.mingrn.keeper.core;

import java.util.Map;

/**
 * 多表分页工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public class QueryPageUtil {
    private QueryPageUtil() {
    }

    /**
     * 多表分页
     *
     * @param params     参数
     * @param pageNumber 页码
     * @param pageSize   页数量
     */
    public static void pagination(Map<String, Object> params, int pageNumber, int pageSize) {

        if (pageNumber > 0 && pageSize > 0) {
            int startRow = pageSize * (pageNumber - 1) + 1;
            int endRow = pageSize * pageNumber;
            int offset = pageSize * (pageNumber - 1);

            params.put("offset", offset);
            params.put("startRow", startRow);
            params.put("endRow", endRow);
        }
    }
}
