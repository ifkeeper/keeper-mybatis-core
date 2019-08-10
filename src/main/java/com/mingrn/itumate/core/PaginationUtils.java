package com.mingrn.itumate.core;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-08-10 21:42
 */
public final class PaginationUtils {

    /**
     * 单表分页
     * <p>单表分页基于 tkMapper 单表分页实现. 使用示例如下:
     * <pre>{@code
     *   int pageNumber = ...;
     *   int sizeSize   = ...;
     *
     *   // 封装单表查询
     *   PageHelper.startPage(pageNumber, sizeSize);
     *
     *   // 查询数据
     *   List<User> userList = userService.find();
     *
     *   // 分页信息
     *   PageInfo pageInfo = PaginationUtils.newSingleTablePagination(userList);
     * }</pre>
     *
     * @param list 单表数据
     * @return pageInfo
     * @see com.github.pagehelper.PageInfo
     */
    public static <T> PageInfo newSingleTablePagination(List<T> list) {
        return newSingleTablePagination(list, 8);
    }

    /**
     * 单表分页
     *
     * @param list          单表数据
     * @param navigatePages 导航页数
     * @param list          单表数据
     * @return pageInfo
     * @see com.github.pagehelper.PageInfo
     */
    public static <T> PageInfo newSingleTablePagination(List<T> list, int navigatePages) {
        return new PageInfo<>(list, navigatePages);
    }

    /**
     * 多表分页
     *
     * @param list     数据
     * @param pageNum  页码数量
     * @param pageSize 每页数量
     * @param total    总数据量
     * @return multiTablePage
     * @see MultiTablePage
     */
    public static <T> MultiTablePage newMultiTablePagination(List<T> list, int pageNum, int pageSize, int total) {
        return newMultiTablePagination(list, pageNum, pageSize, total, 8);
    }

    /**
     * 多表分页
     *
     * @param list          数据
     * @param pageNum       页码数量
     * @param pageSize      每页数量
     * @param total         总数据量
     * @param navigatePages 导航也数量
     * @return multiTablePage
     * @see MultiTablePage
     */
    @SuppressWarnings("unchecked")
    public static <T> MultiTablePage newMultiTablePagination(List<T> list, int pageNum, int pageSize, int total, int navigatePages) {
        return new MultiTablePage(list, pageNum, pageSize, total, navigatePages);
    }

    /** 多表分页 */
    @SuppressWarnings({"rawtypes"})
    @Getter
    @Setter
    public static class MultiTablePage<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        //当前页
        private int pageNum;
        //每页的数量
        private int pageSize;
        //当前页的数量
        private int size;
        //排序
        private String orderBy;

        //由于startRow和endRow不常用，这里说个具体的用法
        //可以在页面中"显示startRow到endRow 共size条数据"

        //当前页面第一个元素在数据库中的行号
        private int startRow = 0;
        //当前页面最后一个元素在数据库中的行号
        private int endRow = 0;
        //总记录数
        private long total;
        //总页数
        private int pages;
        //结果集
        private List<T> list;

        //第一页
        private int firstPage;
        //前一页
        private int prePage;
        //下一页
        private int nextPage;
        //最后一页
        private int lastPage;

        //是否为第一页
        private boolean isFirstPage = false;
        //是否为最后一页
        private boolean isLastPage = false;
        //是否有前一页
        private boolean hasPreviousPage = false;
        //是否有下一页
        private boolean hasNextPage = false;
        //导航页码数
        private int navigatePages;
        //所有导航页号
        private int[] navigatepageNums;

        /**
         * 包装Page对象
         *
         * @param list     数据
         * @param pageNum  页码数量
         * @param pageSize 每页数量
         * @param total    总数据量
         */
        public MultiTablePage(List<T> list, int pageNum, int pageSize, int total) {
            this(list, pageNum, pageSize, total, 8);
        }

        /**
         * 包装Page对象
         *
         * @param list          数据
         * @param pageNum       页码数量
         * @param pageSize      每页数量
         * @param total         总数据量
         * @param navigatePages 导航也数量
         */
        public MultiTablePage(List<T> list, int pageNum, int pageSize, int total, int navigatePages) {

            this.list = list;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.total = total;
            this.navigatePages = navigatePages;

            this.size = list.size();

            if (this.total < 0) {
                this.pages = 1;
            }

            this.pages = pageSize > 0 ? total / pageSize + ((total % pageSize == 0) ? 0 : 1) : 0;

            //计算导航页
            calcNavigatePageNums();
            //计算前后页，第一页，最后一页
            calcPage();
            //判断页面边界
            judgePageBoudary();
        }

        /**
         * 计算导航页
         */
        private void calcNavigatePageNums() {
            //当总页数小于或等于导航页码数时
            if (pages <= navigatePages) {
                navigatepageNums = new int[pages];
                for (int i = 0; i < pages; i++) {
                    navigatepageNums[i] = i + 1;
                }
            } else { //当总页数大于导航页码数时
                navigatepageNums = new int[navigatePages];
                int startNum = pageNum - navigatePages / 2;
                int endNum = pageNum + navigatePages / 2;

                if (startNum < 1) {
                    startNum = 1;
                    //(最前navigatePages页
                    for (int i = 0; i < navigatePages; i++) {
                        navigatepageNums[i] = startNum++;
                    }
                } else if (endNum > pages) {
                    endNum = pages;
                    //最后navigatePages页
                    for (int i = navigatePages - 1; i >= 0; i--) {
                        navigatepageNums[i] = endNum--;
                    }
                } else {
                    //所有中间页
                    for (int i = 0; i < navigatePages; i++) {
                        navigatepageNums[i] = startNum++;
                    }
                }
            }
        }

        /**
         * 计算前后页，第一页，最后一页
         */
        private void calcPage() {
            if (navigatepageNums != null && navigatepageNums.length > 0) {
                firstPage = navigatepageNums[0];
                lastPage = navigatepageNums[navigatepageNums.length - 1];
                if (pageNum > 1) {
                    prePage = pageNum - 1;
                }
                if (pageNum < pages) {
                    nextPage = pageNum + 1;
                }
            }
        }

        /**
         * 判定页面边界
         */
        private void judgePageBoudary() {
            isFirstPage = pageNum == 1;
            isLastPage = pageNum == pages;
            hasPreviousPage = pageNum > 1;
            hasNextPage = pageNum < pages;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("PageInfo{");
            sb.append("pageNum=").append(pageNum);
            sb.append(", pageSize=").append(pageSize);
            sb.append(", size=").append(size);
            sb.append(", startRow=").append(startRow);
            sb.append(", endRow=").append(endRow);
            sb.append(", total=").append(total);
            sb.append(", pages=").append(pages);
            sb.append(", list=").append(list);
            sb.append(", firstPage=").append(firstPage);
            sb.append(", prePage=").append(prePage);
            sb.append(", nextPage=").append(nextPage);
            sb.append(", lastPage=").append(lastPage);
            sb.append(", isFirstPage=").append(isFirstPage);
            sb.append(", isLastPage=").append(isLastPage);
            sb.append(", hasPreviousPage=").append(hasPreviousPage);
            sb.append(", hasNextPage=").append(hasNextPage);
            sb.append(", navigatePages=").append(navigatePages);
            sb.append(", navigatepageNums=");

            if (navigatepageNums == null) {
                sb.append("null");
            } else {
                sb.append('[');
                for (int i = 0; i < navigatepageNums.length; ++i) {
                    sb.append(i == 0 ? "" : ", ").append(navigatepageNums[i]);
                }
                sb.append(']');
            }
            sb.append('}');
            return sb.toString();
        }
    }
}
