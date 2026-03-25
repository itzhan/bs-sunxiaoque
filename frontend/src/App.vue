<template>
  <n-config-provider :theme-overrides="themeOverrides">
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
import type { GlobalThemeOverrides } from 'naive-ui'

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#1B2838',
    primaryColorHover: '#253648',
    primaryColorPressed: '#13202E',
    primaryColorSuppl: '#1B2838',
    bodyColor: '#FAFAF7',
    cardColor: '#FFFFFF',
    modalColor: '#FFFFFF',
    popoverColor: '#FFFFFF',
    inputColor: '#F5F5F2',
    tableColor: '#FFFFFF',
    borderColor: '#E8E6E1',
    dividerColor: '#E8E6E1',
    borderRadius: '6px',
    textColorBase: '#1B2838',
    textColor1: '#1B2838',
    textColor2: '#5A6677',
    textColor3: '#8E99A4',
    fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, sans-serif"
  },
  Button: {
    textColorPrimary: '#FFFFFF',
    borderRadiusMedium: '6px'
  },
  Card: {
    borderRadius: '10px',
    borderColor: '#E8E6E1'
  },
  Input: {
    borderRadius: '6px',
    color: '#F5F5F2'
  },
  Tag: {
    borderRadius: '4px'
  },
  Tabs: {
    tabTextColorLine: '#5A6677',
    tabTextColorActiveLine: '#1B2838',
    tabTextColorHoverLine: '#1B2838',
    barColor: '#C7A951'
  }
}
</script>
