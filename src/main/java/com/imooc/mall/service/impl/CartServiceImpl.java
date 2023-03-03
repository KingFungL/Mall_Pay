package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.consts.MallConst;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartProductVo;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.image.RescaleOp;
import java.math.BigDecimal;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author King
 * @create 2023-02-25 9:31 下午
 * @Description:
 */
@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLATE = "cary_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        //判断商品是否存在
        if(product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //商品是否正常在售
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //商品库存是否充足
        if(product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //写入到Redis
        //key：caty_1 使用Hash结构实现Redis高性能
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //从redis读出数据
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if(StringUtils.isEmpty(value)){
            //没有该商品
            cart = new Cart(product.getId(), quantity, form.getSelected());
        }else{
            //已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(String.format(CART_REDIS_KEY_TEMPLATE, uid),
                String.valueOf(product.getId()),
                gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries =  opsForHash.entries(redisKey);

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //TODO 需要优化，使用mysql的in（避免for循环查数据）
            Product product = productMapper.selectByPrimaryKey(productId);
            if(product != null){
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                        );
                cartProductVoList.add(cartProductVo);

                if(!cart.getProductSelected()){
                    selectAll = false;
                }

                //业务：计算总价（只计算选中的）
                if(cart.getProductSelected()){
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice()); //add方法需要重新返回累加后的值
                }

            }

            cartTotalQuantity += cart.getQuantity();
        }

        //有一个未选中就不是全选
        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);

        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //从redis读出数据
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品,报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);
        if (form.getQuantity() != null
                && form.getQuantity() >= 0){
            cart.setQuantity(form.getQuantity());
        }
        if(form.getSelected() != null){
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);

    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //从redis读出数据
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品,报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        opsForHash.delete(redisKey, String.valueOf(productId));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {

        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);

        return ResponseVo.success(sum);
    }

    private List<Cart> listForCart(Integer uid){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries =  opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }
        return cartList;
    }
}
