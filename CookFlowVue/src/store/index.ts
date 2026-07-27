import { createPinia } from 'pinia';
import type { App } from 'vue';

// 创建pinia实例
const pinia = createPinia();

// 导出插件安装函数
export function setupStore(app: App<Element>) {
  app.use(pinia);
}

export default pinia;