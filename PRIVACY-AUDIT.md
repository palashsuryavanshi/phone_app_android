# Phone Privacy Audit

## Permissions
- CALL_PHONE (required for calling)
- READ_CONTACTS (optional, for contact names)
- READ_CALL_LOG (optional, for recents)
- POST_NOTIFICATIONS (optional, for call notifications)
- BLUETOOTH_* (for headset routing)

## Data classification
- System-owned: contacts, call logs (accessed via providers, not duplicated)
- App-owned preferences: theme, onboarding, favorites (SharedPreferences/DataStore)
- No remote data, no analytics, no tracking SDKs

## Network
- No INTERNET permission declared
- Cleartext traffic disabled
- No networking dependencies

## Storage
- Preferences in DataStore/SharedPreferences
- No Room databases currently storing sensitive records
- No cryptographic claims beyond Android sandbox

## Security
- Optional biometric/device-credential app lock via BiometricPrompt
- No custom encryption
- No secrets in logs or backups (dataExtractionRules configured)
