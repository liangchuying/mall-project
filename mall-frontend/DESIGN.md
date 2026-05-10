# DESIGN.md - 购物商城设计系统

## Visual Theme & Atmosphere

**Modern E-commerce Aesthetic**

A clean, trustworthy e-commerce experience that emphasizes product visibility and user confidence. The design follows a content-first philosophy with generous whitespace, clear visual hierarchy, and a balanced color palette that builds trust while maintaining visual interest.

- **Mood**: Professional, approachable, conversion-focused
- **Density**: Airy with strategic information density in product grids
- **Design Philosophy**: Form follows function - every visual element serves the user journey from discovery to checkout
- **Atmosphere**: Contemporary retail that feels both premium and accessible

---

## Color Palette & Roles

### Primary Colors

| Name | Hex | Role | Usage |
|------|-----|------|-------|
| Primary Blue | `#3B82F6` | Primary action | CTA buttons, links, active states |
| Primary Dark | `#1E40AF` | Primary hover | Button hover states, emphasized elements |
| Primary Light | `#DBEAFE` | Primary background | Primary button backgrounds, badges |

### Neutral Colors

| Name | Hex | Role | Usage |
|------|-----|------|-------|
| White | `#FFFFFF` | Canvas base | Main background, cards |
| Gray-50 | `#F9FAFB` | Surface light | Section backgrounds, hover states |
| Gray-100 | `#F3F4F6` | Surface medium | Borders, dividers |
| Gray-200 | `#E5E7EB` | Border | Input borders, card borders |
| Gray-300 | `#D1D5DB` | Border light | Secondary dividers |
| Gray-400 | `#9CA3AF` | Text secondary | Subtitles, placeholders |
| Gray-500 | `#6B7280` | Text tertiary | Helper text, descriptions |
| Gray-600 | `#4B5563` | Text body | Body paragraphs |
| Gray-800 | `#1F2937` | Text heading | Headlines, titles |
| Gray-900 | `#111827` | Text primary | Important text, navigation |

### Functional Colors

| Name | Hex | Role | Usage |
|------|-----|------|-------|
| Success | `#10B981` | Positive states | Success messages, in-stock |
| Warning | `#F59E0B` | Warning states | Low stock, promotions |
| Error | `#EF4444` | Negative states | Error messages, out-of-stock |
| Info | `#3B82F6` | Information | Info messages, notifications |

### Accent Colors (for categories/promotions)

| Name | Hex | Role | Usage |
|------|-----|------|-------|
| Accent Coral | `#F97316` | Call-to-action | Sale banners, urgent actions |
| Accent Purple | `#8B5CF6` | Premium | VIP badges, premium features |
| Accent Pink | `#EC4899` | Limited offers | Flash sales, countdowns |

---

## Typography Rules

### Font Families

| Role | Font Family | Weight | Usage |
|------|-------------|--------|-------|
| Body | System sans-serif | 400-500 | All body text, UI elements |
| Heading | System sans-serif | 600-700 | Headlines, section titles |
| Mono | System monospace | 400-500 | Prices, codes, technical data |

### Typography Hierarchy

| Level | Size | Weight | Line Height | Letter Spacing | Usage |
|-------|------|--------|-------------|----------------|-------|
| H1 | 48px | 700 | 1.2 | -0.02em | Page titles, hero headlines |
| H2 | 36px | 700 | 1.25 | -0.015em | Section headers |
| H3 | 24px | 600 | 1.3 | -0.01em | Card titles, subsections |
| H4 | 18px | 600 | 1.4 | normal | Component titles |
| Body Large | 16px | 400 | 1.6 | normal | Product descriptions |
| Body | 14px | 400 | 1.5 | normal | Standard text |
| Body Small | 12px | 400 | 1.4 | normal | Captions, helpers |
| Price | 20px | 700 | 1.2 | normal | Product prices |
| Label | 14px | 500 | 1.4 | normal | Form labels |
| Button | 14px | 600 | 1.2 | 0.02em | Button text |

### Typography Best Practices

- All headings use sentence case (capitalize only first word and proper nouns)
- Prices always display with 2 decimal places and currency symbol
- Line height increases with font size for better readability
- Negative letter spacing on headings for a premium, modern look

---

## Component Stylings

### Buttons

#### Primary Button
- Background: `#3B82F6`
- Text: White, 600 weight
- Padding: 12px 24px
- Border radius: 8px
- Hover: `#1E40AF`
- Active: Scale 0.98
- Disabled: `#9CA3AF` background, cursor not-allowed

#### Secondary Button
- Background: White
- Text: `#3B82F6`, 600 weight
- Border: 2px solid `#3B82F6`
- Padding: 10px 22px (accounting for border)
- Border radius: 8px
- Hover: `#DBEAFE` background
- Active: Scale 0.98

#### Ghost Button
- Background: Transparent
- Text: `#3B82F6`, 600 weight
- Padding: 12px 24px
- Border radius: 8px
- Hover: `#DBEAFE` background
- Active: Scale 0.98

#### CTA Button (Prominent)
- Background: `#F97316`
- Text: White, 600 weight
- Padding: 14px 32px
- Border radius: 8px
- Hover: `#EA580C`
- Active: Scale 0.98
- Shadow: `0 4px 12px rgba(249, 115, 22, 0.3)`

### Cards

#### Product Card
- Background: White
- Border: 1px solid `#E5E7EB`
- Border radius: 12px
- Padding: 16px
- Hover: Border `#3B82F6`, shadow `0 8px 24px rgba(0, 0, 0, 0.08)`
- Image: Full width, aspect ratio 1:1, rounded 8px
- Title: H3, truncate after 2 lines
- Price: Price style, accent color on sale

#### Category Card
- Background: White
- Border: 1px solid `#E5E7EB`
- Border radius: 12px
- Hover: Border `#3B82F6`, translate Y -2px
- Image: Full width, aspect ratio 16:9, rounded 8px
- Title: H3, centered

### Inputs

#### Text Input
- Background: White
- Border: 1px solid `#D1D5DB`
- Border radius: 8px
- Padding: 12px 16px
- Font: Body, `#1F2937`
- Focus: Border `#3B82F6`, ring 2px `#DBEAFE`
- Error: Border `#EF4444`, error text below
- Disabled: Background `#F3F4F6`, text `#9CA3AF`

### Navigation

#### Top Navigation
- Background: White
- Border bottom: 1px solid `#E5E7EB`
- Height: 64px
- Logo: H4, `#1F2937`, 700 weight
- Links: Body, `#4B5563`, hover `#3B82F6`
- Active: `#3B82F6`, 600 weight

#### Sidebar Navigation
- Background: White
- Width: 256px
- Links: Body, `#4B5563`, padding 12px 16px
- Hover: Background `#F3F4F6`
- Active: Background `#DBEAFE`, text `#1E40AF`, 600 weight

### Badges

#### Stock Badge
- In Stock: Background `#D1FAE5`, text `#065F46`
- Low Stock: Background `#FEF3C7`, text `#92400E`
- Out of Stock: Background `#FEE2E2`, text `#991B1B`

#### Discount Badge
- Background: `#FEE2E2`
- Text: `#DC2626`, 700 weight
- Border radius: 4px
- Padding: 4px 8px

---

## Layout Principles

### Spacing Scale

| Token | Value | Usage |
|-------|-------|-------|
| space-1 | 4px | Icon padding, tiny gaps |
| space-2 | 8px | Small element spacing |
| space-3 | 12px | Button padding, card padding |
| space-4 | 16px | Section padding, card gap |
| space-6 | 24px | Component spacing |
| space-8 | 32px | Section spacing |
| space-12 | 48px | Major sections |
| space-16 | 64px | Page margins |

### Grid System

- Container max-width: 1280px
- Columns: 12-column grid
- Gutter: 24px
- Breakpoints:
  - Mobile: < 640px (1 column)
  - Tablet: 640px - 1024px (2-3 columns)
  - Desktop: ≥ 1024px (4-6 columns)

### Whitespace Philosophy

- Generous vertical rhythm (24px - 48px between sections)
- Content never touches edges (min 16px padding on mobile, 24px on desktop)
- Card layouts use consistent padding (16px - 24px)
- Negative whitespace for visual grouping

---

## Depth & Elevation

### Shadow System

| Level | CSS | Usage |
|-------|-----|-------|
| None | none | Default state |
| Sm | `0 1px 2px 0 rgba(0, 0, 0, 0.05)` | Subtle elevation |
| Md | `0 4px 6px -1px rgba(0, 0, 0, 0.1)` | Cards, dropdowns |
| Lg | `0 10px 15px -3px rgba(0, 0, 0, 0.1)` | Modals, elevated cards |
| Xl | `0 20px 25px -5px rgba(0, 0, 0, 0.1)` | Popovers, tooltips |

### Surface Hierarchy

1. **Base**: `#FFFFFF` (no shadow) - Default canvas
2. **Elevated**: White + shadow-sm - Cards, panels
3. **Floating**: White + shadow-md - Dropdowns, menus
4. **Modal**: White + shadow-lg - Modals, dialogs
5. **Overlay**: `#111827` at 50% opacity - Backdrops

---

## Do's and Don'ts

### Do's
- ✅ Use consistent spacing from the scale
- ✅ Maintain visual hierarchy through typography
- ✅ Provide clear hover states for all interactive elements
- ✅ Use the primary color sparingly for emphasis
- ✅ Keep buttons at least 44px tall for touch targets
- ✅ Use semantic color for functional feedback
- ✅ Maintain consistent border radius (8px - 12px)

### Don'ts
- ❌ Use multiple primary colors on the same page
- ❌ Mix rounded and square corners
- ❌ Use shadows lighter than the background
- ❌ Create buttons without hover/active states
- ❌ Use body weight for headings
- ❌ Stack more than 3 colors without hierarchy
- ❌ Use text lighter than `#6B7280` for body content

---

## Responsive Behavior

### Breakpoints

| Breakpoint | Width | Columns | Notes |
|------------|-------|---------|-------|
| Mobile | < 640px | 1 | Stacked layouts, hamburger menu |
| Tablet | 640px - 1024px | 2-3 | Grid adapts, partial navigation |
| Desktop | ≥ 1024px | 4-6 | Full layout, full navigation |

### Touch Targets
- Minimum: 44x44px
- Recommended: 48x48px
- Buttons: Minimum 44px height
- Links: Full-width on mobile

### Mobile Adaptations
- Navigation collapses to hamburger menu
- Product grids: 2 columns on large mobile
- Horizontal scroll for category lists
- Sticky bottom navigation for key actions
- Simplified modal dialogs

---

## Agent Prompt Guide

### Quick Color Reference
- Primary action: `#3B82F6` (blue)
- Success: `#10B981` (green)
- Warning: `#F59E0B` (amber)
- Error: `#EF4444` (red)
- Sale/CTA: `#F97316` (coral)
- Text: `#1F2937` (dark), `#4B5563` (medium), `#6B7280` (light)
- Background: `#FFFFFF` (white), `#F9FAFB` (light gray)

### Ready-to-Use Prompts

**For product cards:**
"Create a product card with white background, 12px border radius, 16px padding, 1px #E5E7EB border that turns #3B82F6 on hover. Include a 1:1 aspect ratio product image with 8px border radius, H3 title (24px, 600 weight), and price in 20px bold text."

**For buttons:**
"Create a primary button with #3B82F6 background, white text (600 weight), 12px 24px padding, 8px border radius. Hover state changes background to #1E40AF. Include disabled state with #9CA3AF background."

**For forms:**
"Design a form with 14px 500 weight labels above inputs. Inputs have white background, 1px #D1D5DB border, 12px 16px padding, 8px border radius. Focus state shows #3B82F6 border with 2px #DBEAFE ring."

**For navigation:**
"Create a top navigation bar with 64px height, white background, and 1px #E5E7EB bottom border. Logo is 18px bold #1F2937 text. Navigation links are 14px #4B5563, changing to #3B82F6 on hover and active states."
