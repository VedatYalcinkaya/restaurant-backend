# Ala Söğüş Backend

Bu proje, Ala Söğüş restoranı için geliştirilen backend uygulamasıdır.

## Proje Yapısı

Proje, Maven multi-module yapısı ile geliştirilmiştir:
- `core`: Temel bileşenler ve ortak utility sınıfları
- `entities`: Veritabanı varlıkları
- `repositories`: Veritabanı işlemleri
- `business`: İş mantığı
- `webapi`: REST API katmanı

## Yapılandırma

Projede üç ana yapılandırma dosyası bulunur:

1. `application.properties`: Genel yapılandırma ayarları
2. `application-dev.properties`: Geliştirme ortamı ayarları
3. `application-prod.properties`: Üretim ortamı ayarları

## Geliştirme Ortamı Kurulumu

1. Projeyi klonlayın
2. `application.properties` dosyasındaki `spring.profiles.active` değerini gerekirse `dev` olarak değiştirin
3. Maven ile projeyi derleyin: `mvn clean install`
4. Uygulamayı çalıştırın: `java -jar webapi/target/ala-sogus-api.jar`

## Production Ortamı Kurulumu

1. Projeyi derleyin: `mvn clean package -DskipTests`
2. `webapi/target/ala-sogus-api.jar` dosyasını sunucuya yükleyin
3. Gerekli production konfigürasyonlarını sağlayın
4. `application.properties` dosyasında `spring.profiles.active=prod` olduğundan emin olun
5. Uygulamayı çalıştırın: `java -jar ala-sogus-api.jar`

## Dikkat Edilmesi Gerekenler

- Production secret değerlerini repoya commit etmeyin
- Geliştirme yaparken uygun profili kullanın
- CORS, iletişim alıcısı ve OpenAPI production URL ayarlarını environment değişkenleriyle özelleştirebilirsiniz
