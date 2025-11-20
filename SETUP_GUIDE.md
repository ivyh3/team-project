# Setup Guide - API Keys Configuration

## Quick Setup (5 minutes)

### Step 1: Add Dotenv Dependency

Add this to your `pom.xml` in the `<dependencies>` section:

```xml
<!-- Dotenv for environment variable management -->
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Step 2: Create Your .env File

```bash
touch .env
```

### Step 3: Fill in Your API Keys

Edit `.env` with your actual credentials:

```env
# Firebase
FIREBASE_PROJECT_ID=my-actual-project-id
FIREBASE_STORAGE_BUCKET=my-actual-project-id.appspot.com

# Gemini API (get from https://aistudio.google.com/app/apikey)
GEMINI_API_KEY=AIzaSyC...your-actual-key

# Google OAuth (get from https://console.cloud.google.com)
GOOGLE_OAUTH_CLIENT_ID=1234567890-abc123.apps.googleusercontent.com
GOOGLE_OAUTH_CLIENT_SECRET=GOCSPX-your-actual-secret
```

### Step 4: Add Firebase Config

Download your Firebase service account key and save it as:

```
src/main/resources/firebase-config.json
```

## Getting API Keys

### 1. Firebase Setup

1. Go to https://console.firebase.google.com
2. Create a project (or use existing)
3. Enable Authentication, Firestore, and Storage
4. Go to Project Settings → Service Accounts
5. Click "Generate New Private Key"
6. Save as `src/main/resources/firebase-config.json`
7. Note your Project ID for the `.env` file

### 2. Gemini API Key

1. Go to https://aistudio.google.com/app/apikey
2. Click "Create API Key"
3. Copy the key to your `.env` file

### 3. Google OAuth

1. Go to https://console.cloud.google.com
2. Select your project (or create one)
3. Go to "APIs & Services" → "Credentials"
4. Click "Create Credentials" → "OAuth 2.0 Client ID"
5. Choose "Desktop app" or "Web application"
6. Add redirect URI: `http://localhost:8080/oauth/callback`
7. Copy Client ID and Client Secret to your `.env` file

## Troubleshooting

**Problem:** `Config.getGeminiApiKey()` returns empty string
**Solution:** Make sure your `.env` file is in the project root directory and contains `GEMINI_API_KEY=...`

**Problem:** App can't find `.env` file
**Solution:** The `.env` file should be in the same directory as `pom.xml`

**Problem:** Changes to `.env` not taking effect
**Solution:** Restart your application - env variables are loaded at startup
