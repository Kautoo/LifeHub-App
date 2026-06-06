# Google Stitch Prompts for LifeHub App

## 🎯 App Overview Prompt

Design a modern, premium Android productivity app called "LifeHub" - an all-in-one life management app for a 19-year-old Malaysian student. The app combines task management, notes, habits, budgeting, journaling, and wellness tracking in one beautiful interface.

**Design Style:** Glassmorphism with gradient accents, dark mode primary, soft shadows, rounded corners (20dp), spring-physics micro-animations.

**Color Palette:**
- Primary: #6C63FF (Purple-Blue)
- Secondary: #00D2FF (Cyan)
- Accent: #FF6B6B (Coral Red)
- Success: #4CAF50 (Green)
- Warning: #FFB74D (Amber)
- Background: #0D1117 (Deep Dark) / #1A1A2E (Dark Purple)
- Surface: #16213E (Dark Blue) with 0.95 alpha (glassmorphism)
- Cards: White/White with 0.08 alpha overlays on dark backgrounds

**Typography:** 
- Display/Headlines: **Space Grotesk** (Bold, techy, modern geometric font)
- Body/Labels: **Plus Jakarta Sans** (Clean, premium, slightly rounded)
- Bold headers (ExtraBold weight), clean body text, emoji icons for visual delight.

**Bottom Navigation:** 5 tabs - Home (🏠), Todo (✅), Notes (📝), Habits (🎯), More (☰)

---

## 📱 Screen-by-Screen Prompts

### 1. Splash Screen
Design a premium splash screen for "LifeHub" app:
- Deep dark gradient background (#0D1117 to #1A1A2E)
- Large centered app icon with subtle glow effect
- "LifeHub" text in **Space Grotesk Bold** white font (34sp)
- Tagline "Your life, organized. One hub at a time." in **Plus Jakarta Sans Regular** (14sp, white with 0.8 alpha)
- Animated loading dots at bottom (3 dots, pulsing)
- Floating decorative circles in background (subtle purple/cyan)
- Clean, minimal, premium feel

### 2. Home Dashboard
Design a modern home dashboard screen:
- Greeting header: "Good Morning ☀️ Fizi" in **Space Grotesk Bold** (28sp) with time-based greeting
- Username "Fizi" in **Plus Jakarta Sans Regular** (16sp, onSurfaceVariant color)
- Motivational quote card below greeting (gradient purple card) in **Plus Jakarta Sans Medium** (14sp)
- Horizontal scrolling stat cards (4 cards):
  - 📋 Pending Tasks (count) — value in **Space Grotesk Bold** (20sp)
  - ✅ Completed (count)
  - 🎯 Habits (count)
  - 💰 Balance (RM amount)
- Quick Access section: 4 gradient cards in a row (Todo, Notes, Budget, Habits) - each with emoji icon and gradient background, title in **Plus Jakarta Sans Bold** (15sp)
- Recent Tasks section: top 5 tasks with status indicators, title in **Plus Jakarta Sans SemiBold**
- Today's Progress card: large gradient card showing habits done, tasks completed, pending count — numbers in **Space Grotesk ExtraBold** (28sp)
- Branding card at bottom: "LifeHub" in **Space Grotesk Bold** (24sp), tagline in **Plus Jakarta Sans Regular**
- Glassmorphism cards with soft shadows
- Staggered animation (cards appear one by one)

### 3. Todo/Task Screen
Design a task management screen:
- Header: "✅ Tasks" in **Space Grotesk Bold** (34sp) with "X pending • Y done" subtitle in **Plus Jakarta Sans Regular** (14sp)
- Circular progress indicator showing completion percentage — percentage in **Plus Jakarta Sans Bold**
- Task cards with:
  - Color-coded priority bar on left (🔴 High, 🟡 Medium, 🟢 Low)
  - Checkbox with animated checkmark
  - Title in **Plus Jakarta Sans SemiBold** (strikethrough when done)
  - Description in **Plus Jakarta Sans Regular** (bodySmall, onSurfaceVariant color)
  - Priority label in **Plus Jakarta Sans Medium** (labelSmall)
  - Delete button
- Section headers: "To Do" / "Completed" in **Space Grotesk Bold** (titleLarge)
- Floating action button (+) with gradient
- Add Task dialog:
  - Title input with **Plus Jakarta Sans** text
  - Description input
  - Priority selector (3 chips: Low, Medium, High) in **Plus Jakarta Sans Medium**
  - "Add Task" gradient button in **Plus Jakarta Sans Bold**
- Empty state: "✨ All clear!" title in **Space Grotesk Bold**, subtitle in **Plus Jakarta Sans Regular**

### 4. Notes Screen
Design a notes management screen:
- Header: "📝 Notes" with note count
- Search bar with search icon
- Note cards with:
  - Title (bold)
  - Content preview (3 lines max)
  - Last updated date
  - Delete button
- Floating action button (+) with purple gradient
- Add Note dialog:
  - Title input
  - Content textarea (min 4 lines)
  - "Save" gradient button
- Empty state: "📝 No notes yet - Tap + to write your first note"

### 5. Habits Tracker Screen
Design a habit tracking screen:
- Header: "🎯 Habits" with "X of Y today" and circular progress
- Summary card (gradient green):
  - 🎯 Done count
  - 📋 Total count
  - Motivational message based on progress
- Habit cards with:
  - Emoji icon in circle
  - Habit name
  - Frequency (Daily/Weekly)
  - Animated checkmark (✅ when completed)
  - Delete button
- Floating action button (+) with green gradient
- Add Habit dialog:
  - Name input
  - Icon selector (6 emoji options: 💪📚🏃💧🧘🎵)
  - Frequency selector (Daily/Weekly chips)
  - "Create" gradient button
- Empty state: "🎯 No habits yet - Start building good habits!"

### 6. Budget Tracker Screen
Design a budget/finance tracking screen:
- Header: "💰 Budget"
- Balance card (gradient teal):
  - Large balance amount in white
  - Income (📈) and Expense (📉) split below
- Transaction list:
  - Category emoji icon in colored circle
  - Description
  - Category name
  - Amount with +/- prefix and color (green for income, red for expense)
  - Date
  - Delete button
- Floating action button (+) with teal gradient
- Add Transaction dialog:
  - Description input
  - Amount input (RM)
  - Type selector (Income/Expense chips with icons)
  - Category selector (emoji grid: 🍔🚗🎮📚🛍️📄🏦💰📦)
  - "Add" gradient button
- Empty state: "💰 No transactions yet - Tap + to add income or expense"

### 7. Daily Journal Screen
Design a journal/mood tracking screen:
- Header: "📓 Journal" with entry count
- Journal cards with:
  - Mood emoji (😊😐😢😠🤩)
  - Date
  - Entry text (3 lines max)
  - Delete button
- Floating action button (+) with purple gradient
- Add Entry dialog:
  - Mood selector (5 large emoji buttons)
  - Textarea for journal entry
  - "Save" gradient button
- Empty state: "📓 No entries yet - How are you feeling today?"

### 8. Goal Tracker Screen
Design a goal tracking screen:
- Header: "🎯 Goals"
- Goal cards with:
  - Emoji icon (12 options)
  - Goal name
  - Progress bar (0-100%)
  - Percentage text
  - Delete button
- Sections: "Active" and "Completed" goals
- Floating action button (+) with gradient
- Add Goal dialog:
  - Name input
  - Icon selector (12 emojis)
  - Progress buttons (25%, 50%, 75%, 100%)
  - "Create" gradient button
- Empty state: "🎯 No goals yet - Set your first goal!"

### 9. Water Tracker Screen
Design a water intake tracking screen:
- Header: "💧 Water Tracker"
- Large circular progress ring showing glasses consumed vs target
- Current count in center (e.g., "3/8 glasses")
- "Tap to add glass" button with water drop animation
- Weekly bar chart (7 bars, last 7 days)
- "Daily goal reached!" badge when target met
- Quick add buttons: +1 glass, +2 glasses
- Empty state: "💧 Start tracking your water intake!"

### 10. Calendar/Events Screen
Design a calendar and events screen:
- Header: "📅 Calendar"
- Monthly calendar grid:
  - Current day highlighted
  - Days with events have dots
  - Swipeable month navigation
- Events list below calendar:
  - Event cards with title, date, time
  - Countdown badges ("Tomorrow!", "3 days")
  - Delete button
- Floating action button (+) with gradient
- Add Event dialog:
  - Title input
  - Date picker
  - Time picker (optional)
  - "Add Event" gradient button

### 11. Pomodoro Timer Screen
Design a focus/pomodoro timer screen:
- Header: "🍅 Pomodoro"
- Large circular timer display (countdown)
- Timer presets: 15, 25, 30, 45, 60 minutes
- Start/Pause/Reset buttons with gradient
- Session counter: "Sessions today: X"
- Minutes focused: "Minutes today: X min"
- Break mode indicator (auto-switches after timer)
- Pulse animation when timer is running
- Empty state: "🍅 Ready to focus? Pick a timer!"

### 12. Settings Screen
Design a settings screen:
- Header: "⚙️ Settings"
- Setting items:
  - 🌙 Dark Mode toggle (switch)
  - 📱 App Version
  - 💜 Made with ❤️ by Fizi
- Glassmorphism card style
- Clean, minimal design

### 13. More/Menu Screen
Design a "More" features hub screen:
- Header: "☰ More Features"
- Grid of feature cards (2 columns):
  - 💰 Budget (teal gradient)
  - 📅 Calendar (blue gradient)
  - 📓 Journal (purple gradient)
  - 🎯 Goals (green gradient)
  - 💧 Water (cyan gradient)
  - 🍅 Pomodoro (red gradient)
  - ⚙️ Settings (gray gradient)
- Each card: emoji icon + title + brief description
- Glassmorphism style with colored gradient accents
- Tap to navigate to feature

---

## 🎨 Design System Prompt

Create a consistent design system for LifeHub:

**Cards:**
- Corner radius: 20dp
- Shadow: 8dp elevation with colored ambient light
- Background: Surface color with 0.95 alpha (glassmorphism)
- Padding: 16-20dp

**Buttons:**
- Primary: Gradient (#6C63FF to #00D2FF)
- Corner radius: 16dp
- Padding: 24dp horizontal, 14dp vertical
- Shadow: 8dp elevation
- Press state: Scale down to 0.92

**Floating Action Button:**
- Circular, 56dp size
- Gradient background
- Shadow: 12dp elevation
- Icon: White, 24dp

**Typography:**
- Display/Headlines font: **Space Grotesk** (Bold, techy, modern geometric font)
  - Display Large: 34sp, Bold, letter-spacing -0.5
  - Headline Large: 28sp, Bold, letter-spacing -0.3
  - Headline Medium: 24sp, Bold
- Body/Labels font: **Plus Jakarta Sans** (Clean, premium, slightly rounded)
  - Title Large: 20sp, SemiBold
  - Body Large: 16sp, Normal
  - Body Medium: 14sp, Normal
  - Label Large: 14sp, Medium
  - Label Small: 11sp, Medium
- All text: White on dark backgrounds, Dark on light cards
- Use **Space Grotesk** for all headers, screen titles, and big text
- Use **Plus Jakarta Sans** for body content, descriptions, labels, and small text

**Animations:**
- Spring physics (bouncy)
- Staggered entry (60ms delay between items)
- Fade in + scale on appear
- Slide in from bottom for FAB
- Smooth state transitions

**Empty States:**
- Large animated emoji (floating effect)
- Bold title
- Subtitle with hint
- Centered layout

---

## 🔥 Advanced UI Ideas

**Glassmorphism Effect:**
- Semi-transparent cards (alpha 0.08-0.15)
- Blur effect behind cards
- Subtle border (1dp, white with 0.1 alpha)
- Soft colored shadows

**Gradient Combinations:**
- Primary: Purple → Cyan
- Success: Green → Teal
- Warning: Amber → Orange
- Danger: Red → Pink
- Calm: Blue → Indigo

**Micro-Animations:**
- Bouncy button press (spring scale 0.92)
- Emoji pop-in (spring scale 0→1)
- Card slide-in (from bottom/side)
- Checkbox animation (scale + color transition)
- Progress ring animation (smooth fill)
- Counter animation (number count up)

**Empty State Animations:**
- Floating emoji (sine wave Y offset)
- Gentle scale pulse (1.0 → 1.1)
- Fade in with delay

---

## 🔤 Font Usage Guide (IMPORTANT!)

### Space Grotesk (Headers & Display)
Use for ALL headers, titles, big numbers, and display text:
- Screen titles (e.g., "✅ Tasks", "💰 Budget")
- Section headers (e.g., "To Do", "Completed")
- Big numbers (balance, counts, percentages)
- App name "LifeHub"
- Greeting text ("Good Morning ☀️")
- Stat values on cards

### Plus Jakarta Sans (Body & Labels)
Use for ALL body content, descriptions, and small text:
- Task titles and descriptions
- Note content
- Labels and captions
- Button text
- Input field text
- Subtitles and metadata
- Empty state subtitles
- Dialog body text

### Rule of Thumb:
- If it's BIG and BOLD → **Space Grotesk**
- If it's readable body text → **Plus Jakarta Sans**
- Never use default Android system font!
