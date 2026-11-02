import type { Category, Product, ProductPrice, Seller, PriceHistory } from './types'

const BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${BASE}${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(options?.headers || {}) }
  })
  if (!response.ok) {
    const body = await response.json().catch(() => ({}))
    throw new Error(body.error || `Request failed (${response.status})`)
  }
  if (response.status === 204) return undefined as T
  return response.json()
}

export const api = {
  products: (search = '') => request<Product[]>(`/products${search ? `?search=${encodeURIComponent(search)}` : ''}`),
  categories: () => request<Category[]>('/categories'),
  sellers: () => request<Seller[]>('/sellers'),
  prices: (productId: number) => request<ProductPrice[]>(`/products/${productId}/prices`),
  history: (productId: number) => request<PriceHistory[]>(`/products/${productId}/history`),
  addWishlist: (userId: number, productId: number) =>
    request('/wishlist', { method: 'POST', body: JSON.stringify({ userId, productId }) }),
  addAlert: (userId: number, productId: number, targetPrice: number) =>
    request('/alerts', { method: 'POST', body: JSON.stringify({ userId, productId, targetPrice }) })
}
