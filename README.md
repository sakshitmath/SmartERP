# 🏢 SmartERP — Cloud ERP System

A cloud-based, web-based Enterprise Resource Planning system for Indian businesses covering accounting, inventory, GST, billing and financial reporting.

---

## 🚀 Live Demo

| | Link |
|---|---|
| 🔵 Backend API | Coming soon |
| 🟢 Frontend | Coming soon |
| 📋 Swagger Docs | [Backend URL]/swagger-ui.html |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (Amazon Corretto) |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT Authentication |
| Database | PostgreSQL + Hibernate JPA |
| ORM | Spring Data JPA |
| PDF Generation | iText PDF 7 |
| Excel Export | Apache POI |
| API Documentation | Swagger OpenAPI 3.0 |
| Frontend | React.js + Tailwind CSS + ShadCN UI |
| Deployment | Railway (Backend) + Vercel (Frontend) |
| Build Tool | Maven |

---

## 📌 Core Features

- 🔐 JWT Authentication — Register, Login, Protected Routes
- 🏢 Multi-Company Management (up to 5 companies per user)
- 📒 Ledger Management — Customer, Supplier, Bank, Cash
- 📦 Inventory & Stock Management with auto quantity update
- 🧾 Purchase & Sales Vouchers with double-entry accounting
- 💰 GST Auto-calculation — CGST / SGST / IGST
- 📄 PDF Invoice Generation (iText)
- 📊 Financial Reports — Balance Sheet, P&L, Trial Balance
- ⌨️ Keyboard-first Navigation (Tally-style shortcuts)
- 📋 Swagger API Documentation

---

## 📁 Project Structure
SmartERP/

├── smarterp-backend/

│   ├── src/main/java/com/smarterp/

│   │   ├── controllers/        # REST API Controllers

│   │   ├── services/           # Business Logic

│   │   ├── repositories/       # Database Layer

│   │   ├── entities/           # JPA Entities

│   │   ├── dto/

│   │   │   ├── request/        # Incoming request models

│   │   │   └── response/       # Outgoing response models

│   │   ├── security/           # JWT Filter + Utility

│   │   ├── configuration/      # Spring Security Config

│   │   ├── exceptions/         # Global Exception Handler

│   │   ├── reports/            # PDF + Excel generators

│   │   ├── gst/                # GST calculation engine

│   │   └── utilities/          # Helper classes

│   ├── src/main/resources/

│   │   └── application.properties

│   └── pom.xml

├── smarterp-frontend/

└── README.md
---

## 🗄️ Database Tables

| Table | Purpose |
|---|---|
| users | Login accounts |
| companies | Business entities |
| ledger_groups | Accounting groups (Assets, Liabilities, Income, Expenses) |
| ledgers | Customer / Supplier / Bank / Cash accounts |
| stock_items | Inventory items with HSN, GST rate, quantity |
| vouchers | Sales / Purchase / Payment / Receipt / Journal |
| voucher_entries | Double-entry debit/credit records |
| invoices | GST Invoices with line items |
| invoice_line_items | Individual items per invoice |
| gst_records | GST transaction records |
| inventory_transactions | Stock IN / OUT history |
| audit_logs | Complete activity tracking |

---

## 🔗 API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Company
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/companies | Create company |
| GET | /api/companies | Get all companies |
| GET | /api/companies/{id} | Get company by ID |
| PUT | /api/companies/{id} | Update company |
| DELETE | /api/companies/{id} | Soft delete company |

### Ledger
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/ledger-groups | Create ledger group |
| GET | /api/ledger-groups/company/{id} | Get all ledger groups |
| POST | /api/ledgers | Create ledger |
| GET | /api/ledgers/company/{id} | Get all ledgers |
| GET | /api/ledgers/{id} | Get ledger by ID |
| PUT | /api/ledgers/{id} | Update ledger |
| DELETE | /api/ledgers/{id} | Soft delete ledger |

### Inventory
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/stock-items | Create stock item |
| GET | /api/stock-items/company/{id} | Get all stock items |
| GET | /api/stock-items/{id} | Get stock item by ID |
| PUT | /api/stock-items/{id} | Update stock item |
| DELETE | /api/stock-items/{id} | Soft delete stock item |

### Vouchers
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/vouchers/purchase | Create purchase voucher |
| POST | /api/vouchers/sales | Create sales voucher |
| POST | /api/vouchers/payment | Create payment voucher |
| POST | /api/vouchers/receipt | Create receipt voucher |
| GET | /api/vouchers/company/{id} | Get all vouchers |

### Reports
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/reports/balance-sheet/{companyId} | Balance Sheet |
| GET | /api/reports/profit-loss/{companyId} | Profit & Loss |
| GET | /api/reports/trial-balance/{companyId} | Trial Balance |
| GET | /api/reports/stock-summary/{companyId} | Stock Summary |
| GET | /api/reports/gst-summary/{companyId} | GST Summary |

---

## ⚙️ Local Setup

### Prerequisites
- Java 21
- PostgreSQL
- Maven

### Steps
```bash
# Clone the repo
git clone https://github.com/sakshitmath/SmartERP.git
cd SmartERP/smarterp-backend

# Create database
psql -U postgres -c "CREATE DATABASE smarterp_db;"

# Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smarterp_db
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8081

# Run
./mvnw spring-boot:run
```

App runs on: `http://localhost:8081`
Swagger UI: `http://localhost:8081/swagger-ui.html`

---

## ⌨️ Keyboard Shortcuts

### Global
| Key | Action |
|---|---|
| F1 | Company Selection |
| F2 | Change Financial Year |
| ESC | Go Back |
| Ctrl+H | Dashboard |
| Ctrl+Q | Logout |
| Ctrl+K | Global Search |

### Vouchers
| Key | Action |
|---|---|
| F5 | Payment Voucher |
| F6 | Receipt Voucher |
| F7 | Journal Voucher |
| F8 | Sales Voucher |
| F9 | Purchase Voucher |
| Alt+F8 | Credit Note |
| Alt+F9 | Debit Note |

### Masters
| Key | Action |
|---|---|
| Alt+L | Create Ledger |
| Alt+S | Create Stock Item |
| Alt+G | Create Group |

### Reports
| Key | Action |
|---|---|
| Alt+B | Balance Sheet |
| Alt+P | Profit & Loss |
| Alt+T | Trial Balance |
| Alt+R | Stock Summary |
| Alt+X | GST Reports |

### Billing
| Key | Action |
|---|---|
| Ctrl+B | Create Invoice |
| Ctrl+P | Print Invoice |
| Ctrl+Shift+P | Export PDF |

---

## 👩‍💻 Developer

**Sakshi Torgalmath**
Java Full Stack Developer | B.E. CSE 2025 | CGPA 8.5

Stack: Java 21 · Spring Boot 3 · PostgreSQL · React.js · JWT · Hibernate · REST APIs · Git
