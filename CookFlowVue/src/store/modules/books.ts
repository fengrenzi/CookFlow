import { defineStore } from 'pinia';

// 定义书籍类型
export interface Book {
  id: number;
  title: string;
  author: string;
  category: string;
  level: string;
  rating: number;
  price: number;
  isNew: boolean;
  isHot: boolean;
  coverUrl: string;
}

// 定义作者类型
export interface Author {
  id: number;
  name: string;
  specialty: string;
}

// 定义分类类型
export interface ThirdCategory {
  id: string;
  name: string;
}

export interface SecondaryCategory {
  id: string;
  name: string;
  thirdCategories: ThirdCategory[];
}

export interface PrimaryCategory {
  id: string;
  name: string;
  secondaryCategories: SecondaryCategory[];
}

export const useBooksStore = defineStore('books', {
  state: () => ({
    // 书籍数据
    books: [] as Book[],

    // 作者数据
    recommendedAuthors: [
      { id: 1, name: '张大厨', specialty: '川菜大师' },
      { id: 2, name: '李厨师', specialty: '法式料理' },
      { id: 3, name: '王师傅', specialty: '甜点专家' },
      { id: 4, name: '赵美食家', specialty: '日式料理' }
    ] as Author[],

    // 分类数据
    primaryCategories: [
      {
        id: 'cuisine',
        name: '菜系地域',
        secondaryCategories: [
          {
            id: 'chinese',
            name: '国内菜系',
            thirdCategories: [
              { id: 'chinese-sichuan', name: '川菜' },
              { id: 'chinese-guangdong', name: '粤菜' },
              { id: 'chinese-hunan', name: '湘菜' },
              { id: 'chinese-shandong', name: '鲁菜' },
              { id: 'chinese-huaiyang', name: '淮扬菜' },
              { id: 'chinese-other', name: '其他菜系' }
            ]
          },
          {
            id: 'international',
            name: '国际菜系',
            thirdCategories: [
              { id: 'int-french', name: '法式料理' },
              { id: 'int-italian', name: '意大利菜' },
              { id: 'int-japanese', name: '日式料理' },
              { id: 'int-korean', name: '韩式料理' },
              { id: 'int-thai', name: '泰式料理' },
              { id: 'int-other', name: '其他国际菜' }
            ]
          }
        ]
      },
      {
        id: 'scenario',
        name: '场景用途',
        secondaryCategories: [
          {
            id: 'daily',
            name: '日常烹饪',
            thirdCategories: [
              { id: 'daily-quick', name: '快手菜' },
              { id: 'daily-home', name: '家常菜' },
              { id: 'daily-breakfast', name: '早餐菜谱' },
              { id: 'daily-lunch', name: '午餐菜谱' },
              { id: 'daily-dinner', name: '晚餐菜谱' }
            ]
          },
          {
            id: 'special',
            name: '特殊场合',
            thirdCategories: [
              { id: 'special-family', name: '家庭聚餐' },
              { id: 'special-party', name: '节日宴客' },
              { id: 'special-picnic', name: '露营野餐' },
              { id: 'special-other', name: '其他场合' }
            ]
          },
          {
            id: 'diet',
            name: '饮食目标',
            thirdCategories: [
              { id: 'diet-weight', name: '减肥餐' },
              { id: 'diet-healthy', name: '健康饮食' },
              { id: 'diet-nutrition', name: '营养均衡' },
              { id: 'diet-beauty', name: '美容养颜' }
            ]
          }
        ]
      },
      {
        id: 'crowd',
        name: '人群需求',
        secondaryCategories: [
          {
            id: 'general',
            name: '通用人群',
            thirdCategories: [
              { id: 'general-family', name: '家庭食谱' },
              { id: 'general-vegetarian', name: '素食食谱' },
              { id: 'general-meat', name: '肉食食谱' },
              { id: 'general-dessert', name: '甜点食谱' }
            ]
          },
          {
            id: 'health',
            name: '特定健康人群',
            thirdCategories: [
              { id: 'health-lowfat', name: '低脂餐' },
              { id: 'health-highprotein', name: '高蛋白餐' },
              { id: 'health-diabetic', name: '糖尿病餐' },
              { id: 'health-cardiac', name: '心血管健康' }
            ]
          },
          {
            id: 'identity',
            name: '特定身份人群',
            thirdCategories: [
              { id: 'identity-pregnant', name: '孕妇餐' },
              { id: 'identity-child', name: '儿童辅食' },
              { id: 'identity-elderly', name: '老年食谱' },
              { id: 'identity-athlete', name: '运动健身餐' }
            ]
          }
        ]
      },
      {
        id: 'method',
        name: '烹饪方式',
        secondaryCategories: [
          {
            id: 'quick',
            name: '快手菜',
            thirdCategories: [
              { id: 'quick-10min', name: '10分钟快手菜' },
              { id: 'quick-30min', name: '30分钟快手菜' },
              { id: 'quick-onepot', name: '一锅菜' },
              { id: 'quick-stirfry', name: '小炒类' }
            ]
          },
          {
            id: 'traditional',
            name: '传统技法',
            thirdCategories: [
              { id: 'trad-stew', name: '炖煮类' },
              { id: 'trad-steam', name: '蒸煮类' },
              { id: 'trad-fry', name: '煎炸类' },
              { id: 'trad-braise', name: '焖烧类' }
            ]
          },
          {
            id: 'specialty',
            name: '特色料理',
            thirdCategories: [
              { id: 'specialty-grill', name: '烧烤' },
              { id: 'specialty-sushi', name: '寿司' },
              { id: 'specialty-pasta', name: '意大利面' },
              { id: 'specialty-bake', name: '烘焙' }
            ]
          }
        ]
      },
      {
        id: 'season',
        name: '季节时令',
        secondaryCategories: [
          {
            id: 'spring',
            name: '春季',
            thirdCategories: [
              { id: 'spring-vegetable', name: '春季时蔬' },
              { id: 'spring-health', name: '春季养生' },
              { id: 'spring-recipe', name: '春季菜谱' }
            ]
          },
          {
            id: 'summer',
            name: '夏季',
            thirdCategories: [
              { id: 'summer-cool', name: '清凉解暑' },
              { id: 'summer-salad', name: '夏季沙拉' },
              { id: 'summer-soup', name: '夏季汤品' }
            ]
          },
          {
            id: 'autumn',
            name: '秋季',
            thirdCategories: [
              { id: 'autumn-harvest', name: '秋季丰收' },
              { id: 'autumn-nourish', name: '秋季滋补' },
              { id: 'autumn-dessert', name: '秋季甜品' }
            ]
          },
          {
            id: 'winter',
            name: '冬季',
            thirdCategories: [
              { id: 'winter-warming', name: '温暖冬季' },
              { id: 'winter-hotpot', name: '火锅料理' },
              { id: 'winter-soup', name: '冬季汤羹' }
            ]
          }
        ]
      },
      {
        id: 'occasion',
        name: '节日庆典',
        secondaryCategories: [
          {
            id: 'traditional',
            name: '传统节日',
            thirdCategories: [
              { id: 'occasion-spring', name: '春节食谱' },
              { id: 'occasion-midautumn', name: '中秋月饼' },
              { id: 'occasion-dragonboat', name: '端午粽子' }
            ]
          },
          {
            id: 'western',
            name: '西方节日',
            thirdCategories: [
              { id: 'occasion-christmas', name: '圣诞料理' },
              { id: 'occasion-thanksgiving', name: '感恩节' },
              { id: 'occasion-halloween', name: '万圣节' }
            ]
          },
          {
            id: 'special',
            name: '特殊日子',
            thirdCategories: [
              { id: 'occasion-birthday', name: '生日派对' },
              { id: 'occasion-wedding', name: '婚礼甜点' },
              { id: 'occasion-anniversary', name: '周年纪念' }
            ]
          }
        ]
      },
      {
        id: 'skill',
        name: '烹饪技巧',
        secondaryCategories: [
          {
            id: 'basic',
            name: '基础技巧',
            thirdCategories: [
              { id: 'skill-cutting', name: '刀工技巧' },
              { id: 'skill-seasoning', name: '调味技巧' },
              { id: 'skill-fire', name: '火候掌握' }
            ]
          },
          {
            id: 'advanced',
            name: '进阶技巧',
            thirdCategories: [
              { id: 'skill-sousvide', name: '低温慢煮' },
              { id: 'skill-fermentation', name: '发酵技术' },
              { id: 'skill-plating', name: '菜品摆盘' }
            ]
          },
          {
            id: 'special',
            name: '特色技巧',
            thirdCategories: [
              { id: 'skill-cake', name: '蛋糕装饰' },
              { id: 'skill-noodle', name: '手工拉面' },
              { id: 'skill-dough', name: '面团发酵' }
            ]
          }
        ]
      },
      {
        id: 'nutrition',
        name: '营养健康',
        secondaryCategories: [
          {
            id: 'vitamin',
            name: '维生素补充',
            thirdCategories: [
              { id: 'nutrition-vita', name: '维生素食谱' },
              { id: 'nutrition-antioxidant', name: '抗氧化食品' },
              { id: 'nutrition-immunity', name: '增强免疫力' }
            ]
          },
          {
            id: 'balance',
            name: '营养均衡',
            thirdCategories: [
              { id: 'nutrition-protein', name: '高蛋白饮食' },
              { id: 'nutrition-fiber', name: '高纤维饮食' },
              { id: 'nutrition-carb', name: '碳水控制' }
            ]
          },
          {
            id: 'health',
            name: '健康调理',
            thirdCategories: [
              { id: 'nutrition-detox', name: '排毒养颜' },
              { id: 'nutrition-digest', name: '助消化' },
              { id: 'nutrition-heart', name: '心脏健康' }
            ]
          }
        ]
      },
      {
        id: 'equipment',
        name: '厨具适用',
        secondaryCategories: [
          {
            id: 'common',
            name: '常见厨具',
            thirdCategories: [
              { id: 'equipment-pot', name: '锅具菜谱' },
              { id: 'equipment-pan', name: '煎锅料理' },
              { id: 'equipment-oven', name: '烤箱食谱' }
            ]
          },
          {
            id: 'small',
            name: '小家电',
            thirdCategories: [
              { id: 'equipment-blender', name: '料理机' },
              { id: 'equipment-pressure', name: '压力锅' },
              { id: 'equipment-airfryer', name: '空气炸锅' }
            ]
          },
          {
            id: 'specialty',
            name: '专业厨具',
            thirdCategories: [
              { id: 'equipment-sousvide', name: '低温慢煮机' },
              { id: 'equipment-smoker', name: '烟熏设备' },
              { id: 'equipment-pasta', name: '意面机' }
            ]
          }
        ]
      }
    ] as PrimaryCategory[],

    // 加载状态
    loading: false,
    error: null as string | null
  }),

  getters: {
    // 热销书籍
    hotBooks: (state) => {
      return state.books
        .filter(book => book.isHot)
        .sort((a, b) => b.rating - a.rating)
        .slice(0, 3);
    },

    // 阅读热榜
    readingRank: (state) => {
      return [...state.books]
        .sort((a, b) => b.rating - a.rating)
        .slice(0, 5);
    },

    // 推荐书籍
    recommendedBooks: (state) => {
      return [...state.books]
        .sort(() => Math.random() - 0.5)
        .slice(0, 4);
    }
  },

  actions: {
    // 从后端获取书籍数据
    async fetchBooks() {
      this.loading = true;
      this.error = null;
      try {
        // 这里将来会替换为真实的API调用
        // const response = await api.get('/books');
        // this.books = response.data;

        // 暂时使用模拟数据
        this.books = this.generateMockBooks();
      } catch (error) {
        this.error = '获取书籍数据失败';
        console.error('Failed to fetch books:', error);
      } finally {
        this.loading = false;
      }
    },

    // 从后端获取作者数据
    async fetchAuthors() {
      try {
        // 这里将来会替换为真实的API调用
        // const response = await api.get('/authors/recommended');
        // this.recommendedAuthors = response.data;
      } catch (error) {
        console.error('Failed to fetch authors:', error);
      }
    },

    // 模拟生成书籍数据
    generateMockBooks(): Book[] {
      const books: Book[] = [];
      const titles = [
        '舌尖上的中国', '法式料理艺术', '烘焙大师指南', '健康素食烹饪',
        '海鲜料理大全', '家常菜谱精选', '意大利面制作大全', '川菜烹饪秘籍',
        '甜点制作基础', '日式料理入门', '粤菜经典', '烧烤技巧大全',
        '儿童营养食谱', '减肥餐单', '节日特辑', '快手早餐',
        '地中海饮食', '分子料理', '中式面点', '调酒艺术'
      ];

      const authors = [
        '张大厨', '李厨师', '王师傅', '赵美食家', '陈烹饪师',
        '刘大厨', '黄师傅', '周美食专家', '吴厨师', '郑烹饪大师'
      ];

      const categories = ['中餐', '西餐', '甜点', '烘焙', '素食', '海鲜', '养生', '饮品'];
      const difficultyLevels = ['入门', '进阶', '专业', '大师级'];

      for (let i = 1; i <= 30; i++) {
        books.push({
          id: i,
          title: titles[i % titles.length] as string,
          author: authors[Math.floor(Math.random() * authors.length)] as string,
          category: categories[Math.floor(Math.random() * categories.length)] as string,
          level: difficultyLevels[Math.floor(Math.random() * difficultyLevels.length)] as string,
          rating: Math.round((3 + Math.random() * 2) * 10) / 10, // 3-5星之间
          price: Math.round((20 + Math.random() * 200) * 100) / 100, // 20-220元之间
          isNew: Math.random() > 0.7,
          isHot: Math.random() > 0.8,
          coverUrl: `https://picsum.photos/seed/book${i}/300/400`
        });
      }

      return books;
    }
  }
});