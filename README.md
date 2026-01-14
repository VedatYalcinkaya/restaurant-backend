1	# Restaurant Backend API
     2	
     3	Bu depo, restoran işletmeleri için menü yönetimi ve rezervasyon süreçlerini yöneten Spring Boot tabanlı bir backend uygulamasıdır.
     4	
     5	## Özellikler
     6	
     7	- Menü kategorileri ve menü öğeleri yönetimi
     8	- Menü öğeleri için görsel yükleme desteği
     9	- Rezervasyon oluşturma, güncelleme ve durum yönetimi
    10	- Tarih, durum, müşteri e-posta/telefon gibi kriterlerle rezervasyon sorgulama
    11	
    12	## Proje Yapısı
    13	
    14	Maven multi-module mimarisi kullanılmıştır:
    15	
    16	- `core`: Ortak yardımcılar ve altyapı bileşenleri
    17	- `entities`: JPA varlıkları ve enumlar
    18	- `repositories`: Veri erişim katmanı
    19	- `business`: İş kuralları ve servisler
    20	- `webapi`: REST API katmanı
    21	
    22	## Teknolojiler
    23	
    24	- Java 17
    25	- Spring Boot 3
    26	- Maven
    27	- PostgreSQL / MySQL (runtime sürücüleri)
    28	
    29	## Kurulum ve Çalıştırma
    30	
    31	1. Depoyu klonlayın.
    32	2. `webapi/src/main/resources/application.properties` içinde gerekli ayarları yapın.
    33	3. Geliştirme ortamı için `application-dev.properties` dosyasındaki değerleri güncelleyin.
    34	4. Derleme:
    35	   ```bash
    36	   mvn clean install
    37	   ```
    38	5. Çalıştırma:
    39	   ```bash
    40	   java -jar webapi/target/demirci-api.jar
    41	   ```
    42	
    43	## Konfigürasyon Notları
    44	
    45	- `application-dev.properties` geliştirme ortamı için örnek ayarları içerir.
    46	- Üretim ortamı için ayrı bir konfigürasyon dosyası hazırlayıp `spring.profiles.active` ile seçebilirsiniz.
    47	
    48	## API Dokümantasyonu
    49	
    50	Uygulama çalışırken Swagger arayüzü üzerinden API uç noktalarını inceleyebilirsiniz:
    51	
    52	- `http://localhost:8080/swagger-ui/index.html`
