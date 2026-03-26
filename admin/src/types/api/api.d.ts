declare namespace Api {
  namespace Common {
    interface PaginationParams {
      current: number
      size: number
      total: number
    }
    type CommonSearchParams = Pick<PaginationParams, 'current' | 'size'>
    interface PaginatedResponse<T = any> {
      records: T[]
      total: number
      page: number
      size: number
    }
  }

  namespace Auth {
    interface LoginParams {
      username: string
      password: string
    }
    interface LoginResponse {
      token: string
      userId: number
      username: string
      nickname: string
      avatar: string | null
      role: number
    }
    interface UserInfo {
      userId: number
      userName: string
      roles: string[]
      buttons: string[]
      email: string
      avatar?: string
      nickname?: string
    }
  }

  namespace SystemManage {
    type UserList = Api.Common.PaginatedResponse<UserListItem>
    interface UserListItem {
      id: number
      username: string
      nickname: string
      phone: string
      email: string
      avatar: string
      gender: number
      role: number
      status: number
      createTime: string
      updateTime: string
    }
    type UserSearchParams = Partial<
      Pick<UserListItem, 'username' | 'status'> & Api.Common.CommonSearchParams & { keyword: string }
    >
    type RoleList = Api.Common.PaginatedResponse<RoleListItem>
    interface RoleListItem {
      roleId: number
      roleName: string
      roleCode: string
      description: string
      enabled: boolean
      createTime: string
    }
    type RoleSearchParams = Partial<
      Pick<RoleListItem, 'roleId' | 'roleName' | 'roleCode' | 'description' | 'enabled'> &
        Api.Common.CommonSearchParams & { startTime: string | null; endTime: string | null }
    >
  }

  namespace Gallery {
    interface Exhibition {
      id: number
      title: string
      description: string
      coverImage: string
      images: string
      categoryId: number
      categoryName: string
      location: string
      hallName: string
      startDate: string
      endDate: string
      dailyCapacity: number
      ticketPrice: number
      status: number
      viewCount: number
      createTime: string
      updateTime: string
    }
    interface ExhibitionCategory {
      id: number
      name: string
      description: string
      sortOrder: number
      status: number
      createTime: string
    }
    interface ExhibitItem {
      id: number
      exhibitionId: number
      name: string
      artist: string
      era: string
      description: string
      coverImage: string
      audioUrl: string
      sortOrder: number
      createTime: string
    }
    interface VirtualTour {
      id: number
      exhibitionId: number
      title: string
      description: string
      panoramaUrl: string
      thumbnail: string
      tourType: number
      sortOrder: number
      createTime: string
    }
    interface TimeSlot {
      id: number
      exhibitionId: number
      slotName: string
      startTime: string
      endTime: string
      maxCapacity: number
      createTime: string
    }
    interface Reservation {
      id: number
      userId: number
      exhibitionId: number
      timeSlotId: number
      reservationDate: string
      numVisitors: number
      contactName: string
      contactPhone: string
      status: number
      cancelReason: string
      exhibitionTitle: string
      slotName: string
      username: string
      createTime: string
    }
    interface Announcement {
      id: number
      title: string
      content: string
      coverImage: string
      status: number
      topFlag: number
      createTime: string
    }
    interface Comment {
      id: number
      userId: number
      exhibitionId: number
      content: string
      rating: number
      status: number
      nickname: string
      avatar: string
      exhibitionTitle: string
      createTime: string
    }
    interface Dashboard {
      totalUsers: number
      totalExhibitions: number
      totalReservations: number
      todayReservations: number
      totalComments: number
      totalVisits: number
    }
  }
}
