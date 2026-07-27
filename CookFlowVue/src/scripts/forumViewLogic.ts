import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getShareList, getQuestionList, getActivityList } from '@/api/forum'
import type { ShareItem, QuestionItem, ActivityItem } from '@/types/forum'

export class ForumViewLogic {
  public forumType = ref('share')
  public isLoading = ref(false)
  public isLoadingMore = ref(false)

  // 数据
  public shareItems = ref<ShareItem[]>([])
  public questionItems = ref<QuestionItem[]>([])
  public activities = ref<ActivityItem[]>([])
  public hotActivities = ref<ActivityItem[]>([])

  // 分页
  private currentPage = 1
  private pageSize = 12
  private hasMore = true

  private router = useRouter()

  constructor() {
    this.loadData()
  }

  async loadData(reset = true) {
    if (reset) {
      this.currentPage = 1
      this.hasMore = true
    }
    if (!this.hasMore) return

    this.isLoadingMore.value = true
    try {
      let res
      switch (this.forumType.value) {
        case 'share':
          res = await getShareList({ page: this.currentPage, size: this.pageSize })
          const newShares = res.rows || []
          if (reset) this.shareItems.value = newShares
          else this.shareItems.value.push(...newShares)
          this.hasMore = newShares.length === this.pageSize
          break
        case 'question':
          res = await getQuestionList({ page: this.currentPage, size: this.pageSize })
          const newQuestions = res.rows || []
          if (reset) this.questionItems.value = newQuestions
          else this.questionItems.value.push(...newQuestions)
          this.hasMore = newQuestions.length === this.pageSize
          break
        case 'activity':
          res = await getActivityList({ page: this.currentPage, size: this.pageSize })
          const newActivities = res.rows || []
          if (reset) this.activities.value = newActivities
          else this.activities.value.push(...newActivities)
          this.hasMore = newActivities.length === this.pageSize
          break
      }
      if (reset) this.currentPage = 1
      this.currentPage++
    } catch (error) {
      console.error('加载数据失败', error)
    } finally {
      this.isLoadingMore.value = false
      this.isLoading.value = false
    }
  }

  public loadMore() {
    if (!this.isLoadingMore.value && this.hasMore) {
      this.loadData(false)
    }
  }

  public debouncedLoadMore(delay = 200) {
    let timeout: any
    return () => {
      clearTimeout(timeout)
      timeout = setTimeout(() => this.loadMore(), delay)
    }
  }

  public navigateToDetail(id: number) {
    if (this.forumType.value === 'share') {
      this.router.push(`/forum/share/${id}`)
    } else if (this.forumType.value === 'question') {
      this.router.push(`/forum/question/${id}`)
    } else {
      this.router.push(`/activity/${id}`)
    }
  }

  // 其他方法（toggleExpand, toggleFavorite 等）可根据需要实现，部分可能需要调用 API
  public toggleFavorite(itemId: number) {
    // 调用 API 收藏/取消收藏
  }

  public switchForumType(type: string) {
    this.forumType.value = type
    this.loadData(true)
  }
}

export function useForumViewLogic() {
  return new ForumViewLogic()
}