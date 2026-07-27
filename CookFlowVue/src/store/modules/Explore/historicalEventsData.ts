// 历史事件数据定义和模拟数据

// 历史事件接口定义
export interface HistoricalEvent {
  id: string;
  year: string; // 可以是负数（公元前）或字符串形式
  title: string;
  description: string;
  category?: string; // 可选的分类标签
  imageUrl?: string; // 可选的图片URL
  relatedRecipeIds?: string[]; // 相关食谱ID
}

// 生成近50年的随机历史事件数据
export const mockHistoricalEvents: HistoricalEvent[] = [
  // 生成1974-2024年间的随机事件
  {
    id: 'recent-1',
    year: '1974',
    title: '微波炉开始普及家庭厨房',
    description: '微波炉技术成熟并开始进入普通家庭，彻底改变了人们的烹饪方式和饮食习惯。',
    category: '技术',
    imageUrl: '/images/microwave-popular.jpg'
  },
  {
    id: 'recent-2',
    year: '1977',
    title: '速食文化兴起',
    description: '随着生活节奏加快，各种方便食品和快餐连锁店开始在中国大中城市兴起。',
    category: '文化',
    imageUrl: '/images/fast-food.jpg'
  },
  {
    id: 'recent-3',
    year: '1980',
    title: '家用冰箱普及',
    description: '家用冰箱在中国城市家庭中的普及率大幅提高，延长了食品保存时间，改变了采购习惯。',
    category: '生活',
    imageUrl: '/images/refrigerator.jpg'
  },
  {
    id: 'recent-4',
    year: '1983',
    title: '第一家肯德基在中国开业',
    description: '美国快餐巨头肯德基在北京开设第一家门店，标志着西方快餐正式进入中国市场。',
    category: '商业',
    imageUrl: '/images/kfc-first.jpg'
  },
  {
    id: 'recent-5',
    year: '1986',
    title: '中国烹饪协会成立',
    description: '中国烹饪协会正式成立，致力于推动烹饪技艺传承和餐饮业发展。',
    category: '组织',
    imageUrl: '/images/culinary-association.jpg'
  },
  {
    id: 'recent-6',
    year: '1989',
    title: '粤菜北上热潮',
    description: '粤菜餐厅在北京、上海等北方城市大量开设，推动了中国菜系间的交流融合。',
    category: '菜系',
    imageUrl: '/images/guangdong-cuisine.jpg'
  },
  {
    id: 'recent-7',
    year: '1992',
    title: '第一届中国烹饪大赛举办',
    description: '首届全国烹饪技能大赛举办，展示了中国烹饪技艺的最高水平。',
    category: '赛事',
    imageUrl: '/images/culinary-competition.jpg'
  },
  {
    id: 'recent-8',
    year: '1995',
    title: '绿色食品标准出台',
    description: '中国正式颁布绿色食品标准，有机健康食品概念开始受到关注。',
    category: '标准',
    imageUrl: '/images/green-food.jpg'
  },
  {
    id: 'recent-9',
    year: '1998',
    title: '互联网与餐饮结合',
    description: '早期餐饮网站出现，开始探索互联网与传统餐饮业的结合模式。',
    category: '创新',
    imageUrl: '/images/internet-food.jpg'
  },
  {
    id: 'recent-10',
    year: '2001',
    title: '中国加入WTO后的餐饮业变化',
    description: '中国加入WTO后，国际餐饮品牌加速进入中国市场，带来了新的经营理念和烹饪技术。',
    category: '国际',
    imageUrl: '/images/wto-food.jpg'
  },
  {
    id: 'recent-11',
    year: '2004',
    title: '分子料理概念引入',
    description: '分子料理这一融合科学与烹饪的创新概念开始进入中国高端餐厅。',
    category: '创新',
    imageUrl: '/images/molecular-cuisine.jpg'
  },
  {
    id: 'recent-12',
    year: '2007',
    title: '农家乐旅游兴起',
    description: '以农村特色美食为核心的农家乐旅游形式在全国范围内兴起。',
    category: '旅游',
    imageUrl: '/images/rural-food.jpg'
  },
  {
    id: 'recent-13',
    year: '2010',
    title: '上海世博会美食文化展示',
    description: '上海世博会期间，各国美食文化集中展示，促进了国际饮食文化交流。',
    category: '交流',
    imageUrl: '/images/expo-food.jpg'
  },
  {
    id: 'recent-14',
    year: '2012',
    title: '舌尖上的中国首播',
    description: '纪录片《舌尖上的中国》首播，引发全国范围内的美食文化热潮。',
    category: '媒体',
    imageUrl: '/images/a-bite-of-china.jpg'
  },
  {
    id: 'recent-15',
    year: '2015',
    title: '外卖平台爆发式增长',
    description: '移动互联网外卖平台用户量爆发式增长，彻底改变了人们的用餐习惯。',
    category: '科技',
    imageUrl: '/images/food-delivery.jpg'
  },
  {
    id: 'recent-16',
    year: '2017',
    title: '国潮美食兴起',
    description: '传统文化元素与现代美食结合的国潮美食开始受到年轻消费者追捧。',
    category: '潮流',
    imageUrl: '/images/chinese-wave-food.jpg'
  },
  {
    id: 'recent-17',
    year: '2019',
    title: '人造肉技术突破',
    description: '植物蛋白肉技术取得重大突破，开始在餐饮市场试点推广。',
    category: '科技',
    imageUrl: '/images/plant-based-meat.jpg'
  },
  {
    id: 'recent-18',
    year: '2020',
    title: '疫情下的餐饮数字化转型',
    description: '新冠疫情推动餐饮业加速数字化转型，无接触配送和预制菜成为新趋势。',
    category: '变革',
    imageUrl: '/images/digital-food.jpg'
  },
  {
    id: 'recent-19',
    year: '2022',
    title: '预制菜产业蓬勃发展',
    description: '预制菜产业迎来爆发式增长，成为餐饮业和家庭厨房的重要选择。',
    category: '产业',
    imageUrl: '/images/pre-made-food.jpg'
  },
  {
    id: 'recent-20',
    year: '2024',
    title: 'AI烹饪助手进入家庭',
    description: '人工智能烹饪助手开始进入普通家庭，为用户提供个性化的烹饪指导和食谱推荐。',
    category: '智能',
    imageUrl: '/images/ai-cooking.jpg'
  }
];

// 生成模拟历史事件的函数（用于store中）
export const generateMockHistoricalEvents = (): HistoricalEvent[] => {
  return [...mockHistoricalEvents]; // 返回模拟数据的副本
};

// 根据年代范围过滤历史事件的辅助函数
export const filterEventsByTimeRange = (
  events: HistoricalEvent[],
  startYear: number,
  endYear: number
): HistoricalEvent[] => {
  return events.filter(event => {
    // 将year字符串转换为数字进行比较
    const eventYear = parseInt(event.year);
    return eventYear >= startYear && eventYear <= endYear;
  });
};

// 将年代字符串转换为可排序的数值
export const parseYearToNumeric = (year: string): number => {
  const numericYear = parseInt(year);
  // 处理负数（公元前）和可能的特殊格式
  return isNaN(numericYear) ? 0 : numericYear;
};

// 获取时间轴的最小和最大年份
export const getTimeRange = (events: HistoricalEvent[]): { min: number; max: number } => {
  if (events.length === 0) {
    return { min: 0, max: 2023 };
  }
  
  const years = events.map(event => parseYearToNumeric(event.year));
  return {
    min: Math.min(...years),
    max: Math.max(...years)
  };
};