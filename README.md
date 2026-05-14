# Mahila Shakti Unnati

Mahila Shakti Unnati is an Android application developed to digitally manage the financial activities of Women Self Help Groups (SHGs). The application helps in maintaining member records, savings contributions, loan distribution, repayment tracking, and financial report generation through a simple and user-friendly interface.



# Problem Statement

Many Women Self Help Groups in rural and semi-urban areas still rely on manual bookkeeping methods for managing savings and loans. This often leads to:

* Miscalculation of balances
* Difficulty in tracking member contributions
* Delayed financial reporting
* Poor loan repayment tracking
* Risk of data loss

Mahila Shakti Unnati provides a digital solution to simplify SHG financial management and improve transparency and efficiency.

# Features

## Member Management

* Add and manage SHG members
* Store member details and identification data

## Savings Management

* Record weekly/monthly savings
* Track contribution history
* View latest savings information

## Loan Management

* Apply and issue loans
* Interest calculation
* Loan eligibility tracking
* Active loan monitoring

## Repayment System

* Record repayments
* Outstanding balance tracking
* Automatic loan closure after repayment completion

## Dashboard & Reports

* Total group capital overview
* Active loan statistics
* Pending contributions
* Financial report generation

## Authentication

* Firebase Email & Password login
* Secure session handling
* Logout functionality

## Additional Features

* WhatsApp report sharing
* Custom theme settings
* Modern UI design
* Offline Room Database support
* Real-time UI updates using LiveData
* Separate signup screen for new users
* Improved login flow with user-friendly validation messages


# Tech Stack

* Kotlin
* Android Studio
* Room Database
* Firebase Authentication
* MVVM Architecture
* LiveData & ViewModel
* XML UI Design



# Architecture

The project follows MVVM (Model View ViewModel) architecture.

## Model Layer

* Room entities
* DAO interfaces
* Repository

## ViewModel Layer

* Business logic
* Data processing
* LiveData management

## View Layer

* Activities
* RecyclerViews
* XML layouts



# Database Components

## Entities

* Member
* Savings
* Loan
* Repayment

## DAO Operations

* Insert member
* Record savings
* Apply loans
* Update repayment
* Retrieve active loans
* Generate summaries


# Firebase Integration

The project uses Firebase Authentication for:

* User login
* User session management
* Secure authentication flow

> Note: `google-services.json` is excluded from the public repository for security purposes.



# Future Enhancements

* Cloud synchronization
* Multi-admin support
* SMS notifications
* AI-based financial suggestions
* Analytics dashboard
* PDF export improvements
* Multi-language support


# App Screenshots

## Authentication

### Login Screen
<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/79c45014-9232-4fdb-aa1d-734040225828" />


### Signup Screen
<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/688ea576-d6d4-4f17-aaf1-72ba6d703b6c" />



## Dashboard

<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/49e4537f-ff61-4c97-9356-8e3e8750ce75" />



<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/e32f80f8-8abe-490d-8787-6c764e623d03" />


## Member Management

<img width="1080" height="2340" alt="image" src="https://github.com/user-attachments/assets/62655b84-39ec-4231-aaca-f0ad8e06d6fe" />



<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/540eaede-b87a-4f85-9729-0af8b7c1e14a" />


## Savings Management

<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/ddab443b-1ee8-4a3d-861d-85e6ed29db18" />


## Loan Management

<img width="1080" height="2340" alt="image" src="https://github.com/user-attachments/assets/db6bc6b3-1ac2-4c1c-835e-ef45ea9e9d6b" />



<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/4af71ef6-954c-482d-bbc0-0b2ebb709ad9" />


## Reports & Export

<img width="945" height="2048" alt="image" src="https://github.com/user-attachments/assets/8947cd3e-885c-40a7-9675-ef6d8ac8b6a7" />








