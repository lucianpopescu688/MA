# 🚗 Automatic Service App 🏍️
## 🎯 Overview

The **Automatic Service App** is a smart mobile application that helps vehicle owners never forget important car maintenance and document renewals. The app tracks kilometers driven and time elapsed, automatically notifying users when parts need replacement (oil, filters, tires, brake pads) or when documents expire (vehicle inspection, insurance, road tax).

---

### Target Users

👤 **Individual Owners** - Personal assistant preventing costly breakdowns and ensuring legal compliance

🏢 **Fleet Managers** - Comprehensive oversight for companies managing vehicle fleets (Bolt, Uber, delivery services)

---

### Value Proposition

- ✅ Automated maintenance reminders based on km/time
- ✅ Complete service history with photo attachments
- ✅ Legislative updates and compliance alerts
- ✅ Nearby service center recommendations
- ✅ Cost tracking and budget forecasting
- ✅ Fleet-wide analytics and reporting

---

## ✨ Key Features

### 🔔 Smart Notifications
Receive timely alerts before services are due, based on distance traveled or time elapsed

### 📊 Dashboard Overview
Visual health indicators for all vehicles with color-coded status (green/yellow/red)

### 📱 Offline-First Design
Full functionality without internet connection - sync when back online

### 🏭 Fleet Management
Multi-vehicle oversight with role-based access and comprehensive analytics

### 📸 Document Management
OCR-powered receipt scanning with automatic data extraction

### 📈 Cost Analytics
Track maintenance expenses and predict future costs

---

## 🗂️ Domain Model

### Core Entities

<table>
<tr>
<td width="50%">

#### 🚙 **Vehicle**
- `id` - Unique identifier
- `userId` - Owner reference
- `make` - Manufacturer name
- `model` - Model name
- `year` - Manufacturing year
- `vin` - Vehicle ID number
- `currentKilometers` - Odometer
- `purchaseDate` - Acquisition date

</td>
<td width="50%">

#### 🔧 **ServiceItem**
- `id` - Unique identifier
- `vehicleId` - Vehicle reference
- `itemType` - Service type enum
- `lastServiceDate` - Last serviced
- `lastServiceKm` - Last service km
- `intervalKm` - Km interval
- `intervalDays` - Time interval
- `nextDueDate` - Calculated due
- `nextDueKm` - Calculated km due
- `status` - Current status enum

</td>
</tr>
<tr>
<td>

#### 📝 **MaintenanceRecord**
- `id` - Unique identifier
- `vehicleId` - Vehicle reference
- `serviceItemId` - Service reference
- `serviceDate` - Service date
- `kilometersAtService` - Odometer
- `serviceProvider` - Provider name
- `cost` - Service cost
- `description` - Work details
- `attachments` - Photos/receipts

</td>
<td>

#### 🔔 **Notification**
- `id` - Unique identifier
- `userId` - User reference
- `vehicleId` - Vehicle reference
- `serviceItemId` - Service reference
- `notificationType` - Type enum
- `message` - Alert message
- `sentDate` - Send timestamp
- `isRead` - Read status

</td>
</tr>
<tr>
<td>

#### 🏢 **Fleet**
- `id` - Unique identifier
- `companyName` - Company name
- `adminUserId` - Admin reference
- `settings` - Configuration JSON
- `subscriptionTier` - Billing level
- `vehicleCount` - Fleet size

</td>
<td>

#### 👤 **User**
- `id` - Unique identifier
- `email` - Login email
- `passwordHash` - Secure password
- `firstName` - First name
- `lastName` - Last name
- `role` - User role enum
- `notificationPreferences` - Settings
- `timezone` - User timezone

</td>
</tr>
</table>

---

## 🔄 CRUD Operations

### ➕ Create

| Operation | Description | Key Features |
|-----------|-------------|--------------|
| **Vehicle** | Add new vehicle with VIN, make, model, current km | Auto-initializes standard service items based on manufacturer |
| **ServiceItem** | Define custom maintenance tasks | Supports km-based, time-based, or both interval types |
| **MaintenanceRecord** | Document completed service work | OCR extracts data from receipt photos automatically |
| **Fleet** | Register company fleet | Configure alerts, cost centers, approval workflows |

### 👁️ Read

| Operation | Description | Key Features |
|-----------|-------------|--------------|
| **Dashboard** | View all vehicles and status | Color-coded health indicators, upcoming services, monthly costs |
| **Vehicle Details** | Complete vehicle profile | Timeline with maintenance history, countdown indicators |
| **Service List** | All configured service items | Filtered by status, progress bars, estimated costs |
| **History** | Complete maintenance records | Searchable, filterable, exportable as PDF |

### ✏️ Update

| Operation | Description | Key Features |
|-----------|-------------|--------------|
| **Kilometers** | Manual entry or GPS tracking | Auto-recalculates all distance-based service items |
| **Service Status** | Mark service as completed | Creates maintenance record, recalculates next due date |
| **Vehicle Info** | Modify vehicle details | Archive when sold, preserving complete history |
| **Preferences** | Customize notifications | Channels, quiet hours, warning thresholds, frequency |

### 🗑️ Delete

| Operation | Description | Key Features |
|-----------|-------------|--------------|
| **Vehicle** | Remove from account | 30-day recovery period, archive option available |
| **Record** | Remove incorrect entries | Warns about calculation impacts, reverts service item |
| **ServiceItem** | Remove custom items | Standard items protected, history preserved |
| **Notification** | Dismiss alerts | Doesn't affect underlying schedule |

---

## 💾 Data Persistence

### 📱 Local Storage (SQLite/Realm)

```
┌─────────────────────────────────────┐
│      LOCAL DATABASE (OFFLINE)       │
├─────────────────────────────────────┤
│ ✓ Complete vehicle data             │
│ ✓ Service items & schedules         │
│ ✓ Maintenance history               │
│ ✓ User preferences                  │
│ ✓ Pending sync queue                │
│ ✓ Photos (device file system)      │
└─────────────────────────────────────┘
```

**Benefits:**
- ⚡ Instant load times
- 📶 Full offline functionality
- 🔄 Background sync when online

### ☁️ Server Storage (PostgreSQL)

```
┌─────────────────────────────────────┐
│     SERVER DATABASE (CLOUD)         │
├─────────────────────────────────────┤
│ ✓ User accounts & authentication    │
│ ✓ Vehicle registry (all users)     │
│ ✓ Service data with backup          │
│ ✓ Notification scheduling           │
│ ✓ Legislative updates database      │
│ ✓ Fleet analytics & reports         │
│ ✓ Service provider directory        │
└─────────────────────────────────────┘
```

**Benefits:**
- 🔐 Enterprise-grade security
- 🔄 Multi-device synchronization
- 📊 Cross-fleet analytics
- 💾 Automatic backups

### Synchronization Strategy

| Operation | Local | Server | Sync Timing |
|-----------|-------|--------|-------------|
| **Create Vehicle** | Immediate | Background | On connectivity |
| **Add Record** | Immediate | Async photos | When online |
| **Update Km** | Immediate | Batched | Every 5 min |
| **Mark Complete** | Immediate | Immediate | Real-time |
| **Delete** | Soft delete | Soft delete | Next sync |

---

## 🌐 Offline Functionality

### Scenarios

#### ➕ **CREATE** - At Service Center (No Internet)

```
User completes oil change → Documents immediately
  ↓
✓ Select vehicle (from local cache)
✓ Choose service item
✓ Enter odometer & cost
✓ Capture receipt photo
  ↓
Saves locally with 'pending_sync' flag
  ↓
When online → Uploads to server → Confirms sync
```

#### 👁️ **READ** - On Road Trip (No Coverage)

```
User checks maintenance status
  ↓
✓ Dashboard loads instantly (local DB)
✓ All vehicles show current status
✓ Browse detailed schedules
✓ View complete history with photos
✓ All calculations work perfectly
```

**Limitation:** New legislative updates unavailable offline

#### ✏️ **UPDATE** - Multi-Day Trip (Intermittent Connection)

```
User updates odometer periodically
  ↓
✓ Each update saves locally
✓ Recalculates service due dates
✓ Generates local notifications
✓ Updates queue for sync
  ↓
When online → Batch sync → Server validates
```

#### 🗑️ **DELETE** - Remove Wrong Record (Offline)

```
User deletes incorrect record
  ↓
✓ Shows confirmation
✓ Marks deleted locally
✓ Removes from view
✓ Queues deletion
  ↓
When online → Syncs to server → Broadcasts to devices
```

### Conflict Resolution

When multiple devices make conflicting changes offline:

```
Device A (offline): Updates odometer to 50,000 km
Device B (online):  Updates odometer to 50,100 km
  ↓
Device A comes online
  ↓
Server detects conflict (backward movement)
  ↓
Rejects Device A → Sends Device B value
  ↓
User notified: "Odometer conflict resolved - using 50,100 km"
```

**Strategy:** Last write wins (server timestamp) + user notification

---

## 🛠️ Technology
