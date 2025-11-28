// src/services/authService.ts
import axios from 'axios'

export interface LoginData {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
}

const API_URL = 'http://localhost:8080/api/auth'

export async function login(data: LoginData): Promise<LoginResponse> {
  const response = await axios.post<LoginResponse>(`${API_URL}/login`, data)
  return response.data
}
