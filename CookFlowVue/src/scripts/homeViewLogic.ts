import { useRouter } from 'vue-router'
import { useHomeStore } from '@/store/modules/home'

export function useHomeViewLogic() {
  const router = useRouter()
  const homeStore = useHomeStore()

  const handleCategoryClick = (path: string) => {
    if (path.startsWith('/')) {
      router.push(path)
    } else if (path.startsWith('http')) {
      window.open(path, '_blank')
    } else {
      window.location.href = path
    }
  }

  const initializeData = async () => {
    await homeStore.fetchAllHomeData()
  }

  return {
    handleCategoryClick,
    initializeData
  }
}