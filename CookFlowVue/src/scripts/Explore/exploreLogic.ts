import { ref, computed } from 'vue'
import { useExploreStore } from '@/store/modules/Explore'
import { getRankingLists, getHistoricalEvents } from '@/api/explore'

export function useExploreView() {
  const exploreStore = useExploreStore()
  const activeModule = ref('cuisine-history')
  const isLoading = ref(false)
  const error = ref(null)
  const modalVisible = ref(false)
  const currentRankingList = ref('')
  const fullRankingData = ref([])

  const rankingLists = computed(() => exploreStore.rankingLists)
  const rankingListsLimited = computed(() => {
    const result = {}
    for (const [key, list] of Object.entries(rankingLists.value)) {
      result[key] = list ? list.slice(0, 10) : []
    }
    return result
  })

  const modules = [
    { id: 'hot-recipes', name: '热度菜品' },
    { id: 'cuisine-history', name: '菜系历史' },
    // ...
  ]

  const loadRankings = async () => {
    isLoading.value = true
    try {
      const data = await getRankingLists()
      exploreStore.rankingLists = data
    } catch (err) {
      error.value = '加载榜单失败'
    } finally {
      isLoading.value = false
    }
  }

  const loadHistoricalEvents = async () => {
    try {
      const events = await getHistoricalEvents()
      exploreStore.historicalEvents = events
    } catch (err) {
      console.error('加载历史事件失败', err)
    }
  }

  const loadAllData = async () => {
    await Promise.all([loadRankings(), loadHistoricalEvents()])
  }

  const openMoreModal = (listName: string) => {
    currentRankingList.value = listName
    fullRankingData.value = rankingLists.value[listName] || []
    modalVisible.value = true
  }

  const closeMoreModal = () => {
    modalVisible.value = false
  }

  const handleRankingItemClick = (router: any, item: any) => {
    if (item.path) router.push(item.path)
  }

  return {
    activeModule,
    isLoading,
    error,
    modules,
    rankingLists,
    rankingListsLimited,
    modalVisible,
    currentRankingList,
    fullRankingData,
    switchModule: (id: string) => { activeModule.value = id },
    loadAllData,
    openMoreModal,
    closeMoreModal,
    handleRankingItemClick
  }
}