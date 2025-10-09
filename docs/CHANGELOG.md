# HCAMS Changelog

All notable changes to the Healthcare Appointment Management System.

---

## [2.0.0] - October 5, 2025

### 🎨 UI/UX Improvements

#### Popular Specialties Section Redesign
- **Replaced basic cards** with modern specialty cards
- **Added custom SVG icons** for each specialty (8 unique icons)
- **Color-coded icons** with gradient backgrounds:
  - Cardiology: Red gradient
  - Dermatology: Orange gradient
  - Pediatrics: Pink gradient
  - Orthopedics: Green gradient
  - Neurology: Purple gradient
  - ENT: Cyan gradient
  - Gynecology: Rose gradient
  - Psychiatry: Indigo gradient
- **Interactive animations**:
  - Hover lift effect (8px)
  - Icon scale and rotate on hover
  - Top border animation
  - Shadow transitions
- **"Book Now" buttons** on each specialty card
- **Subtitle added** for better context

#### Hero Section Updates
- Changed background from dark purple to light blue-yellow gradient
- Added subtle dot pattern overlay
- Updated title with gradient text effect
- Improved statistics cards with white backgrounds
- Enhanced button styling with shadows and hover effects
- Better contrast and readability

#### Branding Updates
- Changed "MediBook" to "HCAMS" throughout application
- Removed emoji from "Trusted Healthcare Platform" badge
- Updated page title, navigation, footer, and modal

### 📚 Documentation Reorganization

#### New Structure
```
docs/
├── README.md                  # Documentation index
├── QUICK_START.md
├── DOCTOR_CREDENTIALS.md
├── EMAIL_OTP_SETUP.md
├── setup/                     # Setup guides
│   ├── SETUP_GUIDE.md
│   ├── SETUP_INSTRUCTIONS.md
│   └── START_HERE.md
├── development/               # Technical docs
│   ├── TECHNICAL_REPORT.md
│   └── REPORT.md
└── archive/                   # Historical docs
    ├── DEBUG_LOGIN.md
    ├── FIX_APPLIED.md
    ├── FIXES_SUMMARY.md
    ├── FINAL_SUMMARY.md
    ├── WORKFLOW_IMPROVEMENTS.md
    ├── UI_REDESIGN.md
    └── BRANDING_UPDATE.md
```

#### Documentation Improvements
- Created comprehensive README.md
- New Setup Guide with troubleshooting
- Documentation index with quick links
- Moved historical documents to archive/
- Organized by purpose (setup, development, archive)

### 🐛 Bug Fixes
- **Foreign Key Constraint** - Fixed DataLoader delete order
- **OTP Repository** - Added @Modifying and @Transactional annotations
- **Stats Contrast** - Added white card backgrounds for better readability

### ✅ Build Status
- Build: SUCCESS
- Compilation errors: 0
- All tests: Passing

---

## [1.0.0] - Initial Release

### Features
- Patient appointment booking system
- Doctor schedule management
- Admin dashboard and analytics
- JWT-based authentication
- Role-based access control
- Real-time slot availability
- Medical notes and prescriptions

### Technology Stack
- Java 17
- Spring Boot 3.3.3
- MySQL 8.0
- Thymeleaf
- JWT Authentication

---

## Upgrade Guide

### From 1.0.0 to 2.0.0

**No breaking changes** - Direct upgrade supported.

1. Pull latest code
2. Clear browser cache (Ctrl+Shift+R)
3. Restart application
4. Enjoy new UI!

**What to expect:**
- New specialty cards design
- Light theme hero section
- Better documentation structure
- HCAMS branding

---

## Contributors

- Amp AI Assistant
- Project Team

---

**Last Updated**: October 5, 2025  
**Current Version**: 2.0.0  
**Status**: ✅ Production Ready
