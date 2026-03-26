<template>
  <div class="art-card h-105 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>展览浏览趋势</h4>
        <p>本年度各月总浏览量</p>
      </div>
    </div>
    <ArtLineChart
      height="calc(100% - 56px)"
      :data="data"
      :xAxisData="xAxisData"
      :showAreaColor="true"
      :showAxisLine="false"
    />
  </div>
</template>

<script setup lang="ts">
  /**
   * 全年展览浏览量趋势
   * 反映光影美术馆各展览的月度浏览总量
   */
  const data = [1200, 980, 1580, 2100, 3200, 2800, 3500, 4100, 4580, 3900, 5200, 6100]

  const xAxisData = [
    '1月', '2月', '3月', '4月', '5月', '6月',
    '7月', '8月', '9月', '10月', '11月', '12月'
  ]
</script>
