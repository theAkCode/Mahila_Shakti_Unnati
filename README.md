# Mahila-Shakti Unnati

Mahila-Shakti Unnati is an Android application designed for Self-Help Groups (SHGs) to digitally manage their financial activities. The application acts as a digital ledger for maintaining member details, savings records, loan tracking, and financial summaries, replacing traditional manual bookkeeping methods.

## Problem Statement

Self-Help Groups often maintain financial records manually in physical registers, which can lead to:
- Calculation errors
- Difficulty in tracking member savings
- Lack of transparency
- Time-consuming record maintenance
- Risk of data loss or damage

Mahila-Shakti Unnati provides a simple digital solution to address these issues.

## Features

- Add and manage SHG member details
- Record weekly savings contributions
- Mark savings status (Paid / Pending)
- Automatically calculate total group savings
- Issue loans to members
- Track loan repayments and remaining balances
- Prevent multiple unpaid loans for a member
- Display financial summaries and member-wise records
- Offline functionality with local data storage

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Room Database (SQLite)
- Material Design 3
- Android Emulator
- Git
- GitHub
- Claude AI / ChatGPT

## System Architecture

```text
User Interface (Jetpack Compose)
            ↓
Business Logic Layer
            ↓
DAO (Data Access Object)
            ↓
Room Database (SQLite)
```

## Project Modules

### Member Management
Handles adding, viewing, and managing SHG member information.

### Savings Management
Tracks weekly savings contributions and updates financial records automatically.

### Loan Management
Manages loan issuance, repayment tracking, and interest calculations.

### Financial Summary
Displays total savings, active loans, and member-wise financial information.

## Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/mahila-shakti-unnati.git
```

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Run the application on:
   - Android Emulator
   - Physical Android Device

## Future Enhancements

- Firebase cloud backup
- Multi-device synchronization
- User authentication
- PDF/Excel report export
- Notifications and reminders
- Digital payment integration

## Project Objective

To empower Self-Help Groups by providing a simple, offline, and user-friendly digital financial management system that improves accuracy, transparency, and efficiency.

