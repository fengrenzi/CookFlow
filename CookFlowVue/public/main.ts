import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router';
import { setupStore } from '@/store';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import '@/styles/global.css';
import '@fortawesome/fontawesome-free/css/all.css';
import * as echarts from 'echarts';

fetch('/china.geojson')
  .then(res => res.json())
  .then(geoJson => {
    echarts.registerMap('china', geoJson);
    console.log('中国地图注册成功');
  })
  .catch(err => console.error('地图加载失败', err));

const app = createApp(App);
app.use(router);
app.use(ElementPlus);
setupStore(app);
app.mount('#app');