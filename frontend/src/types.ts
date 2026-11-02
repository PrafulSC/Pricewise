export type Category = { id: number; categoryName: string; description?: string }
export type Product = {
  id: number
  name: string
  brand?: string
  description?: string
  imageUrl?: string
  category?: Category | null
}
export type Seller = { id: number; sellerName: string; website?: string }
export type ProductPrice = {
  id: number
  price: number
  currency: string
  productUrl?: string
  availability: string
  updatedAt?: string
  seller: Seller
}
export type PriceHistory = { id: number; price: number; recordedAt: string }
