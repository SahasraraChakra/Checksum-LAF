Drop the design fonts here (next to your app, folder name "fonts") and they are registered at startup:
  Product Sans  (ProductSans-Regular.ttf, -Bold.ttf ...)   <- first choice for UI text
  Open Sans     (OpenSans-Regular.ttf, -Bold.ttf ...)      <- fallback, as in the mock-up
  JetBrains Mono (JetBrainsMono-Regular.ttf ...)           <- digests, paths, text areas
Different folder: -Dchk.fontDir=/path/to/fonts   Size: -Dchk.fontSize=11.5
Without them the look falls back to Segoe UI / Inter (bundled) and Consolas / DejaVu Sans Mono.
