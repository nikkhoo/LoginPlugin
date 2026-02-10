# LoginPlugin

A secure login plugin for Paper Minecraft servers using MySQL database and BCrypt password hashing.

## Features

- ✅ User registration with password confirmation
- ✅ Secure BCrypt password hashing
- ✅ MySQL database integration
- ✅ Connection pooling with HikariCP
- ✅ Async database operations
- ✅ Login/logout tracking
- ✅ Player account management

## Requirements

- Paper 1.20.4 or higher
- Java 17 or higher
- MySQL 5.7 or higher
- Maven (for building)

## Installation

1. Clone or download this repository
2. Build the plugin: `mvn clean package`
3. Copy the JAR file from `target/` to your server's `plugins/` folder
4. Restart your server
5. Edit `plugins/LoginPlugin/config.yml` with your MySQL credentials
6. Restart your server again

## Configuration

Edit `config.yml` in the `plugins/LoginPlugin/` folder:

```yaml
database:
  host: localhost
  port: 3306
  database: minecraft
  user: root
  password: ""
```

## Commands

### Player Commands

- `/register <password> <confirm-password>` - Register a new account
- `/login <password>` - Login to your account
- `/logout` - Logout from your account

## Security

- Passwords are hashed using BCrypt with cost factor of 12
- Password hashes are never logged or exposed
- Database connections are pooled for performance
- All database operations are asynchronous

## Database Schema

The plugin automatically creates a `players` table with the following structure:

```sql
CREATE TABLE `players` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `uuid` VARCHAR(36) UNIQUE NOT NULL,
  `username` VARCHAR(16) UNIQUE NOT NULL,
  `password_hash` VARCHAR(60) NOT NULL,
  `last_login` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_logged_in` BOOLEAN DEFAULT FALSE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## Development

### Project Structure

```
LoginPlugin/
├── pom.xml
├── plugin.yml
├── src/main/java/com/example/loginplugin/
│   ├── LoginPlugin.java          (Main plugin class)
│   ├── database/
│   │   ├── DatabaseManager.java  (Database connection management)
│   │   └── PlayerManager.java    (Player account operations)
│   ├── commands/
│   │   ├── LoginCommand.java
│   │   ├── RegisterCommand.java
│   │   └── LogoutCommand.java
│   ├── listeners/
│   │   ├── PlayerJoinListener.java
│   │   └── PlayerQuitListener.java
│   └── util/
│       └── PasswordUtil.java     (Password hashing and verification)
└── src/main/resources/
    └── config.yml
```

## Building

```bash
mvn clean package
```

The compiled JAR will be in the `target/` folder.

## License

MIT License

## Support

For issues and feature requests, please open an issue on GitHub.