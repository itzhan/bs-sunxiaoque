<template>
  <n-config-provider :theme="darkTheme" :theme-overrides="themeOverrides">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <router-view v-slot="{ Component }">
            <transition name="page" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { darkTheme } from 'naive-ui'
import type { GlobalThemeOverrides } from 'naive-ui'

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#c9a96e',
    primaryColorHover: '#d4b87a',
    primaryColorPressed: '#b8943f',
    primaryColorSuppl: '#c9a96e',
    bodyColor: '#0f0f1a',
    cardColor: '#1a1a2e',
    modalColor: '#1a1a2e',
    popoverColor: '#1a1a2e',
    inputColor: '#16213e',
    tableColor: '#1a1a2e',
    borderColor: '#2a2a45',
    dividerColor: '#22223a',
    borderRadius: '6px',
    fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, sans-serif"
  },
  Button: {
    textColorPrimary: '#1a1a2e',
    borderRadiusMedium: '6px'
  },
  Card: {
    borderRadius: '12px',
    borderColor: '#2a2a45'
  },
  Input: {
    borderRadius: '6px'
  },
  Tag: {
    borderRadius: '4px'
  }
}
</script>
