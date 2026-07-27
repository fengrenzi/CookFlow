<template>
  <div id="app">
    <component :is="currentLayout">
      <router-view v-slot="{ Component }">
        <component :is="Component" />
      </router-view>
    </component>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();

// 根据路由meta信息动态选择布局
const currentLayout = computed(() => {
  const layoutName = route.meta.layout as string;
  if (layoutName === 'MainLayout') {
    return MainLayout;
  }
  return 'div';
});
</script>

<style>
html, body, #app {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
