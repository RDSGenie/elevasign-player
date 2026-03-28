# ElevaSign Player

App Android TV para el sistema de señalización digital ElevaSign. Corre en Onn. 4K Streaming Box (Android 12, API 31).

## Setup (4 pasos)

### 1. Instalar Java JDK 17
- Descargar: https://adoptium.net/temurin/releases/?version=17
- Windows x64 → `.msi` → instalar con defaults
- Verificar: `java -version` en terminal nueva

### 2. Instalar Android Studio
- Descargar: https://developer.android.com/studio
- Aceptar defaults (incluye Android SDK)
- En SDK Manager instalar: Android 12 (API 31)

### 3. Configurar Firebase (FCM)
- Ir a: https://console.firebase.google.com
- New project → "ElevaSign"
- Add Android app → Package: `com.elevasign.player`
- Descargar `google-services.json` → copiar a `app/google-services.json`

### 4. Build y deploy
1. Abrir este directorio en Android Studio → esperar Gradle sync
2. Conectar Onn. box por USB con ADB debugging habilitado
3. Run → seleccionar el dispositivo

> **local.properties** ya está configurado con las credenciales de Supabase.
> **No commitear** `local.properties` ni `app/google-services.json`.

---

## Arquitectura

```
ElevaSignApp (Application + Hilt)
    │
    └── MainActivity
          ├── PairingScreen   → player-register → guarda screen_id
          └── PlayerScreen    → reproduce playlist en loop fullscreen
                │
                ├── ImageSlide  (Coil AsyncImage)
                ├── VideoPlayer (Media3 ExoPlayer)
                ├── AnnouncementOverlay (overlay/fullscreen)
                └── TickerOverlay (scrolling text)

Background (WorkManager):
  ├── SyncWorker   (15 min) → player-sync → descarga media → Room
  └── HeartbeatWorker (15 min) → player-heartbeat → ejecuta comandos

Boot:
  └── BootReceiver → lanza MainActivity al encender dispositivo
```

## Comandos remotos soportados

| Comando | Acción |
|---|---|
| `restart_app` | Reinicia la app |
| `reboot_device` | Reinicia el dispositivo (requiere Device Owner) |
| `force_sync` | Sincroniza contenido inmediatamente |
| `clear_cache` | Borra cache local y re-descarga |
| `set_volume` | Cambia volumen (`payload: {level: 0-100}`) |
| `take_screenshot` | Captura pantalla (TODO: Phase 4) |

## Kiosk Mode (Device Owner)

Para Lock Task Mode completo (sin poder salir):
```bash
adb shell dpm set-device-owner com.elevasign.player/.receiver.DeviceAdminReceiver
```

Esto requiere factory reset previo. Para testing, la app funciona sin Device Owner (solo sin Lock Task).

## Package

```
com.elevasign.player
├── data/
│   ├── local/db/       Room entities + DAOs
│   ├── local/datastore PlayerPreferences (DataStore)
│   ├── local/file/     MediaFileManager
│   ├── remote/         Retrofit API + DTOs
│   └── repository/     PlayerRepository
├── di/                 Hilt modules
├── domain/
│   ├── model/          PlaybackItem, ActiveAnnouncement, DeviceCommand
│   └── usecase/        SyncManifestUseCase, HandleCommandUseCase
├── ui/
│   ├── pairing/        PairingScreen + ViewModel
│   ├── player/         PlayerScreen + ViewModel + composables
│   └── theme/          MaterialTheme dark
├── worker/             SyncWorker, HeartbeatWorker
├── service/            FCMService
├── receiver/           BootReceiver, DeviceAdminReceiver
├── ElevaSignApp.kt
└── MainActivity.kt
```

## Backend

Todas las Edge Functions están en `elevasign-admin/supabase/functions/`:
- `player-register` — emparejamiento inicial
- `player-sync` — manifiesto de contenido
- `player-heartbeat` — telemetría + comandos pendientes
- `player-command-result` — reportar resultado de comando

Supabase URL: `https://tvwxjauesfzwsxydjfyn.supabase.co`
