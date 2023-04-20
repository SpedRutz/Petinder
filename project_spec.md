# **PeTinder**

## Table of Contents

1. [App Overview](#App-Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Build Notes](#Build-Notes)

## App Overview

### Description 

**Pet Adoption app that utilizes te Petfinder API to help user to find pets to adopt in their selected area. Allows user login and chat messaging betweeen logged in users.**

### App Evaluation

<!-- Evaluation of your app across the following attributes -->

- **Category: Lifestyle Social**

- **Mobile: Yes. The app can be used on all android deveices**

- **Story: Connect pet lovers with adoptable pets and facilitate communication between users who are interested in pet adoption. PeTinder allows all people to easily search for pets and makes the adoption process significantly faster, and builds a community of pet enthusiasts**

- **Market: The app is to target pet lovers, indiviuals interested in adoption, animal shelters, and pet organizations. PeTinder The app provides significant value to people interested in pet adoption by streamlining the process and fostering connections between users.**

- **Habit: Users will open the app regularly,keeps users up to date with the latest pet adoption organizations, and search for pet adoption sites and can communicate with other pet owners looking to adopt or simply want to chat**

- **Scope: The app starts with basic pet finde=ing features, user authentication, and chatting features.The app will later be be exapanded to include user profiles, adoption application forms, and other feature to enhance the pet finding experience**

## Product Spec

### 1. User Features (Required and Optional)

Required Features:

- **RecyclerView to display the organzation data**
- **Filters results by location, breed, age, ,gender, and size**
- **Organization data must include their phone number, address, and name** 
- **Search bar to find other logged in users on the app**
- **Screen the displays the users on the app**
- **Private messaging features between users**
- **The app will use the Firebase Realtime Database in order to handle login/signup and for users to send messages in real time**

Stretch Features:

- **Contact the animal shelter and send user to their phone app once their phone number is clicked/tapped** 

### 2. Chosen API(s): Petfinder API

- **Return list of Animals based on Zip Code**
  - **User Can search for the animal they choose**
  - **User can filter results based on location, age, size, etc.**
- **Get Information/Organizations**
     - **API will display the information of the chosen animal**
     - **Will display phone numbers of Organizations**
     - **Displays location of Organization**
     

### 3. User Interaction

Required Features

- **Login Activity**
  - **Prompts the User to make an account (Email, user, password)**
  - **Logs user into the petfinder activity**
  
- **Pet Finding Activity**
  - **User type in their Zip codes todisplay pets/ shelters near them**
  - **Users can uses filters filters such as breed,type,etc**
  - **Can find users to interact with based on search**

- **User Searching activity**
    - **user can search other user on the app**
    - **Allows the user search users on the app based on their name**

- **User Chatting Activity**
    - **Users can chat with each other**
    - **The design will be similar to a text-message**

## Digital Wireframes


### Login Activity
<img src="https://i.imgur.com/WeuuwJm.png" width=600>

### Pet Finder Activity
<img src="https://i.imgur.com/O5vAvjm.png" width=600>

### User Search Activity
<img src="https://i.imgur.com/LRsOYe5.png" width=600>



### User Chat Activity
<img src="https://i.imgur.com/3zviUHs.png" width=600>





## Build Notes

In the Login Activity, the sign-up button will change to "log in"  if the user already has an account 

For Milestone 2, include **2+ GIFs** of the build process here!

## License

Copyright **2023** **Shane Douglas**, **Liang Liu** , **Eugene Senanu** , **Ahmed Mustapha** , **Mohammad Alam**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.