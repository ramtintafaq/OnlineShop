# Online Shop Web Application

## Project Description
This project is an online shopping platform built using **Spring Boot**. The platform allows users to browse products, add them to their shopping cart, and checkout. The checkout process clears the cart, and users can manage their profiles and past purchases. Admins can manage products, view sales, and create product listings. Additionally, users can reset their passwords via email through the **Forgot Password** feature.

To enhance performance and scalability, this application leverages **Redis** for caching frequently accessed data such as product listings, brands, and shopping cart information.

## Features

### User:
- Sign up and log in to the platform.
- Browse products and filter by categories and price range.
- Add products to the shopping cart.
- View and manage items in the shopping cart.
- Checkout to clear the shopping cart.
- View past purchases.
- **Forgot Password**: Request a password reset link via email.
- **Password Reset**: Change password securely via the reset link sent to your email.

### Admin:
- Create and manage product listings.
- View sales reports and track the number of products sold.
- Admins can only manage products they created.

### General:
- Product search functionality.
- REST API for product management and shopping cart operations.
- Local file storage for product images.

## Technologies Used
- **Java 22**
- **Spring Boot 3.0**
- **Spring Data JPA** for data persistence
- **MySQL** for database management
- **Spring Security** for authentication and role-based access
- **Spring Email** for sending password reset emails
- **Redis** for caching frequently accessed data
- **Maven** for dependency management
- **Local File Storage** for handling product images

## Installation and Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/ramtintafaq/OnlineShop.git
    cd OnlineShop
    ```

2. **Configure the database, Redis, and email settings**:
   - Edit the `src/main/resources/application.properties` file with your MySQL, Redis, and email credentials:
     ```properties
     # Database Configuration
     spring.datasource.url=jdbc:mysql://localhost:3306/online_shop_db
     spring.datasource.username=shopuser
     spring.datasource.password=shoppass
     spring.jpa.hibernate.ddl-auto=update

     # Redis Configuration
     spring.cache.type=redis
     spring.redis.host=localhost
     spring.redis.port=6379

     # Email Settings for Password Reset
     spring.mail.host=smtp.gmail.com
     spring.mail.port=587
     spring.mail.username=your_email@gmail.com
     spring.mail.password=your_email_password
     spring.mail.properties.mail.smtp.auth=true
     spring.mail.properties.mail.smtp.starttls.enable=true
     ```

3. **Run Redis Server (if not already running)**:
    ```bash
    redis-server
    ```

4. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

5. **Access the application**:
   Open a browser and navigate to `http://localhost:8080`.

## Usage

### For Users:
- Sign up and log in to browse products.
- Add products to your shopping cart and manage cart items.
- Checkout to clear the cart.
- If you forget your password, use the **Forgot Password** feature to request a reset link.
- Change your password via the link sent to your registered email.

### For Admins:
- Log in to create and manage product listings.
- Track product sales and manage inventory.

## Contribution
If you'd like to contribute to this project, feel free to open an issue or submit a pull request.

## Contact
For any questions or feedback, reach out to me via [LinkedIn](https://www.linkedin.com/in/ramtintafaghodi/).
