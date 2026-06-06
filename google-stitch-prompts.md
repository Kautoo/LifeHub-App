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

**Typography:** Bold headers (ExtraBold weight), clean body text, emoji icons for visual delight.

**Bottom Navigation:** 5 tabs - Home (🏠), Todo (✅), Notes (📝), Habits (🎯), More (☰)

---

## 📱 Screen-by-Screen Prompts

### 1. Splash Screen
Design a premium splash screen for "LifeHub" app:
- Deep dark gradient background (#0D1117 to #1A1A2E)
- Large centered app icon with subtle glow effect
- "LifeHub" text in ExtraBold white font
- Tagline: "Your life, organized. One hub at a time."
- Animated loading dots at bottom (3 dots, pulsing)
- Floating decorative circles in background (subtle purple/cyan)
- Clean, minimal, premium feel

### 2. Home Dashboard
Design a modern home dashboard screen:
- Greeting header: "Good Morning ☀️ Fizi" with time-based greeting
- Motivational quote card below greeting (gradient purple card)
- Horizontal scrolling stat cards (4 cards):
  - 📋 Pending Tasks (count)
  - ✅ Completed (count)
  - 🎯 Habits (count)
  - 💰 Balance (RM amount)
- Quick Access section: 4 gradient cards in a row (Todo, Notes, Budget, Habits) - each with emoji icon and gradient background
- Recent Tasks section: top 5 tasks with status indicators
- Today's Progress card: large gradient card showing habits done, tasks completed, pending count
- Branding card at bottom: "LifeHub - Your life, organized"
- Glassmorphism cards with soft shadows
- Staggered animation (cards appear one by one)

### 3. Todo/Task Screen
Design a task management screen:
- Header: "✅ Tasks" with "X pending • Y done" subtitle
- Circular progress indicator showing completion percentage
- Task cards with:
  - Color-coded priority bar on left (🔴 High, 🟡 Medium, 🟢 Low)
  - Checkbox with animated checkmark
  - Title (strikethrough when done)
  - Description preview
  - Delete button
- Section headers: "To Do" and "Completed"
- Floating action button (+) with gradient
- Add Task dialog:
  - Title input
  - Description input
  - Priority selector (3 chips: Low, Medium, High)
  - "Add Task" gradient button
- Empty state: "✨ All clear! Tap + to add your first task"

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
- Display: 28sp, ExtraBold
- Title: 20sp, Bold
- Body: 14sp, Normal
- Label: 12sp, Medium
- All text: White on dark, Dark on light cards

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
