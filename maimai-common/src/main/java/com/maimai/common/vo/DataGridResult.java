package com.maimai.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * mlsama
 * 2017/12/12 22:46
 * 描述:easyUI分页结果类
 */
@Getter
@Setter
@ToString
public class DataGridResult implements Serializable{
    //每页的数据
    private List<?> rows;
    //总页数
    private Long total;

}
