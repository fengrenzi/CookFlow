package com.hnit.system.service.impl;

import com.hnit.common.utils.uuid.IdUtils;
import com.hnit.system.domain.*;
import com.hnit.system.domain.dto.AddToCartDTO;
import com.hnit.system.domain.dto.UpdateCartQuantityDTO;
import com.hnit.system.domain.vo.*;
import com.hnit.system.mapper.*;
import com.hnit.system.service.IShoppingCartService;
import com.hnit.system.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Resource
    private ShoppingCartMapper cartMapper;
    @Resource
    private RecipeIngredientsMapper recipeIngredientsMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ImagesRecordMapper imagesRecordMapper;
    @Resource
    private ImageManagementMapper imageManagementMapper;
    @Resource
    private RecipesMapper recipesMapper;
    @Resource
    private BookMapper booksMapper;
    @Resource
    private BookRecipesMapper bookRecipesMapper;

    // ==================== 基础 CRUD ====================

    @Override
    @Transactional
    public void addToCart(Long userId, AddToCartDTO dto) {
        ShoppingCart exist = cartMapper.selectByUserAndItem(userId, dto.getItemType(), dto.getItemId());
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + dto.getQuantity());
            cartMapper.updateQuantity(exist.getId(), exist.getQuantity());
        } else {
            ShoppingCart cart = new ShoppingCart();
            cart.setId(IdUtils.fastSimpleUUID());
            cart.setUserId(userId);
            cart.setItemType(dto.getItemType());
            cart.setItemId(dto.getItemId());
            cart.setQuantity(dto.getQuantity());
            cartMapper.insert(cart);
        }
    }

    @Override
    @Transactional
    public void updateQuantity(Long userId, String cartId, UpdateCartQuantityDTO dto) {
        ShoppingCart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在");
        }
        if (dto.getQuantity() <= 0) {
            cartMapper.deleteById(cartId);
        } else {
            cartMapper.updateQuantity(cartId, dto.getQuantity());
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, String cartId) {
        ShoppingCart cart = cartMapper.selectById(cartId);
        if (cart != null && cart.getUserId().equals(userId)) {
            cartMapper.deleteById(cartId);
        }
    }

    // ==================== 获取购物车列表（含详情） ====================

    @Override
    public List<ShoppingCartItemVO> getCartList(Long userId) {
        List<ShoppingCart> carts = cartMapper.selectByUserId(userId);
        List<ShoppingCartItemVO> result = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            ShoppingCartItemVO vo = new ShoppingCartItemVO();
            vo.setCartId(cart.getId());
            vo.setItemType(cart.getItemType());
            vo.setItemId(cart.getItemId());
            vo.setQuantity(cart.getQuantity());

            switch (cart.getItemType()) {
                case "recipe":
                    Recipes recipe = recipesMapper.selectRecipesById(cart.getItemId());
                    if (recipe != null) {
                        RecipeDetailVO detail = new RecipeDetailVO();
                        detail.setTitle(recipe.getTitle());
                        detail.setImageUrl(getRecipeMainImage(recipe.getId()));
                        vo.setDetail(detail);
                        vo.setIngredients(getRecipeIngredients(recipe.getId()));
                    } else {
                        vo.setIngredients(new ArrayList<>());
                    }
                    break;

                case "book":
                    BookVo book = booksMapper.selectById(cart.getItemId());
                    if (book != null) {
                        BookDetailVO detail = new BookDetailVO();
                        detail.setTitle(book.getTitle());
                        detail.setCoverUrl(getBookCoverImage(book.getCoverImageId()));
                        detail.setRecipeCount(book.getRecipeCount());
                        vo.setDetail(detail);
                        vo.setIngredients(new ArrayList<>()); // 整本书不展示食材
                    } else {
                        vo.setIngredients(new ArrayList<>());
                    }
                    break;

                case "book_recipe":
                    // book_recipes 主键是 BIGINT，item_id 是 String，需要转换
                    BookRecipe bookRecipe = bookRecipesMapper.selectById(Long.valueOf(cart.getItemId()));
                    if (bookRecipe != null) {
                        Recipes recipe2 = recipesMapper.selectRecipesById(bookRecipe.getRecipeId());
                        BookVo book2 = booksMapper.selectById(bookRecipe.getBookId());

                        BookRecipeDetailVO detail = new BookRecipeDetailVO();
                        detail.setBookTitle(book2 != null ? book2.getTitle() : "");
                        detail.setPageNumber(bookRecipe.getPageNumber());
                        detail.setRecipeName(recipe2 != null ? recipe2.getTitle() : "");
                        detail.setRecipeImageUrl(getRecipeMainImage(recipe2.getId()));
                        vo.setDetail(detail);

                        vo.setIngredients(getRecipeIngredients(recipe2.getId()));
                    } else {
                        vo.setIngredients(new ArrayList<>());
                    }
                    break;

                default:
                    vo.setIngredients(new ArrayList<>());
            }
            result.add(vo);
        }
        return result;
    }

    // ==================== 生成购物清单文本 ====================

    @Override
    public ShoppingListTextVO generateShoppingListText(Long userId, List<String> selectedCartIds) {
        // 1. 获取用户购物车中所有项
        List<ShoppingCart> allCarts = cartMapper.selectByUserId(userId);
        // 2. 筛选出选中的项（如果 selectedCartIds 为空或 null，则视为全选）
        List<ShoppingCart> selectedCarts;
        if (selectedCartIds == null || selectedCartIds.isEmpty()) {
            selectedCarts = allCarts;
        } else {
            Set<String> selectedSet = new HashSet<>(selectedCartIds);
            selectedCarts = allCarts.stream()
                    .filter(cart -> selectedSet.contains(cart.getId()))
                    .collect(Collectors.toList());
        }

        // 3. 汇总食材
        Map<String, IngredientVO> ingredientMap = new LinkedHashMap<>(); // 保持顺序
        for (ShoppingCart cart : selectedCarts) {
            if ("recipe".equals(cart.getItemType()) || "book_recipe".equals(cart.getItemType())) {
                String recipeId;
                if ("recipe".equals(cart.getItemType())) {
                    recipeId = cart.getItemId();
                } else {
                    // book_recipe：通过 book_recipes 获取 recipe_id
                    BookRecipe bookRecipe = bookRecipesMapper.selectById(Long.valueOf(cart.getItemId()));
                    if (bookRecipe == null) continue;
                    recipeId = bookRecipe.getRecipeId();
                }
                // 获取该菜谱的所有食材
                List<IngredientVO> ingredients = getRecipeIngredients(recipeId);
                for (IngredientVO ing : ingredients) {
                    String key = ing.getId(); // 使用食材 ID 作为唯一键
                    BigDecimal totalAmount = ing.getAmount().multiply(BigDecimal.valueOf(cart.getQuantity()));
                    if (ingredientMap.containsKey(key)) {
                        IngredientVO existing = ingredientMap.get(key);
                        existing.setAmount(existing.getAmount().add(totalAmount));
                    } else {
                        IngredientVO copy = new IngredientVO();
                        copy.setId(ing.getId());
                        copy.setName(ing.getName());
                        copy.setAmount(totalAmount);
                        copy.setUnit(ing.getUnit());
                        copy.setImageUrl(ing.getImageUrl());
                        ingredientMap.put(key, copy);
                    }
                }
            }
            // book 类型不参与食材汇总
        }

        // 4. 生成文本
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (IngredientVO ing : ingredientMap.values()) {
            sb.append(index++).append(". ").append(ing.getName())
                    .append(": ").append(ing.getAmount()).append(" ").append(ing.getUnit()).append("\n");
        }
        sb.append("\n共 ").append(ingredientMap.size()).append(" 种食材");

        ShoppingListTextVO vo = new ShoppingListTextVO();
        vo.setText(sb.toString());
        vo.setTotalCount(ingredientMap.size());
        return vo;
    }

    // ==================== 辅助方法 ====================

    private String getRecipeMainImage(String recipeId) {
        List<ImagesRecord> images = imagesRecordMapper.selectByRecipeId(recipeId);
        if (images != null && !images.isEmpty()) {
            ImageManagement img = imageManagementMapper.selectById(images.get(0).getImageId());
            if (img != null) {
                return ImageUtils.getFullUrl(img.getId());
            }
        }
        return null;
    }

    private String getBookCoverImage(String coverImageId) {
        if (coverImageId == null) return null;
        ImageManagement img = imageManagementMapper.selectById(coverImageId);
        if (img != null) {
            return ImageUtils.getFullUrl(img.getId());
        }
        return null;
    }

    private List<IngredientVO> getRecipeIngredients(String recipeId) {
        List<IngredientVO> result = new ArrayList<>();
        List<RecipeIngredients> riList = recipeIngredientsMapper.selectRecipeIngredientsByRecipeId(recipeId);
        for (RecipeIngredients ri : riList) {
            Category category = categoryMapper.selectCategoryById(ri.getCategoryId());
            if (category != null && "ingredients".equals(category.getTableName())) {
                IngredientVO ing = new IngredientVO();
                ing.setId(category.getId());
                ing.setName(category.getDesignation());
                ing.setAmount(ri.getAmount());
                ing.setUnit(ri.getUnit());
                if (category.getImageId() != null) {
                    ImageManagement img = imageManagementMapper.selectById(category.getImageId());
                    if (img != null) {
                        ing.setImageUrl(ImageUtils.getFullUrl(img.getId()));
                    }
                }
                result.add(ing);
            }
        }
        return result;
    }
}