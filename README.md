# ХАБ
Аюулгүй ажиллагааны зааварчилгаа өгөх апп

### Төслийн бүтэц


```
├── app
│   ├── src/main
│   │   │── assets/fonts
│   │   │── java/mn/btgt/safetyinst
│   │   │   │── activity
│   │   │   │   │── AddInfoActivity.java    - Зааварчилгаатай танилцсан хэрэглэгчийн зураг гарын үсгийг авч сэрвэрлүү илгээх 
│   │   │   │   │── DeviceListActivity.java - Printert холбогдсон төхөөрөмжүүдийн жагсаалт харуулах
│   │   │   │   │── LoginImeiActivity.java  - Нэвтрэх хэсэг
│   │   │   │   │── MainActivity.java       - Зааварчилгаа унших үндсэн цонх
│   │   │   │   │── SettingsActivity.java   - Принтерийн тохиргоо хийх
│   │   │   │   └── SplashActivity.java     - Эхлэл цонх
│   │   │   │── database
│   │   │   │   │── CategoryTable.java      - Зааварчилгааны ангилал хүснэгт
│   │   │   │   │── DatabaseHelper.java     - Өгөгдлийн сангийн үндсэн класс
│   │   │   │   │── SNoteTable.java         - Зааварчилгааны хүснэгт
│   │   │   │   │── SettingsTable.java      - Тохиргооны хүснэгт
│   │   │   │   │── SignDataTable.java      - Хэрэглэгчдийн танилцсан зааварчилгаа болон мэдээлэл хадгалах
│   │   │   │   └── UserTable.java          - Хэрэглэгчийн хүснэгт
│   │   │   │── model
│   │   │   │   │── Category.java           - Ангилал модел
│   │   │   │   │── FaceResult.java         - Зураг авхад хэрэглэгчийн нүүрний хэмжээ зэргийг авах модел
│   │   │   │   │── SNote.java              - Зааварчилгааны модел
│   │   │   │   │── Settings.java           - Тохиргоо
│   │   │   │   │── SignData.java           - Зааварчилгаатай танилцсан хэрэглэгч, болон зааварчилгааны модел
│   │   │   │   └── User.java               - Хэрэглэгч модел
│   │   │   │── provider
│   │   │   │   └── SettingsProvider.java   - Програмын тохиргоог бусад програмд хуваалцах provider
│   │   │   │── utils
│   │   │   │   │── BluetoothPrintService.java - 
│   │   │   │   │── CompressUtils.java         - Byte array compress decompress хийх 
│   │   │   │   │── ConnectionDetector.java    - Интертет холболттой эсэх ямар сүлжээгээр холбогдож байгаа
│   │   │   │   │── DbBitmap.java              - Bitmap to ByteArray to Bitmap
│   │   │   │   │── EscPosPrinter.java         - Принтертэй холбогдож баримт хэвлэх
│   │   │   │   │── ImageUtils.java           
│   │   │   │   │── NetworkChangeReceiver.java - Интернетэд холбогдох үед локал бааз дээрх мэдээлэл сэрвэрт илгээх
│   │   │   │   │── PrefManager.java           - Sharedpreference-тэй ажиллах 
│   │   │   │   └── SAFCONSTANT.java           - Програмын үндсэн util функцүүд
│   │   │   │── views
│   │   │   │   │── FaceOverlayView.java
│   │   │   │   └── FaceView.java
│   │   │   └── AppMain.java
│   │   │── res │
│   │   └── AndroidManifest.xml
│   │── .gitignore
│   │── build.gradle
│   └── proguard-rules.pro
├── gradle/wrapper
├── .gitignore
├── LICENSE
├── README.MD
├── build.gradle
├── gradlew
├── gradlew.bat
├── keystore.jks
└── settings.gradle
```
