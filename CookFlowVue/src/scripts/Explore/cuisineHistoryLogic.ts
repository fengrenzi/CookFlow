import { ref, computed } from 'vue';
import { useExploreStore } from '@/store/modules/Explore';
import { parseYearToNumeric, getTimeRange, mockHistoricalEvents } from '@/store/modules/Explore/historicalEventsData';

export function useCuisineHistoryLogic() {
  // 获取store
  const exploreStore = useExploreStore();
  // 响应式数据
  const visibleRange = ref([0, 100]);
  const isLoading = ref(false);
  // 自定义时间轴相关状态
  const timelineContainer = ref<HTMLElement | null>(null);
  const sliderLeft = ref<HTMLElement | null>(null);
  const sliderRight = ref<HTMLElement | null>(null);
  const sliderRange = ref<HTMLElement | null>(null);
  // 拖动状态
  const isDraggingLeft = ref(false);
  const isDraggingRight = ref(false);
  const isDraggingRange = ref(false);
  const dragStartX = ref(0);
  const startLeftPercent = ref(0);
  const startRightPercent = ref(0);
  const startDragLeftX = ref(0);
  const startDragRightX = ref(0);
  // 计算属性：根据可见范围筛选显示的事件
  const displayedEvents = computed(() => {
    // 直接从store获取历史事件数据
    let historicalEvents = exploreStore.historicalEvents;
    // 如果store数据为空，返回空数组（不使用mock数据，确保用户看到实际情况）
    if (!historicalEvents || !historicalEvents.length) {
      return [];
    }
    // 将年份转换为数值用于排序和筛选
    const eventsWithNumericYear = historicalEvents.map(event => ({
      ...event,
      numericYear: parseYearToNumeric(event.year)
    }));

    // 按年份排序
    eventsWithNumericYear.sort((a, b) => a.numericYear - b.numericYear);
    // 获取时间范围
    const range = getTimeRange(historicalEvents);
    const totalRange = range.max - range.min;
    // 确保visibleRange有有效的默认值
    const currentVisibleRange = visibleRange.value || [0, 100];
    const startPercent = currentVisibleRange[0] || 0;
    const endPercent = currentVisibleRange[1] || 100;
    // 根据可见范围计算实际的年份范围
    const startYear = range.min + (totalRange * startPercent / 100);
    const endYear = range.min + (totalRange * endPercent / 100);
    // 筛选在可见范围内的事件
    const filteredEvents = eventsWithNumericYear.filter(event => {
      const isInRange = event.numericYear >= startYear && event.numericYear <= endYear;
      return isInRange;
    });
    const result = filteredEvents.sort((a, b) => b.numericYear - a.numericYear); // 倒序显示，最新的在上面
    return result;
  });

  // 初始化自定义时间轴
  async function initializeChart(container: HTMLElement): Promise<void> {
    isLoading.value = true;
    try {
      // 清空容器并创建自定义时间轴结构
      container.innerHTML = '';
      // 创建时间轴线
      const timelineWrapper = document.createElement('div');
      timelineWrapper.className = 'timeline-wrapper';
      // 创建主时间轴
      const timelineMain = document.createElement('div');
      timelineMain.className = 'timeline-main';
      // 创建时间轴刻度和标签
      createTimelineMarkers(timelineMain);
      // 创建时间点
      createTimelinePoints(timelineMain);
      // 创建缩放滑块
      const sliderWrapper = document.createElement('div');
      sliderWrapper.className = 'timeline-slider-wrapper';
      // 滑块轨道
      const sliderTrack = document.createElement('div');
      sliderTrack.className = 'timeline-slider-track';
      // 滑块范围
      sliderRange.value = document.createElement('div');
      sliderRange.value.className = 'timeline-slider-range';
      sliderRange.value.style.left = '0%';
      sliderRange.value.style.width = '100%';
      // 左滑块
      sliderLeft.value = document.createElement('div');
      sliderLeft.value.className = 'timeline-slider-handle timeline-slider-left';
      // 右滑块
      sliderRight.value = document.createElement('div');
      sliderRight.value.className = 'timeline-slider-handle timeline-slider-right';
      // 组装滑块
      sliderTrack.appendChild(sliderRange.value);
      sliderTrack.appendChild(sliderLeft.value);
      sliderTrack.appendChild(sliderRight.value);
      sliderWrapper.appendChild(sliderTrack);
      // 组装时间轴
      timelineWrapper.appendChild(timelineMain);
      timelineWrapper.appendChild(sliderWrapper);
      container.appendChild(timelineWrapper);
      // 保存容器引用
      timelineContainer.value = timelineMain;
      // 绑定事件
      bindSliderEvents();
      // 响应窗口大小变化
      window.addEventListener('resize', handleResize);
    } catch (error) {
      console.error('初始化时间轴失败:', error);
    } finally {
      isLoading.value = false;
    }
  }

  // 创建时间轴刻度和标签
  function createTimelineMarkers(container: HTMLElement): void {
    // 获取事件数据
    let historicalEvents = exploreStore.historicalEvents || [];
    // 如果没有数据，使用默认时间范围（确保时间轴始终显示）
    let range;
    if (!historicalEvents.length) {
      // 默认使用从1970年到2025年的时间范围
      range = { min: 1970, max: 2025 };
    } else {
      range = getTimeRange(historicalEvents);
    }
    const totalRange = range.max - range.min;
    // 创建主要刻度（每10年一个）
    const markerStep = Math.ceil(totalRange / 10); // 根据时间跨度动态调整
    for (let year = range.min; year <= range.max; year += markerStep) {
      const marker = document.createElement('div');
      marker.className = 'timeline-marker';
      // 计算位置百分比
      const positionPercent = ((year - range.min) / totalRange) * 100;
      marker.style.left = `${positionPercent}%`;
      // 创建标签
      const label = document.createElement('div');
      label.className = 'timeline-marker-label';
      // 格式化年份显示
      if (year < 0) {
        label.textContent = `公元前${Math.abs(year)}年`;
      } else if (year === 0) {
        label.textContent = '公元元年';
      } else {
        label.textContent = `${year}年`;
      }
      marker.appendChild(label);
      container.appendChild(marker);
    }

    // 创建中心线
    const centerLine = document.createElement('div');
    centerLine.className = 'timeline-center-line';
    container.appendChild(centerLine);
  }

  // 创建时间点
  function createTimelinePoints(container: HTMLElement): void {
    // 获取事件数据
    let historicalEvents = exploreStore.historicalEvents || [];
    // 如果没有事件数据，直接返回，不创建任何时间点
    if (!historicalEvents.length) {
      return;
    }
    // 将年份转换为数值
    const eventsWithNumericYear = historicalEvents.map(event => ({
      ...event,
      numericYear: parseYearToNumeric(event.year)
    })).sort((a, b) => a.numericYear - b.numericYear);
    // 获取时间范围
    const range = getTimeRange(historicalEvents);
    const totalRange = range.max - range.min;
    // 创建时间点
    eventsWithNumericYear.forEach((event, index) => {
      const point = document.createElement('div');
      point.className = 'timeline-point';
      point.dataset.eventId = event.id;
      point.dataset.eventIndex = index.toString();
      // 计算位置百分比
      const positionPercent = ((event.numericYear - range.min) / totalRange) * 100;
      point.style.left = `${positionPercent}%`;
      // 创建提示框内容
      point.title = `${event.year}\n${event.title}\n${event.description}`;
      // 添加点击事件
      point.addEventListener('click', () => handlePointClick(event));
      container.appendChild(point);
    });
  }

  // 处理时间点点击
  function handlePointClick(event: any): void {
    scrollToEventCard(event.id);
  }

  // 绑定滑块事件
  function bindSliderEvents(): void {
    if (!sliderLeft.value || !sliderRight.value || !sliderRange.value) return;
    // 左滑块拖动
    sliderLeft.value.addEventListener('mousedown', (e) => startDragging('left', e));
    // 右滑块拖动
    sliderRight.value.addEventListener('mousedown', (e) => startDragging('right', e));
    // 滑块范围拖动
    sliderRange.value.addEventListener('mousedown', (e) => startDragging('range', e));
    // 全局鼠标事件
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', stopDragging);
    // 滚轮缩放
    if (timelineContainer.value) {
      timelineContainer.value.addEventListener('wheel', handleWheelZoom);
    }
  }

  // 开始拖动
  function startDragging(type: 'left' | 'right' | 'range', event: MouseEvent): void {
    event.preventDefault();
    dragStartX.value = event.clientX;
    const currentRange = visibleRange.value;
    startLeftPercent.value = currentRange[0] || 0;
    startRightPercent.value = currentRange[1] || 100;
    if (type === 'left') {
      isDraggingLeft.value = true;
      startDragLeftX.value = event.clientX;
    } else if (type === 'right') {
      isDraggingRight.value = true;
      startDragRightX.value = event.clientX;
    } else if (type === 'range') {
      isDraggingRange.value = true;
    }
    // 添加拖动类
    if (sliderLeft.value) sliderLeft.value.classList.toggle('dragging', type === 'left');
    if (sliderRight.value) sliderRight.value.classList.toggle('dragging', type === 'right');
    if (sliderRange.value) sliderRange.value.classList.toggle('dragging', type === 'range');
  }

  // 处理鼠标移动
  function handleMouseMove(event: MouseEvent): void {
    if (!sliderLeft.value || !sliderRight.value || !sliderRange.value) return;
    const deltaX = event.clientX - dragStartX.value;
    const containerWidth = timelineContainer.value?.offsetWidth || 1;
    const percentPerPixel = 100 / containerWidth;
    let newLeft = startLeftPercent.value;
    let newRight = startRightPercent.value;
    if (isDraggingLeft.value) {
      newLeft = startLeftPercent.value + deltaX * percentPerPixel;
      newLeft = Math.max(0, Math.min(newLeft, newRight - 5)); // 最小5%宽度
    } else if (isDraggingRight.value) {
      newRight = startRightPercent.value + deltaX * percentPerPixel;
      newRight = Math.min(100, Math.max(newRight, newLeft + 5)); // 最小5%宽度
    } else if (isDraggingRange.value) {
      const rangeWidth = newRight - newLeft;
      newLeft = startLeftPercent.value + deltaX * percentPerPixel;
      newRight = newLeft + rangeWidth;
      // 边界检查
      if (newLeft < 0) {
        newRight -= newLeft;
        newLeft = 0;
      } else if (newRight > 100) {
        newLeft -= newRight - 100;
        newRight = 100;
      }
    }
    if (isDraggingLeft.value || isDraggingRight.value || isDraggingRange.value) {
      updateSliderPosition(newLeft, newRight);
    }
  }

  // 停止拖动
  function stopDragging(): void {
    isDraggingLeft.value = false;
    isDraggingRight.value = false;
    isDraggingRange.value = false;
    // 移除拖动类
    if (sliderLeft.value) sliderLeft.value.classList.remove('dragging');
    if (sliderRight.value) sliderRight.value.classList.remove('dragging');
    if (sliderRange.value) sliderRange.value.classList.remove('dragging');
  }
  // 处理滚轮缩放
  function handleWheelZoom(event: WheelEvent): void {
    event.preventDefault();
    if (!timelineContainer.value) return;
    const containerRect = timelineContainer.value.getBoundingClientRect();
    const mouseX = event.clientX - containerRect.left;
    const mousePercent = (mouseX / containerRect.width) * 100;
    const currentRange = visibleRange.value;
    const rangeWidth = (currentRange[1] || 100) - (currentRange[0] || 0);
    // 滚轮方向决定缩放方向
    const zoomFactor = event.deltaY > 0 ? 0.9 : 1.1; // 缩小或放大
    const newRangeWidth = Math.max(5, Math.min(95, rangeWidth * zoomFactor));
    const centerShift = (rangeWidth - newRangeWidth) / 2;
    // 计算新的左右边界，以鼠标位置为中心进行缩放
    const mouseRelativePos = (mousePercent - (currentRange[0] || 0)) / rangeWidth;
    const leftShift = centerShift * (2 * mouseRelativePos - 1);
    let newLeft = (currentRange[0] || 0) - leftShift;
    let newRight = newLeft + newRangeWidth;
    // 边界检查
    if (newLeft < 0) {
      newRight -= newLeft;
      newLeft = 0;
    } else if (newRight > 100) {
      newLeft -= newRight - 100;
      newRight = 100;
    }
    updateSliderPosition(newLeft, newRight);
  }

  // 更新滑块位置
  function updateSliderPosition(leftPercent: number, rightPercent: number): void {
    if (!sliderLeft.value || !sliderRight.value || !sliderRange.value) return;
    // 更新状态
    visibleRange.value = [leftPercent, rightPercent];
    // 更新UI
    sliderRange.value.style.left = `${leftPercent}%`;
    sliderRange.value.style.width = `${rightPercent - leftPercent}%`;
    sliderLeft.value.style.left = `${leftPercent}%`;
    sliderRight.value.style.left = `${rightPercent}%`;
    // 更新显示的事件
    updateDisplayedEvents();
    // 同步更新时间点可见性
    updateTimelinePointsVisibility();
  }

  // 更新时间点可见性
  function updateTimelinePointsVisibility(): void {
    if (!timelineContainer.value) return;
    const points = timelineContainer.value.querySelectorAll('.timeline-point');
    const currentRange = visibleRange.value;
    // 获取事件数据并排序
    let historicalEvents = exploreStore.historicalEvents || [];
    if (!historicalEvents.length) {
      historicalEvents = [...mockHistoricalEvents];
    }
    const eventsWithNumericYear = historicalEvents.map(event => ({
      ...event,
      numericYear: parseYearToNumeric(event.year)
    })).sort((a, b) => a.numericYear - b.numericYear);
    // 获取时间范围
    const range = getTimeRange(historicalEvents);
    const totalRange = range.max - range.min;
    points.forEach((point, index) => {
      if (eventsWithNumericYear[index]) {
        const event = eventsWithNumericYear[index];
        const positionPercent = ((event.numericYear - range.min) / totalRange) * 100;

        // 检查是否在可见范围内
        const isVisible = positionPercent >= (currentRange[0] || 0) - 2 && positionPercent <= (currentRange[1] || 100) + 2;
        (point as HTMLElement).style.display = isVisible ? 'block' : 'none';
      }
    });
  }

  // 滚动到指定ID的事件卡片
  function scrollToEventCard(eventId: string): void {
    // 使用setTimeout确保DOM已经更新
    setTimeout(() => {
      const eventCard = document.querySelector(`[data-event-id="${eventId}"]`);
      if (eventCard) {
        // 获取events-container元素
        const eventsContainer = eventCard.closest('.events-container');
        if (eventsContainer) {
          // 计算滚动位置，使卡片居中
          const containerRect = eventsContainer.getBoundingClientRect();
          const cardRect = eventCard.getBoundingClientRect();
          // 计算需要滚动的距离
          const scrollLeft = eventsContainer.scrollLeft +
            cardRect.left - containerRect.left -
            (containerRect.width - cardRect.width) / 2;
          // 平滑滚动
          eventsContainer.scrollTo({
            left: scrollLeft,
            behavior: 'smooth'
          });
          // 添加高亮效果
          eventCard.classList.add('highlight');
          setTimeout(() => {
            eventCard.classList.remove('highlight');
          }, 2000);
        }
      }
    }, 100);
  }

  // 提示框函数已移除，使用原生title属性

  // 更新显示的事件
  function updateDisplayedEvents(): void {
    console.log('cuisineHistoryLogic: updateDisplayedEvents called');
    console.log('cuisineHistoryLogic: Displayed events updated:', displayedEvents.value.length);
    // 更新时间点可见性
    updateTimelinePointsVisibility();
  }
  // 响应窗口大小变化
  function handleResize(): void {
    // 重新创建时间轴标记和点，以适应新的窗口大小
    if (timelineContainer.value) {
      // 清空并重新创建
      const parent = timelineContainer.value.parentElement;
      if (parent && parent.parentElement) {
        const grandParent = parent.parentElement;
        const container = grandParent.parentElement;
        if (container) {
          // 重新初始化时间轴
          initializeChart(container);
        }
      }
    }
  }

  // 销毁时间轴组件
  function destroyChart(): void {
    // 移除事件监听器
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('mouseup', stopDragging);
    window.removeEventListener('resize', handleResize);
    // 清空引用
    timelineContainer.value = null;
    sliderLeft.value = null;
    sliderRight.value = null;
    sliderRange.value = null;
  }

  return {
    displayedEvents,
    isLoading,
    visibleRange,
    initializeChart,
    updateDisplayedEvents,
    destroyChart
  };
}

// 日期格式化工具函数
export function formatDateToYear(dateStr: string): string {
  try {
    const date = new Date(dateStr);
    return date.getFullYear().toString() + '年';
  } catch (error) {
    return dateStr;
  }
}