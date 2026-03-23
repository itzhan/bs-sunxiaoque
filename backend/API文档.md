# 光影美术馆在线导览与预约系统 API 文档

## 基本信息

| 项目 | 说明 |
|------|------|
| 项目名称 | 光影美术馆在线导览与预约系统 |
| Base URL | `http://localhost:8080` |
| 认证方式 | JWT Bearer Token |
| 请求格式 | `Content-Type: application/json` |

### 认证说明

需认证的接口请在请求头中携带：

```
Authorization: Bearer <your_jwt_token>
```

### 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 分页响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | zhangwei | 123456 |
| 普通用户 | （其他用户） | 123456 |

---

## 1. 认证模块 `/api/auth`

### 1.1 用户登录

**POST** `/api/auth/login`

**请求体：**

```json
{
  "username": "zhangwei",
  "password": "123456"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "zhangwei",
    "nickname": "张伟",
    "avatar": null,
    "role": "USER"
  }
}
```

---

### 1.2 用户注册

**POST** `/api/auth/register`

**请求体：**

```json
{
  "username": "newuser",
  "password": "123456",
  "nickname": "新用户",
  "phone": "13800138000",
  "email": "user@example.com"
}
```

> `phone` 和 `email` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 2,
    "username": "newuser",
    "nickname": "新用户",
    "avatar": null,
    "role": "USER"
  }
}
```

---

### 1.3 获取当前用户信息

**GET** `/api/auth/info`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangwei",
    "nickname": "张伟",
    "avatar": null,
    "phone": "13800138000",
    "email": "zhangwei@example.com",
    "role": "USER",
    "status": 1
  }
}
```

---

## 2. 用户管理（管理员）`/api/users`

### 2.1 用户列表

**GET** `/api/users?page=1&size=10&keyword=&status=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页条数，默认 10 |
| keyword | string | 否 | 搜索关键词（用户名/昵称） |
| status | int | 否 | 状态筛选：0-禁用，1-启用 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "zhangwei",
        "nickname": "张伟",
        "phone": "13800138000",
        "email": "zhangwei@example.com",
        "status": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 2.2 用户详情

**GET** `/api/users/{id}`

**请求头：** `Authorization: Bearer <token>`

**路径参数：** `id` - 用户 ID

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangwei",
    "nickname": "张伟",
    "phone": "13800138000",
    "email": "zhangwei@example.com",
    "avatar": null,
    "gender": 0,
    "status": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 2.3 修改用户状态

**PUT** `/api/users/{id}/status`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "status": 1
}
```

> `status`: 0-禁用，1-启用

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 2.4 重置用户密码

**PUT** `/api/users/{id}/reset-password`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> 密码将被重置为 `123456`

---

## 3. 个人资料 `/api/profile`

### 3.1 获取当前用户资料

**GET** `/api/profile`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangwei",
    "nickname": "张伟",
    "phone": "13800138000",
    "email": "zhangwei@example.com",
    "avatar": null,
    "gender": 0,
    "createTime": "2026-01-01 10:00:00"
  }
}
```

---

### 3.2 更新个人资料

**PUT** `/api/profile`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "nickname": "张伟",
  "phone": "13800138000",
  "email": "zhangwei@example.com",
  "avatar": "https://example.com/avatar.jpg",
  "gender": 1
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.3 修改密码

**PUT** `/api/profile/password`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "oldPassword": "123456",
  "newPassword": "newpass123"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 4. 展览分类 `/api/categories`

### 4.1 分类列表（分页）

**GET** `/api/categories?page=1&size=10&keyword=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| keyword | string | 否 | 搜索关键词 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "油画",
        "description": "经典油画作品",
        "sortOrder": 1,
        "status": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 4.2 获取全部分类（不分页）

**GET** `/api/categories/all`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "油画",
      "description": "经典油画作品",
      "sortOrder": 1,
      "status": 1
    }
  ]
}
```

---

### 4.3 分类详情

**GET** `/api/categories/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "油画",
    "description": "经典油画作品",
    "sortOrder": 1,
    "status": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 4.4 新增分类

**POST** `/api/categories`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "name": "油画",
  "description": "经典油画作品",
  "sortOrder": 1,
  "status": 1
}
```

> `description`、`sortOrder`、`status` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 4.5 更新分类

**PUT** `/api/categories/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "name": "油画",
  "description": "经典油画作品",
  "sortOrder": 1,
  "status": 1
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 4.6 删除分类

**DELETE** `/api/categories/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 5. 展览管理 `/api/exhibitions`

### 5.1 展览列表

**GET** `/api/exhibitions?page=1&size=10&title=&categoryId=&status=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| title | string | 否 | 展览标题关键词 |
| categoryId | long | 否 | 分类 ID |
| status | int | 否 | 状态筛选 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "印象派大师展",
        "description": "印象派经典作品",
        "coverImage": "https://example.com/cover.jpg",
        "categoryId": 1,
        "location": "1号展厅",
        "hallName": "主展厅",
        "startDate": "2026-04-01",
        "endDate": "2026-06-30",
        "dailyCapacity": 500,
        "ticketPrice": 50.00,
        "viewCount": 100,
        "status": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 5.2 展览详情

**GET** `/api/exhibitions/{id}`

**请求头：** `Authorization: Bearer <token>`

> 访问详情时 `viewCount` 会自动 +1

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "印象派大师展",
    "description": "印象派经典作品",
    "coverImage": "https://example.com/cover.jpg",
    "images": ["https://example.com/1.jpg", "https://example.com/2.jpg"],
    "categoryId": 1,
    "location": "1号展厅",
    "hallName": "主展厅",
    "startDate": "2026-04-01",
    "endDate": "2026-06-30",
    "dailyCapacity": 500,
    "ticketPrice": 50.00,
    "viewCount": 101,
    "status": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 5.3 新增展览

**POST** `/api/exhibitions`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "title": "印象派大师展",
  "description": "印象派经典作品",
  "coverImage": "https://example.com/cover.jpg",
  "images": ["https://example.com/1.jpg", "https://example.com/2.jpg"],
  "categoryId": 1,
  "location": "1号展厅",
  "hallName": "主展厅",
  "startDate": "2026-04-01",
  "endDate": "2026-06-30",
  "dailyCapacity": 500,
  "ticketPrice": 50.00,
  "status": 1
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 5.4 更新展览

**PUT** `/api/exhibitions/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：** 同新增展览

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 5.5 删除展览

**DELETE** `/api/exhibitions/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 6. 展品管理 `/api/exhibit-items`

### 6.1 展品列表

**GET** `/api/exhibit-items?page=1&size=10&exhibitionId=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| exhibitionId | long | 否 | 展览 ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "exhibitionId": 1,
        "name": "星空",
        "artist": "梵高",
        "era": "1889年",
        "description": "经典名作",
        "coverImage": "https://example.com/star.jpg",
        "audioUrl": "https://example.com/audio.mp3",
        "sortOrder": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 6.2 展品详情

**GET** `/api/exhibit-items/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "exhibitionId": 1,
    "name": "星空",
    "artist": "梵高",
    "era": "1889年",
    "description": "经典名作",
    "coverImage": "https://example.com/star.jpg",
    "audioUrl": "https://example.com/audio.mp3",
    "sortOrder": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 6.3 新增展品

**POST** `/api/exhibit-items`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "exhibitionId": 1,
  "name": "星空",
  "artist": "梵高",
  "era": "1889年",
  "description": "经典名作",
  "coverImage": "https://example.com/star.jpg",
  "audioUrl": "https://example.com/audio.mp3",
  "sortOrder": 1
}
```

> `artist`、`era`、`description`、`coverImage`、`audioUrl`、`sortOrder` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 6.4 更新展品

**PUT** `/api/exhibit-items/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：** 同新增展品

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 6.5 删除展品

**DELETE** `/api/exhibit-items/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 7. 虚拟导览 `/api/virtual-tours`

### 7.1 虚拟导览列表

**GET** `/api/virtual-tours?page=1&size=10&exhibitionId=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| exhibitionId | long | 否 | 展览 ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "exhibitionId": 1,
        "title": "主展厅全景导览",
        "description": "360度全景体验",
        "panoramaUrl": "https://example.com/panorama.html",
        "thumbnail": "https://example.com/thumb.jpg",
        "tourType": "PANORAMA",
        "sortOrder": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 7.2 虚拟导览详情

**GET** `/api/virtual-tours/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "exhibitionId": 1,
    "title": "主展厅全景导览",
    "description": "360度全景体验",
    "panoramaUrl": "https://example.com/panorama.html",
    "thumbnail": "https://example.com/thumb.jpg",
    "tourType": "PANORAMA",
    "sortOrder": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 7.3 新增虚拟导览

**POST** `/api/virtual-tours`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "exhibitionId": 1,
  "title": "主展厅全景导览",
  "description": "360度全景体验",
  "panoramaUrl": "https://example.com/panorama.html",
  "thumbnail": "https://example.com/thumb.jpg",
  "tourType": "PANORAMA",
  "sortOrder": 1
}
```

> `description`、`thumbnail`、`tourType`、`sortOrder` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 7.4 更新虚拟导览

**PUT** `/api/virtual-tours/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：** 同新增虚拟导览

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.5 删除虚拟导览

**DELETE** `/api/virtual-tours/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 8. 时段管理 `/api/time-slots`

### 8.1 时段列表（按展览，不分页）

**GET** `/api/time-slots?exhibitionId=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| exhibitionId | long | 是 | 展览 ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "exhibitionId": 1,
      "slotName": "上午场",
      "startTime": "09:00",
      "endTime": "12:00",
      "maxCapacity": 200,
      "createTime": "2026-01-01 10:00:00"
    }
  ]
}
```

---

### 8.2 时段详情

**GET** `/api/time-slots/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "exhibitionId": 1,
    "slotName": "上午场",
    "startTime": "09:00",
    "endTime": "12:00",
    "maxCapacity": 200,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 8.3 新增时段

**POST** `/api/time-slots`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "exhibitionId": 1,
  "slotName": "上午场",
  "startTime": "09:00",
  "endTime": "12:00",
  "maxCapacity": 200
}
```

> `maxCapacity` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 8.4 更新时段

**PUT** `/api/time-slots/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：** 同新增时段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 8.5 删除时段

**DELETE** `/api/time-slots/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 8.6 查询时段可用容量

**GET** `/api/time-slots/{id}/available?date=2026-04-01`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| date | string | 是 | 日期，格式 yyyy-MM-dd |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "timeSlotId": 1,
    "date": "2026-04-01",
    "maxCapacity": 200,
    "bookedCount": 50,
    "availableCount": 150
  }
}
```

---

## 9. 预约管理 `/api/reservations`

### 9.1 预约列表（管理员）

**GET** `/api/reservations?page=1&size=10&exhibitionId=&status=&username=`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| exhibitionId | long | 否 | 展览 ID |
| status | int | 否 | 预约状态 |
| username | string | 否 | 用户名 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "zhangwei",
        "exhibitionId": 1,
        "exhibitionTitle": "印象派大师展",
        "timeSlotId": 1,
        "slotName": "上午场",
        "reservationDate": "2026-04-01",
        "numVisitors": 2,
        "contactName": "张伟",
        "contactPhone": "13800138000",
        "status": 0,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 9.2 我的预约

**GET** `/api/reservations/my?page=1&size=10&status=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| status | int | 否 | 预约状态 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "exhibitionId": 1,
        "exhibitionTitle": "印象派大师展",
        "timeSlotId": 1,
        "slotName": "上午场",
        "reservationDate": "2026-04-01",
        "numVisitors": 2,
        "contactName": "张伟",
        "contactPhone": "13800138000",
        "status": 0,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 9.3 创建预约

**POST** `/api/reservations`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "exhibitionId": 1,
  "timeSlotId": 1,
  "reservationDate": "2026-04-01",
  "numVisitors": 2,
  "contactName": "张伟",
  "contactPhone": "13800138000"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 9.4 取消预约

**PUT** `/api/reservations/{id}/cancel`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "reason": "行程变更"
}
```

> `reason` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 9.5 确认预约（管理员）

**PUT** `/api/reservations/{id}/confirm`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 9.6 完成预约（管理员）

**PUT** `/api/reservations/{id}/complete`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 10. 公告管理 `/api/announcements`

### 10.1 公告列表

**GET** `/api/announcements?page=1&size=10&status=`

**请求头：** `Authorization: Bearer <token>`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| status | int | 否 | 状态筛选 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "开馆通知",
        "content": "美术馆将于4月1日正式开放...",
        "coverImage": "https://example.com/cover.jpg",
        "status": 1,
        "topFlag": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 10.2 公告详情

**GET** `/api/announcements/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "开馆通知",
    "content": "美术馆将于4月1日正式开放...",
    "coverImage": "https://example.com/cover.jpg",
    "status": 1,
    "topFlag": 1,
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-01 10:00:00"
  }
}
```

---

### 10.3 新增公告

**POST** `/api/announcements`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "title": "开馆通知",
  "content": "美术馆将于4月1日正式开放...",
  "coverImage": "https://example.com/cover.jpg",
  "status": 1,
  "topFlag": 1
}
```

> `coverImage`、`status`、`topFlag` 为可选字段

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 10.4 更新公告

**PUT** `/api/announcements/{id}`

**请求头：** `Authorization: Bearer <token>`

**请求体：** 同新增公告

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 10.5 删除公告

**DELETE** `/api/announcements/{id}`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 11. 评论管理 `/api/comments`

### 11.1 评论列表（管理员）

**GET** `/api/comments?page=1&size=10&exhibitionId=&status=`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| exhibitionId | long | 否 | 展览 ID |
| status | int | 否 | 审核状态 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "zhangwei",
        "exhibitionId": 1,
        "content": "展览非常精彩！",
        "rating": 5,
        "status": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 11.2 发表评论

**POST** `/api/comments`

**请求头：** `Authorization: Bearer <token>`

**请求体：**

```json
{
  "exhibitionId": 1,
  "content": "展览非常精彩！",
  "rating": 5
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  }
}
```

---

### 11.3 审核通过（管理员）

**PUT** `/api/comments/{id}/approve`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.4 审核拒绝（管理员）

**PUT** `/api/comments/{id}/reject`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.5 删除评论（管理员）

**DELETE** `/api/comments/{id}`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 12. 收藏管理 `/api/favorites`

### 12.1 我的收藏列表

**GET** `/api/favorites?page=1&size=10`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "exhibitionId": 1,
        "exhibitionTitle": "印象派大师展",
        "coverImage": "https://example.com/cover.jpg",
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 12.2 切换收藏状态

**POST** `/api/favorites/{exhibitionId}`

**请求头：** `Authorization: Bearer <token>`

**说明：** 若已收藏则取消收藏，若未收藏则添加收藏（切换操作）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "favorited": true
  }
}
```

---

### 12.3 检查是否已收藏

**GET** `/api/favorites/{exhibitionId}/check`

**请求头：** `Authorization: Bearer <token>`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "favorited": true
  }
}
```

---

## 13. 数据看板 `/api/dashboard`

### 13.1 统计数据

**GET** `/api/dashboard`

**请求头：** `Authorization: Bearer <token>`（需管理员权限）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userCount": 100,
    "exhibitionCount": 10,
    "reservationCount": 500,
    "todayReservationCount": 20,
    "commentCount": 200
  }
}
```

---

## 14. 文件上传 `/api/files`

### 14.1 上传文件

**POST** `/api/files/upload`

**请求头：** `Authorization: Bearer <token>`

**Content-Type：** `multipart/form-data`

**请求体：** `file` - 文件字段（表单上传）

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "https://example.com/uploads/2026/01/xxx.jpg"
  }
}
```

---

## 15. 公开接口（无需认证）`/api/public`

以下接口无需携带 Token，供门户/展示端使用。

### 15.1 展览列表（已发布）

**GET** `/api/public/exhibitions?page=1&size=10&title=&categoryId=`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |
| title | string | 否 | 展览标题关键词 |
| categoryId | long | 否 | 分类 ID |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "印象派大师展",
        "description": "印象派经典作品",
        "coverImage": "https://example.com/cover.jpg",
        "categoryId": 1,
        "location": "1号展厅",
        "startDate": "2026-04-01",
        "endDate": "2026-06-30",
        "ticketPrice": 50.00,
        "viewCount": 100
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 15.2 展览详情（含展品、导览、时段）

**GET** `/api/public/exhibitions/{id}`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "印象派大师展",
    "description": "印象派经典作品",
    "coverImage": "https://example.com/cover.jpg",
    "images": ["https://example.com/1.jpg"],
    "categoryId": 1,
    "location": "1号展厅",
    "hallName": "主展厅",
    "startDate": "2026-04-01",
    "endDate": "2026-06-30",
    "dailyCapacity": 500,
    "ticketPrice": 50.00,
    "viewCount": 101,
    "exhibitItems": [
      {
        "id": 1,
        "name": "星空",
        "artist": "梵高",
        "coverImage": "https://example.com/star.jpg"
      }
    ],
    "virtualTours": [
      {
        "id": 1,
        "title": "主展厅全景导览",
        "panoramaUrl": "https://example.com/panorama.html",
        "thumbnail": "https://example.com/thumb.jpg"
      }
    ],
    "timeSlots": [
      {
        "id": 1,
        "slotName": "上午场",
        "startTime": "09:00",
        "endTime": "12:00",
        "maxCapacity": 200
      }
    ]
  }
}
```

---

### 15.3 公告列表（已发布）

**GET** `/api/public/announcements?page=1&size=10`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "开馆通知",
        "content": "美术馆将于4月1日正式开放...",
        "coverImage": "https://example.com/cover.jpg",
        "topFlag": 1,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

### 15.4 全部分类（已启用）

**GET** `/api/public/categories`

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "油画",
      "description": "经典油画作品",
      "sortOrder": 1
    }
  ]
}
```

---

### 15.5 展览评论（已审核通过）

**GET** `/api/public/exhibitions/{id}/comments?page=1&size=10`

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "zhangwei",
        "content": "展览非常精彩！",
        "rating": 5,
        "createTime": "2026-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

---

## 附录：预约/评论状态说明

### 预约状态

| 值 | 说明 |
|----|------|
| 0 | 待确认 |
| 1 | 已确认 |
| 2 | 已完成 |
| 3 | 已取消 |

### 评论审核状态

| 值 | 说明 |
|----|------|
| 0 | 待审核 |
| 1 | 已通过 |
| 2 | 已拒绝 |

### 用户/分类/展览/公告状态

| 值 | 说明 |
|----|------|
| 0 | 禁用/下架 |
| 1 | 启用/发布 |
