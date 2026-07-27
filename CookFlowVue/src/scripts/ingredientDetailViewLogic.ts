import { ref, onMounted } from 'vue';

// 整合的模拟JSON数据结构，便于后续替换为后端API
const mockIngredientDetailResponse = {
  // carouselItems: 轮播图数据数组
  "carouselItems": [
    { 
      "id": 1, 
      "name": "西红柿", 
      "description": "西红柿富含维生素C和番茄红素，是常见的烹饪食材。", 
      "image": "/hots/1.jpg"
    },
    { 
      "id": 2, 
      "name": "西红柿", 
      "description": "新鲜番茄，酸甜可口，适合多种烹饪方式。", 
      "image": "/hots/2.jpg"
    },
    { 
      "id": 3, 
      "name": "西红柿", 
      "description": "优质番茄，富含营养，是健康饮食的理想选择。", 
      "image": "/hots/3.jpg"
    }
  ],
  // selectionTips: 选购技巧数据（整合图二数据）
  "selectionTips": {
    "categories": [
      {
        "id": "1",
        "name": "视觉标准",
        "items": ["颜色", "形状", "光泽", "纹理"]
      },
      {
        "id": "2",
        "name": "触觉标准",
        "items": ["硬度", "弹性", "重量", "表面质感"]
      },
      {
        "id": "3",
        "name": "嗅觉标准",
        "items": ["新鲜度", "特有香气", "异味检测"]
      },
      {
        "id": "4",
        "name": "时令指南",
        "items": ["当季月份", "最佳产地", "品种对比"]
      },
      {
        "id": "5",
        "name": "避坑指南",
        "items": ["常见造假手段", "催熟识别", "保鲜剂检测"]
      }
    ]
  },
  
  // processingSteps: 食材处理数据（整合图三数据）
  "processingSteps": {
    "categories": [
      {
        "id": "1",
        "name": "清洗技巧",
        "items": ["流水冲洗", "盐水浸泡", "小苏打清洗", "淀粉去污"]
      },
      {
        "id": "2",
        "name": "切割方式",
        "items": ["切丝", "切片", "切块", "切末"]
      },
      {
        "id": "3",
        "name": "基础刀工",
        "items": ["切丝", "切片", "切块", "切末"]
      },
      {
        "id": "4",
        "name": "花刀技法",
        "items": ["十字花刀", "麦穗花刀", "梳子花刀", "牡丹刀花"]
      },
      {
        "id": "5",
        "name": "专业技法",
        "items": ["滚刀块", "马蹄朵", "象牙条", "蓑衣片"]
      },
      {
        "id": "6",
        "name": "计量参考",
        "subcategories": [
          {
            "name": "容量参考",
            "items": ["一撮~拇指食指捏量", "一茶匙~5ml", "一汤匙~15ml"]
          },
          {
            "name": "体积类比",
            "items": ["一个鸡蛋=50g", "一个苹果=150g", "一片生姜=10g"]
          },
          {
            "name": "容器类比",
            "items": ["一碗米=150g", "一勺油=8g", "一瓣蒜=5g"]
          }
        ]
      },
      {
        "id": "7",
        "name": "预处理",
        "items": ["腌制", "焯水", "泡发", "去腥"]
      }
    ]
  },
  
  // knowledgePoints: 食材科普数据（整合图四数据）
  "knowledgePoints": {
    "categories": [
      {
        "id": "1",
        "name": "基础信息",
        "items": ["学名", "科属", "原产地", "历史渊源"]
      },
      {
        "id": "2",
        "name": "营养价值",
        "items": ["热量", "营养成分", "食疗功效", "适宜人群"]
      },
      {
        "id": "3",
        "name": "储存方法",
        "items": ["冷藏", "冷冻", "常温", "特殊储存"]
      },
      {
        "id": "4",
        "name": "搭配禁忌",
        "items": ["相克食物", "药物禁忌", "疾病禁忌"]
      },
      {
        "id": "5",
        "name": "文化故事",
        "items": ["历史典故", "文学作品", "民间传说"]
      }
    ]
  }
};

// 模拟API请求函数，可后续替换为真实的fetch或axios调用
const fetchIngredientDetailFromApi = async (): Promise<any> => {
  // 模拟网络延迟
  await new Promise(resolve => setTimeout(resolve, 300));
  
  // 这里返回模拟数据，实际项目中应该是：
  // return await fetch(`/api/ingredients/${ingredientId}`).then(res => res.json());
  
  // 可以根据传入的ID返回不同的数据，目前总是返回西红柿的数据
  return { ...mockIngredientDetailResponse };
};

// 定义食材详情视图的逻辑组合式函数
export function useIngredientDetailLogic() {
  // 响应式数据
  const activeTab = ref('selection');
  const currentIndex = ref(0);
  const loading = ref(false);
  const error = ref<string | null>(null);
  
  // 从整合的模拟数据初始化
  const carouselItems = ref<any[]>(mockIngredientDetailResponse.carouselItems);
  const selectionTips = ref<any>(mockIngredientDetailResponse.selectionTips);
  const processingSteps = ref<any>(mockIngredientDetailResponse.processingSteps);
  const knowledgePoints = ref<any>(mockIngredientDetailResponse.knowledgePoints);
  
  // 轮播图方法
  const prevSlide = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--;
    }
  };
  
  const nextSlide = () => {
    if (carouselItems.value && currentIndex.value < carouselItems.value.length - 1) {
      currentIndex.value++;
    }
  };
  
  // 根据食材ID获取食材详情（使用模拟API）
  const fetchIngredientDetails = async () => {
    loading.value = true;
    error.value = null;
    
    try {
      // 调用模拟API获取数据
      const data = await fetchIngredientDetailFromApi();
      
      // 更新响应式数据
      carouselItems.value = data.carouselItems || [];
      selectionTips.value = data.selectionTips || { categories: [] };
      processingSteps.value = data.processingSteps || { categories: [] };
      knowledgePoints.value = data.knowledgePoints || { categories: [] };
    } catch (err) {
      error.value = '获取食材详情失败';
    } finally {
      loading.value = false;
    }
  };
  
  // 组件挂载时执行
  onMounted(() => {
    fetchIngredientDetails();
  });
  
  // 返回需要暴露给组件的数据和方法
  return {
    activeTab,
    currentIndex,
    loading,
    error,
    carouselItems,
    selectionTips,
    processingSteps,
    knowledgePoints,
    prevSlide,
    nextSlide,
    // 暴露获取数据的方法，便于组件在需要时重新加载
    fetchIngredientDetails
  };
}