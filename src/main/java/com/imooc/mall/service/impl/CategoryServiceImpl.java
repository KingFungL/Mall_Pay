package com.imooc.mall.service.impl;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author King
 * @create 2023-02-14 10:17 下午
 * @Description:
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categoryList = categoryMapper.selectAll();

        //查询partent_id = 0的数据 （for循环方式）
//        for(Category category : categoryList){
//            if(category.getParentId().equals(MallConst.ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category, categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

        //Lambda表达式 + stream
        categoryVoList = categoryList.stream()
                .filter(e -> e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                .map(e -> category2CategoryVo(e))
                .collect(Collectors.toList());

        //查询子目录
        findSubCategory(categoryVoList, categoryList);

        return ResponseVo.success(categoryVoList);
    }

    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categoryList){
        for(CategoryVo categoryVo : categoryVoList){
            List<CategoryVo> subCategoryVoList = new ArrayList<>();

            for(Category category : categoryList){
                //如果查到内容，设置subCategory，继续往下查
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                //降序
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                //递归：把新找到的子目录再调用该方法继续找下一级子目录
                findSubCategory(subCategoryVoList, categoryList);
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return  categoryVo;
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories){
        for(Category category : categories){
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }
}
