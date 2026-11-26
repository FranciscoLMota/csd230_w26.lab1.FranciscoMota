# API Design Document

> **Base URL:** `http://localhost:8080`
>
> Dates use ISO-8601 format, for example `2026-11-02T10:30:00`.

---

# Books

## Get All Books

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/books`  
**Data Expected:** Doesn't apply  
**Returns:** A list of books

### Sample Return

```json
[
  {
    "id": 1,
    "price": 19.99,
    "carts": [],
    "productType": "BookEntity",
    "title": "The Hobbit",
    "copies": 10,
    "author": "J.R.R. Tolkien",
    "isbn": "978-0547928227"
  },
  {
    "id": 2,
    "price": 14.99,
    "carts": [],
    "productType": "BookEntity",
    "title": "1984",
    "copies": 7,
    "author": "George Orwell",
    "isbn": "978-0451524935"
  }
]
```

## Get a Book by ID

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/books/{id}`  
**Data Expected:** A book ID in the URL, such as `1`  
**Returns:** The matching book  
**Possible Error:** `404 Not Found`

### Sample Return

```json
{
  "id": 1,
  "price": 19.99,
  "carts": [],
  "productType": "BookEntity",
  "title": "The Hobbit",
  "copies": 10,
  "author": "J.R.R. Tolkien",
  "isbn": "978-0547928227"
}
```

## Create a Book

**HTTP Method:** `POST`  
**Endpoint:** `http://localhost:8080/api/rest/books`  
**Data Expected:** A book as JSON  
**Returns:** The saved book, including its generated ID

### Sample Request Body

```json
{
  "title": "The Hobbit",
  "author": "J.R.R. Tolkien",
  "isbn": "978-0547928227",
  "price": 19.99,
  "copies": 10
}
```

### Sample Return

```json
{
  "id": 1,
  "price": 19.99,
  "carts": [],
  "productType": "BookEntity",
  "title": "The Hobbit",
  "copies": 10,
  "author": "J.R.R. Tolkien",
  "isbn": "978-0547928227"
}
```

## Replace a Book by ID

**HTTP Method:** `PUT`  
**Endpoint:** `http://localhost:8080/api/rest/books/{id}`  
**Data Expected:** A book ID in the URL and updated book data as JSON  
**Returns:** The updated book. If the ID does not exist, a new book is saved with that ID.

### Sample Request Body

```json
{
  "title": "The Hobbit: Revised Edition",
  "author": "J.R.R. Tolkien",
  "price": 24.99,
  "copies": 5
}
```

### Sample Return

```json
{
  "id": 1,
  "price": 24.99,
  "carts": [],
  "productType": "BookEntity",
  "title": "The Hobbit: Revised Edition",
  "copies": 5,
  "author": "J.R.R. Tolkien",
  "isbn": "978-0547928227"
}
```

> **Note:** The current PUT implementation does not update `isbn` when the book already exists.

## Delete a Book by ID

**HTTP Method:** `DELETE`  
**Endpoint:** `http://localhost:8080/api/rest/books/{id}`  
**Data Expected:** A book ID in the URL  
**Returns:** An empty response body

### Sample Return

```text
Empty response body
```

---

# Disc Magazines

## Get All Disc Magazines

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/discmags`  
**Data Expected:** Doesn't apply  
**Returns:** A list of disc magazines

### Sample Return

```json
[
  {
    "id": 3,
    "price": 16.99,
    "carts": [],
    "productType": "DiscMagEntity",
    "title": "Tech Monthly with Disc",
    "copies": 15,
    "orderQty": 4,
    "currentIssue": "2026-11-02T10:30:00",
    "hasDisc": true
  },
  {
    "id": 4,
    "price": 12.99,
    "carts": [],
    "productType": "DiscMagEntity",
    "title": "Gaming Weekly with Disc",
    "copies": 8,
    "orderQty": 3,
    "currentIssue": "2026-11-09T10:30:00",
    "hasDisc": true
  }
]
```

## Get a Disc Magazine by ID

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/discmags/{id}`  
**Data Expected:** A disc magazine ID in the URL  
**Returns:** The matching disc magazine  
**Possible Error:** `404 Not Found`

### Sample Return

```json
{
  "id": 3,
  "price": 16.99,
  "carts": [],
  "productType": "DiscMagEntity",
  "title": "Tech Monthly with Disc",
  "copies": 15,
  "orderQty": 4,
  "currentIssue": "2026-11-02T10:30:00",
  "hasDisc": true
}
```

## Create a Disc Magazine

**HTTP Method:** `POST`  
**Endpoint:** `http://localhost:8080/api/rest/discmags`  
**Data Expected:** A disc magazine as JSON  
**Returns:** The saved disc magazine, including its generated ID

### Sample Request Body

```json
{
  "title": "Tech Monthly with Disc",
  "price": 16.99,
  "copies": 15,
  "orderQty": 4,
  "currentIssue": "2026-11-02T10:30:00",
  "hasDisc": true
}
```

### Sample Return

```json
{
  "id": 3,
  "price": 16.99,
  "carts": [],
  "productType": "DiscMagEntity",
  "title": "Tech Monthly with Disc",
  "copies": 15,
  "orderQty": 4,
  "currentIssue": "2026-11-02T10:30:00",
  "hasDisc": true
}
```

## Replace a Disc Magazine by ID

**HTTP Method:** `PUT`  
**Endpoint:** `http://localhost:8080/api/rest/discmags/{id}`  
**Data Expected:** A disc magazine ID in the URL and updated data as JSON  
**Returns:** The updated disc magazine. If the ID does not exist, a new one is saved with that ID.

### Sample Request Body

```json
{
  "title": "Tech Monthly: Holiday Edition",
  "price": 18.99,
  "copies": 12,
  "orderQty": 6,
  "currentIssue": "2026-12-01T09:00:00",
  "hasDisc": false
}
```

### Sample Return

```json
{
  "id": 3,
  "price": 18.99,
  "carts": [],
  "productType": "DiscMagEntity",
  "title": "Tech Monthly: Holiday Edition",
  "copies": 12,
  "orderQty": 6,
  "currentIssue": "2026-12-01T09:00:00",
  "hasDisc": false
}
```

## Delete a Disc Magazine by ID

**HTTP Method:** `DELETE`  
**Endpoint:** `http://localhost:8080/api/rest/discmags/{id}`  
**Data Expected:** A disc magazine ID in the URL  
**Returns:** An empty response body

### Sample Return

```text
Empty response body
```

---

# Magazines

## Get All Magazines

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/magazines`  
**Data Expected:** Doesn't apply  
**Returns:** A list of magazines

### Sample Return

```json
[
  {
    "id": 5,
    "price": 9.99,
    "carts": [],
    "productType": "MagazineEntity",
    "title": "Science Monthly",
    "copies": 30,
    "orderQty": 10,
    "currentIssue": "2026-11-02T10:30:00"
  },
  {
    "id": 6,
    "price": 11.99,
    "carts": [],
    "productType": "MagazineEntity",
    "title": "World Travel",
    "copies": 20,
    "orderQty": 5,
    "currentIssue": "2026-11-15T09:00:00"
  }
]
```

## Get a Magazine by ID

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/magazines/{id}`  
**Data Expected:** A magazine ID in the URL  
**Returns:** The matching magazine  
**Possible Error:** `404 Not Found`

### Sample Return

```json
{
  "id": 5,
  "price": 9.99,
  "carts": [],
  "productType": "MagazineEntity",
  "title": "Science Monthly",
  "copies": 30,
  "orderQty": 10,
  "currentIssue": "2026-11-02T10:30:00"
}
```

## Create a Magazine

**HTTP Method:** `POST`  
**Endpoint:** `http://localhost:8080/api/rest/magazines`  
**Data Expected:** A magazine as JSON  
**Returns:** The saved magazine, including its generated ID

### Sample Request Body

```json
{
  "title": "Science Monthly",
  "price": 9.99,
  "copies": 30,
  "orderQty": 10,
  "currentIssue": "2026-11-02T10:30:00"
}
```

### Sample Return

```json
{
  "id": 5,
  "price": 9.99,
  "carts": [],
  "productType": "MagazineEntity",
  "title": "Science Monthly",
  "copies": 30,
  "orderQty": 10,
  "currentIssue": "2026-11-02T10:30:00"
}
```

## Replace a Magazine by ID

**HTTP Method:** `PUT`  
**Endpoint:** `http://localhost:8080/api/rest/magazines/{id}`  
**Data Expected:** A magazine ID in the URL and updated magazine data as JSON  
**Returns:** The updated magazine. If the ID does not exist, a new magazine is saved with that ID.

### Sample Request Body

```json
{
  "title": "Science Monthly: Winter Edition",
  "price": 11.99,
  "copies": 25,
  "orderQty": 8,
  "currentIssue": "2026-12-01T09:00:00"
}
```

### Sample Return

```json
{
  "id": 5,
  "price": 11.99,
  "carts": [],
  "productType": "MagazineEntity",
  "title": "Science Monthly: Winter Edition",
  "copies": 25,
  "orderQty": 8,
  "currentIssue": "2026-12-01T09:00:00"
}
```

## Delete a Magazine by ID

**HTTP Method:** `DELETE`  
**Endpoint:** `http://localhost:8080/api/rest/magazines/{id}`  
**Data Expected:** A magazine ID in the URL  
**Returns:** An empty response body

### Sample Return

```text
Empty response body
```

---

# Tickets

## Get All Tickets

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/tickets`  
**Data Expected:** Doesn't apply  
**Returns:** A list of tickets

### Sample Return

```json
[
  {
    "id": 7,
    "price": 49.99,
    "carts": [],
    "productType": "TicketEntity",
    "description": "Concert admission ticket"
  },
  {
    "id": 8,
    "price": 24.99,
    "carts": [],
    "productType": "TicketEntity",
    "description": "Museum admission ticket"
  }
]
```

## Get a Ticket by ID

**HTTP Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/rest/tickets/{id}`  
**Data Expected:** A ticket ID in the URL  
**Returns:** The matching ticket  
**Possible Error:** `404 Not Found`

### Sample Return

```json
{
  "id": 7,
  "price": 49.99,
  "carts": [],
  "productType": "TicketEntity",
  "description": "Concert admission ticket"
}
```

## Create a Ticket

**HTTP Method:** `POST`  
**Endpoint:** `http://localhost:8080/api/rest/tickets`  
**Data Expected:** A ticket as JSON  
**Returns:** The saved ticket, including its generated ID

### Sample Request Body

```json
{
  "description": "Concert admission ticket",
  "price": 49.99
}
```

### Sample Return

```json
{
  "id": 7,
  "price": 49.99,
  "carts": [],
  "productType": "TicketEntity",
  "description": "Concert admission ticket"
}
```

## Replace a Ticket by ID

**HTTP Method:** `PUT`  
**Endpoint:** `http://localhost:8080/api/rest/tickets/{id}`  
**Data Expected:** A ticket ID in the URL and updated ticket data as JSON  
**Returns:** The updated ticket. If the ID does not exist, a new ticket is saved with that ID.

### Sample Request Body

```json
{
  "description": "VIP concert admission ticket",
  "price": 79.99
}
```

### Sample Return

```json
{
  "id": 7,
  "price": 79.99,
  "carts": [],
  "productType": "TicketEntity",
  "description": "VIP concert admission ticket"
}
```

## Delete a Ticket by ID

**HTTP Method:** `DELETE`  
**Endpoint:** `http://localhost:8080/api/rest/tickets/{id}`  
**Data Expected:** A ticket ID in the URL  
**Returns:** An empty response body

### Sample Return

```text
Empty response body
```
