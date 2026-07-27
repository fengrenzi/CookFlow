// src/api/region.ts
import request from '@/utils/request'

export interface FoodCard {
  recipeId: string
  title: string
  imageUrl: string
  difficulty: number
  prepTime: number
  description: string
  isSpecialty: boolean
}

export interface RegionData {
  code: string
  name: string
  level: number
  lng: number
  lat: number
  foodCount: number
  foods?: FoodCard[]
}

// 临时模拟数据
export const getProvinces = () => {
  return Promise.resolve([
    { code: '110000', name: '北京市', level: 1, lng: 116.4074, lat: 39.9042, foodCount: 10 },
    { code: '310000', name: '上海市', level: 1, lng: 121.4802, lat: 31.2363, foodCount: 8 },
    { code: '440000', name: '广东省', level: 1, lng: 113.2644, lat: 23.1291, foodCount: 15 },
    { code: '350000', name: '福建省', level: 1, lng: 119.2951, lat: 26.1008, foodCount: 6 },
    { code: '510000', name: '四川省', level: 1, lng: 104.0657, lat: 30.6595, foodCount: 12 },
  ] as RegionData[])
}

export const getCities = (provinceCode: string) => {
  const mockCities: Record<string, RegionData[]> = {
    '110000': [{ code: '110100', name: '北京市', level: 2, lng: 116.4074, lat: 39.9042, foodCount: 10 }],
    '310000': [{ code: '310100', name: '上海市', level: 2, lng: 121.4802, lat: 31.2363, foodCount: 8 }],
    '440000': [
      { code: '440100', name: '广州市', level: 2, lng: 113.2644, lat: 23.1291, foodCount: 8 },
      { code: '440300', name: '深圳市', level: 2, lng: 114.0579, lat: 22.5431, foodCount: 7 },
    ],
  }
  return Promise.resolve(mockCities[provinceCode] || [])
}

export const drillDown = (regionCode: string) => {
  const mockData: Record<string, RegionData> = {
    '110000': {
      code: '110000',
      name: '北京市',
      level: 1,
      lng: 116.4074,
      lat: 39.9042,
      foodCount: 2,
      foods: [
        {
          recipeId: '1',
          title: '北京烤鸭',
          imageUrl: 'https://picsum.photos/200/150?random=1',
          difficulty: 2,
          prepTime: 60,
          description: '北京特色美食，外酥里嫩',
          isSpecialty: true,
        },
        {
          recipeId: '2',
          title: '炸酱面',
          imageUrl: 'https://picsum.photos/200/150?random=2',
          difficulty: 1,
          prepTime: 30,
          description: '老北京传统面食',
          isSpecialty: true,
        },
      ],
    },
    '310000': {
      code: '310000',
      name: '上海市',
      level: 1,
      lng: 121.4802,
      lat: 31.2363,
      foodCount: 1,
      foods: [
        {
          recipeId: '3',
          title: '小笼包',
          imageUrl: 'https://picsum.photos/200/150?random=3',
          difficulty: 2,
          prepTime: 45,
          description: '皮薄馅多，汤汁鲜美',
          isSpecialty: true,
        },
      ],
    },
    '440100': {
      code: '440100',
      name: '广州市',
      level: 2,
      lng: 113.2644,
      lat: 23.1291,
      foodCount: 1,
      foods: [
        {
          recipeId: '4',
          title: '肠粉',
          imageUrl: 'https://picsum.photos/200/150?random=4',
          difficulty: 1,
          prepTime: 15,
          description: '广州特色早餐',
          isSpecialty: true,
        },
      ],
    },
  }
  return Promise.resolve(mockData[regionCode] || { code: regionCode, name: '未知地区', level: 2, lng: 0, lat: 0, foodCount: 0, foods: [] })
}