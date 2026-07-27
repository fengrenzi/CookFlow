import { defineStore } from 'pinia';

// 定义轮播图项的类型
interface CarouselItem {
  imageUrl: string;
  title: string;
  tags: string[];
  description: string;
  features: { name: string; value: string }[];
}

// 定义提示项的类型
interface TipItem {
  id: number;
  title: string;
  description: string;
}

// 定义处理步骤的类型
interface ProcessingStep {
  id: number;
  title: string;
  description: string;
  image: string;
}

// 定义营养数据的类型
interface Nutrient {
  name: string;
  value: string;
}

// 定义知识点的类型
interface KnowledgePoint {
  id: number;
  content: string;
}

// 导出食材详情store
export const useIngredientDetailStore = defineStore('ingredientDetail', {
  state: () => ({
    // 轮播图数据
    carouselItems: [
      {
        imageUrl: '/hots/1.jpg',
        title: '新鲜有机蔬菜',
        tags: ['有机认证', '新鲜采摘', '营养丰富'],
        description: '我们的蔬菜来自有机农场，不使用化学农药和肥料，确保您吃到最健康的食材。每天早晨新鲜采摘，保证最佳口感和营养价值。',
        features: [
          { name: '产地', value: '本地农场' },
          { name: '保质期', value: '7天' },
          { name: '存储方式', value: '冷藏保存' }
        ]
      },
      {
        imageUrl: '/hots/2.jpg',
        title: '优质肉类食材',
        tags: ['安全检疫', '新鲜配送', '高蛋白'],
        description: '精选优质肉类，经过严格的安全检疫，确保食品安全。采用冷链配送，保持肉质新鲜。富含优质蛋白质，是健康饮食的理想选择。',
        features: [
          { name: '产地', value: '正规养殖场' },
          { name: '保质期', value: '3天' },
          { name: '存储方式', value: '冷冻保存' }
        ]
      },
      {
        imageUrl: '/hots/3.jpg',
        title: '时令水果精选',
        tags: ['时令水果', '鲜甜多汁', '维生素丰富'],
        description: '当季新鲜水果，自然成熟，鲜甜多汁。富含多种维生素和矿物质，有助于增强免疫力，保持身体健康。',
        features: [
          { name: '产地', value: '优质果园' },
          { name: '保质期', value: '5天' },
          { name: '存储方式', value: '常温或冷藏' }
        ]
      },
      {
        imageUrl: '/hots/3.jpg',
        title: '时令水果精选',
        tags: ['时令水果', '鲜甜多汁', '维生素丰富'],
        description: '当季新鲜水果，自然成熟，鲜甜多汁。富含多种维生素和矿物质，有助于增强免疫力，保持身体健康。',
        features: [
          { name: '产地', value: '优质果园' },
          { name: '保质期', value: '5天' },
          { name: '存储方式', value: '常温或冷藏' }
        ]
      },
      {
        imageUrl: '/hots/3.jpg',
        title: '时令水果精选',
        tags: ['时令水果', '鲜甜多汁', '维生素丰富'],
        description: '当季新鲜水果，自然成熟，鲜甜多汁。富含多种维生素和矿物质，有助于增强免疫力，保持身体健康。',
        features: [
          { name: '产地', value: '优质果园' },
          { name: '保质期', value: '5天' },
          { name: '存储方式', value: '常温或冷藏' }
        ]
      }
    ] as CarouselItem[],
    
    // 食材挑选提示数据
    selectionTips: [
      {
        id: 1,
        title: '选择新鲜食材',
        description: '挑选食材时，应选择新鲜、无损伤、无异味的食材，以确保食物的营养价值和安全性。'
      },
      {
        id: 2,
        title: '注意食材颜色',
        description: '新鲜的食材通常具有鲜艳的颜色，如新鲜的蔬菜叶片翠绿，水果色泽光亮。'
      },
      {
        id: 3,
        title: '查看保质期',
        description: '购买包装食品时，务必查看保质期和生产日期，避免购买过期或即将过期的产品。'
      },
      {
        id: 4,
        title: '闻气味辨别',
        description: '通过闻气味可以辨别食材的新鲜程度，新鲜的食材应有自然的清香，无异味。'
      },
      {
        id: 5,
        title: '选择时令食材',
        description: '时令食材不仅价格更加合理，而且口感和营养价值也更佳，应优先选择。'
      }
    ] as TipItem[],
    
    // 食材处理步骤数据
    processingSteps: [
      {
        id: 1,
        title: '清洗准备',
        description: '将食材用清水冲洗干净，去除表面的泥土、农药残留和杂质。对于叶菜类，可以在盐水中浸泡10-15分钟。',
        image: '/hots/1.jpg'
      },
      {
        id: 2,
        title: '切配处理',
        description: '根据烹饪需要，将食材切成适当的形状和大小。切配时应注意刀工，保持食材的均匀性。',
        image: '/hots/2.jpg'
      },
      {
        id: 3,
        title: '预处理',
        description: '根据食材特性，进行焯水处理、腌制、上浆等预处理操作，以改善食材的口感和烹饪效果。',
        image: '/hots/3.jpg'
      },
      {
        id: 4,
        title: '保存方法',
        description: '处理后的食材如需保存，应根据其特性选择合适的保存方式，如冷藏、冷冻或常温保存。',
        image: '/hots/1.jpg'
      }
    ] as ProcessingStep[],
    
    // 营养价值数据
    nutritionData: [
      { name: '蛋白质', value: '12.5g' },
      { name: '碳水化合物', value: '25.8g' },
      { name: '膳食纤维', value: '3.2g' },
      { name: '维生素A', value: '85μg' },
      { name: '维生素C', value: '24mg' },
      { name: '钙', value: '150mg' },
      { name: '铁', value: '2.8mg' },
      { name: '锌', value: '1.2mg' }
    ] as Nutrient[],
    
    // 科普知识点
    knowledgePoints: [
      { id: 1, content: '食材中的膳食纤维有助于促进肠道蠕动，预防便秘，降低胆固醇水平。' },
      { id: 2, content: '深色蔬菜通常含有更多的抗氧化物质和维生素，对身体健康更有益。' },
      { id: 3, content: '适当的食材搭配可以提高营养吸收率，如富含维生素C的食材可以促进铁的吸收。' },
      { id: 4, content: '合理的烹饪方式可以最大限度地保留食材的营养价值，如蒸、煮等方式优于油炸。' },
      { id: 5, content: '食材的储存方式会影响其营养价值，应根据食材特性选择合适的储存方法。' },
      { id: 6, content: '了解食材的季节性特征，选择当季食材不仅口感更好，而且营养价值更高。' }
    ] as KnowledgePoint[]
  }),
  
  getters: {
    // 获取所有轮播图项
    getAllCarouselItems: (state) => state.carouselItems,
    
    // 获取所有挑选提示
    getAllSelectionTips: (state) => state.selectionTips,
    
    // 获取所有处理步骤
    getAllProcessingSteps: (state) => state.processingSteps,
    
    // 获取所有营养数据
    getAllNutritionData: (state) => state.nutritionData,
    
    // 获取所有知识点
    getAllKnowledgePoints: (state) => state.knowledgePoints
  }
});