# Demirci Yazılım Backend

Bu proje, Demirci Yazılım şirketi için geliştirilen web sitesinin backend uygulamasıdır.

## Proje Yapısı

Proje, Maven multi-module yapısı ile geliştirilmiştir:
- `core`: Temel bileşenler, utility sınıfları
- `entities`: Veritabanı varlıkları
- `repositories`: Veritabanı işlemleri
- `business`: İş mantığı
- `webapi`: REST API ve web arayüzü

## Yapılandırma

Projede üç ana yapılandırma dosyası bulunmaktadır:

1. `application.properties`: Genel yapılandırma ayarları (GitHub'da paylaşılır)
2. `application-dev.properties`: Geliştirme ortamı için örnek konfigürasyon (GitHub'da paylaşılır)
3. `application-prod.properties`: Üretim ortamı için gerçek gizli bilgiler (GitHub'a yüklenmez)

## Geliştirme Ortamı Kurulumu

1. Projeyi klonlayın
2. `application.properties` dosyasındaki `spring.profiles.active` değerini `dev` olarak değiştirin
3. Maven ile projeyi derleyin: `mvn clean install`
4. Uygulamayı çalıştırın: `java -jar webapi/target/demirci-api.jar`

## Production Ortamı Kurulumu

1. Projeyi derleyin: `mvn clean package -DskipTests`
2. `webapi/target/demirci-api.jar` dosyasını sunucuya yükleyin
3. `application-prod.properties` dosyasını aynı dizine yükleyin
4. `application.properties` dosyasında `spring.profiles.active=prod` olduğundan emin olun
5. Uygulamayı çalıştırın: `java -jar demirci-api.jar`

## Dikkat Edilmesi Gerekenler

- `application-prod.properties` dosyasını asla GitHub'a yüklemeyin!
- Geliştirme yaparken profili `dev` olarak ayarlayın
- Üretim ortamında profili `prod` olarak ayarlayın 