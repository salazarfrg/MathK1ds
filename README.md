# MathKids — Multiplication & Division for 8-year-olds

An Android app (Kotlin + Jetpack Compose) that teaches long division and
multiplication using the **Singapore Math** approach — the same method
in your photo (color-coded quotient/divisor/subtraction, digit-by-digit,
bring-down), turned interactive.

## What it does

**Division screen**
- Rebuilds the pencil-and-paper layout on screen (divisor in gold, quotient
  in purple, working lines in red — matching your board).
- At each step the child is asked "how many times does the divisor fit?"
  with 4 tappable number choices (Concrete→Pictorial→Abstract: no typing
  needed at this age).
- Shows the multiply-and-subtract for that step, then the "bring down"
  digit, before moving to the next digit — exactly the workflow in the
  photo (98 remainder 2 for 492 ÷ 5).
- Wrong taps get a gentle "try again," never a red X or lost life.

**Multiplication screen**
- Facts up to 9×9 shown as an **array of dots** (concrete visual — rows × columns).
- 2-digit factors (11–20) use the Singapore **area/split model**:
  `7 × 23 = (7 × 20) + (7 × 3)`, shown as two colored blocks the child adds.

**Both screens**
- Adaptive difficulty: 3 correct answers in a row levels up; 2 misses eases
  back down — keeps the child in their challenge zone instead of
  frustration or boredom.
- Stars accumulate as a light, non-competitive reward (no timers, no
  penalties — Singapore Math philosophy avoids speed pressure at this age).

## Project structure

```
MathKids/
  app/src/main/java/com/ricewood/mathkids/
    model/           DivisionModel.kt, MultiplicationModel.kt   (pure logic, unit-testable)
    viewmodel/        GameViewModel.kt                          (stars, streak, level)
    ui/screens/        HomeScreen, DivisionScreen, MultiplicationScreen
    ui/components/    ChoicePad, StarBadge, FeedbackBubble
    ui/theme/          Color/Theme/Type (matches your board's palette)
    MainActivity.kt    Navigation graph
```

## Build the APK — no Android Studio needed

I generated the full source but can't compile it in this sandbox (no
Android SDK / network access here). The project includes a GitHub
Actions workflow (`.github/workflows/build-apk.yml`) that builds the
APK in the cloud for free — you just need a (free) GitHub account.

1. Go to **github.com**, sign up if you don't have an account.
2. Click **+ → New repository**. Name it e.g. `mathkids`. Leave it
   Public or Private, don't add a README (we already have one).
3. On the new repo's page, click **"uploading an existing file"**
   (or `Add file → Upload files`).
4. Unzip `MathKids-android-source.zip` on your computer and drag the
   *contents* of the `MathKids` folder (not the folder itself) into
   the GitHub upload box — `app/`, `.github/`, `build.gradle.kts`,
   `settings.gradle.kts`, `gradle.properties`, `README.md`, etc.
5. Click **Commit changes**.
6. Go to the **Actions** tab of your repo. You should see "Build APK"
   running automatically (it also runs on every push). Click it and
   wait a couple of minutes.
7. When it finishes (green check), open the run, scroll to
   **Artifacts**, and download **MathKids-debug-apk** — that's a zip
   containing `app-debug.apk`.
8. Transfer `app-debug.apk` to your Android phone (email it to
   yourself, Google Drive, USB, WhatsApp — anything) and tap it to
   install. You'll need to allow "install from unknown sources" the
   first time Android asks.

That's the whole flow — no local installs, no SDK download on your
machine. If the Action fails, open the failed step's log; the most
common cause is a rejected/renamed file during upload (make sure
`.github/workflows/build-apk.yml` made it into the repo — GitHub's
drag-and-drop upload sometimes hides dotfiles/dot-folders, so if you
don't see `.github` in your repo after uploading, create it manually:
**Add file → Create new file**, type `.github/workflows/build-apk.yml`
as the filename, and paste the workflow content in).

### If you'd rather not use GitHub at all

Command-line-only local build is possible without the Android Studio
*IDE*, but you'd still need to install: a JDK, the Android
command-line SDK tools, and Gradle — which in practice is a similar
amount of setup to Android Studio itself. The GitHub Actions route
above avoids all of that, so it's the path I'd recommend.

## Extending it further (ideas)

- Add sound effects / haptic feedback on correct answers (`SoundPool`).
- Add a "word problem" mode using bar models (another core Singapore Math
  tool) — e.g. "Maria has 492 stickers to share among 5 friends."
- Persist stars/level with `DataStore` so progress survives app restarts.
- Add a parent/teacher dashboard screen summarizing accuracy per operation.
