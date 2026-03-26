import request from '@/utils/http'

export function fetchLogin(params: Api.Auth.LoginParams) {
  return request.post<Api.Auth.LoginResponse>({
    url: '/api/auth/login',
    params
  })
}

export async function fetchGetUserInfo(): Promise<Api.Auth.UserInfo> {
  const user = await request.get<any>({ url: '/api/auth/info' })
  return {
    userId: user.id,
    userName: user.username,
    nickname: user.nickname,
    email: user.email || '',
    avatar: user.avatar || '',
    roles: user.role === 0 ? ['R_SUPER', 'R_ADMIN'] : ['R_USER'],
    buttons: []
  }
}
