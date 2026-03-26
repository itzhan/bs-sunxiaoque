<template>
  <div class="art-card p-5 flex-b mb-5 max-sm:mb-4">
    <div>
      <h2 class="text-2xl font-medium">关于光影美术馆</h2>
      <p class="text-g-700 mt-1">{{ systemName }} —— 光影交织的艺术殿堂，沉浸式展览预约管理平台</p>
      <p class="text-g-700 mt-1">基于 Spring Boot + Vue3 + Element Plus 构建的全栈展览管理系统</p>

      <div class="flex flex-wrap gap-3.5 max-w-150 mt-9">
        <div
          class="w-60 flex-cb h-12.5 px-3.5 border border-g-300 c-p rounded-lg text-sm bg-g-100 duration-300 hover:-translate-y-1 max-sm:w-full"
          v-for="item in infoList"
          :key="item.label"
        >
          <span class="text-g-700">{{ item.label }}</span>
          <span class="text-g-500 text-xs">{{ item.value }}</span>
        </div>
      </div>
    </div>
    <img class="w-75 max-md:!hidden" src="@imgs/draw/draw1.png" alt="draw1" />
  </div>
</template>

<script setup lang="ts">
  import AppConfig from '@/config'

  const systemName = AppConfig.systemInfo.name

  const infoList = [
    { label: '后端框架', value: 'Spring Boot 3.x' },
    { label: '前端框架', value: 'Vue 3 + TypeScript' },
    { label: 'UI 组件库', value: 'Element Plus' },
    { label: '数据库', value: 'MySQL 8.0' }
  ]
</script>
