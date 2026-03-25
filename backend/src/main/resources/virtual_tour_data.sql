-- 更新虚拟导览数据为真实可嵌入的全景资源
-- 使用 Google Maps Embed API Street View 模式

-- 1. 光之诗篇VR全景导览 → 凡尔赛宫镜厅
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCCAo2vsXUJIAAAQfCThDzQ!2m2!1d48.80541!2d2.12028!3f180!4f0!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1551038247-3d9af20df552?w=400&h=300&fit=crop',
  description = '凡尔赛宫镜厅全景导览 —— 踏入这座17世纪的艺术殿堂，感受357面镜子映射出的无尽光影之美'
WHERE id = 1;

-- 2. 光之瀑布3D体验 → 大英博物馆中庭
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcE9KTlExcmFwS09FZnNLUXRyeDltb3RGOUhRNE94V3JfSHRZcmJx!2m2!1d51.51902!2d-0.12695!3f220!4f10!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1569587112025-0d460e81a126?w=400&h=300&fit=crop',
  description = '大英博物馆大中庭全景体验 —— 由诺曼·福斯特设计的玻璃穹顶下，探索人类文明的瑰宝'
WHERE id = 2;

-- 3. 数字花园VR全景 → 纽约大都会博物馆
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcE5QOXpfWjhKNEFZME5JSXZTZUowRGU1X3pYbm1FcUhaOFdUTmtI!2m2!1d40.77940!2d-73.96325!3f90!4f0!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1565060299509-453c4f3bc905?w=400&h=300&fit=crop',
  description = '大都会艺术博物馆全景导览 —— 穿梭于世界顶级艺术收藏之间，体验跨越5000年的艺术之旅'
WHERE id = 3;

-- 4. 镜界全景体验 → 卢浮宫
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcFBJZGhQLThpWThlUEM4bDRyS0NNUl92a1pMd3RfTDRfRFVJZ2xQ!2m2!1d48.86075!2d2.33759!3f180!4f0!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=400&h=300&fit=crop',
  description = '卢浮宫全景导览 —— 在世界最大的艺术博物馆中漫步，与蒙娜丽莎和胜利女神面对面'
WHERE id = 4;

-- 5. 影像诗学线上展厅 → 乌菲兹美术馆
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcE1DeGh3NXg4UWh6YUhnWFRWbU5iQjRjUUxkclRWVDJNYjhOR3N5!2m2!1d43.76832!2d11.25531!3f270!4f10!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1476304884326-cd2c88572c5f?w=400&h=300&fit=crop',
  description = '佛罗伦萨乌菲兹美术馆全景 —— 文艺复兴艺术的圣殿，波提切利《春》与达芬奇杰作尽在眼前'
WHERE id = 5;

-- 6. 百年光影回溯之旅 → 梵蒂冈博物馆
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCy39lMaF7S1JA-OPGfqJPA!2m2!1d41.90647!2d12.45368!3f0!4f10!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1533154683836-84ea7a0bc310?w=400&h=300&fit=crop',
  description = '梵蒂冈博物馆全景导览 —— 仰望西斯廷教堂穹顶壁画，感受米开朗基罗笔下的神圣光影'
WHERE id = 6;

-- 7. 光影国风全景游 → 故宫博物院
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcE9FbkRUNThIME5DYmJVa01tcXFVcmd2OGNvZC1EUzhCRzRCWmhj!2m2!1d39.91660!2d116.39716!3f180!4f0!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1508804185872-d7badad00f7d?w=400&h=300&fit=crop',
  description = '故宫博物院太和殿全景 —— 走进紫禁城的核心，领略600年皇家建筑的恢弘气势与光影之美'
WHERE id = 7;

-- 8. 墨韵流光3D互动 → 东京国立博物馆
UPDATE virtual_tour SET
  panorama_url = 'https://www.google.com/maps/embed?pb=!4v1711000000000!6m8!1m7!1sCAoSLEFGMVFpcE5ELThNX0xOOVFiX3llMDAyZDZZdE1teU9SX2VMS1k3bUtGVWxQ!2m2!1d35.71869!2d139.77647!3f0!4f0!5f0.7820865974627469',
  thumbnail = 'https://images.unsplash.com/photo-1480796927426-f609979314bd?w=400&h=300&fit=crop',
  description = '东京国立博物馆全景体验 —— 探索日本最古老的综合博物馆，东西方艺术的光影交融'
WHERE id = 8;
