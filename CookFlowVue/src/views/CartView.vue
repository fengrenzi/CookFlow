<template>
  <div class="cart-container">
    <!-- 购物车为空状态 -->
    <div v-if="cartItems.length === 0" class="empty-cart">
      <div class="empty-cart-icon">🛒</div>
      <h3>购物车还是空的</h3>
      <p>快去添加一些菜谱或书籍中的食材吧！</p>
      <router-link to="/recipes" class="btn btn-primary">去浏览菜谱</router-link>
    </div>

    <!-- 购物车列表 -->
    <div v-else class="cart-content">
      <!-- 购物车项目列表 -->
      <div class="cart-items">
        <!-- 已选中提示和操作按钮 -->
        <div class="selected-header"
          style="display: flex; align-items: center; justify-content: space-between; margin: 0 0 20px 0;">
          <p class="selected-count" style="color: #ff4d4f; margin: 0; font-size: 16px;">已选中 {{
            selectedItems.length }} 个项目</p>
          <div class="selection-actions" style="display: flex; align-items: center; gap: 10px;">
            <input type="checkbox" id="select-all-checkbox"
              :checked="selectedItems.length === cartItems.length && cartItems.length > 0"
              @change="handleSelectAllCheckbox" style="width: 16px; height: 16px; cursor: pointer;">
            <label for="select-all-checkbox" class="btn btn-danger" style="padding: 0;">
              全选
            </label>
            <button class="btn btn-danger" @click="handleDeleteAllSelected" style="padding: 5px 15px; font-size: 14px;"
              :disabled="selectedItems.length === 0">
              全部删除
            </button>
          </div>
        </div>
        <div v-for="item in cartItems" :key="item.id" class="cart-item" :class="`cart-item-${item.type}`">
          <!-- 多选按钮 -->
          <div class="checkbox-container">
            <input type="checkbox" :id="`item-${item.id}`" :checked="selectedItems.includes(item.id)"
              @change="handleItemSelection(item.id)" class="item-checkbox">
            <label :for="`item-${item.id}`" class="checkbox-label"></label>
          </div>
          <!-- 菜谱类型的购物车项 -->
          <template v-if="item.type === 'recipe'">
            <div class="recipe-item-content">
              <!-- 上部：菜谱信息 -->
              <div class="recipe-item-header">
                <div class="recipe-main-info">
                  <img :src="item.recipeImageUrl" :alt="item.recipeName" class="recipe-image">
                  <div class="recipe-info">
                    <h4 class="recipe-name">{{ item.recipeName }}</h4>
                    <span class="item-type">菜谱</span>
                  </div>
                </div>
                <div class="quantity-control">
                  <button class="quantity-btn decrease" @click="handleDecrease(item.id)">
                    -
                  </button>
                  <span class="quantity-value">{{ item.quantity }}</span>
                  <button class="quantity-btn increase" @click="handleIncrease(item.id)">
                    +
                  </button>
                </div>
              </div>
              <!-- 下部：食材列表 -->
              <div class="ingredients-list" :class="{ 'ingredients-list-selected': selectedItems.includes(item.id) }">
                <h5>食材：</h5>
                <div class="ingredients-grid">
                  <div v-for="ingredient in item.ingredients" :key="ingredient.id" class="ingredient-item" :class="{ 'ingredient-item-selected': selectedItems.includes(item.id) }">
                    <img :src="ingredient.imageUrl || '/images/default-ingredient.png'" :alt="ingredient.name" class="ingredient-image" :class="{ 'ingredient-image-selected': selectedItems.includes(item.id) }">
                    <div class="ingredient-info">
                      <span class="ingredient-name">{{ ingredient.name }}</span>
                      <span class="ingredient-amount">{{ ingredient.amount * item.quantity }} {{ ingredient.unit
                        }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>

          <!-- 书籍类型的购物车项 -->
          <template v-else-if="item.type === 'book'">
            <div class="book-item-content">
              <!-- 上部：书籍信息 -->
              <div class="book-item-header">
                <img :src="item.bookImageUrl" :alt="item.bookName" class="book-image">
                <div class="book-info">
                  <h4 class="book-name">{{ item.bookName }}</h4>
                  <span class="page-info">第{{ item.pageNumber }}页</span>
                </div>
              </div>

              <!-- 中部：菜谱信息 -->
              <div class="book-recipe-info">
                <div class="recipe-main-info">
                  <img :src="item.recipeImageUrl" :alt="item.recipeName" class="recipe-image">
                  <div class="recipe-info">
                    <h5 class="recipe-name">{{ item.recipeName }}</h5>
                    <span class="item-type">书籍菜谱</span>
                  </div>
                </div>
                <div class="quantity-control">
                  <button class="quantity-btn decrease" @click="handleDecrease(item.id)">
                    -
                  </button>
                  <span class="quantity-value">{{ item.quantity }}</span>
                  <button class="quantity-btn increase" @click="handleIncrease(item.id)">
                    +
                  </button>
                </div>
              </div>

              <!-- 下部：食材列表 -->
              <div class="ingredients-list" :class="{ 'ingredients-list-selected': selectedItems.includes(item.id) }">
                <h5>食材：</h5>
                <div class="ingredients-grid">
                  <div v-for="ingredient in item.ingredients" :key="ingredient.id" class="ingredient-item" :class="{ 'ingredient-item-selected': selectedItems.includes(item.id) }">
                    <img :src="ingredient.imageUrl || '/images/default-ingredient.png'" :alt="ingredient.name" class="ingredient-image" :class="{ 'ingredient-image-selected': selectedItems.includes(item.id) }">
                    <div class="ingredient-info">
                      <span class="ingredient-name">{{ ingredient.name }}</span>
                      <span class="ingredient-amount">{{ ingredient.amount * item.quantity }} {{ ingredient.unit
                        }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>

      <!-- 生成购物清单区域 -->
      <div class="shopping-list-section">
        <h3>购物清单</h3>
        <div class="list-summary">
          <div>
            <p>共 {{ selectedItems.length > 0 ? selectedIngredients.length : 0 }} 种食材</p>
          </div>
          <div class="list-actions">
            <button class="btn btn-primary generate-text-btn" @click="handleGenerateTextList">
              生成文本清单
            </button>
            <button class="btn btn-secondary generate-image-btn" @click="handleGenerateImageList">
              生成图片清单
            </button>
          </div>
        </div>

        <!-- 购物清单预览 -->
        <div class="shopping-list-preview">
          <h4>食材汇总</h4>
          <div class="ingredients-summary">
            <div v-if="selectedItems.length > 0">
              <div v-for="(ingredient, index) in selectedIngredients" :key="`${ingredient.name}-${index}`"
                class="summary-item">
                <img :src="ingredient.imageUrl" :alt="ingredient.name" class="summary-image">
                <div class="summary-info">
                  <span class="summary-name">{{ ingredient.name }}</span>
                  <span class="summary-amount">{{ ingredient.amount }} {{ ingredient.unit }}</span>
                </div>
              </div>
            </div>
            <div v-else class="empty-summary">
              <p style="color: #999; text-align: center; padding: 30px 0; margin: 0;">请选择要加入购物清单的项目</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 文本清单弹窗 -->
    <div v-if="showTextModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>购物清单文本</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <pre class="text-list">{{ shoppingListText }}</pre>
          <button class="btn btn-primary copy-btn" @click="copyTextList">
            复制文本
          </button>
        </div>
      </div>
    </div>

    <!-- 图片清单弹窗 -->
    <div v-if="showImageModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content image-modal" @click.stop>
        <div class="modal-header">
          <h3>购物清单图片</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <div ref="imageListRef" class="image-list-container">
            <div class="image-list-header">
              <h2>购物清单</h2>
              <p>{{ new Date().toLocaleString() }}</p>
            </div>
            <div class="image-list-items">
              <div
                v-for="(ingredient, index) in (selectedItems.length > 0 ? selectedIngredients : allIngredients)"
                :key="ingredient.id || `${ingredient.name}-${index}`"
                class="image-list-item">
                <img :src="ingredient.imageUrl" :alt="ingredient.name" class="image-item-image">
                <div class="image-item-info">
                  <span class="image-item-name">{{ ingredient.name }}</span>
                  <span class="image-item-amount">{{ ingredient.amount }} {{ ingredient.unit }}</span>
                </div>
              </div>
            </div>
            <div class="image-list-footer">
              <p>共 {{ selectedItems.length > 0 ? selectedIngredients.length : 0 }} 种食材</p>
            </div>
          </div>
          <button class="btn btn-primary download-btn" @click="downloadImageList">
            下载图片
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartItem, removeCartItem, generateShoppingListText } from '@/api/shoppingCart'

// 响应式数据
const cartItems = ref<any[]>([])      // 购物车列表，替代 cartStore.allItems
const showTextModal = ref(false)
const showImageModal = ref(false)
const selectedItems = ref<string[]>([])  // 存储购物车项的 id
const shoppingListText = ref('')
const imageListRef = ref<HTMLElement>()

// 加载购物车数据
const loadCart = async () => {
  try {
    const data = await getCartList()
    // 将后端返回的数据转换为前端期望的结构（适配原有模板字段）
    cartItems.value = data.map((item: any) => {
      // 根据 itemType 映射 type 字段（模板中使用了 type 和具体字段）
      let mappedItem: any = {
        id: item.cartId || item.id,
        type: item.itemType === 'recipe' ? 'recipe' : 'book',  // 模板中只有 recipe 和 book 两种，book_recipe 也归为 book
        quantity: item.quantity,
        ingredients: item.ingredients || [],
      }
      if (item.itemType === 'recipe') {
        mappedItem.recipeName = item.detail?.title || ''
        mappedItem.recipeImageUrl = item.detail?.imageUrl || ''
      } else {
        // book 或 book_recipe
        mappedItem.bookName = item.detail?.bookTitle || item.detail?.title || ''
        mappedItem.bookImageUrl = item.detail?.coverUrl || item.detail?.imageUrl || ''
        mappedItem.pageNumber = item.detail?.pageNumber || 1
        mappedItem.recipeName = item.detail?.recipeName || item.detail?.title || ''
        mappedItem.recipeImageUrl = item.detail?.recipeImageUrl || item.detail?.imageUrl || ''
      }
      return mappedItem
    })
  } catch (error) {
    ElMessage.error('加载购物车失败')
  }
}

// 保存购物车到后端（更新数量或删除后重新加载）
const refreshCart = () => {
  loadCart()
}

// 处理数量增减
const handleIncrease = async (itemId: string) => {
  const item = cartItems.value.find(i => i.id === itemId)
  if (item) {
    try {
      await updateCartItem(itemId, { quantity: item.quantity + 1 })
      item.quantity++
      ElMessage.success('数量已更新')
    } catch (error) {
      ElMessage.error('更新失败')
    }
  }
}

const handleDecrease = async (itemId: string) => {
  const item = cartItems.value.find(i => i.id === itemId)
  if (item) {
    if (item.quantity > 1) {
      try {
        await updateCartItem(itemId, { quantity: item.quantity - 1 })
        item.quantity--
        ElMessage.success('数量已更新')
      } catch (error) {
        ElMessage.error('更新失败')
      }
    } else {
      // 当数量为1时，弹出确认对话框
      try {
        await ElMessageBox.confirm(
          '确定要删除该菜谱吗？',
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await removeCartItem(itemId)
        // 从本地列表中删除
        cartItems.value = cartItems.value.filter(i => i.id !== itemId)
        // 从选中列表中移除
        const idx = selectedItems.value.indexOf(itemId)
        if (idx > -1) selectedItems.value.splice(idx, 1)
        ElMessage.success('菜谱已删除')
      } catch {
        ElMessage.info('已取消删除')
      }
    }
  }
}

// 处理项目选择
const handleItemSelection = (itemId: string) => {
  const index = selectedItems.value.indexOf(itemId)
  if (index > -1) {
    selectedItems.value.splice(index, 1)
  } else {
    selectedItems.value.push(itemId)
  }
}

// 全选/取消全选
const handleSelectAllCheckbox = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.checked) {
    selectedItems.value = cartItems.value.map(item => item.id)
  } else {
    selectedItems.value = []
  }
}

// 删除选中的项目
const handleDeleteAllSelected = async () => {
  if (selectedItems.value.length === 0) return
  try {
    await ElMessageBox.confirm(
      `确定要删除所有选中的${selectedItems.value.length}个项目吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 逐个删除
    for (const itemId of selectedItems.value) {
      await removeCartItem(itemId)
    }
    // 刷新列表
    await loadCart()
    selectedItems.value = []
    ElMessage.success('已删除所有选中项目')
  } catch {
    ElMessage.info('已取消删除')
  }
}

// 计算选中项目的食材汇总（基于当前 cartItems）
const selectedIngredients = computed(() => {
  const ingredientMap = new Map<string, { id?: string; name: string; amount: number; unit: string; imageUrl: string }>()
  cartItems.value.forEach(item => {
    if (selectedItems.value.includes(item.id)) {
      item.ingredients.forEach((ingredient: any) => {
        const key = ingredient.name
        const totalAmount = ingredient.amount * item.quantity
        if (ingredientMap.has(key)) {
          const existing = ingredientMap.get(key)!
          ingredientMap.set(key, {
            ...existing,
            amount: existing.amount + totalAmount
          })
        } else {
          ingredientMap.set(key, {
            id: ingredient.id,
            name: ingredient.name,
            amount: totalAmount,
            unit: ingredient.unit,
            imageUrl: ingredient.imageUrl
          })
        }
      })
    }
  })
  return Array.from(ingredientMap.values())
})

// 计算所有食材汇总（用于图片弹窗中的默认显示）
const allIngredients = computed(() => {
  const ingredientMap = new Map<string, { id?: string; name: string; amount: number; unit: string; imageUrl: string }>()
  cartItems.value.forEach(item => {
    item.ingredients.forEach((ingredient: any) => {
      const key = ingredient.name
      const totalAmount = ingredient.amount * item.quantity
      if (ingredientMap.has(key)) {
        const existing = ingredientMap.get(key)!
        ingredientMap.set(key, {
          ...existing,
          amount: existing.amount + totalAmount
        })
      } else {
        ingredientMap.set(key, {
          id: ingredient.id,
          name: ingredient.name,
          amount: totalAmount,
          unit: ingredient.unit,
          imageUrl: ingredient.imageUrl
        })
      }
    })
  })
  return Array.from(ingredientMap.values())
})

// 生成购物清单（文本）
const handleGenerateTextList = async () => {
  try {
    let text = ''
    if (selectedItems.value.length > 0) {
      // 调用后端接口生成文本
      const res = await generateShoppingListText(selectedItems.value)
      text = res.text
    } else {
      // 如果没有选中项，生成全部食材的文本（前端计算）
      text = '购物清单\n'
      text += `生成时间: ${new Date().toLocaleString()}\n\n`
      allIngredients.value.forEach(ing => {
        text += `- ${ing.name}: ${ing.amount} ${ing.unit}\n`
      })
      text += `\n共 ${allIngredients.value.length} 种食材`
    }
    shoppingListText.value = text
    showTextModal.value = true
  } catch (error) {
    ElMessage.error('生成失败')
  }
}

// 生成图片清单
const handleGenerateImageList = async () => {
  await nextTick()
  showImageModal.value = true
}

// 复制文本清单
const copyTextList = () => {
  navigator.clipboard.writeText(shoppingListText.value)
    .then(() => ElMessage.success('文本已复制到剪贴板'))
    .catch(() => ElMessage.error('复制失败，请手动复制'))
}

// 下载图片清单
const downloadImageList = async () => {
  // 如果需要真正实现图片生成，请安装 html2canvas 并实现，这里保留原有提示
  ElMessage.info('图片生成功能需要引入html2canvas库')
}

// 关闭弹窗
const closeModal = () => {
  showTextModal.value = false
  showImageModal.value = false
}

// 处理滚动显示返回顶部按钮（保留原逻辑）
const handleScroll = () => {
  // 空实现，可后续添加
}

onMounted(() => {
  loadCart()
  window.addEventListener('scroll', handleScroll)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
@import '@/styles/components/CartView.css';
</style>