# Restaurant Backend API 🍽️

Spring Boot tabanlı Restaurant Backend projesi.  
Rezervasyon, menü ve kariyer süreçlerini yöneten RESTful bir backend mimarisi sunar.

Proje, katmanlı mimari ve Maven multi-module yapısı kullanılarak geliştirilmiştir.

---

## ✨ Özellikler

- Rezervasyon Yönetimi
- Menü ve Kategori Yönetimi
- Kariyer / İş Başvuruları
- JWT Authentication
- Spring Security
- PostgreSQL + JPA (Hibernate)
- Cloudinary Dosya & Görsel Yükleme
- Swagger (OpenAPI)
- Katmanlı Mimari (Controller / Service / Repository)

---

## 🧱 Proje Yapısı (Multi-Module)

restaurant-backend  
├── core  
│   └── ortak yardımcı sınıflar, altyapı yapıları  
├── entities  
│   └── JPA Entity sınıfları  
├── repositories  
│   └── Spring Data JPA repository’leri  
├── business  
│   └── Service katmanı, iş kuralları  
└── webapi  
    └── Controller’lar, config, security, main application  

---

## 🛠️ Kullanılan Teknolojiler

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Cloudinary
- Swagger / OpenAPI

---

## ✅ Gereksinimler

- Java 17 veya üzeri
- Maven
- PostgreSQL
- (Opsiyonel) pgAdmin

---

## ⚙️ Kurulum ve Çalıştırma

### Projeyi Klonla

git clone https://github.com/VedatYalcinkaya/restaurant-backend.git  
cd restaurant-backend  

---

### PostgreSQL Veritabanı

- Database: restaurant_db  
- Username: postgres  
- Password: your_password  

---

### Application Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db  
spring.datasource.username=postgres  
spring.datasource.password=your_password  

spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  

app.jwt.secret=CHANGE_THIS_SECRET  
app.jwt.expiration=86400000  

cloudinary.cloud-name=YOUR_CLOUD_NAME  
cloudinary.api-key=YOUR_API_KEY  
cloudinary.api-secret=YOUR_API_SECRET  

---

### Build

mvn clean install  

---

### Çalıştırma

java -jar webapi/target/*.jar  

veya IDE üzerinden webapi modülündeki Spring Boot main class çalıştırılabilir.

---

## 📚 Swagger / OpenAPI

http://localhost:8080/swagger-ui/index.html  

---

## 🔐 JWT Authentication

Authorization header kullanımı:

Authorization: Bearer YOUR_TOKEN  

---

## 🧪 Test

- Swagger UI
- Postman

---

## 🚀 Deployment

- application-prod.properties dosyasını repoya ekleme
- Ortam değişkenleri kullanılması önerilir
- Nginx + systemd ile servis olarak çalıştırılabilir

---

## 🤝 Katkı

Issue ve Pull Request’ler açıktır.

---

## 📄 Lisans

Henüz lisans eklenmemiştir.  
MIT veya Apache 2.0 lisansı eklenebilir.

---

## 👤 Geliştirici

Vedat Yalçınkaya  
GitHub: https://github.com/VedatYalcinkaya
