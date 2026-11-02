# PriceWise

A full-stack e-commerce price comparison MVP built with React + TypeScript + Vite, Spring Boot, and MySQL.

## Included features
- Product catalog, search, and category filtering
- Seller management
- Multiple seller prices per product
- Lowest-price comparison
- Price history recording when an admin updates a seller price
- Wishlist endpoints and UI
- Basic target-price alert creation and listing
- Admin-style product/seller/price management UI (demo project; secure admin authentication is not included yet)

## Requirements
- Java 21
- Maven 3.9+ (or use the Maven wrapper if generated locally)
- Node.js 20+
- MySQL 8+

## 1. Database setup

Run the SQL script:

```bash
mysql -u root -p < database/pricewise.sql
```

This creates `pricewise_db`, tables, and sample categories, sellers, products, and prices.

## 2. Backend setup

Set environment variables in PowerShell:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

Then:

```bash
cd backend
mvn spring-boot:run
```

Backend URL: `http://localhost:8080`

Health endpoint: `GET http://localhost:8080/api/health`

If you prefer IntelliJ, open `backend/pom.xml` as a Maven project and run `PricewiseApplication`.

## 3. Frontend setup

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the Vite URL printed in the terminal (usually `http://localhost:5173`).

The frontend expects the backend at `http://localhost:8080/api`. Change `VITE_API_BASE_URL` in `frontend/.env` if needed.

## Main API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/products` | List/search products (`?search=&categoryId=`) |
| GET | `/api/products/{id}` | Product details |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/sellers` | List sellers |
| POST | `/api/sellers` | Create seller |
| GET | `/api/products/{id}/prices` | Seller prices for product |
| POST | `/api/prices` | Add/update seller price; records price history |
| GET | `/api/products/{id}/history` | Price history |
| GET | `/api/categories` | Categories |
| GET | `/api/wishlist/{userId}` | Wishlist for demo user ID |
| POST | `/api/wishlist` | Add product to wishlist |
| DELETE | `/api/wishlist/{wishlistId}` | Remove wishlist item |
| GET | `/api/alerts/{userId}` | List target-price alerts |
| POST | `/api/alerts` | Create target-price alert |

## Important project notes
- This is a local academic MVP. It uses manually maintained seller prices and does not scrape Amazon, Flipkart, or Croma.
- Wishlist and alert APIs use a `userId` to keep the MVP straightforward. Add JWT authentication and ownership checks before real deployment.
- Price alerts are stored; automatic email/SMS delivery is a future enhancement.
- The sample product/seller prices are illustrative demo data, not live retail prices.
- Never commit real database passwords or API keys.

## Folder structure

```text
PriceWise/
├── backend/
├── frontend/
├── database/
└── documentation/
```
