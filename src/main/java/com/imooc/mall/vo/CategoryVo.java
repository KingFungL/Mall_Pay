package com.imooc.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author King
 * @create 2023-02-14 10:13 下午
 * @Description:
 */
@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;

}
