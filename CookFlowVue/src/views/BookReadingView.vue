<template>
    <div class="book-reading-container" :class="{ 'dark-mode': isDarkMode }">
        <div class="content-wrapper">
            <!-- 书籍标题栏 -->
            <div class="book-title-bar">
                <h1 class="book-title">{{ bookStore.bookData.title || '书籍阅读' }}</h1>
                <button class="add-to-bookshelf-btn" :class="{ 'added': isInBookshelf }" @click="handleAddToBookshelf">
                    {{ isInBookshelf ? '已加入书架' : '加入书架' }}
                </button>
            </div>

            <!-- 阅读区域 -->
            <div class="reading-area">
                <div class="page-container">
                    <div class="container">
                        <div class="right" v-for="book in bookPages" :key="book.page" ref="rightRefs">
                            <figure class="front" @click="prevCard">
                                <div class="page-content" :style="textStyle">{{ book.content.front }}</div>
                                <div :class="getPageNumberClass((book.page - 1) * 2 + 1)"
                                    @click.stop="handlePageNumberClick($event, (book.page - 1) * 2 + 1)">{{ (book.page -
                                        1) * 2 + 1 }}</div>
                                <!-- 书签标识（背面） -->
                                <div v-if="isBookmarked && (bookmarkPageNum === (book.page - 1) * 2 + 1 || bookmarkPageNum === (book.page - 1) * 2 + 2)"
                                    class="bookmark-indicator">
                                    {{ bookmarkPageNum }}
                                </div>

                                <!-- 弹幕控制组件 - 只在全局弹幕启用时显示 -->
                                <div v-if="isDanmuEnabled" class="danmu-control"
                                    @click.stop="togglePageDanmu((book.page - 1) * 2 + 1)">
                                    <i
                                        :class="isPageDanmuEnabled((book.page - 1) * 2 + 1) ? 'fas fa-comment-dots' : 'far fa-comment-dots'"></i>
                                    <!-- 弹幕数量角标 -->
                                    <span v-if="getPageDanmuCount((book.page - 1) * 2 + 1) > 0"
                                        class="danmu-count-badge">
                                        {{ getPageDanmuCount((book.page - 1) * 2 + 1) }}
                                    </span>
                                </div>
                                <!-- 为front页面添加弹幕容器 -->
                                <div class="page-danmu-container" :id="`page-${book.page}-front`"
                                    style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none;">
                                </div>
                            </figure>
                            <figure class="back" @click="(e) => nextCard(e)">
                                <div class="page-content" :style="textStyle">{{ book.content.back }}</div>
                                <div :class="getPageNumberClass((book.page - 1) * 2 + 2)"
                                    @click.stop="handlePageNumberClick($event, (book.page - 1) * 2 + 2)">{{ (book.page -
                                        1) * 2 + 2 }}</div>
                                <!-- 书签标识（正面，带页面覆盖效果） -->
                                <div v-if="isBookmarked && (bookmarkPageNum === (book.page - 1) * 2 + 1 || bookmarkPageNum === (book.page - 1) * 2 + 2)"
                                    class="bookmark-indicator bookmark-back">
                                    {{ bookmarkPageNum }}
                                </div>

                                <!-- 弹幕控制组件 - 只在全局弹幕启用时显示 -->
                                <div v-if="isDanmuEnabled" class="danmu-control"
                                    @click.stop="togglePageDanmu((book.page - 1) * 2 + 2)">
                                    <i
                                        :class="isPageDanmuEnabled((book.page - 1) * 2 + 2) ? 'fas fa-comment-dots' : 'far fa-comment-dots'"></i>
                                    <!-- 弹幕数量角标 -->
                                    <span v-if="getPageDanmuCount((book.page - 1) * 2 + 2) > 0"
                                        class="danmu-count-badge">
                                        {{ getPageDanmuCount((book.page - 1) * 2 + 2) }}
                                    </span>
                                </div>
                                <!-- 为back页面添加弹幕容器 -->
                                <div class="page-danmu-container" :id="`page-${book.page}-back`"
                                    style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none;">
                                </div>
                            </figure>
                        </div>
                    </div>

                    <!-- 浮动控制面板-->
                    <div class="floating-controls">
                        <button class="control-btn back-btn" @click="goBack" title="返回上一页">
                            <i class="fas fa-arrow-up"></i>
                        </button>
                        <button class="control-btn first-page-btn" @click="gotoPage(1)" title="回到第一页">
                            <i class="fas fa-arrow-left"></i>
                        </button>
                        <button class="control-btn toc-btn" @click="toggleTocPanel" title="目录">
                            <i class="fas fa-list"></i>
                        </button>
                        <button class="control-btn bookmark-btn" @click="toggleBookmark" title="书签（点击两下取消）">
                            <i :class="isBookmarked ? 'fas fa-bookmark' : 'far fa-bookmark'"></i>
                        </button>
                        <button class="control-btn font-btn" @click="toggleFontPanel" title="字体">
                            <i class="fas fa-font"></i>
                        </button>

                        <button class="control-btn cart-btn" @click="toggleCartModal" title="购物车">
                            <i class="fas fa-shopping-cart"></i>
                        </button>
                        <button class="control-btn theme-btn" @click="toggleTheme" title="切换主题">
                            <i :class="isDarkMode ? 'fas fa-sun' : 'fas fa-moon'"></i>
                        </button>
                        <button class="control-btn danmu-btn" @click="toggleGlobalDanmu" title="弹幕开关">
                            <i :class="isDanmuEnabled ? 'fas fa-comment-dots' : 'far fa-comment-dots'"></i>
                        </button>
                        <!-- 弹幕发送按钮 -->
                        <div class="danmu-send-container" v-if="isDanmuEnabled">
                            <button class="danmu-send-button" @click="toggleDanmuSendPanel" title="发送弹幕">
                                发
                            </button>
                            <!-- 弹幕发送弹出层 -->
                            <div v-if="showDanmuSendPanel" class="danmu-send-popup" @click.stop>
                                <div class="danmu-page-info">
                                    <label for="danmu-page-input" style="margin-right: 8px; color: #666;">页码：</label>
                                    <input id="danmu-page-input" type="number" v-model.number="inputDanmuPage" min="1"
                                        :max="bookStore.bookData.totalPages" />
                                </div>
                                <textarea v-model="danmuContent" placeholder="发送弹幕...(5-50字)" maxlength="50"
                                    @keyup.enter="sendDanmu" @input="danmuContent = danmuContent.substring(0, 50)"
                                    class="danmu-popup-input" rows="3"></textarea>
                                <button @click="sendDanmu"
                                    :disabled="!danmuContent.trim() || danmuContent.length < 5 || danmuContent.length > 50"
                                    class="danmu-popup-send-btn">
                                    发送
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 书架操作弹出层 -->
        <div class="bookshelf-modal" v-if="showBookshelfModal" @click.self="showBookshelfModal = false">
            <div class="bookshelf-modal-content">
                <div class="bookshelf-modal-icon"
                    :class="{ 'success': bookshelfModalSuccess, 'error': !bookshelfModalSuccess }">
                    <i v-if="bookshelfModalSuccess" class="fas fa-check-circle"></i>
                    <i v-else class="fas fa-times-circle"></i>
                </div>
                <div class="bookshelf-modal-message">{{ bookshelfModalMessage }}</div>
                <button class="bookshelf-modal-btn" @click="showBookshelfModal = false">确定</button>
            </div>
        </div>

        <!-- 目录面板 -->
        <div class="toc-panel" :class="{ 'active': showTocPanel }">
            <div class="toc-search">
                <input type="text" placeholder="搜索" v-model="tocSearchQuery" class="toc-search-input" />
            </div>
            <div class="toc-book-info">
                <img :src="bookStore.bookData.coverImage || '/defult.jpg'" :alt="bookStore.bookData.title"
                    class="toc-book-cover" />
                <div class="toc-book-details">
                    <h4 class="toc-book-title">{{ bookStore.bookData.title || '明朝那些事儿（全集）' }}</h4>
                    <p class="toc-book-author">{{ bookStore.bookData.author || '当年明月' }}</p>
                </div>
            </div>
            <!-- 排序功能 -->
            <div class="toc-sort">
                <button class="sort-btn sort-toggle" :class="{ 'active': sortOrder === 'desc' }"
                    @click="toggleSortOrder" title="切换排序方式">
                    <el-icon v-if="sortOrder === 'asc'">
                        <ArrowUp />
                    </el-icon>
                    <el-icon v-else>
                        <ArrowDown />
                    </el-icon>
                </button>
            </div>
            <div class="toc-content">
                <div v-for="item in sortedBookContent" :key="item.id" class="toc-item"
                    @click="gotoContentPage(item.id)">
                    <div class="toc-item-content">
                        <span class="toc-item-title">{{ item.content }}</span>
                        <span class="toc-item-page">第{{ item.id }}页</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 购物车弹出层 -->
        <div class="cart-modal" v-if="showCartModal" @click.self="closeCartModal">
            <div class="cart-modal-content">
                <div class="cart-modal-header">
                    <h3>添加到购物车</h3>
                    <button class="cart-modal-close" @click="closeCartModal">&times;</button>
                </div>
                <div class="cart-modal-body">
                    <div class="cart-option">
                        <button class="cart-option-btn" :disabled="isBookInCart" @click="addWholeBookToCart">
                            加入整本书
                            <span v-if="isBookInCart" class="cart-option-status">（已加入）</span>
                        </button>
                    </div>
                    <div class="cart-option">
                        <label>页码(点击页码可取消)：</label>
                        <input type="number" v-model.number="targetPageNum" :min="1"
                            :max="bookStore.bookData.totalPages || 100" placeholder="请输入页码" class="page-input" />
                        <button class="cart-option-btn page-confirm-btn"
                            :disabled="!targetPageNum || isPageInCart(targetPageNum)" @click="addSpecificPageToCart">
                            确认加入
                            <span v-if="targetPageNum && isPageInCart(targetPageNum)"
                                class="cart-option-status">（已加入）</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 取消收藏确认弹出层 -->
        <div class="remove-confirm-modal" v-if="showRemoveConfirmModal" @click.self="closeRemoveConfirmModal">
            <div class="remove-confirm-content">
                <div class="remove-confirm-header">
                    <h3>取消收藏确认</h3>
                </div>
                <div class="remove-confirm-body">
                    <p>确定要取消收藏第{{ pageToRemove }}页吗？</p>
                </div>
                <div class="remove-confirm-footer">
                    <button class="remove-confirm-cancel" @click="closeRemoveConfirmModal">取消</button>
                    <button class="remove-confirm-ok" @click="confirmRemovePage">确定</button>
                </div>
            </div>
        </div>

        <!-- 字体设置面板 -->
        <div class="font-panel" :class="{ 'active': showFontPanel }">
            <div class="font-size-control">
                <label>字号大小</label>
                <div class="font-size-slider-container">
                    <span class="font-size-label">A</span>
                    <input type="range" min="12" max="24" v-model.number="fontSize" @input="updateFontSize"
                        class="font-size-slider" />
                    <span class="font-size-label large">A</span>
                </div>
            </div>
            <div class="font-family-control">
                <label>字体</label>
                <div class="font-category-tabs">
                    <button v-for="category in fontCategories" :key="category.id" class="font-category-tab"
                        :class="{ 'active': currentFontCategory === category.id }"
                        @click="currentFontCategory = category.id">
                        {{ category.label }}
                    </button>
                </div>
                <div class="font-family-options">
                    <div v-for="font in filteredFonts" :key="font.value" class="font-option"
                        :class="{ 'active': fontFamily === font.value }" @click="selectFont(font.value)"
                        :style="{ fontFamily: font.fontFamily }">
                        {{ font.name }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch, nextTick } from 'vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import type { Ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useBookDetailStore } from '@/store/modules/bookDetail';
import { useBookReadingStore } from '@/store/modules/bookReading';
import { ElMessage } from 'element-plus';

interface BookContentItem {
    id: number;
    content: string;
}
interface BookPage {
    page: number;
    content: {
        front: string;
        back: string;
    }
}
interface DanmuData {
    id: string;
    content: string;
    pageNum: number;
    likedBy: Set<string>;
    likes: number;
    isLiked: boolean;
}

// 路由和状态管理
const router = useRouter(); // 路由实例
const bookStore = useBookDetailStore(); // 书籍详情状态管理
const bookReadingStore = useBookReadingStore(); // 阅读设置状态管理

// 书籍内容相关
const bookContent = ref<BookContentItem[]>([]); // 书籍内容数组
const bookPages = ref<BookPage[]>([]); // 分页后的书籍页面
const hasMoreContent = ref(true); // 是否还有更多内容可以加载
const flippedOrder = new Map<number, number>(); // 记录已翻页的顺序

// 显示状态相关
const isDarkMode = ref(bookReadingStore.getThemePreference() || false); // 是否为暗色主题
const isBookmarked = ref(false); // 是否已添加书签
const bookmarkPageNum = ref(0); // 书签页码
const isDanmuEnabled = ref(false); // 是否启用弹幕
const isInBookshelf = ref(false); // 书籍是否在书架中
const showTocPanel = ref(false); // 是否显示目录面板
const showFontPanel = ref(false); // 是否显示字体设置面板
const showDanmuSendPanel = ref(false); // 是否显示弹幕发送面板
const inputDanmuPage = ref(1); // 输入的弹幕页码
const pageDanmuEnabled = ref<Map<number, boolean>>(new Map()); // 各页面弹幕启用状态
const playingDanmuPages = ref<Map<number, boolean>>(new Map()); // 正在播放弹幕的页面

// 搜索和排序
const tocSearchQuery = ref(''); // 目录搜索关键词
const sortOrder = ref('asc'); // 排序方式（升序/降序）

// 书签操作相关
const bookmarkClickCount = ref(0); // 书签点击次数
const lastBookmarkClickTime = ref(0); // 上次点击书签的时间
const BOOKMARK_DOUBLE_CLICK_THRESHOLD = 300; // 双击时间阈值（毫秒）
const chapterBookmarks = ref<Set<string>>(new Set()); // 章节书签集合

// 弹幕相关
const showDanmuPanel = ref(false); // 是否显示弹幕面板
const danmuContent = ref(''); // 弹幕内容
const activeDanmuPage = ref(1); // 当前活跃弹幕页面
const currentUserId = 'user_' + Date.now().toString().slice(-6); // 当前用户ID
const danmuAnimations = new Map<string, HTMLElement>(); // 弹幕动画元素映射
const danmuList = ref<Array<{
    id: string;
    content: string;
    pageNum: number;
    likes: number;
    createdAt: string;
    likedBy: Set<string>;
    isLiked: boolean;
}>>([]); // 弹幕列表

// 模态框相关
const showBookshelfModal = ref(false); // 是否显示书架操作模态框
const bookshelfModalMessage = ref(''); // 书架操作提示信息
const bookshelfModalSuccess = ref(false); // 书架操作是否成功
const showCartModal = ref(false); // 是否显示购物车模态框
const targetPageNum = ref<number | null>(null); // 目标页码
const isBookInCart = ref(false); // 书籍是否在购物车中
const cartPages = ref<Set<number>>(new Set()); // 购物车中的页面集合
const showRemoveConfirmModal = ref(false); // 是否显示取消收藏确认模态框
const pageToRemove = ref<number>(0); // 要移除的页码


// 字体相关
const fontSize = ref(bookReadingStore.getFontSizePreference()); // 字体大小
const fontFamily = ref(bookReadingStore.getFontPreference()); // 当前字体
const fontData = ref([ // 字体数据配置 - 按分类组织的可用字体列表
    {
        id: 1,
        category: '仓耳',
        values: [
            { name: '仓耳今楷04', fontFamily: '"CangerJinkai04", serif' },
            { name: '仓耳华新', fontFamily: '"CangerHuaxin", sans-serif' },
            { name: '仓耳今楷05', fontFamily: '"CangerJinkai05", serif' },
            { name: '仓耳玄三04', fontFamily: '"CangerXuansan04", sans-serif' },
            { name: '仓耳玄三05', fontFamily: '"CangerXuansan05", sans-serif' },
            { name: '仓耳云黑04', fontFamily: '"CangerYunhei04", sans-serif' },
            { name: '仓耳云黑05', fontFamily: '"CangerYunhei05", sans-serif' },
            { name: '仓耳明楷04', fontFamily: '"CangerMingkai04", serif' },
            { name: '仓耳明楷05', fontFamily: '"CangerMingkai05", serif' },
            { name: '仓耳状元楷', fontFamily: '"CangerZhuangyuankai", serif' },
            { name: '仓耳玉楷', fontFamily: '"CangerYukai", serif' }
        ]
    },
    {
        id: 2,
        category: '方正',
        values: [
            { name: '方正宋三', fontFamily: '"FangSong", "仿宋", serif' },
            { name: '方正悠宋', fontFamily: '"SimSun", "宋体", serif' },
            { name: '方正兰亭黑', fontFamily: '"Microsoft YaHei", "微软雅黑", sans-serif' },
            { name: '方正兰亭圆', fontFamily: '"WenQuanYi Micro Hei", sans-serif' },
            { name: '方正盛世楷书', fontFamily: '"STKaiti", "楷体", serif' },
            { name: '方正聚珍新仿', fontFamily: '"FangSong", "仿宋", serif' },
            { name: '方正宋一', fontFamily: '"STSong", "华文中宋", serif' },
            { name: '方正黑体', fontFamily: '"SimHei", "黑体", sans-serif' }
        ]
    },
    {
        id: 3,
        category: '其他',
        values: [
            { name: '思源宋体', fontFamily: '"Noto Serif SC", serif' },
            { name: '思源黑体', fontFamily: '"Noto Sans SC", sans-serif' },
            { name: '寒蝉活宋体', fontFamily: '"HanChanHuoSong", serif' },
            { name: '汉仪空山楷', fontFamily: '"HYKongShanKai", serif' },
            { name: '汇文正楷', fontFamily: '"HuiWenZhengKai", serif' },
            { name: '寒蝉正楷体', fontFamily: '"HanChanZhengKai", serif' },
            { name: 'AI楷', fontFamily: '"AIKaiTi", serif' },
            { name: '京华老宋体', fontFamily: '"JingHuaLaoSong", serif' },
            { name: '霞鹜文楷', fontFamily: '"XiaWuWenKai", serif' }
        ]
    }
]);
const currentFontCategory = ref(0); // 0表示"全部"类别
// 计算属性 - 获取字体类别列表，包含全部类别和各个分类
const fontCategories = computed(() => {
    const totalCount = fontData.value.reduce((count, category) => count + category.values.length, 0);
    const categories = [
        { id: 0, label: `全部(${totalCount})` },
        ...fontData.value.map(category => ({
            id: category.id,
            label: `${category.category}(${category.values.length})`
        }))
    ];

    return categories;
});
// 计算属性 - 获取当前文本样式对象，包含字体大小和字体系列
const textStyle = computed(() => {
    const allFonts: Array<{ name: string, fontFamily: string }> = [];
    fontData.value.forEach(category => {
        allFonts.push(...category.values);
    });
    return {
        fontSize: `${fontSize.value}px`,
        fontFamily: fontFamily.value === 'system'
            ? 'system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
            : allFonts.find(f => f.name === fontFamily.value)?.fontFamily || 'system-ui, sans-serif'
    };
});
const filteredFonts = computed(() => {
    if (currentFontCategory.value === 0) {
        const allFonts: Array<{ name: string, fontFamily: string, category?: string }> = [];
        fontData.value.forEach(category => {
            allFonts.push(...category.values.map(font => ({
                ...font,
                category: category.category // 保留原始类别信息
            })));
        });
        return allFonts.map(font => ({
            name: font.name,
            value: font.name.toLowerCase().replace(/\s+/g, '-'),
            fontFamily: font.fontFamily,
            category: '其他' // 为全部类别下的字体设置默认category
        }));
    }
    const category = fontData.value.find(cat => cat.id === currentFontCategory.value);
    if (category) {
        return category.values.map(font => ({
            name: font.name,
            value: font.name.toLowerCase().replace(/\s+/g, '-'),
            fontFamily: font.fontFamily,
            category: category.category
        }));
    }
    return [];
});


const sortedBookContent = computed(() => {
    let content = [...bookContent.value];
    if (tocSearchQuery.value.trim()) {
        const query = tocSearchQuery.value.toLowerCase();
        content = content.filter(item =>
            item.content.toLowerCase().includes(query)
        );
    }
    if (sortOrder.value === 'desc') {
        return content.sort((a, b) => b.id - a.id);
    } else {
        return content.sort((a, b) => a.id - b.id);
    }
});
const danmuStyles: Record<string, string> = {
    position: 'absolute',
    color: '#fff',
    textShadow: '1px 1px 2px rgba(0, 0, 0, 0.8)',
    zIndex: '10',
    pointerEvents: 'auto',
    padding: '4px 8px',
    background: 'rgba(0, 0, 0, 0.6)',
    borderRadius: '8px',
    transition: 'opacity 0.3s ease, transform 0.3s ease, height 0.2s ease',
    userSelect: 'none',
    opacity: '0',
    transform: 'scale(0.8)',
    minWidth: '100px',
    maxWidth: '180px',
    wordWrap: 'break-word',
    whiteSpace: 'normal',
    display: 'flex',
    flexDirection: 'column',
    lineHeight: '1.4'
};
const buttonStyles: Record<string, string> = {
    background: 'none',
    border: 'none',
    color: '#fff',
    cursor: 'pointer',
    fontSize: '12px',
    display: 'flex',
    alignItems: 'center',
    gap: '2px'
};
const actionsStyles: Record<string, string> = {
    display: 'none',
    gap: '8px',
    marginTop: '4px',
    width: '100%',
    justifyContent: 'flex-start'
};
var isAnimating = false;
var rightRefs = ref([]) as Ref<HTMLElement[]>;
var rightLen = 0;
var start = 0;
var flipCounter = 0;

// 获取书籍内容，支持分页加载
// @param startId: 起始内容ID，默认为1
// @param limit: 每页加载的内容数量，默认为10
const fetchBookContent = async (startId = 1, limit = 10) => {
    try {
        await new Promise(resolve => setTimeout(resolve, 200));
        const newContent = [];
        const maxPossibleId = bookStore.bookData.totalPages;
        for (let i = 0; i < limit; i++) {
            const currentId = startId + i;
            if (currentId > maxPossibleId) break;
            newContent.push({
                id: currentId,
                content: currentId === 1 ? '首页' : `第${currentId - 1}页内容`
            });
        }
        if (startId === 1) {
            bookContent.value = newContent;
        } else {
            bookContent.value.push(...newContent);
        }
        updateBookPages();
        return newContent;
    } catch (error) {
        return [];
    }
};

const checkAndLoadMore = () => {
    const totalPages = bookStore.bookData.totalPages;
    const currentVisibleId = start;
    const totalLoadedPages = bookPages.value.length;
    if (currentVisibleId >= totalLoadedPages - 2 && hasMoreContent.value && totalLoadedPages < totalPages) {
        loadMoreContent();
    }
};

const loadMoreContent = async () => {
    const totalPages = bookStore.bookData.totalPages;
    if (bookPages.value.length >= totalPages) {
        hasMoreContent.value = false;
        return;
    }
    const lastId = bookContent.value.length > 0
        ? Math.max(...bookContent.value.map(item => item.id))
        : 0;
    const maxPossibleId = totalPages;
    if (lastId >= maxPossibleId) {
        hasMoreContent.value = false;
        return;
    }
    const remainingPages = totalPages - bookPages.value.length;
    const maxItemsToLoad = remainingPages * 2;
    const limit = Math.min(10, maxItemsToLoad);
    const currentPagesCount = bookPages.value.length;
    await fetchBookContent(lastId + 1, limit);
    setTimeout(() => {
        const newPagesCount = bookPages.value.length;
        const addedPages = newPagesCount - currentPagesCount;
        if (addedPages > 0) {
            rightLen += addedPages;
        } else {
            rightLen = bookPages.value.length;
        }
        updateZIndexesAfterLoad();
        if (bookPages.value.length >= totalPages) {
            hasMoreContent.value = false;
        }
    }, 0);
};

// 更新书籍分页数据，将内容数组转换为页面数组（每页包含正反两面）
const updateBookPages = () => {
    const totalPages = bookStore.bookData.totalPages;
    const currentPagesCount = bookPages.value.length;
    for (let i = currentPagesCount * 2; i < bookContent.value.length && bookPages.value.length < totalPages; i += 2) {
        const pageNum = Math.floor(i / 2) + 1;
        if (pageNum > totalPages) break;
        bookPages.value.push({
            page: pageNum,
            content: {
                front: bookContent.value[i]?.content || '',
                back: bookContent.value[i + 1]?.content || ''
            }
        });
    }
};

// 更新页面加载后的层级关系，确保翻页效果正确显示
const updateZIndexesAfterLoad = () => {
    rightRefs.value.forEach((element, index) => {
        if (!element) return;
        if (element.classList.contains('left')) {
            const flipOrder = flippedOrder.get(index) || 0;
            element.style.zIndex = (flipOrder * 5).toString();
        } else if (element.classList.contains('right')) {
            if (index === start) {
                element.style.zIndex = "100";
            } else if (index > start) {
                const distance = index - start;
                element.style.zIndex = Math.max(1, 100 - distance).toString();
            } else {
                element.style.zIndex = "1";
            }
        }
    });
};

const recalculateFlippedOrder = () => {
    const entries = Array.from(flippedOrder.entries());
    entries.sort(([indexA], [indexB]) => indexA - indexB);
    flippedOrder.clear();
    entries.forEach(([index], newOrder) => {
        flippedOrder.set(index, newOrder + 1);
    });
    flipCounter = entries.length;
};

// 翻到上一页
const prevCard = () => {
    if (rightLen < 1 || start >= bookPages.value.length || isAnimating) return;
    playingDanmuPages.value.forEach((_, pageNum) => {
        clearPageDanmu(pageNum);
        playingDanmuPages.value.delete(pageNum);
        pageDanmuEnabled.value.set(pageNum, false);
    });
    isAnimating = true;
    const currentPage = rightRefs.value[start];
    const currentIndex = start;
    if (currentPage) {
        currentPage.className = 'right';
        void currentPage.offsetWidth;
        currentPage.classList.add('left');
        rightLen--;
        flipCounter++;
        flippedOrder.set(currentIndex, flipCounter);
        setTimeout(() => {
            start++;
            checkAndLoadMore();
            updateZIndexesAfterLoad();
            const currentPageNum = start + 1;
            showPageDanmu(currentPageNum);
            isAnimating = false; // 动画完成后才允许再次点击
        }, 500); // 动画完成后才重置isAnimating状态
    } else {
        isAnimating = false;
    }
};

// 翻到下一页
// @param e: 鼠标事件对象
const nextCard = (e: MouseEvent) => {
    if (isAnimating) return;
    let isPageNumberClick = false;
    if (e && e.target) {
        let target: HTMLElement | null = e.target as HTMLElement;
        while (target && !target.classList.contains('page-number')) {
            target = target.parentElement;
        }
        isPageNumberClick = !!target && target.classList.contains('page-number');
    }
    if (isPageNumberClick) {
        return;
    }
    if (start <= 0) return;
    playingDanmuPages.value.forEach((_, pageNum) => {
        clearPageDanmu(pageNum);
        playingDanmuPages.value.delete(pageNum);
        pageDanmuEnabled.value.set(pageNum, false);
    });
    isAnimating = true;
    const targetIndex = start - 1;
    const currentPage = rightRefs.value[targetIndex];
    if (currentPage) {
        currentPage.className = 'left';
        void currentPage.offsetWidth;
        currentPage.className = "right";
        start = targetIndex;
        rightLen++;
        flippedOrder.delete(targetIndex);
        recalculateFlippedOrder();
        updateZIndexesAfterLoad();
        const currentPageNum = start + 1;
        showPageDanmu(currentPageNum);
    }
    setTimeout(() => {
        const currentPageNum = start + 1;
        showPageDanmu(currentPageNum);
        isAnimating = false; // 动画完成后才允许再次点击
    }, 500); // 动画完成后才重置isAnimating状态
};

const toggleSortOrder = () => {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
};
const isPageDanmuEnabled = (pageNum: number): boolean => {
    return pageDanmuEnabled.value.get(pageNum) ?? false;
};
const getPageDanmuCount = (pageNum: number): number => {
    return danmuList.value.filter(danmu => danmu.pageNum === pageNum).length;
};
const togglePageDanmu = (pageNum: number) => {
    const isEnabled = pageDanmuEnabled.value.get(pageNum) ?? false;
    if (isEnabled) {
        clearPageDanmu(pageNum);
        pageDanmuEnabled.value.set(pageNum, false);
        playingDanmuPages.value.delete(pageNum);
        return;
    }
    const pageDanmuCount = danmuList.value.filter(danmu => danmu.pageNum === pageNum).length;
    if (pageDanmuCount === 0) {
        ElMessage.info('该页面暂无弹幕');
        return;
    }
    pageDanmuEnabled.value.set(pageNum, true);
    showPageDanmu(pageNum);
};
const clearPageDanmu = (pageNum: number) => {
    const cardIndex = Math.ceil(pageNum / 2);
    const pageSide = pageNum % 2 === 1 ? 'front' : 'back';
    const danmuContainer = document.querySelector(`#page-${cardIndex}-${pageSide}`);
    if (danmuContainer) {
        const danmuElements = danmuContainer.querySelectorAll('.danmu');
        danmuElements.forEach(element => {
            const htmlElement = element as HTMLElement;
            const danmuId = htmlElement.dataset.danmuId;
            if (danmuId) {
                danmuAnimations.delete(danmuId);
            }
            htmlElement.style.opacity = '0';
            htmlElement.style.transform = 'scale(0.8)';
            setTimeout(() => {
                if (htmlElement.parentNode) {
                    htmlElement.parentNode.removeChild(htmlElement);
                }
            }, 300);
        });
    }
};

// 根据内容ID跳转到对应的阅读页面
// @param pageId: 内容ID，从1开始计数，每个实际页面包含2个内容ID
const gotoContentPage = (pageId: number) => {
    const pageNumber = Math.ceil(pageId / 2);
    if (pageNumber > 0 && pageNumber <= bookPages.value.length) {
        playingDanmuPages.value.forEach((_, pageNum) => {
            clearPageDanmu(pageNum);
            playingDanmuPages.value.delete(pageNum);
            pageDanmuEnabled.value.set(pageNum, false);
        });
        gotoPage(pageNumber);
        if (pageId % 2 === 0) {
            setTimeout(() => {
                const tempStart = start;
                const tempRightLen = rightLen;
                const tempIsAnimating = isAnimating;
                isAnimating = false;
                prevCard();
                setTimeout(() => {
                    start = tempStart;
                    rightLen = tempRightLen;
                    isAnimating = tempIsAnimating;
                }, 10);
            }, 50); // 减少延迟时间
        }
        setTimeout(() => {
            showPageDanmu(pageNumber);
        }, 200); // 减少延迟时间
    }
    showTocPanel.value = false;
};

// 返回上一页
const goBack = () => {
    router.back();
};

// 切换主题
// 切换深色/浅色主题模式
const toggleTheme = () => {
    isDarkMode.value = !isDarkMode.value;
    bookReadingStore.saveThemePreference(isDarkMode.value);
    // 切换主题后更新页面层级
    setTimeout(() => {
        updateZIndexesAfterLoad();
    }, 0);
};

// 切换书签 - 适应数据逐步加载的特性
const toggleBookmark = async () => {
    const currentPageNum = start * 2 + 1;
    const currentPageNumBack = start * 2 + 2;
    const totalPages = bookStore.bookData.totalPages;
    if (currentPageNum === 1) {
        ElMessage.success({ message: '首页不支持添加书签', duration: 2000 });
        return;
    }
    if (totalPages % 2 === 1 && currentPageNum === totalPages) {
        ElMessage.success({ message: '最后一页不支持添加书签', duration: 2000 });
        return;
    }
    if (currentPageNum > totalPages || currentPageNumBack > totalPages) {
        ElMessage.success({ message: '无效页面不支持添加书签', duration: 2000 });
        return;
    }
    const bookId = bookStore.bookData.id;
    const currentTime = Date.now();
    if (!bookId) {
        ElMessage.error('无法获取书籍信息，无法添加书签');
        return;
    }
    if (currentTime - lastBookmarkClickTime.value < BOOKMARK_DOUBLE_CLICK_THRESHOLD) {
        removeBookmark(bookId);
        bookmarkClickCount.value = 0;
        lastBookmarkClickTime.value = 0;
        return;
    }
    bookmarkClickCount.value++;
    lastBookmarkClickTime.value = currentTime;
    setTimeout(() => {
        bookmarkClickCount.value = 0;
    }, 1000);
    try {
        await ensurePageLoaded(currentPageNum);
        const allBookmarks = JSON.parse(localStorage.getItem('bookmarks') || '{}');
        const existingBookmark = allBookmarks[bookId];
        const bookmarkData = {
            bookId,
            pageNum: currentPageNum,
            timestamp: new Date().toISOString(),
            bookTitle: bookStore.bookData.title
        };
        if (existingBookmark && existingBookmark.pageNum === currentPageNum) {
            ElMessage.success({ message: '当前页面已添加书签', duration: 1500 });
            return;
        } else if (existingBookmark && existingBookmark.pageNum !== currentPageNum) {
            allBookmarks[bookId] = bookmarkData;
            localStorage.setItem('bookmarks', JSON.stringify(allBookmarks));
            isBookmarked.value = true;
            bookmarkPageNum.value = currentPageNum;
            ElMessage.success({ message: `书签已更新到第${currentPageNum}页`, duration: 2000 });
        } else {
            allBookmarks[bookId] = bookmarkData;
            localStorage.setItem('bookmarks', JSON.stringify(allBookmarks));
            isBookmarked.value = true;
            bookmarkPageNum.value = currentPageNum;
            ElMessage.success({ message: `第${currentPageNum}页已添加书签`, duration: 2000 });
        }
    } catch (error) {
        ElMessage.error('书签操作失败，请重试');
    }
};

// 移除书籍的书签
// @param bookId: 书籍ID
const removeBookmark = (bookId: string) => {
    try {
        const allBookmarks = JSON.parse(localStorage.getItem('bookmarks') || '{}');
        delete allBookmarks[bookId];
        localStorage.setItem('bookmarks', JSON.stringify(allBookmarks));
        isBookmarked.value = false;
        bookmarkPageNum.value = 0;
        ElMessage.info('书签已移除');
        ElMessage.success({ message: '书签已移除', duration: 1500 });
    } catch (error) {
        ElMessage.error('移除书签失败，请重试');
    }
};

// 检查并恢复书签位置
const checkAndRestoreBookmark = async () => {
    const bookId = bookStore.bookData.id;
    if (!bookId) return;
    try {
        const allBookmarks = JSON.parse(localStorage.getItem('bookmarks') || '{}');
        const bookmark = allBookmarks[bookId];
        if (bookmark) {
            isBookmarked.value = true;
            bookmarkPageNum.value = bookmark.pageNum;
            await ensurePageLoaded(bookmark.pageNum);
            const targetStart = Math.floor((bookmark.pageNum - 1) / 2);
            start = targetStart;
            rightLen = bookPages.value.length;
            rightRefs.value.forEach((element, index) => {
                if (element) {
                    element.className = '';
                    element.classList.add('right');
                    if (index < targetStart) {
                        element.classList.add('left');
                        flipCounter++;
                        flippedOrder.set(index, flipCounter);
                    }
                }
            });
            nextTick(() => {
                updateZIndexesAfterLoad();
                isAnimating = false;
            });
            ElMessage.success({ message: `已为您跳转到上次阅读的位置 - 第${bookmark.pageNum}页`, duration: 2000 });
            setTimeout(() => {
                if (isDanmuEnabled.value) {
                    showPageDanmu(bookmark.pageNum);
                }
            }, 300);
        }
    } catch (error) {
        ElMessage.error('恢复书签失败，请重试');
    }
};

// 确保目标页码的内容已加载
// @param targetPageNum: 目标页码
// @returns Promise<void>
const ensurePageLoaded = async (targetPageNum: number): Promise<void> => {
    const requiredPageIndex = Math.floor((targetPageNum - 1) / 2);
    while (bookPages.value.length <= requiredPageIndex && hasMoreContent.value) {
        await loadMoreContent();
        await new Promise(resolve => setTimeout(resolve, 50));
    }
};

// 处理将书籍添加到书架或从书架移除的操作
const handleAddToBookshelf = () => {
    const bookId = bookStore.bookData.id;
    if (bookId) {
        if (isInBookshelf.value) {
            const success = bookReadingStore.removeFromBookshelf(bookId);
            if (success) {
                isInBookshelf.value = false;
                ElMessage.success('成功从书架移除！');
            } else {
                ElMessage.error('移除失败，请重试。');
            }
        } else {
            const success = bookReadingStore.addToBookshelf(bookId);
            if (success) {
                isInBookshelf.value = true;
                ElMessage.success('成功加入书架！');
            } else {
                ElMessage.error('加入书架失败，请重试。');
            }
        }
    } else {
        ElMessage.error('无法获取书籍信息，请刷新页面重试。');
    }
};

// 切换全局弹幕显示状态
const toggleGlobalDanmu = () => {
    isDanmuEnabled.value = !isDanmuEnabled.value;
    if (isDanmuEnabled.value) {
        // 加载弹幕数据
        loadDanmuData();

        // 直接调用带防抖动功能的showPageDanmu
        showPageDanmu(activeDanmuPage.value);
    } else {
        clearAllDanmu();
    }
};

// 清除所有正在播放的弹幕
const clearAllDanmu = () => {
    document.querySelectorAll('.page-danmu-container .danmu').forEach(el => el.remove());
    danmuAnimations.clear();
    // 清除页面弹幕状态
    pageDanmuEnabled.value.clear();
};

// 切换弹幕发送面板的显示状态
const toggleDanmuSendPanel = () => {
    showDanmuSendPanel.value = !showDanmuSendPanel.value;
    if (showDanmuSendPanel.value) {
        inputDanmuPage.value = activeDanmuPage.value;
        setTimeout(() => {
            const handleOutsideClick = (event: MouseEvent) => {
                const panel = document.querySelector('.danmu-send-popup');
                const button = document.querySelector('.danmu-send-button');
                if (panel && !panel.contains(event.target as Node) &&
                    button && !button.contains(event.target as Node)) {
                    showDanmuSendPanel.value = false;
                    document.removeEventListener('click', handleOutsideClick);
                }
            };
            document.addEventListener('click', handleOutsideClick);
            onUnmounted(() => {
                document.removeEventListener('click', handleOutsideClick);
            });
        }, 0);
    }
};

// 加载当前页面的弹幕数据
const loadDanmuData = () => {
    const bookId = bookStore.bookData.id || 'default';
    try {
        const savedDanmu = localStorage.getItem(`danmuList_${bookId}`);
        if (savedDanmu) {
            danmuList.value = JSON.parse(savedDanmu).map((d: any) => ({
                ...d,
                likedBy: new Set(d.likedBy || []),
                isLiked: new Set(d.likedBy || []).has(currentUserId)
            }));
        }
    } catch (error) {
        ElMessage.error('加载弹幕失败，请重试。');
    }
};

// 保存弹幕数据到本地存储
const saveDanmuData = () => {
    const bookId = bookStore.bookData.id || 'default';
    try {
        const serializedDanmu = danmuList.value.map(d => ({
            ...d,
            likedBy: Array.from(d.likedBy)
        }));
        localStorage.setItem(`danmuList_${bookId}`, JSON.stringify(serializedDanmu));
    } catch (error) {
        ElMessage.error('保存弹幕失败，请重试。');
    }
};

// 发送弹幕
const sendDanmu = () => {
    if (!danmuContent.value.trim()) {
        ElMessage.warning('请输入弹幕内容');
        return;
    }
    if (!inputDanmuPage.value || inputDanmuPage.value < 1 || (bookStore.bookData.totalPages && inputDanmuPage.value > bookStore.bookData.totalPages)) {
        ElMessage.warning('请输入有效的页码');
        return;
    }
    const newDanmu = {
        id: `danmu_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
        content: danmuContent.value.trim(),
        pageNum: inputDanmuPage.value, // 使用用户输入的页码
        likes: 0,
        createdAt: new Date().toISOString(),
        likedBy: new Set<string>(),
        isLiked: false
    };
    danmuList.value.push(newDanmu);
    saveDanmuData();
    danmuContent.value = '';
    showDanmuSendPanel.value = false;
    ElMessage.success('弹幕发送成功');
};

// 生成不重叠的弹幕位置
const generateDanmuPosition = (containerRect: DOMRect, existingDanmus: Array<{ top: number; left: number; right: number; bottom: number; width: number; height: number }>): { top: number; left: number } => {
    const estimatedDanmuHeight = 60;
    const estimatedDanmuWidth = 180;
    const maxHeight = containerRect.height * 0.5;
    const topPadding = 20;
    const leftMin = containerRect.width * 0.05;
    const rightMax = containerRect.width * 0.75;
    const spacing = 10;
    let randomTop = 0;
    let randomLeft = 0;
    let maxAttempts = 10;
    let isOverlapping = false;
    do {
        isOverlapping = false;
        randomTop = Math.floor(Math.random() * (maxHeight - estimatedDanmuHeight - topPadding)) + topPadding;
        randomLeft = Math.floor(Math.random() * (rightMax - estimatedDanmuWidth - leftMin)) + leftMin;
        for (const existingDanmu of existingDanmus) {
            const newDanmuRight = randomLeft + estimatedDanmuWidth;
            const newDanmuBottom = randomTop + estimatedDanmuHeight;
            if (!(randomLeft > existingDanmu.right + spacing ||
                newDanmuRight + spacing < existingDanmu.left ||
                randomTop > existingDanmu.bottom + spacing ||
                newDanmuBottom + spacing < existingDanmu.top)) {
                isOverlapping = true;
                break;
            }
        }
        maxAttempts--;
    } while (isOverlapping && maxAttempts > 0);
    return { top: randomTop, left: randomLeft };
};

// 应用样式到元素
const applyStyles = (element: HTMLElement, styles: Record<string, string>): void => {
    Object.entries(styles).forEach(([key, value]) => {
        (element.style as any)[key] = value;
    });
};

// 创建弹幕元素函数重构版
const createDanmuElement = (danmu: DanmuData): void => {
    if (!isPageDanmuEnabled(danmu.pageNum)) return;
    danmu.isLiked = danmu.likedBy.has(currentUserId);
    const cardIndex = Math.ceil(danmu.pageNum / 2);
    const pageSide = danmu.pageNum % 2 === 1 ? 'front' : 'back';
    const danmuContainer = document.querySelector(`#page-${cardIndex}-${pageSide}`);
    if (!danmuContainer) return;
    const containerRect = danmuContainer.getBoundingClientRect();
    const existingDanmus = Array.from(danmuContainer.querySelectorAll('.danmu')).map(el => {
        const rect = el.getBoundingClientRect();
        return {
            top: rect.top - containerRect.top,
            left: rect.left - containerRect.left,
            right: rect.right - containerRect.left,
            bottom: rect.bottom - containerRect.top,
            width: rect.width,
            height: rect.height
        };
    });
    const { top, left } = generateDanmuPosition(containerRect, existingDanmus);
    const danmuElement = document.createElement('div');
    danmuElement.className = 'danmu';
    danmuElement.dataset.danmuId = danmu.id;
    applyStyles(danmuElement, danmuStyles);
    danmuElement.style.top = `${top}px`;
    danmuElement.style.left = `${left}px`;
    const contentSpan = document.createElement('span');
    contentSpan.textContent = danmu.content;
    danmuElement.appendChild(contentSpan);
    const actionsContainer = document.createElement('div');
    actionsContainer.className = 'danmu-actions';
    applyStyles(actionsContainer, actionsStyles);
    const likeButton = createLikeButton(danmu);
    const copyButton = createCopyButton(danmu);
    actionsContainer.appendChild(likeButton);
    actionsContainer.appendChild(copyButton);
    danmuElement.appendChild(actionsContainer);
    danmuContainer.appendChild(danmuElement);
    setupDanmuEvents(danmuElement, danmu, actionsContainer);
    setupDanmuLifecycle(danmuElement, danmu.id);
    danmuAnimations.set(danmu.id, danmuElement);
};

// 创建点赞按钮
const createLikeButton = (danmu: DanmuData): HTMLButtonElement => {
    const likeButton = document.createElement('button');
    likeButton.className = 'danmu-like-btn';
    if (danmu.isLiked) {
        likeButton.innerHTML = `<i class="fas fa-thumbs-up" style="color: #ff6b6b;"></i> <span>${danmu.likes}</span>`;
    } else {
        likeButton.innerHTML = `<i class="far fa-thumbs-up"></i> <span>${danmu.likes}</span>`;
    }
    applyStyles(likeButton, buttonStyles);
    likeButton.addEventListener('click', (e) => {
        e.stopPropagation();
        handleDanmuLike(danmu, likeButton);
    });
    return likeButton;
};

// 创建复制按钮
const createCopyButton = (danmu: DanmuData): HTMLButtonElement => {
    const copyButton = document.createElement('button');
    copyButton.className = 'danmu-copy-btn';
    copyButton.innerHTML = '<i class="far fa-copy"></i>';

    applyStyles(copyButton, buttonStyles);

    copyButton.addEventListener('click', (e) => {
        e.stopPropagation();
        navigator.clipboard.writeText(danmu.content).then(() => {
            copyButton.innerHTML = '<i class="fas fa-check"></i>';
            setTimeout(() => {
                copyButton.innerHTML = '<i class="far fa-copy"></i>';
            }, 1000);
        });
    });

    return copyButton;
};

// 设置弹幕事件
const setupDanmuEvents = (danmuElement: HTMLElement, _danmu: DanmuData, actionsContainer: HTMLElement): void => {
    danmuElement.addEventListener('click', (e) => {
        e.stopPropagation();
        if (e.target && (e.target as HTMLElement).closest && !(e.target as HTMLElement).closest('button')) {
            actionsContainer.style.display = actionsContainer.style.display === 'none' ? 'flex' : 'none';
        }
    });
};

// 设置弹幕生命周期
const setupDanmuLifecycle = (danmuElement: HTMLElement, danmuId: string): void => {
    let disappearTimer: number | null = null;
    setTimeout(() => {
        danmuElement.style.opacity = '1';
        danmuElement.style.transform = 'scale(1)';
        const showDuration = 5000 + Math.random() * 3000;
        disappearTimer = window.setTimeout(() => {
            fadeOutAndRemove();
        }, showDuration);
    }, 10);
    const fadeOutAndRemove = () => {
        danmuElement.style.opacity = '0';
        danmuElement.style.transform = 'scale(0.8)';
        setTimeout(() => {
            if (danmuElement.parentNode) {
                danmuElement.parentNode.removeChild(danmuElement);
                danmuAnimations.delete(danmuId);
            }
        }, 300);
    };
    danmuElement.addEventListener('mouseenter', () => {
        if (disappearTimer) {
            clearTimeout(disappearTimer);
            disappearTimer = null;
        }
        danmuElement.style.background = 'rgba(0, 0, 0, 0.8)';
        danmuElement.style.transform = 'scale(1.05)';
    });
    danmuElement.addEventListener('mouseleave', () => {
        danmuElement.style.background = 'rgba(0, 0, 0, 0.6)';
        danmuElement.style.transform = 'scale(1)';

        disappearTimer = window.setTimeout(() => {
            fadeOutAndRemove();
        }, 2000);
    });
};

// 处理弹幕点赞操作
// @param danmu: 弹幕数据对象
// @param likeButton: 点赞按钮元素
const handleDanmuLike = (danmu: any, likeButton: HTMLButtonElement) => {
    if (danmu.likedBy.has(currentUserId)) {
        danmu.likedBy.delete(currentUserId);
        danmu.likes--;
        danmu.isLiked = false;
        likeButton.innerHTML = `<i class="far fa-thumbs-up"></i> <span>${danmu.likes}</span>`;
    } else {
        danmu.likedBy.add(currentUserId);
        danmu.likes++;
        danmu.isLiked = true;
        likeButton.innerHTML = `<i class="fas fa-thumbs-up" style="color: #ff6b6b;"></i> <span>${danmu.likes}</span>`;
    }
    saveDanmuData();
};

// 添加一个防抖动标志，避免短时间内重复显示弹幕
let lastDanmuShowTime: number = 0;
const DANMU_SHOW_INTERVAL: number = 500; // 防止重复显示的最小时间间隔（毫秒）

// 显示指定页面的弹幕
// @param pageNum: 页码
const showPageDanmu = (pageNum: number) => {
    // 防抖动机制 - 如果在短时间内重复调用，直接返回
    const currentTime = Date.now();
    if (currentTime - lastDanmuShowTime < DANMU_SHOW_INTERVAL && lastDanmuShowTime > 0) {
        return;
    }
    lastDanmuShowTime = currentTime;

    activeDanmuPage.value = pageNum;

    // 先清除当前页面所有弹幕
    clearPageDanmu(pageNum);

    // 确保状态正确
    if (!isDanmuEnabled.value) return;

    // 设置页面弹幕状态
    pageDanmuEnabled.value.set(pageNum, true);
    playingDanmuPages.value.set(pageNum, true);

    const pageDanmu = danmuList.value.filter(d => d.pageNum === pageNum);
    const totalDelay = pageDanmu.length > 0 ? (pageDanmu.length - 1) * 200 + 100 + 5000 : 0;

    pageDanmu.forEach((danmu, index) => {
        const delay = index * 200 + Math.random() * 100;
        setTimeout(() => {
            // 再次检查状态，确保在创建弹幕时状态仍然有效
            if (playingDanmuPages.value.get(pageNum) && pageDanmuEnabled.value.get(pageNum)) {
                createDanmuElement(danmu);
            }
        }, delay);
    });

    if (totalDelay > 0) {
        setTimeout(() => {
            playingDanmuPages.value.delete(pageNum);
            pageDanmuEnabled.value.set(pageNum, false);
        }, totalDelay);
    } else {
        playingDanmuPages.value.delete(pageNum);
    }
};

// 切换购物车模态框的显示状态
const toggleCartModal = () => {
    showCartModal.value = !showCartModal.value;
    if (showCartModal.value) {
        targetPageNum.value = null;
        checkBookInCart();
    }
};

// 关闭购物车模态框
const closeCartModal = () => {
    showCartModal.value = false;
};

// 检查书籍是否在购物车中
const checkBookInCart = () => {
    const cartData = localStorage.getItem('bookCart');
    if (cartData) {
        try {
            const cart = JSON.parse(cartData);
            const bookId = bookStore.bookData.id;
            isBookInCart.value = cart.wholeBooks?.includes(bookId) || false;
            cartPages.value = new Set(cart.pages?.[bookId] || []);
        } catch (e) {
            ElMessage.error('加载购物车数据失败，请重试。');
        }
    }
};

// 检查指定页码是否在购物车中
// @param pageNum: 页码
// @returns boolean: 是否在购物车中
const isPageInCart = (pageNum: number): boolean => {
    return cartPages.value.has(pageNum);
};

// 将整本书添加到购物车
const addWholeBookToCart = () => {
    const bookId = bookStore.bookData.id;
    if (!bookId) {
        ElMessage.error('无法获取书籍信息');
        return;
    }
    try {
        const cartData = localStorage.getItem('bookCart');
        let cart = cartData ? JSON.parse(cartData) : { wholeBooks: [], pages: {} };
        if (!cart.wholeBooks) {
            cart.wholeBooks = [];
        }
        if (!cart.wholeBooks.includes(bookId)) {
            cart.wholeBooks.push(bookId);
        }
        localStorage.setItem('bookCart', JSON.stringify(cart));
        isBookInCart.value = true;
        const totalPages = bookStore.bookData.totalPages || 100;
        const hasAllPages = cartPages.value.size >= totalPages;
        if (hasAllPages) {
            ElMessage.success('恭喜！已全部加入购物车！');
        } else {
            ElMessage.success('成功加入整本书到购物车！');
        }
        setTimeout(() => {
            closeCartModal();
        }, 3000);
    } catch (e) {
        ElMessage.error('加入购物车失败，请重试。');
    }
};

// 将指定页码添加到购物车
const addSpecificPageToCart = () => {
    const bookId = bookStore.bookData.id;
    const pageNum = targetPageNum.value;
    if (!bookId || !pageNum) {
        ElMessage.error('请输入有效的页码');
        return;
    }
    try {
        const cartData = localStorage.getItem('bookCart');
        let cart = cartData ? JSON.parse(cartData) : { wholeBooks: [], pages: {} };
        if (!cart.pages) {
            cart.pages = {};
        }
        if (!cart.pages[bookId]) {
            cart.pages[bookId] = [];
        }
        if (!cart.pages[bookId].includes(pageNum)) {
            cart.pages[bookId].push(pageNum);
            localStorage.setItem('bookCart', JSON.stringify(cart));
            cartPages.value.add(pageNum);
            ElMessage.success(`成功加入第${pageNum}页到购物车！`);
            const totalPages = bookStore.bookData.totalPages || 100;
            const hasAllPages = cartPages.value.size >= totalPages;
            const bookAdded = isBookInCart.value;
            if (hasAllPages && bookAdded) {
                ElMessage.success('恭喜！已全部加入购物车！');
                setTimeout(() => {
                    closeCartModal();
                }, 3000);
            }
            targetPageNum.value = null;
        }
    } catch (e) {
        ElMessage.error('加入购物车失败，请重试。');
    }
};

// 从购物车中移除指定页码
// @param pageNum: 要移除的页码
const removePageFromCart = (pageNum: number) => {
    const bookId = bookStore.bookData.id;
    if (!bookId) {
        return;
    }
    try {
        const cartData = localStorage.getItem('bookCart');
        if (!cartData) return;
        const cart = JSON.parse(cartData);
        if (cart.pages && cart.pages[bookId]) {
            const index = cart.pages[bookId].indexOf(pageNum);
            if (index > -1) {
                cart.pages[bookId].splice(index, 1);
                if (cart.pages[bookId].length === 0) {
                    delete cart.pages[bookId];
                }
                localStorage.setItem('bookCart', JSON.stringify(cart));
                cartPages.value.delete(pageNum);
                ElMessage.success(`已取消收藏第${pageNum}页`);
            }
        }
    } catch (e) {
        ElMessage.error('取消收藏失败，请重试。');
    }
};

// 处理页码点击事件
// @param e: 鼠标事件对象
// @param pageNum: 页码
const handlePageNumberClick = (e: MouseEvent, pageNum: number) => {
    e.stopPropagation();
    e.stopImmediatePropagation();
    if (isPageInCart(pageNum)) {
        pageToRemove.value = pageNum;
        showRemoveConfirmModal.value = true;
    }
};

// 获取页码元素的样式类
// @param pageNum: 页码
// @returns 包含样式类名的对象
const getPageNumberClass = (pageNum: number) => {
    return {
        'page-number': true,
        'in-cart': isPageInCart(pageNum)
    };
};

// 切换目录面板的显示状态
const toggleTocPanel = () => {
    showTocPanel.value = !showTocPanel.value;
    showFontPanel.value = false;
};

// 切换字体面板的显示状态
const toggleFontPanel = () => {
    showFontPanel.value = !showFontPanel.value;
    showTocPanel.value = false;
};

// 更新字体大小并保存偏好设置
const updateFontSize = () => {
    bookReadingStore.saveFontSizePreference(fontSize.value);
};

// 选择字体并保存偏好设置
// @param fontValue: 字体名称
const selectFont = (fontValue: string) => {
    fontFamily.value = fontValue;
    bookReadingStore.saveFontPreference(fontValue);
};

// 跳转到指定页码
// @param page: 目标页码
const gotoPage = (page: number) => {
    // 如果正在翻页动画中，不允许跳转
    if (isAnimating) return;

    const targetIndex = page - 1;
    if (targetIndex >= 0 && targetIndex < bookPages.value.length) {
        // 不直接设置isAnimating=false，而是让其自然过渡
        flippedOrder.clear();
        flipCounter = 0;
        rightRefs.value.forEach((element, index) => {
            if (element) {
                element.className = '';
                element.classList.add('right');
                if (index < targetIndex) {
                    element.classList.add('left');
                    flipCounter++;
                    flippedOrder.set(index, flipCounter);
                }
            }
        });
        start = targetIndex;
        rightLen = bookPages.value.length - start;
        updateZIndexesAfterLoad();
    }
    showTocPanel.value = false;
};

// 初始化章节书签
const initializeChapters = () => {
    const savedBookmarks = localStorage.getItem('chapterBookmarks');
    if (savedBookmarks) {
        try {
            chapterBookmarks.value = new Set(JSON.parse(savedBookmarks));
        } catch (e) {
            ElMessage.error('加载书签失败，请重试。');
        }
    }
};

// 关闭移除确认模态框
const closeRemoveConfirmModal = () => {
    showRemoveConfirmModal.value = false;
};

// 确认移除页面
const confirmRemovePage = () => {
    removePageFromCart(pageToRemove.value);
    closeRemoveConfirmModal();
};

// 处理点击外部区域的事件
// @param event: 鼠标事件对象
const handleClickOutside = (event: MouseEvent) => {
    const tocPanel = document.querySelector('.toc-panel');
    const tocBtn = document.querySelector('.toc-btn');
    const fontPanel = document.querySelector('.font-panel');
    const fontBtn = document.querySelector('.font-btn');
    if (showTocPanel.value && tocPanel && !tocPanel.contains(event.target as Node) && tocBtn && !tocBtn.contains(event.target as Node)) {
        showTocPanel.value = false;
    }
    if (showFontPanel.value && fontPanel && !fontPanel.contains(event.target as Node) && fontBtn && !fontBtn.contains(event.target as Node)) {
        showFontPanel.value = false;
    }
};

watch(() => showDanmuPanel.value, (newValue) => {
    if (newValue && !isDanmuEnabled.value) {
        isDanmuEnabled.value = true;
        loadDanmuData();
        // 使用防抖动的showPageDanmu，避免重复
        showPageDanmu(activeDanmuPage.value);
    } else if (!newValue) {
        danmuContent.value = '';
    }
});

watch(() => activeDanmuPage.value, (newPage) => {
    showPageDanmu(newPage);
});

onMounted(async () => {
    const route = useRoute();
    const bookId = route.params.id as string || 'default-book-id';
    await bookStore.initializeData(bookId);
    await fetchBookContent();
    initializeChapters();
    document.addEventListener('click', handleClickOutside);
    if (bookStore.bookData.id) {
        isInBookshelf.value = bookReadingStore.isInBookshelf(bookStore.bookData.id);
    }
    checkBookInCart();
    setTimeout(() => {
        rightLen = bookPages.value.length;
        const maxPossibleId = bookStore.bookData.totalPages;
        const currentMaxId = bookContent.value.length;
        hasMoreContent.value = currentMaxId < maxPossibleId;
        start = 0;
        rightLen = bookPages.value.length;
        updateZIndexesAfterLoad();
        (async () => {
            await checkAndRestoreBookmark();
            if (isDanmuEnabled.value) {
                loadDanmuData();
            }
        })();
    }, 0);
});

onUnmounted(() => {
    document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
@import '@/styles/components/BookReadingView.css';
</style>