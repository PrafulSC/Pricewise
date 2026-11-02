import { useEffect, useMemo, useState } from 'react'
import {
  Search, Heart, Bell, TrendingDown, ArrowUpRight, ShoppingBag,
  LayoutDashboard, SlidersHorizontal, ChevronRight, Menu, X, RefreshCw
} from 'lucide-react'
import {
  ResponsiveContainer, LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid
} from 'recharts'
import { api } from './api'
import type { Product, ProductPrice, PriceHistory } from './types'

const money = (value: number) => new Intl.NumberFormat('en-IN', {
  style: 'currency', currency: 'INR', maximumFractionDigits: 0
}).format(value)

const fallbackImages = [
  'https://images.unsplash.com/photo-151170 long',
  'https://images.unsplash.com/photo-1592750475338-74b7b21085ab',
  'https://images.unsplash.com/photo-1496181133206-80ce9b88a853'
]

export default function App() {
  const [products, setProducts] = useState<Product[]>([])
  const [selected, setSelected] = useState<Product | null>(null)
  const [prices, setPrices] = useState<ProductPrice[]>([])
  const [history, setHistory] = useState<PriceHistory[]>([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  const [mobileMenu, setMobileMenu] = useState(false)

  const loadProducts = async (term = '') => {
    setLoading(true)
    setError('')
    try {
      const result = await api.products(term)
      setProducts(result)
      if (result.length && (!selected || !result.some(p => p.id === selected.id))) {
        await selectProduct(result[0])
      } else if (!result.length) {
        setSelected(null); setPrices([]); setHistory([])
      }
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Could not load products')
    } finally { setLoading(false) }
  }

  const selectProduct = async (product: Product) => {
    setSelected(product)
    try {
      const [p, h] = await Promise.all([api.prices(product.id), api.history(product.id)])
      setPrices(p); setHistory(h)
    } catch (e) {
      setPrices([]); setHistory([])
      setError(e instanceof Error ? e.message : 'Could not load product prices')
    }
  }

  useEffect(() => { void loadProducts() }, [])

  const lowest = useMemo(() => prices.length ? Math.min(...prices.map(p => p.price)) : 0, [prices])
  const savings = useMemo(() => prices.length ? Math.max(...prices.map(p => p.price)) - lowest : 0, [prices])
  const chartData = history.map((h, i) => ({
    label: h.recordedAt ? new Date(h.recordedAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' }) : `Entry ${i + 1}`,
    price: Number(h.price)
  }))

  const addWishlist = async () => {
    if (!selected) return
    try { await api.addWishlist(1, selected.id); setNotice('Added to wishlist') }
    catch (e) { setNotice(e instanceof Error ? e.message : 'Could not add wishlist') }
  }

  const createAlert = async () => {
    if (!selected) return
    const value = window.prompt('Notify me when the price goes below (₹):', String(Math.max(1, Math.floor(lowest * .9))))
    if (!value) return
    const target = Number(value)
    if (!Number.isFinite(target) || target <= 0) { setNotice('Enter a valid target price'); return }
    try { await api.addAlert(1, selected.id, target); setNotice('Target price alert saved') }
    catch (e) { setNotice(e instanceof Error ? e.message : 'Could not save alert') }
  }

  const runSearch = (e: React.FormEvent) => { e.preventDefault(); void loadProducts(search) }

  return (
    <div className="app-shell">
      <aside className={`sidebar ${mobileMenu ? 'sidebar-open' : ''}`}>
        <div className="brand"><div className="brand-mark"><TrendingDown size={21}/></div><span>pricewise</span></div>
        <div className="side-label">WORKSPACE</div>
        <button className="nav-item active"><LayoutDashboard size={18}/> Overview</button>
        <button className="nav-item" onClick={() => document.getElementById('catalog')?.scrollIntoView({ behavior: 'smooth' })}><ShoppingBag size={18}/> Explore products</button>
        <button className="nav-item" onClick={() => setNotice('Wishlist is available through the saved-products API.') }><Heart size={18}/> My wishlist</button>
        <button className="nav-item" onClick={() => setNotice('Price alerts are created from a selected product.') }><Bell size={18}/> Price alerts</button>
        <div className="sidebar-bottom">
          <div className="help-card"><div className="help-icon">✦</div><strong>Shop smarter</strong><p>Compare offers across sellers and keep an eye on prices.</p></div>
          <div className="profile"><div className="avatar">DU</div><div><strong>Demo User</strong><small>Local preview</small></div><span className="online-dot"/></div>
        </div>
      </aside>

      <main className="main">
        <header className="topbar">
          <button className="mobile-toggle icon-button" onClick={() => setMobileMenu(!mobileMenu)}>{mobileMenu ? <X/> : <Menu/>}</button>
          <div className="breadcrumb">Workspace <ChevronRight size={14}/> <strong>Overview</strong></div>
          <div className="top-actions"><span className="status-pill"><i/> Demo data</span><button className="icon-button" onClick={() => void loadProducts(search)} aria-label="Refresh"><RefreshCw size={18}/></button><div className="avatar small">DU</div></div>
        </header>

        <section className="welcome">
          <div><div className="eyebrow">SMART SHOPPING STARTS HERE</div><h1>Find the best deal.<br/><span>Every time.</span></h1><p>Compare prices from different sellers, track price changes, and make confident buying decisions.</p></div>
          <div className="welcome-art"><div className="orbit orbit-one"/><div className="orbit orbit-two"/><div className="floating-tag tag-one"><TrendingDown size={16}/> Lowest price</div><div className="floating-product"><ShoppingBag size={54}/><span>₹</span></div><div className="floating-tag tag-two">PriceWise picks</div></div>
        </section>

        <section className="stats-grid">
          <div className="stat-card"><div className="stat-icon purple"><ShoppingBag size={19}/></div><span>Products tracked</span><strong>{products.length}</strong><small>In the demo catalog</small></div>
          <div className="stat-card"><div className="stat-icon green"><TrendingDown size={19}/></div><span>Best available price</span><strong>{lowest ? money(lowest) : '—'}</strong><small>{selected?.name || 'Select a product'}</small></div>
          <div className="stat-card"><div className="stat-icon orange"><SlidersHorizontal size={19}/></div><span>Potential price gap</span><strong>{savings ? money(savings) : '—'}</strong><small>Difference between sellers</small></div>
        </section>

        <section className="content-grid" id="catalog">
          <div className="catalog-panel panel">
            <div className="section-heading"><div><div className="eyebrow">DISCOVER</div><h2>Explore products</h2></div><span className="result-count">{products.length} items</span></div>
            <form className="searchbar" onSubmit={runSearch}><Search size={18}/><input value={search} onChange={e => setSearch(e.target.value)} placeholder="Search phones, laptops, brands..."/><button type="submit">Search</button></form>
            {error && <div className="error-banner">{error} <button onClick={() => void loadProducts(search)}>Retry</button></div>}
            {notice && <div className="notice-banner">{notice}<button onClick={() => setNotice('')}>×</button></div>}
            {loading ? <div className="empty-state">Loading products...</div> : products.length === 0 ? <div className="empty-state">No products found. Try another search.</div> :
              <div className="product-list">{products.map((product, index) => (
                <button className={`product-row ${selected?.id === product.id ? 'selected' : ''}`} key={product.id} onClick={() => void selectProduct(product)}>
                  <div className="product-thumb"><img src={product.imageUrl || fallbackImages[index % fallbackImages.length]} onError={e => { (e.currentTarget as HTMLImageElement).style.display = 'none' }} alt=""/></div>
                  <div className="product-info"><strong>{product.name}</strong><span>{product.brand || 'Popular product'} · {product.category?.categoryName || 'General'}</span><small>View seller comparison</small></div>
                  <ChevronRight size={18} className="row-arrow"/>
                </button>
              ))}</div>}
          </div>

          <div className="detail-column">
            <div className="panel comparison-panel">
              <div className="section-heading"><div><div className="eyebrow">PRICE SNAPSHOT</div><h2>Seller comparison</h2></div><span className="live-dot">● Available</span></div>
              {selected ? <>
                <div className="selected-product"><div><span className="selected-brand">{selected.brand || 'FEATURED PRODUCT'}</span><h3>{selected.name}</h3><p>{selected.description || 'Compare current listed prices from available sellers.'}</p></div><button className="heart-button" onClick={() => void addWishlist()} aria-label="Add to wishlist"><Heart size={19}/></button></div>
                <div className="best-price"><div><span>LOWEST LISTED PRICE</span><strong>{lowest ? money(lowest) : 'No prices yet'}</strong><small>{prices.length} seller listings available</small></div><div className="best-badge"><TrendingDown size={17}/> Best deal</div></div>
                <div className="seller-list">{prices.map((p, i) => <div className="seller-row" key={p.id}><div className="seller-logo">{p.seller?.sellerName?.slice(0, 1) || 'S'}</div><div className="seller-name"><strong>{p.seller?.sellerName}</strong><small>{p.availability || 'Availability unknown'}</small></div><strong className={p.price === lowest ? 'price-best' : 'seller-price'}>{money(Number(p.price))}</strong><a className="visit-link" href={p.productUrl || p.seller?.website || '#'} target="_blank" rel="noreferrer">Visit <ArrowUpRight size={14}/></a></div>)}</div>
                <button className="alert-button" onClick={() => void createAlert()}><Bell size={16}/> Set a target-price alert</button>
              </> : <div className="empty-state">Choose a product to compare prices.</div>}
            </div>

            <div className="panel chart-panel">
              <div className="section-heading"><div><div className="eyebrow">PRICE MOVEMENT</div><h2>Price history</h2></div><span className="chart-label">Recorded entries</span></div>
              {chartData.length > 0 ? <div className="chart-wrap"><ResponsiveContainer width="100%" height="100%"><LineChart data={chartData}><CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#edf0f5"/><XAxis dataKey="label" tick={{ fontSize: 11, fill: '#8b93a5' }} axisLine={false} tickLine={false}/><YAxis tick={{ fontSize: 11, fill: '#8b93a5' }} axisLine={false} tickLine={false} width={65} tickFormatter={v => `₹${Math.round(v / 1000)}k`}/><Tooltip formatter={(v) => money(Number(v))}/><Line type="monotone" dataKey="price" stroke="#6b5ce7" strokeWidth={3} dot={{ r: 4, fill: '#6b5ce7' }} activeDot={{ r: 6 }}/></LineChart></ResponsiveContainer></div> : <div className="chart-empty">Price history will appear here as prices are recorded.</div>}
              <div className="chart-foot"><span><i/> Historical price records</span><span>{history.length} records</span></div>
            </div>
          </div>
        </section>
        <footer>© 2026 PriceWise · Academic mini project · Prices shown are sample data</footer>
      </main>
      {mobileMenu && <button className="mobile-overlay" onClick={() => setMobileMenu(false)} aria-label="Close menu"/>}
    </div>
  )
}
