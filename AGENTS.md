# AGENTS.md

## Amac / Purpose

Bu dosya, bu repository'de yeni sohbet acan AI agent'lar icin varsayilan operasyon rehberidir.
The goal is to help future agents understand the real codebase quickly, preserve existing patterns, and avoid unnecessary refactors.

Bu belge genel bir "best practices" manifestosu degil, repo-gercegine dayali bir calisma kilavuzudur.
It documents current reality, including awkward parts, not an idealized architecture.

## Proje Ozeti / Project Snapshot

- Yigin / Stack: Java 17, Spring Boot 3.2.x, Maven multi-module project.
- Kok `pom.xml`, aggregator parent olarak su modulleri toplar:
  - `core`: ortak config, security, exceptions, result wrappers, file utilities, mapping altyapisi.
  - `entities`: JPA entities ve enums.
  - `repositories`: Spring Data JPA repositories.
  - `business`: DTOs, service interfaces, manager implementations, business rules, mapper classes.
  - `webapi`: controllers ve application bootstrap.
- Ana giris noktasi / Main entrypoint:
  [webapi/src/main/java/com/demirciyazilim/webapi/AlaSogusBackendApplication.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\webapi\src\main\java\com\demirciyazilim\webapi\AlaSogusBackendApplication.java)
- Varsayilan HTTP port'u / Default port: `8082`.

## Uygulama Nasil Bagli / How The App Is Wired

- Uygulama `webapi` modulu icinden acilir ve `com.demirciyazilim` package tree'sini tarar.
- Guvenlik / Security stateless JWT tabanlidir:
  - ana config:
    [core/src/main/java/com/demirciyazilim/core/security/SecurityConfig.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\core\src\main\java\com\demirciyazilim\core\security\SecurityConfig.java)
  - request auth `JwtAuthenticationFilter` ile uygulanir
  - endpoint yetkileri hem `SecurityConfig` matcher'larinda hem de controller seviyesindeki `@PreAuthorize` ile dagitilmistir
  - pratikte kullanilan roller / active roles: `ADMIN`, `EDITOR`, `USER`
- Refresh token temizligi / cleanup, saat basi scheduled olarak calisir:
  [business/src/main/java/com/demirciyazilim/business/concretes/RefreshTokenManager.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\business\src\main\java\com\demirciyazilim\business\concretes\RefreshTokenManager.java)
- OpenAPI / Swagger aktiftir:
  [core/src/main/java/com/demirciyazilim/core/config/OpenApiConfig.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\core\src\main\java\com\demirciyazilim\core\config\OpenApiConfig.java)
- Dosya yukleme / file upload, Cloudinary uzerinden gider:
  [core/src/main/java/com/demirciyazilim/core/utilities/file/CloudinaryService.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\core\src\main\java\com\demirciyazilim\core\utilities\file\CloudinaryService.java)

## Korunacak Mimari Kaliplari / Architecture Rules To Preserve

- Katman sinirlarini koru / Preserve layer boundaries:
  - `webapi`: HTTP concerns, routing, response shaping
  - `business/concretes/*Manager`: business flow
  - `business/rules/*BusinessRules`: domain checks
  - `repositories`: persistence access
  - `entities`: persistence-focused model
- Mevcut response contract'i koru / Preserve the response contract:
  - controllers genelde `ResponseEntity<Result>` dondurur
  - veya `ResponseEntity<DataResult<T>>`
  - ortak wrappers `core/utilities/results` altindadir
- Validation ikiye bolunmus durumdadir / Validation is intentionally split:
  - DTO annotations request shape ve basic constraints icin
  - `BusinessRules` domain invariants icin
- Mapping stili agirlikli olarak manuel mapper class'lari ile yapiliyor.
  Manual mapper pattern is the active norm; do not casually switch a feature to a different mapping style.
- `core` altinda `ModelMapper` altyapisi var, ama aktif feature kodunun baskin stili degil.
  Do not assume the project is standardized on `ModelMapper`.
- Servisler cogu yerde `createdAt` ve `updatedAt` alanlarini manuel set ediyor.
  `BaseEntity` lifecycle hooks olsa bile, mevcut davranisi task acikca istemedikce degistirme.

## Alan Bilgisi / Domain Areas You Should Understand

- Auth ve users
  - `/api/v1/auth` altinda login, register, refresh, logout akislari var
  - access token JWT, refresh token ise DB-backed yapida
  - `UserDetailsServiceImpl` ve `JwtTokenProvider` auth akisinin merkezi parcalari
- Reservations
  - public reservation create endpoint var
  - customer lookup by email/phone su an public durumda
  - admin/editor tarafinda filters, summaries ve status transitions var
- Menus ve menu categories
  - menu'lerde hem `active` hem `available` state'i bulunur
  - image akislari multipart + Cloudinary ile calisir
  - ordering behavior repository sorting ile kodlanmistir
- Job positions ve job applications
  - public apply flow mevcut
  - CV/resume upload, JSON-plus-file multipart convention kullanir
- Contact messages
  - mesajlar DB'ye yazilir
  - email notification best-effort olarak denenir, send hatalari swallow edilir
- Generic file endpoints
  - file upload endpoint'leri genis ve su an security config'te public
  - bir proxy endpoint uzak dosyayi uygun headers ile iletir

## Controller ve Endpoint Kurallari / Request And Controller Conventions

- Controller'lar annotation-heavy yapidadir ve cogu yerde sunlar gorulur:
  - `@Tag`
  - `@Operation`
  - `@CrossOrigin`
  - protected action'larda `@PreAuthorize`
- Yeni bir endpoint veya mevcut endpoint degisiyorsa, tum ilgili katmanlari birlikte dusun:
  - controller route ve response handling
  - `SecurityConfig` matcher rules
  - DTOs ve validation
  - business service interface
  - manager implementation
  - business rules
  - mapper
  - repository queries
- Upload akislari degisiyorsa mevcut multipart convention'i koru:
  - menu endpoint'leri: `menuData` JSON + optional `image`
  - job application endpoint'i: `applicationData` JSON + optional `resume`
- Action endpoint naming tarafinda mevcut verb stiline yakin kal:
  `activate`, `deactivate`, `confirm`, `cancel`, `complete`, `no-show`.

## Config ve Calistirma Beklentileri / Config And Runtime Expectations

- Config dosyalari burada yasar:
  [webapi/src/main/resources](c:\DemirciYazilimProjects\restaurant\restaurant-backend\webapi\src\main\resources)
- Shared `application.properties` su an `spring.profiles.active=prod` set ediyor.
- `application-dev.properties`, environment variables uzerinden credential okuyor.
- `application-prod.properties` workspace icinde mevcut ve sensitive kabul edilmelidir.
- Config icinden secret degerleri asla chat output'a kopyalama, ozetleme veya ifsa etme.
- Repo su an system Maven kullaniyor gibi gorunuyor; Maven wrapper dosyalari ignore edilmis ve mevcut degil.

## Gelecek Agent'lar Icin Calisma Anlasmasi / Working Agreement

- Once oku, sonra degistir / Explore first, edit second.
  Etkilenen controller, manager, business rules, mapper, repository, DTO ve config'i okumadan degisiklik yapma.
- En kucuk guvenli degisikligi tercih et / Prefer the smallest safe change.
- Kullanici istemedikce opportunistic cleanup ekleme.
  Do not smuggle refactors into feature work.
- Public response wrapper stilini gereksiz yere degistirme.
  Preserve `Result` / `DataResult` unless the task explicitly authorizes contract change.
- Security, validation ve persistence'i birbirinden bagimsiz varsayma.
  Bir endpoint degisiyorsa bu uc alanin da etkilenip etkilenmedigini kontrol et.
- Config ve secrets konusunda paranoid davran.
  Never echo live secrets from `application-prod.properties` or env-backed settings.
- Automated test coverage'in zayif veya yok sayilacak kadar az oldugunu varsay.
  Verify with targeted commands when useful, but do not overclaim confidence.
- Upload, auth veya public endpoint'lere dokunuyorsan finalden once ikinci bir security/data exposure pass yap.

## Bilinen Tuzaklar / Known Quirks And Traps

- Gercek module layout disinda kalan supheli bir dosya var:
  [src/main/java/com/demirciyazilim/webapi/controllers/AuthController.java](c:\DemirciYazilimProjects\restaurant\restaurant-backend\src\main\java\com\demirciyazilim\webapi\controllers\AuthController.java)
  Bunu bilincli cleanup yapilmadikca legacy/confusing artifact gibi ele al.
- CORS mantigi birden fazla yerde gorunuyor:
  - aktif davranis security config tarafinda tutulur
  - ayri olarak fully commented bir `CorsConfig` dosyasi da var
  Ucuncu bir source of truth ekleme.
- README terminal output'unda encoding problemleri gorunuyor.
  Kullanici istemedikce "drive-by encoding fix" yapma.
- `application-prod.properties`, repo niyetine gore paylasilmamasi gereken production data barindirabilecek halde workspace'te mevcut.
  Config-related task'larda ekstra dikkatli ol.
- `ModelMapper` siniflari var ama canli feature kodu cogunlukla manual mapping kullaniyor.
  Standard pattern budur diye `ModelMapper` dayatma.
- Bu environment'ta `git status`, `safe.directory` warning'i verebiliyor.
  Repo metadata'ya dayali task'larda git inspection dikkat isteyebilir.

## Build ve Dogrulama Notlari / Build And Verification Notes

- Repo dokumanina gore temel build komutu: `mvn clean install`
- `webapi` tarafinda package edilen artifact adi: `ala-sogus-api`
- Verification gerekiyorsa once targeted ve non-destructive check'leri tercih et
- Bir sey calistirmadiysan veya hic verification yapmadiysan bunu final response'ta acikca belirt

## Bir Kod Gorevi Ne Zaman Tamam Sayilir / Definition Of Done

- Degisiklik mevcut module boundaries icinde kalir
- Security ve validation tutarli sekilde guncellenir
- `Result` / `DataResult` response stili korunur, ancak task bunu bilerek degistiriyorsa istisna olabilir
- Secrets code, docs, logs veya chat output icinde ifsa edilmez
- Is sirasinda fark edilen onemli quirk, debt veya follow-up risk final response'ta belirtilir

## Hizli Zihinsel Model / Quick Mental Model

Bu repo'ya yeni girdiysen su sirayla dusun:

1. Bu degisiklik hangi module'un sorumlulugunda?
2. Hangi public route veya service contract etkileniyor?
3. Hangi DTO validation ve `BusinessRules` birlikte degismeli?
4. Hangi repository query veya sorting behavior ozelligi ayakta tutuyor?
5. Hangi `SecurityConfig` matcher'i ve hangi `@PreAuthorize` kuralini da guncellemek gerekiyor?

Mevcut tasarima yakin kal.
This repository values consistency with existing patterns more than clever rewrites.
