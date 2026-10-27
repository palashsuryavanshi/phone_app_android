# Phone Design System Reference

## Tokens
- `core/designsystem/tokens/PhoneTokens.kt` — spacing, touch targets, icon sizes, elevation, corner radii, typography sizes, motion durations.

## Theme Engine
- `PhoneThemeConfig` — appearance, dynamic color, palette, custom seed, corner scale, typography scale, animation, density, haptics, gradients.
- `PhoneThemeManager` — DataStore-backed singleton, `config: StateFlow`, `update()`.
- `PhoneTheme` — single entry point composable; applies Material 3 color scheme, shapes, typography.
- Presets: `MidnightAurora`, `SoftCloud`, `EmberPulse` (light + dark).

## Components
Located in `core/designsystem/components/`:
- Buttons: `PhonePrimaryButton`, `PhoneSecondaryButton`, `PhoneTonalButton`, `PhoneDestructiveButton`, `PhoneExpressiveIconButton`.
- Inputs: `PhoneSearchField`.
- Rows: `ContactRow`, `CallHistoryRow`.
- Cards: `PhoneCard`, `PhoneSurfaceContainer`.
- States: `EmptyState`, `PhoneLoadingIndicator`, `PhoneErrorRetryState`.
- Controls: `PhonePreferenceRow`, `PhoneSwitchRow`, `PhoneSliderRow`, `PhoneSegmentedControl`, `PhoneSelectionChip`.
- Panels: `PhonePermissionPanel`, `PhoneBottomSheet`, `PhoneConfirmDialog`, `PhonePreviewCard`, `PhoneFloatingDialerButton`.
- Pickers: `PhoneColorPicker`.

## Motion
- `PhoneMotionDurations`, `PhoneMotionSpecs` in tokens; spring-based press feedback in components.

## Accessibility & Responsiveness
- Large touch targets (48dp+), content descriptions, font scaling via MaterialTheme, reduced-motion preference in config.
}
