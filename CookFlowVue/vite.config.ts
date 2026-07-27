import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import tailwindcss from '@tailwindcss/vite';


// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), tailwindcss()],
  
  // 路径别名配置
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      // '@components': resolve(__dirname, 'src/components'),
      // '@views': resolve(__dirname, 'src/views'),
      // '@store': resolve(__dirname, 'src/store'),
      // '@router': resolve(__dirname, 'src/router'),
      // '@assets': resolve(__dirname, 'src/assets'),
      // '@styles': resolve(__dirname, 'src/styles'),
      // '@utils': resolve(__dirname, 'src/utils'),
      // '@layouts': resolve(__dirname, 'src/layouts')
    }
  },
  
  // 服务器配置
  server: {
    port: 80,
    open: false,
  },
  
  // 构建配置
  build: {
    // 提高警告限制
    chunkSizeWarningLimit: 1000,
    
    // 优化代码分割
    rollupOptions: {
      output: {
        manualChunks: {
          // 外部依赖单独打包
          vendor: ['vue', 'vue-router', 'pinia'],
          
          // 按功能模块分割
          'router': ['@/router'],
          'store': ['@/store'],
          
          // 视图页面分割
          'views': []
        },
        
        // 优化输出文件名
        entryFileNames: 'assets/[name].[hash].js',
        chunkFileNames: 'assets/[name].[hash].js',
        assetFileNames: 'assets/[name].[hash].[ext]'
      }
    },
    
    // 优化构建速度
    cssCodeSplit: true,
    sourcemap: false,
    
    // 缩小代码
    minify: 'esbuild'
  },
  
  // CSS配置
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@import "@/styles/global.css";`
      }
    },
    devSourcemap: true
  }
})
