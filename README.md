# OrderKing Frontend

Desktop frontend application for the **OrderKing Customer Management System** built using **Java Swing**. The application communicates with the backend REST API and supports authentication and customer management functionalities.

## Features

The application provides the following functionalities:

### Authentication

#### Login Screen
Allows users to:

- Login using email and password
- Authenticate against the backend API
- Receive and store JWT token
- Access protected customer management screens

#### Register Screen
Allows users to:

- Create a new account
- Register through the backend API

---

### Customer Management

#### Display All Customers
Displays all customers using the **Get Customers API**.

Features:
- Retrieve customers from backend
- Paginated customer listing
- View customer details

---

#### Add Customer
Allows users to:

- Add a new customer
- Submit customer information through API

Fields:
- Name
- Email
- Phone
- Password

---

#### Edit Customer
Allows users to:

- Select an existing customer
- Edit customer details
- Update customer information using API

---

#### Delete Customer
Allows users to:

- Select a customer
- Delete customer from the system

---

## Tech Stack

- **Java**
- **Java Swing**
- **HTTPURLConnection**
- **Gson**
- **JWT Authentication**
- **REST API Integration**

---

## Project Structure

The project follows a clean and maintainable structure.

```text
src
│── api
│     └── ApiClient.java
│
│── model
│     └── Customer.java
│
│── ui
│     ├── CustomerFormDialog.java
│     ├── CustomerTablePanel.java
│     └── MainFrame.java
```

### Folder Responsibilities

#### api
Contains API communication logic.

Example:
- `ApiClient.java`

Responsibilities:
- Login
- Register
- Get customers
- Get customer by ID
- Create customer
- Update customer
- Delete customer
- JWT token handling
- API request handling

---

#### model
Contains application models.

Example:
- `Customer.java`

Responsibilities:
- Represents customer data
- Maps API response objects

---

#### ui
Contains all application screens and UI components.

Examples:
- `MainFrame.java`
- `CustomerTablePanel.java`
- `CustomerFormDialog.java`

Responsibilities:
- Customer table display
- Add customer form
- Edit customer form
- Customer deletion
- Application navigation

---

## Implemented Features

### Authentication APIs

```http
POST /auth/register
POST /auth/login
```

---

### Customer APIs

```http
GET /customers
GET /customers/{id}
POST /customers
PUT /customers/{id}
DELETE /customers/{id}
```

---

## JWT Authentication

The application uses **JWT Authentication**.

After successful login:

- JWT token is retrieved from backend
- Token is stored in `ApiClient`
- Protected requests automatically send:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## Pagination

Customer listing supports **pagination** using backend API:

Example:

```http
GET /customers?page=0&size=10
```

---

## Backend Integration

The application connects to:

```text
http://localhost:8080
```

API requests are handled inside:

```text
ApiClient.java
```

---

## How to Run the Application

### Prerequisites

Before running the application, make sure you have installed:

- **Java 21**
- **Maven**
- **OrderKing Backend API running on port 8080**

---

### 1. Clone Repository

```bash
git clone YOUR_FRONTEND_REPOSITORY_URL
cd YOUR_PROJECT_NAME
```

---

### 2. Install Dependencies

Run:

```bash
mvn clean install
```

---

### 3. Start Backend Server

Make sure the backend API is running:

```text
http://localhost:8080
```

---

### 4. Run the Application

Run:

```bash
mvn exec:java
```

Or open the project in IntelliJ IDEA and run:

```text
MainFrame.java
```

or your application's main entry class.

---

## Application Workflow

1. User registers or logs in  
2. User accesses customer management screen  
3. Customers are fetched using API  
4. User can:
   - View customers
   - Add customer
   - Edit customer
   - Delete customer

---

## Implemented Features

✅ Login Screen  
✅ Register Screen  
✅ Display All Customers  
✅ Add Customer Form  
✅ Edit Customer Form  
✅ Delete Customer  
✅ JWT Authentication  
✅ Backend REST API Integration  
✅ Pagination Support  
✅ Customer Search Support  
✅ Clean Project Structure
