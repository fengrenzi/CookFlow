import request from '@/utils/request'

// 获取食材分类（顶级分类）
export function getIngredientCategories() {
  return request({
    url: '/category/list',
    method: 'get',
    params: {
      tableName: 'ingredients',
      parentId: '',        
      pageNum: 1,
      pageSize: 9999 
    }
  })
}

export function getIngredientTree() {
  return request({
    url: '/ingredients/tree',
    method: 'get'
  })
}

// 获取某个一级分类下的食材列表（子分类）
export function getIngredientList(params: { categoryId: string }) {
  return request({
    url: '/category/list',
    method: 'get',
    params: {
      tableName: 'ingredients',
      parentId: params.categoryId,
      pageNum: 1,
      pageSize: 9999
    }
  })
}

// 获取食材详情（保留原接口）
export function getIngredientDetail(id: string) {
  return request({
    url: `/category/${id}`,
    method: 'get'
  })
}

// 推荐菜谱接口保持不变（需后端实现）
export function getRecommendRecipes(ingredientIds: string[]) {
  return request({
    url: '/ingredients/recommendations',
    method: 'post',
    data: { selectedIngredientIds: ingredientIds }
  })
}

// 获取根据食材推荐的菜谱
export function getRecommendedRecipesByIngredients(ingredientIds: string[]) {
  return request({
    url: '/recipes/recommendByIngredients',
    method: 'get',
    params: { ingredientIds: ingredientIds.join(',') } // 后端用 List<String> 接收逗号分隔
  })
}